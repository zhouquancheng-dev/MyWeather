package com.zqc.mdoel.weather

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * 判断日期时间为并显示为 M月d日 格式
 * @param time 格式 Date类型
 */
fun getWeek(time: Date?): String {
    time?.let {
        val simpleDateFormat = SimpleDateFormat("M月d日", Locale.CHINA)
        return simpleDateFormat.format(time)
    }
    return ""
}

/**
 * 根据传入的日期时间判断为：
 *  昨天、今天、明天；否则为周日、周一、周二、周三、周四、周五、周六
 * @param time 格式 Date类型
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
 * 判断日期时间为并显示为 HH:mm 格式
 * @param time 格式 Date类型
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

/**
 * 判断当前时间[System.currentTimeMillis]是否小于指定的时间（在指定时间之前）
 * @param time 传入指定时间字符串 必须如格式：”HH::mm:ss“
 * @return 当前时间在指定时间[time]之前(小于)返回 true 否则返回 false
 */
@SuppressLint("NewApi")
fun timeCompareBefore(time: String): Boolean {
    val df = DateTimeFormatter.ofPattern("HH:mm")
    val localTime = LocalTime.parse(time, df)
    return LocalTime.now().isBefore(localTime)
}

/**
 * 判断当前时间[System.currentTimeMillis]是否大于指定的时间（在指定时间之后）
 * @param time 传入指定时间字符串 必须如格式：”HH::mm:ss“
 * @return 当前时间在指定时间[time]之后(大于)返回 true 否则返回 false
 */
@SuppressLint("NewApi")
fun timeCompareAfter(time: String): Boolean {
    val df = DateTimeFormatter.ofPattern("HH:mm")
    val localTime = LocalTime.parse(time, df)
    return LocalTime.now().isAfter(localTime)
}