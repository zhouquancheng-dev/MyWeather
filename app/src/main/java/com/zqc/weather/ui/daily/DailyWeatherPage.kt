package com.zqc.weather.ui.daily

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zqc.model.WeatherLoading
import com.zqc.mdoel.lce.LcePage
import com.zqc.weather.ui.daily.components.DailyWeatherContent
import com.zqc.weather.ui.daily.viewmodel.DailyViewModel

@ExperimentalLifecycleComposeApi
@ExperimentalTextApi
@Composable
fun DailyWeatherPage(
    dailyViewModel: DailyViewModel,
    title: String,
    position: Int,
    onBackClick: () -> Unit,
    toWeatherClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    val daily by dailyViewModel.dailyWeather.collectAsStateWithLifecycle(WeatherLoading)
    LcePage(uiState = daily, onRetryClick = { onRetryClick() }) { result ->
        DailyWeatherContent(
            daily = listOf(result.result.daily),
            title = title,
            position = position,
            onBackClick = onBackClick,
            toWeatherClick = toWeatherClick
        )
    }
}