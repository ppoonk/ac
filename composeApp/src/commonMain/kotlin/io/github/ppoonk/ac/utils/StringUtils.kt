package io.github.ppoonk.ac.utils

import com.benasher44.uuid.uuid4

object StringUtils {
    /**
     * 生成一个新的UUID字符串
     *
     * @return UUID字符串
     */
    fun newUUID(): String {
        return uuid4().toString()
    }

    /**
     * 生成符合密码规则的随机密码
     * 密码规则：包含字母、数字(不以0开头)和常用符号
     *
     * @param length 密码长度，默认12位
     * @return 符合规则的随机密码字符串
     */
    fun newRandomPassword(length: Int = 12): String {
        // 定义字符集
        val letters = ('a'..'z') + ('A'..'Z')  // 大小写字母
        val digits = ('1'..'9') // 不包含0，避免开头问题
        val punctuation = "!@#$%^&*()_+-=[]{}|;:,.<>?"  // 常用符号

        val allChars = letters + digits + punctuation.toList()  // 所有可用字符

        // 生成字符
        val remainingChars = (1 until length).map {
            allChars.random()  // 随机选择字符
        }

        return remainingChars.joinToString("")  // 返回生成的密码
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