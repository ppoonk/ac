package io.github.ppoonk.ac.utils

import kotlinx.serialization.json.Json

// 定义验证规则
enum class Validation(
    val regex: Regex, // 正则表达式，用于验证输入
    val tip: String,  // 验证失败时的提示信息
) {
    EMAIL(
        regex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"), // 邮箱格式正则
        tip = "邮箱格式: 123@qq.com，6~40位" // 邮箱格式提示
    ),
    PASSWORD(
        regex = Regex("^[A-Za-z0-9!@#\$%^&*()_+]{6,40}\$"), // 密码格式正则
        tip = "密码格式：123!@%_abc，6~40位" // 密码格式提示
    ),
    DecimalPlaces2(
        regex = Regex("^[0-9]+(\\.[0-9]{1,2})?\$"), // 保留两位小数的数字格式正则
        tip = "请输入数字 (两位小数)" // 提示信息
    ),
    OnlyNumber(
        regex = Regex("^[1-9]\\d*|0\$"), // 仅数字格式正则
        tip = "请输入数字" // 提示信息
    ),
}

// 包装验证结果
sealed class ValidationResult {
    data class Success(val res: String) : ValidationResult() // 验证成功
    data class Failure(val tip: String) : ValidationResult() // 验证失败，包含提示信息
}

object ValidationUtils {
    /**
     * 通用验证方法，验证字符串是否符合指定的规则
     *
     * @param string 待验证的字符串
     * @param regexes 验证规则
     * @return 验证结果，成功或失败
     */
    private fun validate(string: String, vararg regexes: Validation): ValidationResult {
        for (regex in regexes) {
            if (!regex.regex.matches(string)) { // 如果不匹配规则
                return ValidationResult.Failure(tip = regex.tip) // 返回失败结果
            }
        }
        return ValidationResult.Success(string) // 全部匹配则返回成功
    }

    /**
     * 验证邮箱格式
     *
     * @param string 待验证的字符串
     * @return 验证结果
     */
    fun validateEmail(string: String): ValidationResult {
        return validate(string, Validation.EMAIL)
    }

    /**
     * 验证密码格式
     *
     * @param string 待验证的字符串
     * @return 验证结果
     */
    fun validatePassword(string: String): ValidationResult {
        return validate(string, Validation.PASSWORD)
    }

    /**
     * 验证字符串是否为空
     *
     * @param string 待验证的字符串
     * @return 验证结果
     */
    fun validateEmpty(string: String): ValidationResult {
        return if (string.isEmpty()) {
            ValidationResult.Failure(tip = "数据为空") // 如果为空返回失败
        } else {
            ValidationResult.Success(string) // 非空返回成功
        }
    }

    private val jsonParser = Json { prettyPrint = true } // 创建 JSON 解析器

    /**
     * 验证字符串是否为合法的 JSON
     *
     * @param string 待验证的字符串
     * @return 验证结果
     */
    fun validateJson(string: String): ValidationResult {
        return try {
            val jsonElement = Json.parseToJsonElement(string) // 尝试解析 JSON
            val jsonStr = jsonParser.encodeToString(jsonElement) // 格式化 JSON
            ValidationResult.Success(jsonStr) // 返回成功结果
        } catch (e: Exception) {
            ValidationResult.Failure(e.message.toString()) // 解析失败返回错误信息
        }
    }

    /**
     * 验证字符串是否为保留两位小数的数字
     *
     * @param string 待验证的字符串
     * @return 验证结果
     */
    fun validateDecimalPlaces2(
        string: String,
        range: ClosedFloatingPointRange<Double>? = null
    ): ValidationResult {
        when (val res = validate(string, Validation.DecimalPlaces2)) {
            is ValidationResult.Failure -> {
                return res
            }

            is ValidationResult.Success -> {
                if (range == null || string.toDouble() in range) return res
                return ValidationResult.Failure(tip = "[ ${range.start} , ${range.endInclusive} ]")
            }
        }
    }
}