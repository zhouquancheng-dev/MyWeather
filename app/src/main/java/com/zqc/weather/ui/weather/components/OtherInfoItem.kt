package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.mdoel.view.AdaptationScreenWidth
import com.zqc.model.weather.DailyResponse
import com.zqc.model.weather.RealtimeResponse
import com.zqc.model.weather.Weather
import com.zqc.mdoel.weather.getWindDirection
import com.zqc.mdoel.weather.getWindSpeed
import com.zqc.utils.R

fun LazyListScope.otherInfoItem(
    other: Weather
) {
    item(contentType = { other }) {
        AdaptationScreenWidth {
            Row {
                Card(
                    modifier = Modifier
                        .height(160.dp)
                        .weight(1f)
                        .padding(start = 15.dp, end = 2.5.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SunRiseSetContent(sun = other.daily.astro[0])
                        SunRiseSetCurveCanvas(sun = other.daily.astro[0])
                    }
                }
                Card(
                    modifier = Modifier
                        .height(160.dp)
                        .weight(1f)
                        .padding(start = 2.5.dp, end = 15.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    ApparentContent(temp = other.realtime)
                }
            }
        }
    }
}

@Composable
private fun SunRiseSetContent(
    sun: DailyResponse.Daily.Astro
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(25.dp)
                    .padding(start = 6.dp),
                painter = painterResource(id = R.drawable.ic_sun_rise),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "${stringResource(com.zqc.weather.R.string.sun_rise_text)} ${sun.sunrise.time}",
                fontSize = 13.5.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier
                    .size(25.dp)
                    .padding(start = 6.dp),
                painter = painterResource(id = R.drawable.ic_sun_set),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "${stringResource(com.zqc.weather.R.string.sun_set_text)} ${sun.sunset.time}",
                fontSize = 13.5.sp
            )
        }
    }
}

@Composable
private fun ApparentContent(
    temp: RealtimeResponse.Realtime
) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${getWindDirection(temp.wind.direction).direction}",
                fontSize = 14.sp
            )
            Text(
                text = "${getWindSpeed(temp.wind.speed).speed}",
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = com.zqc.weather.R.string.humidity_text),
                fontSize = 14.sp
            )
            Text(
                text = "${(temp.humidity * 100).toInt()}%",
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = com.zqc.weather.R.string.apparent_temperature_text),
                fontSize = 14.sp
            )
            Text(
                text = "${temp.apparentTemperature.toInt()}°",
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = com.zqc.weather.R.string.pressure_text),
                fontSize = 14.sp
            )
            val pressure = temp.pressure
            if (pressure.toInt().toString().length > 4) {
                Text(
                    text = "${pressure.toInt().toString().substring(0, 4)}hPa",
                    fontSize = 18.sp
                )
            } else {
                Text(
                    text = "${pressure.toInt()}hPa",
                    fontSize = 18.sp
                )
            }
        }
    }
}