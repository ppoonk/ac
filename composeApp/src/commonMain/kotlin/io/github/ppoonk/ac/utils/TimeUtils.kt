package io.github.ppoonk.ac.utils

import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.offsetAt
import kotlinx.datetime.toLocalDateTime
import kotlin.time.DurationUnit
import kotlin.time.toDuration

object TimeUtils {
    // 定义本地日期时间格式化器，用于格式化日期时间为指定格式
    private val localDateTimeFormatter = LocalDateTime.Format {
        year() // 年份
        char('-') // 分隔符
        monthNumber() // 月份
        char('-') // 分隔符
        dayOfMonth() // 日期
        char(' ') // 空格
        hour() // 小时
        char(':') // 分隔符
        minute() // 分钟
        char(':') // 分隔符
        second() // 秒
    }

    /**
     * 获取当前系统时间并转化为本地日期时间
     *
     * @return 当前本地日期时间
     */
    fun getCurrentDateTime(): LocalDateTime {
        val currentMoment = Clock.System.now() // 获取当前时间戳
        return currentMoment.toLocalDateTime(TimeZone.currentSystemDefault()) // 转化为本地时间
    }


    /**
     * 将时间戳转换为UTC时间(去除时区偏移)
     *
     * @param timestamp 时间戳（毫秒）
     * @return Instant UTC时间(无时区偏移)
     */
    fun timestampToUTCNoOffset(timestamp: Long): Instant {
        val ins = Instant.fromEpochMilliseconds(timestamp) // 将时间戳转化为 Instant
        val d = TimeZone.currentSystemDefault()
            .offsetAt(ins).totalSeconds.toDuration(DurationUnit.SECONDS) // 转化为 Duration
        return ins.plus(-d) // 减去偏移量，得到不含偏移量的 UTC 时间
    }


    /**
     * 将Instant时间点转换为本地日期字符串(仅包含年月日)
     *
     * @param instant 时间点
     * @return String 格式为 YYYY-MM-DD 的日期字符串
     */
    fun toLocalDateString(instant: Instant): String {
        return instant.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
    }

    /**
     * 将Instant时间点转换为格式化的本地日期时间字符串
     *
     * @param instant 时间点
     * @param formatter 日期时间格式化器，默认使用 yyyy/MM/dd HH:mm:ss 格式
     * @return String 格式化的本地日期时间字符串
     */
    fun toLocalDateFormattedString(
        instant: Instant,
        formatter: DateTimeFormat<LocalDateTime> = localDateTimeFormatter
    ): String {
        return instant.toLocalDateTime(TimeZone.currentSystemDefault()).format(
            formatter // 使用定义的格式化器
        )
    }

//    fun toLocalDateString(
//        s: String,
//        formatter: DateTimeFormat<LocalDateTime> = localDateTimeFormatter
//    ): String {
//        val instant = if (s.endsWith("Z")) {
//            // 处理带 Z 后缀的 UTC 时间字符串
//            kotlinx.datetime.Instant.parse(s)
//        } else {
//            // 处理不带时区的字符串，假定为 UTC
//            val localDateTime = LocalDateTime.parse(s)
//            localDateTime.toInstant(TimeZone.UTC)
//        }
//        return instant.toLocalDateTime(TimeZone.currentSystemDefault()).format(
//            formatter // 使用定义的格式化器
//        )
//    }
}
