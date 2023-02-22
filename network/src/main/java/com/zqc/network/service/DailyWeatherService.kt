package com.zqc.network.service

import com.zqc.model.BaseModel
import com.zqc.model.weather.DailyResponse
import com.zqc.mdoel.weather.CAI_YUN_TOKEN
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DailyWeatherService {
    /**
     * 获取未来的天气信息
     * @param token 令牌
     * @param longitude 纬度
     * @param latitude 经度
     * @param dailySteps 返回未来天数
     * @param begin 过去一天的 Unix 时间戳
     */
    @GET("/v2.6/{token}/{longitude},{latitude}/daily")
    suspend fun getDailyWeather(
        @Path("token") token: String = CAI_YUN_TOKEN,
        @Path("longitude") longitude: String,
        @Path("latitude") latitude: String,
        @Query("dailysteps") dailySteps: Int = 16,
        @Query("begin") begin: Long
    ): BaseModel<DailyResponse>
}