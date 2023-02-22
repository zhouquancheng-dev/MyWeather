package com.zqc.mdoel.weather

import java.text.SimpleDateFormat
import java.util.*

/**
 * 星期几
 */
fun getWeek(time: Date?): String {
    time?.let {
        val simpleDateFormat = SimpleDateFormat("M月d日", Locale.CHINA)
        return simpleDateFormat.format(time)
    }
    return ""
}

/**
 * 根据传入的日期判断为：
 * 昨天、今天、明天；否则为周日、周一、周二、周三、周四、周五、周六
 */
fun getDayOrWeek(time: Date?): String {
    val day = 24 * 60 * 60 * 1000L
    time?.let {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        val simpleWeekFormat = SimpleDateFormat("E", Locale.CHINA)
        val today = simpleDateFormat.format(Date(System.currentTimeMillis()))
        val target = simpleDateFormat.format(time)
        val todayDate = simpleDateFormat.parse(today)?.time
        val targetDate = simpleDateFormat.parse(target)?.time
        return when (todayDate?.minus(targetDate!!)) {
            0L -> "今天"
            day -> "昨天"
            -day -> "明天"
            else -> {
                simpleWeekFormat.format(time)
            }
        }
    }
    return ""
}

/**
 * 小时 HH:mm
 */
fun getHourly(time: Date?): String {
    val day = 24 * 60 * 60 * 1000L
    time?.let {
        val simpleDateFormat = SimpleDateFormat("M月d日", Locale.CHINA)
        val simpleHourFormat = SimpleDateFormat("HH:mm", Locale.CHINA)
        val today = simpleDateFormat.format(Date(System.currentTimeMillis()))
        val target = simpleDateFormat.format(time)
        val todayDate = simpleDateFormat.parse(today)?.time
        val targetDate = simpleDateFormat.parse(target)?.time
        return when (todayDate?.minus(targetDate!!)) {
            -day -> {
                if (simpleHourFormat.format(time) == "00:00") {
                    simpleDateFormat.format(time)
                } else {
                    simpleHourFormat.format(time)
                }
            }
            else -> {
                simpleHourFormat.format(time)
            }
        }
    }
    return ""
}