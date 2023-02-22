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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
        Card(
            modifier = Modifier.fillMaxWidth().height(165.dp).padding(horizontal = 15.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 30.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                SunRiseSetContent(sun = other.daily)
                Spacer(modifier = Modifier.height(25.dp))
                ApparentTemperatureContent(temp = other.realtime)
            }
        }
    }
}

@Composable
private fun SunRiseSetContent(
    sun: DailyResponse.Daily
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_sun_rise),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "日出 ${sun.astro[0].sunrise.time}",
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.width(40.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                modifier = Modifier.size(25.dp),
                painter = painterResource(id = R.drawable.ic_sun_set),
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "日落 ${sun.astro[0].sunset.time}",
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun ApparentTemperatureContent(
    temp: RealtimeResponse.Realtime
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
            Text(
                text = "${getWindSpeed(temp.wind.speed).speed}",
                fontSize = 19.sp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${getWindDirection(temp.wind.direction).direction}",
                fontSize = 14.sp
            )
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
            Text(text = "${(temp.humidity * 100).toInt()}%", fontSize = 19.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "湿度", fontSize = 14.sp)
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
            Text(text = "${temp.apparentTemperature.toInt()}°", fontSize = 19.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "体感", fontSize = 14.sp)
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
            val pressure = temp.pressure
            if (pressure.toInt().toString().length > 4) {
                Text(text = "${pressure.toInt().toString().substring(0, 4)}hPa", fontSize = 18.sp)
            } else {
                Text(text = "${pressure.toInt()}hPa", fontSize = 19.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "气压", fontSize = 14.sp)
        }
    }
}