package com.zqc.network.service

import com.zqc.model.BaseModel
import com.zqc.model.weather.HourlyResponse
import com.zqc.mdoel.weather.CAI_YUN_TOKEN
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HourlyWeatherService {
    /**
     * 获取小时级别预报
     * @param token 令牌
     * @param longitude 纬度
     * @param latitude 经度
     * @param hourlySteps 返回未来小时数
     */
    @GET("/v2.6/{token}/{longitude},{latitude}/hourly")
    suspend fun getHourlyWeather(
        @Path("token") token: String = CAI_YUN_TOKEN,
        @Path("longitude") longitude: String,
        @Path("latitude") latitude: String,
        @Query("hourlysteps") hourlySteps: Int,
    ): BaseModel<HourlyResponse>
}