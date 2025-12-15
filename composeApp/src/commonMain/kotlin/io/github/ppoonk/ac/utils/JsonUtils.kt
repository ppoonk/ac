package io.github.ppoonk.ac.utils

import kotlinx.serialization.Serializable
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
 * @param excludeFields 需要排除的字段列表，这些字段将维持原值
 * @return 包含不同字段的新对象（相同字段为默认值/null），类型与输入对象相同
 */
inline fun <reified T : Any> diffObject(
    old: T,
    new: T,
    excludeFields: List<String> = emptyList()
): T? {
    // 增加判断，如果old==new，直接返回null
    if (old == new) {
        return null
    }

    val jsonOld = Json.encodeToJsonElement(old).jsonObject
    val jsonNew = Json.encodeToJsonElement(new).jsonObject

    val diffResult = deepDiff(jsonOld, jsonNew, excludeFields)
    return Json.decodeFromJsonElement(diffResult)
}


/**
 * 递归比较两个 JsonObject，返回差异字段（相同字段设为 JsonNull）
 *
 * @param oldObj 旧对象
 * @param newObj 新对象
 * @param excludeFields 需要排除的字段列表
 */
fun deepDiff(
    oldObj: JsonObject,
    newObj: JsonObject,
    excludeFields: List<String> = emptyList()
): JsonObject {
    val result = mutableMapOf<String, JsonElement>()

    // 处理所有新对象中的字段
    newObj.forEach { (key, newValue) ->
        // 如果字段在排除列表中，跳过处理
        if (excludeFields.contains(key)) {
            oldObj[key]?.let { oldValue ->
                result[key] = oldValue
            }
            return@forEach
        }

        val oldValue = oldObj[key]

        val diffValue = when {
            // 字段不存在于旧对象 → 直接保留新值
            oldValue == null -> newValue
            // 字段值相同 → 设为 JsonNull
            oldValue == newValue -> JsonNull
            // 嵌套对象 → 递归比较
            oldValue is JsonObject && newValue is JsonObject -> deepDiff(
                oldValue,
                newValue,
                excludeFields
            )
            // 其他情况 → 保留新值
            else -> newValue
        }

        // 只添加非 JsonNull 的字段到结果中
        if (diffValue != JsonNull) {
            result[key] = diffValue
        }
    }

    return JsonObject(result)
}


/**
 * 合并两个对象，将旧对象的字段更新为新对象中的值
 *
 * @param old 旧对象
 * @param new 新对象
 * @return 合并后的新对象，包含旧对象的所有字段，其中新对象中存在差异的字段会被更新
 */
inline fun <reified T : Any, reified N : Any> mergeObject(old: T, new: N): T {
    val jsonOld = Json.encodeToJsonElement(old).jsonObject
    val jsonNew = Json.encodeToJsonElement(new).jsonObject

    val mergedResult = deepMerge(jsonOld, jsonNew)
    return Json.decodeFromJsonElement(mergedResult)
}

/**
 * 递归合并两个 JsonObject，将旧对象的字段用新对象的值更新
 */
fun deepMerge(oldObj: JsonObject, newObj: JsonObject): JsonObject {
    val result = oldObj.toMutableMap()

    newObj.forEach { (key, newValue) ->
        val oldValue = oldObj[key]

        val mergedValue = when {
            // 如果旧对象中没有该字段，直接使用新值
            oldValue == null -> newValue
            // 如果都是 JsonObject，递归合并
            oldValue is JsonObject && newValue is JsonObject -> deepMerge(oldValue, newValue)
            // 其他情况，使用新值
            else -> newValue
        }

        result[key] = mergedValue
    }

    return JsonObject(result)
}

/**
 * 将JSON字符串解析为指定类型T的对象
 * @param T 目标类型，必须是可序列化的类型
 * @return 解析后的对象T
 */
inline fun <reified T> String.decode(): T {
    return myJson.decodeFromString<T>(this)
}


/**
 * 将可序列化的对象列表编码为JSON字符串
 * @param T 列表元素的类型，必须标记为@Serializable
 * @return 序列化后的JSON字符串
 */
inline fun <reified T : @Serializable Any> List<T>.encode(): String {
    return myJson.encodeToString(this)
}

/**
 * 更新列表并保持长度限制
 * @param T 列表元素类型
 * @param newItem 要添加的新项
 * @param limit 列表最大长度限制，默认为20
 * @param duplicateChecker 用于检测重复项的函数
 * @return 更新后的列表
 */
fun <T> MutableList<T>.updateWithLimit(
    newItem: T,
    limit: Int = 20,
    duplicateChecker: (T) -> Boolean
): MutableList<T> {
    return this.apply {
        removeAll(duplicateChecker)
        add(0, newItem)
        while (size > limit) removeAt(size - 1)
    }
}

/**
 * 更新列表并保持长度限制（适用于不可变 List）
 * @param T 列表元素类型
 * @param newItem 要添加的新项
 * @param limit 列表最大长度限制，默认为20
 * @param duplicateChecker 用于检测重复项的函数
 * @return 更新后的列表
 */
fun <T> List<T>.withLimit(
    newItem: T,
    limit: Int = 20,
    duplicateChecker: (T) -> Boolean
): List<T> {
    // 过滤掉重复项，并在开头加入新项
    val filteredList = this.filterNot(duplicateChecker)
    val newList = listOf(newItem) + filteredList

    // 控制长度不超过 limit
    return if (newList.size > limit) {
        newList.take(limit)
    } else {
        newList
    }
}
