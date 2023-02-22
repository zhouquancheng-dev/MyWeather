package com.zqc.weather.ui.daily.repository

import android.app.Application
import android.util.Log
import com.zqc.model.*
import com.zqc.model.room.MyWeatherDatabase
import com.zqc.model.room.entity.CityInfo
import com.zqc.model.weather.DailyResponse
import com.zqc.network.WeatherNetwork
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DailyRepository @Inject constructor(
    context: Application,
    private val network: WeatherNetwork
) {

    private val cityInfoDao = MyWeatherDatabase.getDatabase(context).cityInfoDao()

    private val yesTodayDate = (System.currentTimeMillis() / 1000) - (24 * 60 * 60)

    private var dailyResult: WeatherUiState<BaseModel<DailyResponse>> = WeatherLoading

    suspend fun fetchDailyWeather(
        longitude: String,
        latitude: String,
        begin: Long = yesTodayDate
    ): WeatherUiState<BaseModel<DailyResponse>> {
        flow {
            emit(network.fetchDailyWeather(longitude, latitude, begin))
        }
        .catch {
            it.printStackTrace()
            Log.e("fetchDailyWeather", "onFailure: $it")
            dailyResult = WeatherError("加载错误")
        }
        .conflate()
        .collect { dailyResponse ->
            dailyResult = if (dailyResponse.status == SUCCESSFUL) {
                WeatherSuccess(dailyResponse)
            } else {
                Log.i("fetchDailyWeather", "onFailure status: ${dailyResponse.status}")
                WeatherError("加载失败")
            }
        }
        return dailyResult
    }

    /**
     * 添加城市信息到数据库
     * @return Long
     * -2L 当前准备添加的城市已经存在
     * -1L 当前准备添加的城市已经超出最大添加数
     */
    suspend fun insertCityInfo(address: String, longitude: String, latitude: String): Long {
        val cityInfo = buildCityInfo(address, longitude, latitude)
        val hasCityInfo = cityInfoDao.loadHasCity(address)
        val cityInfoCount = cityInfoDao.loadCount()
        return if (hasCityInfo != null) {
            -2L
        } else {
            if (cityInfoCount < 10) cityInfoDao.insertCity(cityInfo) else -1L
        }
    }

    /**
     * 创建 CityInfo()
     */
    private fun buildCityInfo(address: String, longitude: String, latitude: String): CityInfo {
        return CityInfo(
            address = address,
            longitude = longitude,
            latitude = latitude
        )
    }
}