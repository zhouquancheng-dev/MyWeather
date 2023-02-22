package com.zqc.weather.ui.daily.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.model.weather.DailyResponse
import com.zqc.mdoel.theme.DailyWeekColor
import com.zqc.mdoel.view.AdaptationScreenWidth
import com.zqc.mdoel.weather.*

@ExperimentalTextApi
@Composable
fun DailyLazyRowContent(
    daily: List<DailyResponse.Daily>,
    position: Int,
    modifier: Modifier = Modifier
) {
    AdaptationScreenWidth {
        LazyRow(
            modifier = modifier
                .fillMaxWidth()
                .height(550.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(
                items = daily,
                contentType = { it.skyCon08h20h + it.temperature + it.skyCon20h32h + it.wind + it.airQuality.aqi }
            ) {
                DailyLazyItem(daily = it, position = position)
            }
        }
    }
}

@ExperimentalTextApi
@Composable
private fun DailyLazyItem(
    daily: DailyResponse.Daily,
    position: Int
) {
    Column {
        DailySkyAm(daily, position)
        DailyTempBrokenLine(daily, position)
        DailySkyPm(daily, position)
        DailyWind(daily, position)
        DailyAqi(daily, position)
    }
}

@Composable
private fun DailySkyAm(
    daily: DailyResponse.Daily,
    position: Int
) {
    Row {
        daily.skyCon08h20h.forEachIndexed { index, skyCon ->
            Surface(
                modifier = Modifier.height(150.dp),
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                color = bgColor(index = index, position = position)
            ) {
                Column(
                    modifier = Modifier.width(80.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(text = getDayOrWeek(skyCon.date), color = textColor(date = getDayOrWeek(skyCon.date)))
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = getWeek(skyCon.date),
                        fontSize = 15.sp,
                        color = if (getDayOrWeek(skyCon.date) == "昨天") Color.Gray else DailyWeekColor
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Image(
                        modifier = Modifier.size(26.dp),
                        painter = painterResource(id = getSky(skyCon.value).icon),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(getSky(skyCon.value).info),
                        fontWeight = FontWeight.Bold,
                        color = textColor(date = getDayOrWeek(skyCon.date))
                    )
                }
            }
        }
    }
}

@ExperimentalTextApi
@Composable
private fun DailyTempBrokenLine(
    daily: DailyResponse.Daily,
    position: Int
) {
    Row {
        daily.temperature.forEachIndexed { index, temperature ->
            Surface(
                color = bgColor(index = index, position = position)
            ) {
                Box(modifier = Modifier.height(210.dp), contentAlignment = Alignment.Center) {
                    DailyTemperatureBrokenLineCanvas(
                        dailyTemperature = daily.temperature,
                        dayCurrentTep = temperature.max.toInt(),
                        nightCurrentTep = temperature.min.toInt(),
                        indexTep = index
                    )
                }
            }
        }
    }
}

@Composable
private fun DailySkyPm(
    daily: DailyResponse.Daily,
    position: Int
) {
    Row {
        daily.skyCon20h32h.forEachIndexed { index, skyCon ->
            Surface(
                color = bgColor(index = index, position = position)
            ) {
                Column(
                    modifier = Modifier
                        .width(80.dp)
                        .height(70.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(26.dp),
                        painter = painterResource(id = getSky(skyCon.value).icon),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = stringResource(getSky(skyCon.value).info),
                        fontWeight = FontWeight.Bold,
                        color = textColor(date = getDayOrWeek(skyCon.date))
                    )
                }
            }
        }
    }
}

@Composable
private fun DailyWind(
    daily: DailyResponse.Daily,
    position: Int
) {
    Row {
        daily.wind.forEachIndexed { index, wind ->
            Surface(
                color = bgColor(index = index, position = position)
            ) {
                Row(
                    modifier = Modifier
                        .width(80.dp)
                        .height(50.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    getWindDirection(wind.avg.direction).icon?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = null,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    getWindSpeed(wind.avg.speed).speed?.let {
                        Text(text = it, fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyAqi(
    daily: DailyResponse.Daily,
    position: Int
) {
    Row {
        daily.airQuality.aqi.forEachIndexed { index, aqi ->
            Surface(
                modifier = Modifier.height(50.dp),
                shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp),
                color = bgColor(index = index, position = position)
            ) {
                Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.TopCenter) {
                    if (getDayOrWeek(aqi.date) != "昨天") {
                        Surface(
                            modifier = Modifier.width(60.dp),
                            color = getAqiInfo(aqi.max.chn).color.copy(0.1f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
                                Text(
                                    text = stringResource(getAqiInfo(aqi.max.chn).grade),
                                    color = getAqiInfo(aqi.max.chn).color,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * @param index
 * @param position
 */
@Composable
private fun bgColor(
    index: Int,
    position: Int
): Color {
    return if (index == position) {
        MaterialTheme.colors.onSurface.copy(0.03f)
    } else {
        MaterialTheme.colors.surface
    }
}

/**
 * @param date
 */
@Composable
private fun textColor(
    date: String
): Color {
    return if (date == "昨天") {
        Color.Gray
    } else {
        MaterialTheme.colors.onSurface
    }
}