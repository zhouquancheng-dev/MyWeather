package com.zqc.network.service

import com.zqc.model.city.PlaceResponse
import com.zqc.mdoel.weather.CAI_YUN_TOKEN
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchPlacesService {
    /**
     * 搜索全球城市
     * @param token 令牌
     * @param lang 返回语言
     * @param address 请求地址名
     */
    @GET("/v2/place")
    suspend fun searchPlaces(
        @Query("token") token: String = CAI_YUN_TOKEN,
        @Query("lang") lang: String = "zh_CN",
        @Query("query") address: String
    ): PlaceResponse
}