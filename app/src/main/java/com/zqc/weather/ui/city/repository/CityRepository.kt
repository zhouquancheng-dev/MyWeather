package com.zqc.weather.ui.city.repository

import android.app.Application
import android.util.Log
import com.amap.api.location.AMapLocation
import com.zqc.model.*
import com.zqc.model.city.PlaceResponse
import com.zqc.model.room.MyWeatherDatabase
import com.zqc.model.room.entity.CityInfo
import com.zqc.network.WeatherNetwork
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
class CityRepository @Inject constructor(
    context: Application,
    private val network: WeatherNetwork
){

    private val cityInfoDao = MyWeatherDatabase.getDatabase(context).cityInfoDao()

    private var addressResult: WeatherUiState<List<PlaceResponse.PlaceEntity>> = WeatherLoading

    suspend fun fetchCity(address: String): WeatherUiState<List<PlaceResponse.PlaceEntity>> {
        flow {
            emit(network.searchPlace(address))
        }
        .debounce(500)
        .catch {
            it.printStackTrace()
            Log.e("fetchCity", "onFailure: $it")
            WeatherError("搜索错误")
        }
        .collectLatest { placeResponse ->
            addressResult = if (placeResponse.status == SUCCESSFUL) {
                WeatherSuccess(placeResponse.places)
            } else {
                Log.i("fetchCity", "onFailure status: ${placeResponse.status}")
                WeatherError("搜索失败")
            }
        }
        return addressResult
    }

    fun refreshCityList() = cityInfoDao.loadAllCityInfoFlow()

    /**
     * 添加或更新定位信息
     */
    suspend fun updateCityInfo(aMapLocation: AMapLocation) {
        val cityInfo = buildCityInfo(aMapLocation)
        val cityInfoList = cityInfoDao.loadAllCityInfo()
        val isLocationList = cityInfoDao.loadIsLocationList()
        if (cityInfoList.isNotEmpty()) {
            if (isLocationList.isNotEmpty()) {
                cityInfo.id = isLocationList[0].id
                if (cityInfo != isLocationList[0]) {
                    cityInfoDao.updateCity(cityInfo)
                }
            } else {
                cityInfoDao.insertCity(cityInfo)
            }
        } else {
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

    suspend fun deleteCityInfo(cityInfo: CityInfo) {
        cityInfoDao.deleteCity(cityInfo)
    }

    fun isHasCity(address: String) = cityInfoDao.loadHasCityFlow(address)
}
