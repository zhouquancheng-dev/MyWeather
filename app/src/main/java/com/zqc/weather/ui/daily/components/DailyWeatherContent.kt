package com.zqc.weather.ui.daily.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import com.zqc.model.weather.DailyResponse

@ExperimentalTextApi
@Composable
fun DailyWeatherContent(
    daily: List<DailyResponse.Daily>,
    title: String,
    position: Int,
    onBackClick: () -> Unit,
    toWeatherClick: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        topBar = {
            DailyHeaderContent(
                title = title,
                onBackClick = onBackClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DailyLazyRowContent(
                daily = daily,
                position = position,
                modifier = Modifier.padding(paddingValues)
            )
            if (title != "未来趋势预报") {
                AddButton(toWeatherClick)
            }
        }
    }
}