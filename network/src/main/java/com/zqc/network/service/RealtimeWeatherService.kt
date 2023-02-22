package com.zqc.network.service

import com.zqc.model.BaseModel
import com.zqc.model.weather.RealtimeResponse
import com.zqc.mdoel.weather.CAI_YUN_TOKEN
import retrofit2.http.GET
import retrofit2.http.Path

interface RealtimeWeatherService {
    /**
     * 获取实时天气
     * @param token 令牌
     * @param longitude 纬度
     * @param latitude 经度
     */
    @GET("/v2.6/{token}/{longitude},{latitude}/realtime")
    suspend fun getRealtimeWeather(
        @Path("token") token: String = CAI_YUN_TOKEN,
        @Path("longitude") longitude: String,
        @Path("latitude") latitude: String
    ): BaseModel<RealtimeResponse>
}