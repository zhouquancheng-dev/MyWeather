package com.zqc.model.weather

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * 小时级别预报数据base
 */
data class HourlyResponse(val hourly: Hourly) {

    /**
     * @param status 请求状态
     * @param temperature 地表 2 米气温
     * @param wind 地表 10 米风向风速
     * @param skycon 天气现象
     * @param airQuality 国标 AQI
     */
    data class Hourly(
        val status: String,
        val temperature: List<Temperature>,
        val wind: List<Wind>,
        val skycon: List<SkyCon>,
        @SerializedName("air_quality") val airQuality: AirQuality
    ) {

        /**
         * @param datetime 时间戳
         * @param value 温度
         */
        data class Temperature(val datetime: Date, val value: Float)

        /**
         * @param datetime 时间戳
         * @param speed 风速
         * @param direction 风向
         */
        data class Wind(val datetime: Date, val speed: Float, val direction: Float)

        /**
         * @param datetime 时间戳
         * @param value 天气现象描述
         */
        data class SkyCon(val datetime: Date, val value: String)

        /**
         * 国标 AQI
         */
        data class AirQuality(val aqi: List<AQI>) {
            /**
             * @param datetime 时间戳
             * @param value aqi值
             */
            data class AQI(val datetime: Date, val value: AQIDesc) {
                /**
                 * @param chn 国标
                 */
                data class AQIDesc(val chn: Int)
            }
        }
    }
}