package com.zqc.weather.ui.daily.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zqc.model.BaseModel
import com.zqc.model.WeatherLoading
import com.zqc.model.WeatherUiState
import com.zqc.model.weather.DailyResponse
import com.zqc.weather.ui.daily.repository.DailyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val context: Application,
    private val dailyRepository: DailyRepository
) : ViewModel() {

    private val _dailyWeather = MutableStateFlow<WeatherUiState<BaseModel<DailyResponse>>>(WeatherLoading)
    val dailyWeather: StateFlow<WeatherUiState<BaseModel<DailyResponse>>>
        get() = _dailyWeather.asStateFlow()

    fun getDailyWeather(longitude: String, latitude: String) {
        viewModelScope.launch {
            val result = dailyRepository.fetchDailyWeather(longitude, latitude)
            _dailyWeather.value = result
        }
    }

    /**
     * @return Int
     * -2 当前准备添加的城市已经存在;
     * -1 当前准备添加的城市已经超出最大添加数
     */
    suspend fun insertCityInfo(address: String, longitude: String, latitude: String): Int {
        val rowId = dailyRepository.insertCityInfo(address, longitude, latitude).toInt()
        when (rowId) {
            -2 -> {
                Toast.makeText(context, "当前城市或地区已添加", Toast.LENGTH_SHORT).show()
            }
            -1 -> {
                Toast.makeText(context, "城市数量已达上限，如需添加新的城市，请先删除已有的城市或地区", Toast.LENGTH_SHORT).show()
            }
            else -> {
                return rowId
            }
        }
        return rowId
    }

}