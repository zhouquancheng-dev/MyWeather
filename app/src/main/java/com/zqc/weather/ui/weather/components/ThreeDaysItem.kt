package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.mdoel.view.AdaptationScreenWidth
import com.zqc.mdoel.view.noRippleClickable
import com.zqc.model.weather.DailyResponse
import com.zqc.mdoel.weather.getAqiInfo
import com.zqc.mdoel.weather.getSky
import com.zqc.mdoel.weather.getDayOrWeek
import com.zqc.weather.R

fun LazyListScope.threeDaysItem(
    daily: List<DailyResponse.Daily>,
    toDailyClick: (Int) -> Unit
) {
    item(contentType = { daily }) {
        AdaptationScreenWidth {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .padding(horizontal = 15.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(195.dp),
                        contentPadding = PaddingValues(horizontal = 15.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        items(
                            items = daily,
                            contentType = { it.skyCon08h20h + it.airQuality + it.temperature }
                        ) { item ->
                            ThreeDailyItem(daily = item, toDailyClick = { toDailyClick(it) })
                        }
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 20.dp),
                        shape = RoundedCornerShape(60),
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onBackground.copy(0.1f)),
                        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp, pressedElevation = 1.dp),
                        onClick = { toDailyClick(1) }
                    ) {
                        Text(
                            text = stringResource(id = R.string.to_daily_page_button),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThreeDailyItem(
    daily: DailyResponse.Daily,
    toDailyClick: (index: Int) -> Unit
) {
    Row {
        Sky(
            sky = daily,
            modifier = Modifier.weight(3.5f),
            toDailyClick = { toDailyClick(it + 1) }
        )
        AqiInfo(
            aqi = daily,
            modifier = Modifier.weight(1f),
            toDailyClick = { toDailyClick(it + 1) }
        )
        Temperature(
            temp = daily,
            modifier = Modifier.weight(1.2f),
            toDailyClick = { toDailyClick(it + 1) }
        )
    }
}

@Composable
private fun Sky(
    sky: DailyResponse.Daily,
    modifier: Modifier = Modifier,
    toDailyClick: (index: Int) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        sky.skyCon08h20h.forEachIndexed { index, skyCon08h20h ->
            Row(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .noRippleClickable { toDailyClick(index) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = getSky(skyCon08h20h.value).icon),
                    contentDescription = null,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = getDayOrWeek(skyCon08h20h.date), fontSize = 16.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(getSky(skyCon08h20h.value).info),
                    fontSize = 16.sp, maxLines = 2
                )
                if (getSky(skyCon08h20h.value).info != getSky(sky.skyCon20h32h[index].value).info) {
                    Text(text = stringResource(id = R.string.to_text))
                    Text(
                        text = stringResource(getSky(sky.skyCon20h32h[index].value).info),
                        fontSize = 16.sp, maxLines = 2
                    )
                }
            }
        }
    }
}

@Composable
private fun AqiInfo(
    aqi: DailyResponse.Daily,
    modifier: Modifier = Modifier,
    toDailyClick: (index: Int) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        aqi.airQuality.aqi.forEachIndexed { index, aqi ->
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .noRippleClickable { toDailyClick(index) },
                contentAlignment = Alignment.CenterStart
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = getAqiInfo(aqi.max.chn).color.copy(0.1f),
                    modifier = Modifier.width(35.dp)
                ) {
                    Box(modifier = Modifier.padding(4.dp)) {
                        Text(
                            text = stringResource(getAqiInfo(aqi.max.chn).grade),
                            modifier = Modifier.align(Alignment.Center),
                            color = getAqiInfo(aqi.max.chn).color,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Temperature(
    temp: DailyResponse.Daily,
    modifier: Modifier = Modifier,
    toDailyClick: (index: Int) -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.Start) {
        temp.temperature.forEachIndexed { index, temperature ->
            Box(
                modifier = Modifier
                    .height(60.dp)
                    .fillMaxWidth()
                    .noRippleClickable { toDailyClick(index) },
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "${temperature.max.toInt()}° / ${temperature.min.toInt()}°",
                    fontSize = 16.sp
                )
            }
        }
    }
}