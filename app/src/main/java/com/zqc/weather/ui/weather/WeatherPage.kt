package com.zqc.weather.ui.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.google.accompanist.pager.*
import com.zqc.model.BaseModel
import com.zqc.model.WeatherUiState
import com.zqc.model.room.entity.CityInfo
import com.zqc.model.weather.Weather
import com.zqc.mdoel.lce.LcePage
import com.zqc.mdoel.weather.getSky
import com.zqc.weather.ui.weather.components.WeatherContent
import com.zqc.weather.ui.weather.components.WeatherTopBar
import com.zqc.weather.ui.weather.viewmodel.WeatherViewModel
import kotlin.math.absoluteValue

@ExperimentalPagerApi
@ExperimentalTextApi
@ExperimentalLifecycleComposeApi
@ExperimentalMaterialApi
@Composable
fun WeatherPage(
    weatherViewModel: WeatherViewModel,
    weatherData: WeatherUiState<BaseModel<Weather>>,
    pagerState: PagerState,
    cityInfoList: List<CityInfo>,
    toSettingClick: () -> Unit,
    toCityClick: () -> Unit,
    toDailyClick: (name: String, longitude: String, latitude: String, index :Int) -> Unit,
    toBottomSheetClick: (String) -> Unit,
    onRetryClick: () -> Unit,
) {
    LcePage(uiState = weatherData, onRetryClick = { onRetryClick() }) { result ->
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = getSky(result.result.realtime.skycon).bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
                backgroundColor = MaterialTheme.colors.background.copy(0f),
                topBar = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (cityInfoList.size == pagerState.pageCount) {
                            WeatherTopBar(
                                cityInfo = cityInfoList[pagerState.currentPage],
                                iconColor = Color.White,
                                toCityClick = toCityClick,
                                toSettingClick = toSettingClick
                            )
                            HorizontalPagerIndicator(
                                pagerState = pagerState,
                                indicatorWidth = 7.dp,
                                activeColor = Color.White
                            )
                        }
                    }
                }
            ) { paddingValues ->
                HorizontalPager(
                    count = cityInfoList.size,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    state = pagerState,
                    itemSpacing = 10.dp,
                ) {page ->
                    WeatherContent(
                        weatherViewModel = weatherViewModel,
                        weather = result,
                        currentPage = page,
                        cityInfoList = cityInfoList,
                        contentPadding = paddingValues,
                        modifier = Modifier
                            .graphicsLayer {
                                //计算当前页面距离滚动位置的绝对偏移量，然后根据偏移量来计算效果
                                val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                                //在 80% 到 100% 之间设置 scaleX + scaleY 的动画（缩放）
                                lerp(
                                    start = 0.8f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }
                                //在 50% 到 100% 之间设置 alpha 动画（透明度）
                                alpha = lerp(
                                    start = 0.5f,
                                    stop = 1f,
                                    fraction = 1f - pageOffset.coerceIn(0f, 1f)
                                )
                            },
                        toDailyClick = {
                            toDailyClick("未来趋势预报", cityInfoList[page].longitude, cityInfoList[page].latitude, it)
                        },
                        toBottomSheetClick = { toBottomSheetClick(it) }
                    )
                }
            }
        }
    }
}