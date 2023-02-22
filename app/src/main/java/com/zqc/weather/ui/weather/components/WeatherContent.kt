package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zqc.model.BaseModel
import com.zqc.model.room.entity.CityInfo
import com.zqc.model.weather.Weather
import com.zqc.weather.R
import com.zqc.weather.ui.weather.viewmodel.WeatherViewModel

@ExperimentalLifecycleComposeApi
@ExperimentalTextApi
@ExperimentalMaterialApi
@Composable
fun WeatherContent(
    modifier: Modifier = Modifier,
    weatherViewModel: WeatherViewModel,
    weather: BaseModel<Weather>,
    currentPage: Int,
    cityInfoList: List<CityInfo>,
    contentPadding: PaddingValues,
    toDailyClick: (Int) -> Unit,
    toBottomSheetClick: (String) -> Unit
) {
    val refreshing by weatherViewModel.isRefreshing.collectAsStateWithLifecycle(false)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { weatherViewModel.refresh(cityInfoList[currentPage].id) }
    )
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = contentPadding
    ) {
        item { PullRefreshIndicator(refreshing = refreshing, state = pullRefreshState) }
        realtimeItem(weather.result.realtime)
        airQualityRainItem(weather.result.realtime.airQuality)
        threeDaysItem(
            daily = listOf(weather.result.daily),
            toDailyClick = { toDailyClick(it) }
        )
        hourlyItem(listOf(weather.result.hourly), weather.result.realtime)
        otherInfoItem(weather.result)
        bottomSheet(
            lifeIndex = weather.result.daily,
            toBottomSheetClick = { toBottomSheetClick(it) }
        )
        item {
            Text(
                text = stringResource(R.string.eg_cai_yun),
                fontSize = 12.sp,
                color = Color.White.copy(0.7f),
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}