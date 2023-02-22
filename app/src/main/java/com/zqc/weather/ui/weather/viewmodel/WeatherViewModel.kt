package com.zqc.weather.ui.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.location.AMapLocation
import com.zqc.model.*
import com.zqc.model.room.entity.CityInfo
import com.zqc.model.weather.Weather
import com.zqc.weather.permission.MyLocationListener
import com.zqc.weather.ui.weather.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.measureTimeMillis

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val myLocationListener: MyLocationListener
) : ViewModel() {

    private val _locate = MutableStateFlow("当前无内容")
    val locate: StateFlow<String> = _locate.asStateFlow()

    private val _weatherData = MutableStateFlow<WeatherUiState<BaseModel<Weather>>>(WeatherLoading)
    val weatherData: StateFlow<WeatherUiState<BaseModel<Weather>>> = _weatherData.asStateFlow()

    /**
     * 获取综合天气
     */
    fun getWeather(currentIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val cityInfo = weatherRepository.queryCityLngLatInfo(currentIndex)
            val result = weatherRepository.fetchWeather(cityInfo.longitude, cityInfo.latitude)
            _weatherData.value = result
        }
    }

    /**
     * 获取高德定位信息
     */
    fun getLocation() {
        myLocationListener.apply {
            _locate.value = "正在定位"
            initLocation()
            startLocation()
            setLocationListener(object : MyLocationListener.LocationListener{
                override fun onSuccess(aMapLocation: AMapLocation) {
                    updateCityInfo(aMapLocation)
                    viewModelScope.launch {
                        val time = measureTimeMillis {
                            val result = weatherRepository.fetchWeather(
                                aMapLocation.longitude.toString(),
                                aMapLocation.latitude.toString()
                            )
                            _weatherData.value = result
                        }
                        delay(time)
                    }
                }

                override fun onFailure() {
                    /* TODO */
                }

            })
        }
    }

    val cityInfoList: Flow<List<CityInfo>> = weatherRepository.refreshCityList()

    /**
     * 添加定位位置
     */
    fun updateCityInfo(aMapLocation: AMapLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.updateCityInfo(aMapLocation)
        }
    }

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    /**
     * 下拉刷新
     */
    fun refresh(currentPage: Int) {
        viewModelScope.launch {
            _isRefreshing.value = true
            val time = measureTimeMillis {
                getWeather(currentPage)
            }
            delay(if (time > 1000L) time else 1000L)
            _isRefreshing.value = false
        }
    }

}