package io.github.ppoonk.ac.utils

import kotlinx.serialization.json.Json

// 定义验证规则
enum class ValidationRule(
    val regex: Regex,
    val error: String,
) {
    // 邮箱格式
    EMAIL(
        regex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"),
        error = "Invalid email format"
    ),

    // 密码格式：6~40位，包含字母数字和常用符号
    // PASSWORD(regex = Regex("^[A-Za-z0-9!@#\$%^&*()_+]{6,40}\$")),
    PASSWORD(
        regex = Regex("^(?=.*[A-Za-z0-9\\p{Punct}])(?!0\\d*)[\\p{Alnum}\\p{Punct}]{6,40}$"),
        error = "Invalid password format"
    ),

    // 两位小数
    DECIMAL_PLACES_2(
        regex = Regex("^[0-9]+(\\.[0-9]{1,2})?\$"),
        error = "Please enter a number with 2 decimal places"
    ),

    // 仅数字
    NUMBER(regex = Regex("^[1-9]\\d*|0\$"), error = "Please enter a number")
}

// 包装验证结果
sealed class ValidationResult {
    data class Success(val res: String) : ValidationResult() // 验证成功，返回结果
    data class Failure(val error: String) : ValidationResult() // 验证失败，返回错误信息
}

object ValidationUtils {
    private fun baseValidate(
        input: String,
        regex: ValidationRule,
//        vararg regexes: ValidationRule,
    ): ValidationResult {
        if (!regex.regex.matches(input)) {
            return ValidationResult.Failure(regex.error)
        }
        return ValidationResult.Success(input)
    }

    fun validateEmail(input: String): ValidationResult {
        return baseValidate(input, ValidationRule.EMAIL)
    }

    fun validatePassword(input: String): ValidationResult {
        return baseValidate(input, ValidationRule.PASSWORD)
    }

    fun validateEmpty(input: String): ValidationResult {
        return if (input.isEmpty()) {
            ValidationResult.Failure("Invalid input")
        } else {
            ValidationResult.Success(input)
        }
    }

    private val jsonParser = Json { prettyPrint = true } // 美化输出

    fun validateJson(input: String): ValidationResult {
        return try {
            val jsonElement = Json.parseToJsonElement(input)            // 尝试解析 JSON
            val formattedJson = jsonParser.encodeToString(jsonElement) // 格式化 JSON
            ValidationResult.Success(formattedJson)
        } catch (e: Exception) {
            ValidationResult.Failure(e.message ?: "Json format error")
        }
    }

    fun validateDecimalPlaces2(
        input: String,
        range: ClosedFloatingPointRange<Double>? = null
    ): ValidationResult {
        // 首先验证输入是否符合格式
        when (val res = baseValidate(input, ValidationRule.DECIMAL_PLACES_2)) {
            // 如果格式验证失败，直接返回失败结果
            is ValidationResult.Failure -> {
                return res
            }
            // 判断范围
            is ValidationResult.Success -> {
                // 验证通过，返回成功结果
                if (range == null || input.toDouble() in range) return res
                // 不在指定范围内，返回范围错误提示
                return ValidationResult.Failure("Range must be between ${range.start} and ${range.endInclusive}")
            }
        }
    }

    fun validateNumber(
        input: String,
        range: ClosedRange<Int>? = null
    ): ValidationResult {
        // 首先验证输入是否符合格式
        when (val res = baseValidate(input, ValidationRule.NUMBER)) {
            // 如果格式验证失败，直接返回失败结果
            is ValidationResult.Failure -> {
                return res
            }
            // 判断范围
            is ValidationResult.Success -> {
                // 验证通过，返回成功结果
                if (range == null || input.toInt() in range) return res
                // 不在指定范围内，返回范围错误提示
                return ValidationResult.Failure("Range must be between ${range.start} and ${range.endInclusive}")
            }
        }
    }

    fun validateLength(
        input: String,
        range: ClosedRange<Int>
    ): ValidationResult {
        return if (input.length in range) {
            ValidationResult.Success(input)
        } else {
            ValidationResult.Failure("Range must be between ${range.start} and ${range.endInclusive}")
        }
    }

}