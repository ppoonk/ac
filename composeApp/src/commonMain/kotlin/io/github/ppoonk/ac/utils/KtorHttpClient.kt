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
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.takeFrom
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull

/**
 * JSON 序列化配置
 */
val myJson = Json {
    explicitNulls = false // 不序列化 null 值
    ignoreUnknownKeys = true // 忽略未知字段
    prettyPrint = false // 生产环境不需要格式化输出
    encodeDefaults = true // 强制序列化默认值
//    isLenient = true // 宽松模式
}

/**
 * API 请求配置
 */
data class ApiConfig(
    val url: String,
    val method: HttpMethod,
    val headers: Map<String, String>,
)

/**
 * 后端返回数据格式
 * @param T 泛型数据类型
 */
@Serializable
data class Response<out T>(
    val code: Int, // 响应状态码
    val message: String, // 响应消息
    val data: T? // 响应数据
)

/**
 * 将数据类转换为查询参数 Map
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
                value.booleanOrNull != null -> value.booleanOrNull
                value.intOrNull != null -> value.intOrNull
                value.longOrNull != null -> value.longOrNull
                value.doubleOrNull != null -> value.doubleOrNull
                else -> value.content
            }
        }

        is JsonArray -> value.map { it.toSafeValue() } // 明确返回 List<Any?>
        is JsonObject -> value.toSafeMap()

        else -> null
    }
}

fun JsonElement.toSafeValue(): Any? = when (this) {
    is JsonPrimitive -> {
        when {
            isString -> content
            booleanOrNull != null -> booleanOrNull
            intOrNull != null -> intOrNull
            longOrNull != null -> longOrNull
            doubleOrNull != null -> doubleOrNull
            else -> content
        }
    }

    is JsonArray -> map { it.toSafeValue() }
    is JsonObject -> toSafeMap()
}

/**
 * 包装结果
 */
sealed class Result<out T> {
    data class Success<T>(val code: Int, val message: String, val data: T? = null) :
        Result<T>() // 成功结果

    data class Error(val code: Int? = null, val message: String) : Result<Nothing>() // 错误结果
}

/**
 * 链式处理扩展函数，处理成功结果
 * @param callback 成功回调
 */
fun <T> Result<T>.onSuccess(callback: (Result.Success<T>) -> Unit): Result<T> {
    if (this is Result.Success) {
        callback(this)
    }
    return this
}

/**
 * 链式处理扩展函数，处理失败结果
 * @param callback 失败回调
 */
fun <T> Result<T>.onFailure(callback: (Result.Error) -> Unit): Result<T> {
    if (this is Result.Error) {
        callback(this)
    }
    return this
}

/**
 * API HTTP 客户端封装类
 *
 * 提供基于 Ktor 的 HTTP 客户端功能，支持统一的请求/响应处理
 * 特性包括：
 * - 自动 JSON 序列化/反序列化
 * - 统一响应格式处理（code/message/data）
 * - 请求观察器和响应观察器支持
 * - 统一异常处理和业务错误处理
 * - 支持 GET/POST 等常见 HTTP 方法
 * - 支持查询参数自动转换
 *
 * @param requestObserver 请求观察器，在发送请求前执行。
 *                        返回非空 Result.Error 时会中断请求并直接返回错误。
 * @param responseObserver 响应观察器，在收到响应后执行，可用于统一处理特定 HTTP 状态码等。
 *                         返回非空 Result.Error 时会中断后续处理并直接返回错误。
 */
