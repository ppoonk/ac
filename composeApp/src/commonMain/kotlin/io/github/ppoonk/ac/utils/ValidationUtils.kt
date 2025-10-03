package io.github.ppoonk.ac.utils

import kotlinx.serialization.json.Json

// 定义验证规则
enum class ValidationRule(
    val regex: Regex,
    val tip: String
) {
    EMAIL(
        regex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"),
        tip = "邮箱格式: 123@qq.com，6~40位"
    ),
    PASSWORD(
        regex = Regex("^[A-Za-z0-9!@#\$%^&*()_+]{6,40}\$"),
        tip = "密码格式：123!@%_abc，6~40位"
    ),
    DECIMAL_PLACES_2(
        regex = Regex("^[0-9]+(\\.[0-9]{1,2})?\$"),
        tip = "请输入数字 (两位小数)"
    ),
    ONLY_NUMBER(
        regex = Regex("^[1-9]\\d*|0\$"),
        tip = "请输入数字"
    );
}

// 包装验证结果
sealed class ValidationResult {
    data class Success(val res: String) : ValidationResult() // 验证成功
    data class Failure(val tip: String) : ValidationResult() // 验证失败，包含提示信息
}

object ValidationUtils {
    private fun validate(input: String, vararg regexes: ValidationRule): ValidationResult {
        for (regex in regexes) {
            if (!regex.regex.matches(input)) { // 如果不匹配规则
                return ValidationResult.Failure(tip = regex.tip) // 返回失败结果
            }
        }
        return ValidationResult.Success(input) // 全部匹配则返回成功
    }

    fun validateEmail(input: String): ValidationResult {
        return validate(input, ValidationRule.EMAIL)
    }

    fun validatePassword(input: String): ValidationResult {
        return validate(input, ValidationRule.PASSWORD)
    }


    fun validateEmpty(input: String): ValidationResult {
        return if (input.isEmpty()) {
            ValidationResult.Failure(tip = "数据为空")
        } else {
            ValidationResult.Success(input)
        }
    }

    private val jsonParser = Json { prettyPrint = true } // 创建 JSON 解析器，美化输出

    fun validateJson(input: String): ValidationResult {
        return try {
            val jsonElement = Json.parseToJsonElement(input) // 尝试解析 JSON
            val formattedJson = jsonParser.encodeToString(jsonElement) // 格式化 JSON
            ValidationResult.Success(formattedJson) // 返回成功结果
        } catch (e: Exception) {
            ValidationResult.Failure(e.message ?: "JSON 格式错误") // 解析失败返回错误信息
        }
    }

    fun validateDecimalPlaces2(
        input: String,
        range: ClosedFloatingPointRange<Double>? = null
    ): ValidationResult {
        // 首先验证输入是否符合两位小数的数字格式
        when (val res = validate(input, ValidationRule.DECIMAL_PLACES_2)) {
            // 如果格式验证失败，直接返回失败结果
            is ValidationResult.Failure -> {
                return res
            }
            // 尝试将输入转换为 Double 类型
            is ValidationResult.Success -> {
                // 验证通过，返回成功结果
                if (range == null || input.toDouble() in range) return res
                // 不在指定范围内，返回范围错误提示
                return ValidationResult.Failure(tip = "[ ${range.start} , ${range.endInclusive} ]")
            }
        }
    }
}