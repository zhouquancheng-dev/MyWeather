package com.zqc.weather.ui.city.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amap.api.location.AMapLocation
import com.zqc.model.WeatherLoading
import com.zqc.model.WeatherUiState
import com.zqc.model.city.PlaceResponse
import com.zqc.model.room.entity.CityInfo
import com.zqc.weather.permission.MyLocationListener
import com.zqc.weather.ui.city.repository.CityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@HiltViewModel
class CityViewModel @Inject constructor(
    private val context: Application,
    private val cityRepository: CityRepository,
    private val myLocationListener: MyLocationListener
) : ViewModel() {

    val cityInfoList: Flow<List<CityInfo>> = cityRepository.refreshCityList()

    private val _searchLiveData = MutableStateFlow<WeatherUiState<List<PlaceResponse.PlaceEntity>>>(WeatherLoading)
    val searchLiveData: StateFlow<WeatherUiState<List<PlaceResponse.PlaceEntity>>>
        get() = _searchLiveData.asStateFlow()

    fun searchPlace(address: String) {
        viewModelScope.launch {
            val result = cityRepository.fetchCity(address)
            _searchLiveData.value = result
        }
    }

    /**
     * 点击城市管理中按钮手动定位信息
     */
    fun getLocation(callback: () -> Unit) {
        Toast.makeText(context, "正在定位中", Toast.LENGTH_SHORT).show()
        myLocationListener.apply {
            initLocation()
            startLocation()
            setLocationListener(object : MyLocationListener.LocationListener{
                override fun onSuccess(aMapLocation: AMapLocation) {
                    updateCityInfo(aMapLocation)
                    Toast.makeText(context, "定位成功", Toast.LENGTH_SHORT).show()
                    callback()
                }

                override fun onFailure() {
                    /* TODO */
                }
            })
        }
    }

    /**
     * 更新定位位置
     */
    fun updateCityInfo(aMapLocation: AMapLocation) {
        viewModelScope.launch(Dispatchers.IO) {
            cityRepository.updateCityInfo(aMapLocation)
        }
    }

    /**
     * 删除对应的城市
     */
    fun deleteCityInfo(cityInfo: CityInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            cityRepository.deleteCityInfo(cityInfo)
        }
    }

    /**
     * 确认是否添加过此城市
     */
    fun isHasCity(address: String) = cityRepository.isHasCity(address)
}

