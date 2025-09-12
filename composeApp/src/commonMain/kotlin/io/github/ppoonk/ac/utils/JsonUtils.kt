package io.github.ppoonk.ac.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
/**
 * 比较两个对象的字段值（支持嵌套数据类），生成一个仅包含不同字段的新对象
 *
 * @param old 原始对象
 * @param new 修改后的对象
 * @return 包含不同字段的新对象（相同字段为默认值/null），类型与输入对象相同
 */
inline fun <reified T : Any> diffAndCreateNew(old: T, new: T): T {
    return diffObject(old, new)
}

/**
 * 比较两个对象的字段值（支持嵌套数据类），生成一个仅包含不同字段的新对象
 *
 * @param old 原始对象
 * @param new 修改后的对象
 * @return 包含不同字段的新对象（相同字段为默认值/null），类型与输入对象相同
 */
inline fun <reified T : Any> diffAndCreateNewOrNull(old: T?, new: T?): T? {
    if (old == null && new == null) return null
    if (old == null || new == null) return new

//    val jsonOld = Json.encodeToJsonElement(old)
//    return when (jsonOld) {
//        is JsonObject -> {
//            diffObject(old, new)
//        }
//
//        is JsonArray, is JsonPrimitive, JsonNull -> {
//            if (old == new) null else new
//        }
//    }
    return diffObject(old, new)
}

inline fun <reified T : Any> diffObject(old: T, new: T): T {

    val jsonOld = Json.encodeToJsonElement(old).jsonObject
    val jsonNew = Json.encodeToJsonElement(new).jsonObject
    // 递归比较嵌套字段
    val diffMap = deepDiff(jsonOld, jsonNew).toMutableMap()

    // 移除未变化的字段（值为 JsonNull 的键）
    diffMap.entries.removeAll { it.value == JsonNull }

    val jd = JsonObject(diffMap)
//    MyLogger.debug(MyLogger.API_HTTP_CLIENT) { "jd: $jd" }


    val res:T = Json.decodeFromJsonElement(jd)
//    MyLogger.debug(MyLogger.API_HTTP_CLIENT) { "res: $res" }
    return res
//    return Json.decodeFromJsonElement(JsonObject(diffMap))
}

/**
 * 递归比较两个 JsonObject，返回差异字段（相同字段设为 JsonNull）
 */
fun deepDiff(oldObj: JsonObject, newObj: JsonObject): Map<String, JsonElement> {
    return oldObj.toMutableMap().apply {
        newObj.forEach { (key, newValue) ->
            val oldValue = this[key]
            when {
                // 字段不存在于旧对象 → 直接保留新值
                oldValue == null -> this[key] = newValue
                // 字段值相同 → 设为 JsonNull
                oldValue == newValue -> this[key] = JsonNull
                // 嵌套对象 → 递归比较
                oldValue is JsonObject && newValue is JsonObject -> {
                    val nestedDiff = deepDiff(oldValue, newValue)
                    this[key] = JsonObject(nestedDiff)
                }
                // 其他情况 → 保留新值
                else -> this[key] = newValue
            }
        }
    }
}