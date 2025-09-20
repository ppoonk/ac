package io.github.ppoonk.ac.utils

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject


/**
 * 比较两个对象的差异，返回仅包含变化字段的对象
 */
inline fun <reified T : Any> diff(old: T, new: T): T? {
    val oldJson = Json.encodeToJsonElement(old).jsonObject
    val newJson = Json.encodeToJsonElement(new).jsonObject
    val diff = calculateDiff(oldJson, newJson)

    return if (diff.isEmpty()) null
    else Json.decodeFromJsonElement(JsonObject(diff))
}

/**
 * 计算两个 JsonObject 的差异
 */
fun calculateDiff(old: JsonObject, new: JsonObject): Map<String, JsonElement> {
    val result = mutableMapOf<String, JsonElement>()

    // 找出所有可能的键
    val allKeys = (old.keys + new.keys).toSet()

    for (key in allKeys) {
        val oldValue = old[key]
        val newValue = new[key]

        when {
            // 新增字段
            oldValue == null && newValue != null -> {
                result[key] = newValue
            }
            // 删除字段（根据需求决定是否需要处理）
            oldValue != null && newValue == null -> {
                result[key] = JsonNull
            }
            // 修改字段
            oldValue != null && newValue != null && oldValue != newValue -> {
                when {
                    oldValue is JsonObject && newValue is JsonObject -> {
                        val nestedDiff = calculateDiff(oldValue, newValue)
                        if (nestedDiff.isNotEmpty()) {
                            result[key] = JsonObject(nestedDiff)
                        }
                    }
                    else -> result[key] = newValue
                }
            }
            // 相同字段不需要包含在结果中
        }
    }

    return result
}