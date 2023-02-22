package com.zqc.network

import com.zqc.network.service.*
import javax.inject.Inject

class WeatherNetwork @Inject constructor(){
    /**
     * 不使用泛型实化： private val loginService = ServiceCreator.create(XXXService::class.java)
     * 使用泛型实化： private val loginService = ServiceCreator.create<XXXService>()
     */

    //使用ServiceCreator创建一个PlaceService接口的动态代理对象

    private val searchPlacesService = ServiceCreator.createCaiYunApi<SearchPlacesService>()
    suspend fun searchPlace(address: String) = searchPlacesService.searchPlaces(address = address)

    private val realtimeWeatherService = ServiceCreator.createCaiYunApi<RealtimeWeatherService>()
    suspend fun fetchRealtimeWeather(longitude: String, latitude: String) =
        realtimeWeatherService.getRealtimeWeather(longitude = longitude, latitude = latitude)

    private val hourlyWeatherService = ServiceCreator.createCaiYunApi<HourlyWeatherService>()
    suspend fun fetchHourlyWeather(longitude: String, latitude: String, hourlySteps: Int) =
        hourlyWeatherService.getHourlyWeather(longitude = longitude, latitude = latitude, hourlySteps = hourlySteps)

    private val dailyWeatherService = ServiceCreator.createCaiYunApi<DailyWeatherService>()
    suspend fun fetchDailyWeather(longitude: String, latitude: String, begin: Long) =
        dailyWeatherService.getDailyWeather(longitude = longitude, latitude = latitude, begin = begin)

    private val weatherService = ServiceCreator.createCaiYunApi<WeatherService>()
    suspend fun fetchWeather(longitude: String, latitude: String) =
        weatherService.getWeather(longitude = longitude, latitude = latitude)
}