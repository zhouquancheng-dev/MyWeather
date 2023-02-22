package com.zqc.model.weather

import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * 天级别预报数据base
 */
data class DailyResponse(val daily: Daily) {

    /**
     * @param status 状态
     * @param astro 日出日落时间
     * @param temperature 全天地表 2 米气温
     * @param skyCon08h20h 白天主要天气现象
     * @param skyCon20h32h 夜晚主要天气现象
     * @param wind 全天地表 10 米风速风向
     * @param airQuality 全天空气质量
     * @param lifeIndex 生活指数
     */
    data class Daily(
        val status: String,
        val astro: List<Astro>,
        val temperature: List<Temperature>,
        @SerializedName("skycon_08h_20h") val skyCon08h20h: List<SkyCon>,
        @SerializedName("skycon_20h_32h") val skyCon20h32h: List<SkyCon>,
        val wind: List<Wind>,
        @SerializedName("air_quality") val airQuality: AirQuality,
        @SerializedName("life_index") val lifeIndex: LifeIndex
    ) {

        /**
         * @param sunrise 日出
         * @param sunset 日落
         */
        data class Astro(val sunrise: SunDesc, val sunset: SunDesc) {
            data class SunDesc(val time: String)
        }

        /**
         * @param max 最高温度
         * @param min 最低温度
         * @param date 时间戳
         */
        data class Temperature(val max: Float, val min: Float, val date: Date)

        /**
         * @param value 天气现象代码
         * @param date 时间戳
         */
        data class SkyCon(val value: String, val date: Date)

        /**
         * @param avg 全天平均风速风向
         */
        data class Wind(val avg: WindDesc) {
            /**
             * @param speed 风速
             * @param direction 风向
             */
            data class WindDesc(val speed: Float, val direction: Float)
        }

        /**
         * @param aqi 国标 AQI
         * @param pm25 PM2.5 浓度(μg/m3)
         */
        data class AirQuality(val aqi: List<AQI>, val pm25: List<Pm25>) {
            /**
             * @param date 时间戳
             * @param max aqi最大值
             * @param min aqi最小值
             * @param avg aqi平均值
             */
            data class AQI(val date: Date, val max: AQIDesc, val min: AQIDesc, val avg: AQIDesc) {
                /**
                 * @param chn 国标aqi
                 */
                data class AQIDesc(val chn: Int)
            }

            /**
             * @param max pm2.5最大值
             * @param min pm2.5最小值
             * @param avg pm2.5平均值
             */
            data class Pm25(val max: Int, val min: Int, val avg: Int)
        }

        /**
         * @param ultraviolet 紫外线指数自然语言
         * @param comfort 舒适度指数自然语言
         * @param carWashing 洗车指数自然语言
         * @param coldRisk 感冒指数自然语言
         * @param dressing 穿衣指数自然语言
         */
        data class LifeIndex(
            val ultraviolet: List<LifeDesc>,
            val comfort: List<LifeDesc>,
            val carWashing: List<LifeDesc>,
            val coldRisk: List<LifeDesc>,
            val dressing: List<LifeDesc>
        ) {
            /**
             * @param index 等级
             * @param desc 自然语言描述
             */
            data class LifeDesc(val index: String, val desc: String)
        }
    }
}