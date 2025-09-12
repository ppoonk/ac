package io.github.ppoonk.ac.utils

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.parameters
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull

/**
 * JSON 序列化配置
 */
val myJson = Json {
    explicitNulls = false // 不序列化 null 值
    ignoreUnknownKeys = true // 忽略未知字段
    prettyPrint = true // 格式化输出
    isLenient = true // 宽松模式
    encodeDefaults = true // 强制序列化默认值
}

/**
 * Ktor HTTP 客户端封装
 * @param baseUrl 基础 URL
 */
class KtorHttpClient(baseUrl: String) {
    val client = HttpClient() {
        // 配置默认请求参数
        defaultRequest {
            if (baseUrl.isNotEmpty()) url(baseUrl) // 设置基础 URL
            contentType(ContentType.Application.Json) // 默认 Content-Type
        }
        // 安装 JSON 序列化插件
        install(ContentNegotiation) {
            json(myJson)
        }
        // 配置超时
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000 // 请求超时时间
        }
        // 启用默认验证
        expectSuccess = true
    }
}

/**
 * API 请求配置接口
 */
interface ApiConfig {
    val path: String // 请求路径
    val method: HttpMethod // HTTP 方法
    val auth: Boolean // 是否需要认证
}

/**
 * 后端返回数据格式
 * @param T 泛型数据类型
 */
@Serializable
data class MyResponse<out T>(
    val code: Int, // 响应状态码
    val message: String, // 响应消息
    val data: T? // 响应数据
)

/**
 * 将数据类转换为查询参数 Map。注意；未处理嵌套数据类
 * @param T 泛型数据类型
 * @return 查询参数 Map
 */

inline fun <reified T> T.toQueryParams(): Map<String, Any?> {
    val jsonString = myJson.encodeToString(this) // 序列化为 JSON 字符串
    val jsonObject = myJson.decodeFromString<JsonObject>(jsonString) // 反序列化为 JsonObject
    return jsonObject.toSafeMap() // 转换为 Map
}


fun JsonObject.toSafeMap(): Map<String, Any?> = this.mapValues { (_, value) ->
    when (value) {
        is JsonPrimitive -> {
            when {
                value.isString -> value.content.trim('\"')
                value.booleanOrNull != null -> value.boolean
                value.intOrNull != null -> value.int
                value.longOrNull != null -> value.long
                value.doubleOrNull != null -> value.double
                else -> error("Unsupported JsonPrimitive type: ${value}")
            }
        }

        is JsonArray -> value.map { it.toSafeValue() }.toList() // 明确返回 List<Any?>
        is JsonObject -> value.toSafeMap()

        else -> null
    }
}

fun JsonElement.toSafeValue(): Any? = when (this) {
    is JsonPrimitive -> content
    is JsonArray -> map { it.toSafeValue() }.toList()
    is JsonObject -> toSafeMap()
    else -> null
}

/**
 * 包装响应结果
 */
sealed class MyResult<out T> {
    data class Success<T>(val code: Int, val message: String, val data: T? = null) :
        MyResult<T>() // 成功结果

    data class Error(val code: Int? = null, val message: String) : MyResult<Nothing>() // 错误结果
}

/**
 * 链式处理扩展函数，处理成功结果
 * @param callback 成功回调
 */
fun <T> MyResult<T>.onSuccess(callback: (MyResult.Success<T>) -> Unit): MyResult<T> {
    if (this is MyResult.Success) {
        callback(this)
    }
    return this
}

/**
 * 链式处理扩展函数，处理失败结果
 * @param callback 失败回调
 */
fun <T> MyResult<T>.onFailure(callback: (MyResult.Error) -> Unit): MyResult<T> {
    if (this is MyResult.Error) {
        callback(this)
    }
    return this
}

/**
 * API HTTP 客户端
 * @param openLoading 开启加载回调
 * @param closeLoading 关闭加载回调
 * @param onLog 日志记录回调
 * @param getApiBaseUrl 获取 API 基础 URL 回调
 * @param getToken 获取认证 Token 回调
 */
class ApiHttpClient(
    val onLog: (String) -> Unit = {},
    val getApiBaseUrl: () -> String,
    val getToken: () -> String,
) {
    var httpClient = KtorHttpClient(getApiBaseUrl()) // 初始化 HTTP 客户端

    /**
     * 发起请求
     * @param T 请求参数类型
     * @param R 响应数据类型
     * @param config 请求配置
     * @param params 请求参数
     * @return 包装的响应结果
     */
    suspend inline fun <reified T : Any, reified R : Any> request(
        config: ApiConfig,
        params: T?,
    ): MyResult<R> {
        onLog("Request params：${params}") // 打印请求参数
        try {
            val httpRes = doRequest(config, params) // 发起请求
            val str: String = httpRes.body() // 获取响应字符串
            onLog("Response string：${str}") // 打印响应字符串
            val myResponse = httpRes.body<MyResponse<R>>() // 反序列化响应
            return when (myResponse.code) {
                0 -> MyResult.Success(myResponse.code, myResponse.message, myResponse.data) // 成功结果
                else -> {
                    onLog("Business logic error, status:${myResponse.code}, ${myResponse.message}")
                    MyResult.Error(myResponse.code, myResponse.message) // 业务逻辑错误
                }
            }
        } catch (e: Exception) {
            return when (e) {
                is ClientRequestException -> {
                    onLog("Client error, status:${e.response.status.value}, ${e.message}")
                    MyResult.Error(
                        e.response.status.value,
                        "Client error, status:${e.response.status.value}, ${e.message}"
                    )
                }

                is ServerResponseException -> {
                    onLog("Server error, status:${e.response.status.value}, ${e.message}")
                    MyResult.Error(
                        e.response.status.value,
                        "Server error, status:${e.response.status.value}, ${e.message}"
                    )
                }

                is kotlinx.io.IOException -> {
                    onLog("Network connection failed, ${e.message}")
                    MyResult.Error(message = "Network connection failed, ${e.message}")
                }

                is SerializationException -> {
                    onLog("Data parsing failed, ${e.message}")
                    MyResult.Error(message = "Data parsing failed, ${e.message}")
                }

                else -> {
                    onLog("unknown error, ${e.message}")
                    MyResult.Error(message = "unknown error, ${e.message},${e.cause}")
                }
            }
        }
    }

    /**
     * 执行 HTTP 请求
     * @param T 请求参数类型
     * @param config 请求配置
     * @param params 请求参数
     * @return HTTP 响应
     */
    suspend inline fun <reified T> doRequest(config: ApiConfig, params: T?): HttpResponse {
        return httpClient.client.request {
            method = config.method // 设置 HTTP 方法
            headers {
                append(HttpHeaders.Accept, "application/json") // 设置 Accept 头
                if (config.auth) append(HttpHeaders.Authorization, getToken()) // 设置认证头
            }
            url {
                path(config.path) // 设置请求路径
                when (config.method) {
                    HttpMethod.Get, HttpMethod.Delete -> parameters {
                        params?.toQueryParams()?.forEach { (key, value) ->
                            when (value) {
                                null -> return@forEach // 忽略 null 值
                                else -> parameter(key, value) // 添加查询参数
                            }
                        }
                    }
                }
            }

            when (config.method) {
                HttpMethod.Post, HttpMethod.Put -> {
                    contentType(ContentType.Application.Json) // 设置 Content-Type
                    params?.let {
                        setBody(params) // 设置请求体
                    }
                }
            }
        }
    }
}