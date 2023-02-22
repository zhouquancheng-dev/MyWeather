package com.zqc.model.weather

/**
 * 综合数据bean
 */
data class Weather(
    val alert: AlertResponse.Alert,
    val realtime: RealtimeResponse.Realtime,
    val hourly: HourlyResponse.Hourly,
    val daily: DailyResponse.Daily,
)