package com.zqc.model.air

import java.util.*

/**
 * 墨迹天气生活指数Bean
 */
data class LifeIndexResponse(val code: Int, val data: DataDesc, val msg: String) {

    data class DataDesc(val city: CityDesc, val liveIndex: LiveIndexDesc) {

        data class CityDesc(
            val cityId: Int,
            val counname: String,
            val ianatimezone: String,
            val name: String,
            val pname: String,
            val secondaryname: String,
            val timezone: String
        )

        data class LiveIndexDesc(val index: List<LifeDesc>) {

            data class LifeDesc(
                val code: Int,
                val day: String,
                val desc: String,
                val level: String,
                val name: String,
                val status: String,
                val updatetime: Date
            )

        }

    }

}