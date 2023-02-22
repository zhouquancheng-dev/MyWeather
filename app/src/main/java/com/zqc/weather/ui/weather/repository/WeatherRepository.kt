package com.zqc.weather.ui.weather.repository

import android.app.Application
import android.util.Log
import com.amap.api.location.AMapLocation
import com.zqc.model.*
import com.zqc.model.room.MyWeatherDatabase
import com.zqc.model.room.entity.CityInfo
import com.zqc.model.weather.Weather
import com.zqc.network.WeatherNetwork
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    context: Application,
    private val network: WeatherNetwork
) {

    private val cityInfoDao = MyWeatherDatabase.getDatabase(context).cityInfoDao()

    private var weatherResult: WeatherUiState<BaseModel<Weather>> = WeatherLoading

    /**
     * 获取综合天气
     */
    suspend fun fetchWeather(longitude: String, latitude: String): WeatherUiState<BaseModel<Weather>> {
        flow {
            emit(network.fetchWeather(longitude, latitude))
        }
        .catch {
            it.printStackTrace()
            Log.e("fetchWeather", "onFailure: $it")
            weatherResult = WeatherError("加载错误")
        }
        .conflate()
        .collect { weather ->
            weatherResult = if (weather.status == SUCCESSFUL) {
                WeatherSuccess(weather)
            } else {
                Log.i("fetchWeather", "onFailure status: ${weather.status}")
                WeatherError("加载失败")
            }
        }
        return weatherResult
    }

    /**
     * 查询表中有任何更改的通知
     */
    fun refreshCityList() = cityInfoDao.loadAllCityInfoFlow()

    /**
     * 查询经纬度
     */
    suspend fun queryCityLngLatInfo(id: Int) = cityInfoDao.loadLngLatInfo(id)

    /**
     *  首次添加定位信息（cityInfoList 为 Empty）
     */
    suspend fun updateCityInfo(aMapLocation: AMapLocation) {
        val cityInfo = buildCityInfo(aMapLocation)
        val cityInfoList = cityInfoDao.loadAllCityInfo()
        //在第一次打开app城市管理中size为 0 或者为空 的情况下就直接顺序在第一位插入定位信息
        if (cityInfoList.isEmpty()) {
            cityInfoDao.insertCity(cityInfo)
        }
    }

    /**
     * 创建 CityInfo()
     */
    private fun buildCityInfo(aMapLocation: AMapLocation): CityInfo {
        return CityInfo(
            isLocation = 1,
            longitude = aMapLocation.longitude.toString(),
            latitude = aMapLocation.latitude.toString(),
            address = "${aMapLocation.province}${aMapLocation.city}${aMapLocation.district}${aMapLocation.street}",
            province = aMapLocation.province,
            city = aMapLocation.city,
            district = aMapLocation.district,
            street = aMapLocation.street
        )
    }
}