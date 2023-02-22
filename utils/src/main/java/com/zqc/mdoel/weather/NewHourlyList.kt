package com.zqc.mdoel.weather

import com.zqc.model.weather.HourlyResponse
import com.zqc.model.weather.RealtimeResponse
import java.util.*

fun getNewTemperatureList(
    hourly: HourlyResponse.Hourly,
    realtime: RealtimeResponse.Realtime,
    currentDate: Date = Date(System.currentTimeMillis())
): List<HourlyResponse.Hourly.Temperature> {
    val currentTep = HourlyResponse.Hourly.Temperature(currentDate, realtime.temperature)
    val newTempList: MutableList<HourlyResponse.Hourly.Temperature> = mutableListOf()
    newTempList.add(0, hourly.temperature[0])
    newTempList.add(1, currentTep)
    for (index in 2 until hourly.temperature.size + 1) {
        newTempList.add(index, hourly.temperature[index - 1])
    }
    return newTempList
}

fun getNewSkyList(
    hourly: HourlyResponse.Hourly,
    realtime: RealtimeResponse.Realtime,
    currentDate: Date = Date(System.currentTimeMillis())
): List<HourlyResponse.Hourly.SkyCon> {
    val currentSky = HourlyResponse.Hourly.SkyCon(currentDate, realtime.skycon)
    val newSkyList: MutableList<HourlyResponse.Hourly.SkyCon> = mutableListOf()
    newSkyList.add(0, hourly.skycon[0])
    newSkyList.add(1, currentSky)
    for (index in 2 until hourly.skycon.size + 1) {
        newSkyList.add(index, hourly.skycon[index - 1])
    }
    return newSkyList
}

fun getNewWindList(
    hourly: HourlyResponse.Hourly,
    realtime: RealtimeResponse.Realtime,
    currentDate: Date = Date(System.currentTimeMillis())
): List<HourlyResponse.Hourly.Wind> {
    val currentWind = HourlyResponse.Hourly.Wind(currentDate, realtime.wind.speed, realtime.wind.direction)
    val newWindList: MutableList<HourlyResponse.Hourly.Wind> = mutableListOf()
    newWindList.add(0, hourly.wind[0])
    newWindList.add(1, currentWind)
    for (index in 2 until hourly.skycon.size + 1) {
        newWindList.add(index, hourly.wind[index - 1])
    }
    return newWindList
}

fun getNewAqiList(
    hourly: HourlyResponse.Hourly,
    realtime: RealtimeResponse.Realtime,
    currentDate: Date = Date(System.currentTimeMillis())
): List<HourlyResponse.Hourly.AirQuality.AQI> {
    val currentAqiDesc = HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(realtime.airQuality.aqi.chn)
    val currentAqi = HourlyResponse.Hourly.AirQuality.AQI(currentDate, currentAqiDesc)
    val newAqiList: MutableList<HourlyResponse.Hourly.AirQuality.AQI> = mutableListOf()
    newAqiList.add(0, hourly.airQuality.aqi[0])
    newAqiList.add(1, currentAqi)
    for (index in 2 until hourly.airQuality.aqi.size + 1) {
        newAqiList.add(index, hourly.airQuality.aqi[index - 1])
    }
    return newAqiList
}