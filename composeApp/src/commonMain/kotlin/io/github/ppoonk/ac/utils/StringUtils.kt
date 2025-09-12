package io.github.ppoonk.ac.utils

import com.benasher44.uuid.uuid4

// StringUtils 对象，提供字符串相关的工具方法
object StringUtils {
    /**
     * 生成一个新的 UUID 字符串
     *
     * @return 新的 UUID 字符串
     */
    fun newUUID(): String {
        return uuid4().toString()
    }
}

/**
 * 扩展函数：提取字符串中的数字，如果为空则返回 "0"
 *
 * @return 仅包含数字的字符串，若无数字则返回 "0"
 */
fun String.onlyNumberIfEmptyZero(): String {
    return this.filter { char ->
        char.isDigit()
    }.ifEmpty { "0" }
}

/**
 * 扩展函数：提取字符串中的数字
 *
 * @return 仅包含数字的字符串
 */
fun String.onlyNumber(): String {
    return this.filter { char ->
        char.isDigit()
    }
}