class ApiHttpClient(
    val requestObserver: (ApiConfig) -> Result.Error? = { null },
    val responseObserver: (HttpResponse) -> Result.Error? = { null },
) {
    val httpClient = HttpClient() {
        // 配置默认请求参数
        defaultRequest {
            contentType(ContentType.Application.Json) // 默认 Content-Type
            headers {
                append(HttpHeaders.Accept, "application/json") // 默认 Accept 头
            }
        }
        // 安装 JSON 序列化插件
        install(ContentNegotiation) {
            json(myJson)
        }
        // 配置超时
        install(HttpTimeout) {
            requestTimeoutMillis = 15_000 // 请求超时时间
            connectTimeoutMillis = 10_000 // 连接超时时间
        }
        // 启用默认验证
        // expectSuccess = true

        // 拦截器
//        install(ResponseObserver) {
//            onResponse { response ->
//                val contentType = response.contentType()
//                if (contentType?.match(ContentType.Application.Json) == true) {
//                    try {
//                        val text = response.bodyAsText()
//                        val responseObject = myJson.decodeFromString<JsonObject>(text)
//                        val code = responseObject["code"]?.jsonPrimitive?.intOrNull
//                        if (code != null) {
//                            println("Business code: $code")
//                        }
//                    } catch (e: Exception) {
//                        // 解析失败忽略或记录日志
//                        e.printStackTrace()
//                    }
//                }
//            }
//        }
    }


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
    ): Result<R> {
        try {

            val r1 = requestObserver(config)
            if (r1 != null) return r1

            // 发起请求
            val httpRes = doRequest(config, params)


            val r2 = responseObserver(httpRes)
            if (r2 != null) return r2

            // 反序列化
            val resp = httpRes.body<Response<R>>()

            return when (resp.code) {
                0 -> Result.Success(resp.code, resp.message, resp.data)
                else -> {
                    Result.Error(resp.code, resp.message)
                }
            }
        } catch (e: Exception) {
            return when (e) {
                is ClientRequestException -> Result.Error(
                    e.response.status.value,
                    "Client error, status:${e.response.status.value}, ${e.message}"
                )

                is ServerResponseException -> Result.Error(
                    e.response.status.value,
                    "Server error, status:${e.response.status.value}, ${e.message}"
                )

                is kotlinx.io.IOException -> Result.Error(message = "Network connection failed, ${e.message}")

                is SerializationException -> Result.Error(message = "Data parsing failed, ${e.message}")

                else -> Result.Error(message = "Unknown error, ${e.message},${e.cause}")
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
        return httpClient.request {
            method = config.method
            if (config.headers.isNotEmpty()) {
                headers {
                    config.headers.forEach {
                        append(it.key, it.value)
                    }
                }
            }

            url {
                takeFrom(config.url)
                if (config.method == HttpMethod.Get || config.method == HttpMethod.Delete) {
                    params?.toQueryParams()?.forEach { (key, value) ->
                        when (value) {
                            is List<*> -> {
                                value.forEach { item ->
                                    parameter(key, item.toString())
                                }
                            }

                            null -> return@forEach
                            else -> parameter(key, value)
                        }
                    }
                }
            }

            if (config.method == HttpMethod.Post || config.method == HttpMethod.Post || config.method == HttpMethod.Patch) {
                params?.let {
                    setBody(params)
                }
            }
        }
    }
}


/**
 * 处理HTTP状态码
 * @param httpRes HTTP响应
 * @return 如果需要中断处理则返回Error结果，否则返回null继续处理
 */
fun DefaultHandleHttpStatus(httpRes: HttpResponse): Result.Error? {
    return when (httpRes.status) {
        HttpStatusCode.OK, HttpStatusCode.Created -> {
            // 正常情况，继续处理
            null
        }

        HttpStatusCode.BadRequest -> {
            // 客户端请求错误
            Result.Error(httpRes.status.value, "Bad Request")
        }

        HttpStatusCode.Unauthorized -> {
            // 未授权访问
            Result.Error(httpRes.status.value, "Unauthorized")
        }

        HttpStatusCode.Forbidden -> {
            // 访问被禁止
            Result.Error(httpRes.status.value, "Forbidden")
        }

        HttpStatusCode.NotFound -> {
            // 资源未找到
            Result.Error(httpRes.status.value, "Not Found")
        }

        HttpStatusCode.InternalServerError -> {
            // 服务器内部错误
            Result.Error(httpRes.status.value, "Internal Server Error")
        }

        HttpStatusCode.BadGateway -> {
            // 网关错误
            Result.Error(httpRes.status.value, "Bad Gateway")
        }

        HttpStatusCode.ServiceUnavailable -> {
            // 服务不可用
            Result.Error(httpRes.status.value, "Service Unavailable")
        }

        else -> {
            // 其他未处理的状态码
            if (httpRes.status.value >= 400) {
                Result.Error(httpRes.status.value, "HTTP Error: ${httpRes.status}")
            } else {
                // 对于其他成功的状态码(2xx系列)，继续正常处理
                null
            }
        }
    }
}