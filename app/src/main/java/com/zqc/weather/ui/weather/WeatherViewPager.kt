package com.zqc.weather.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.zqc.model.WeatherLoading
import com.zqc.model.WeatherNoContent
import com.zqc.model.room.entity.CityInfo
import com.zqc.mdoel.lce.NoContent
import com.zqc.weather.permission.FeatureThatRequiresLocationPermissions
import com.zqc.weather.ui.weather.components.WeatherTopBar
import com.zqc.weather.ui.weather.viewmodel.WeatherViewModel

@ExperimentalPagerApi
@ExperimentalPermissionsApi
@ExperimentalLifecycleComposeApi
@ExperimentalTextApi
@ExperimentalMaterialApi
@Composable
fun WeatherViewPager(
    weatherViewModel: WeatherViewModel,
    pagerState: PagerState,
    backSelect: Int,
    toSettingClick: () -> Unit,
    toCityManage: () -> Unit,
    toDailyClick: (name: String, longitude: String, latitude: String, index :Int) -> Unit,
    toBottomSheetClick: (String) -> Unit
) {
    val cityInfoList by weatherViewModel.cityInfoList.collectAsStateWithLifecycle(null)
    val weatherData by weatherViewModel.weatherData.collectAsStateWithLifecycle(WeatherLoading)
    val located by weatherViewModel.locate.collectAsStateWithLifecycle()

    if (cityInfoList == null || cityInfoList.isNullOrEmpty()) {
        if (cityInfoList?.size == 0) {
            if (pagerState.currentPage == 0) {
                FeatureThatRequiresLocationPermissions(weatherViewModel = weatherViewModel)
            }
        }
        NoCityContent(
            location = located,
            toCityManage = { toCityManage() },
            toSettingClick = { toSettingClick() }
        )
    } else {
        CurrentPageEffect(
            pagerState = pagerState,
            weatherViewModel = weatherViewModel,
            cityInfoList = cityInfoList ?: emptyList(),
            backSelect = backSelect
        )
        WeatherPage(
            weatherViewModel = weatherViewModel,
            weatherData = weatherData,
            pagerState = pagerState,
            cityInfoList = cityInfoList ?: emptyList(),
            toSettingClick = toSettingClick,
            toCityClick = toCityManage,
            toDailyClick = { name, longitude, latitude, index ->
                toDailyClick(name, longitude, latitude, index)
            },
            onRetryClick = { weatherViewModel.getWeather(cityInfoList!![pagerState.currentPage].id) },
            toBottomSheetClick = { toBottomSheetClick(it) }
        )
    }
}

@ExperimentalPagerApi
@Composable
private fun CurrentPageEffect(
    pagerState: PagerState,
    weatherViewModel: WeatherViewModel,
    cityInfoList: List<CityInfo>,
    backSelect: Int
) {
    if (cityInfoList.isNotEmpty()) {
        LaunchedEffect(pagerState.currentPage) {
            if (backSelect == -1) {
                val index = if (pagerState.currentPage > cityInfoList.size - 1) 0 else pagerState.currentPage
                weatherViewModel.getWeather(cityInfoList[index].id)
            } else {
                if (pagerState.isScrollInProgress) {
                    snapshotFlow { pagerState.currentPage }.collect { page ->
                        weatherViewModel.getWeather(cityInfoList[page].id)
                    }
                } else {
                    weatherViewModel.getWeather(cityInfoList[backSelect].id)
                }
            }
        }
    }
}

@ExperimentalPagerApi
@Composable
private fun NoCityContent(
    location: String,
    toSettingClick: () -> Unit,
    toCityManage: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
        WeatherTopBar(
            cityInfo = CityInfo(address = ""),
            iconColor = MaterialTheme.colors.onSurface,
            toCityClick = toCityManage,
            toSettingClick = toSettingClick
        )
        NoContent(weatherNoContent = WeatherNoContent(location))
    }
}