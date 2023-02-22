package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.model.weather.HourlyResponse
import com.zqc.mdoel.view.AdaptationScreenWidth
import com.zqc.mdoel.weather.*
import com.zqc.model.weather.RealtimeResponse
import com.zqc.weather.R
import java.util.*

@ExperimentalTextApi
fun LazyListScope.hourlyItem(
    hourly: List<HourlyResponse.Hourly>,
    realtime: RealtimeResponse.Realtime
) {
    val currentDate = Date(System.currentTimeMillis())
    item(contentType = { hourly }) {
        AdaptationScreenWidth {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 25.dp, horizontal = 15.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(id = R.string.hourly_text),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp, start = 16.dp, end = 10.dp)
                    )
                    Divider(modifier = Modifier.padding(horizontal = 10.dp), thickness = 0.5.dp)
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 35.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        items(
                            items = hourly,
                            contentType = { it.temperature + it.airQuality }
                        ) { hourly ->
                            HourlyContent(
                                hourly = hourly,
                                currentDate = currentDate,
                                realtime = realtime
                            )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalTextApi
@Composable
private fun HourlyContent(
    hourly: HourlyResponse.Hourly,
    realtime: RealtimeResponse.Realtime,
    currentDate: Date
) {
    Column(
        modifier = Modifier.height(210.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            val newTemperatureList = getNewTemperatureList(hourly, realtime, currentDate)
            val newAqiList = getNewAqiList(hourly, realtime, currentDate)
            newTemperatureList.forEachIndexed { index, temperature ->
                HourlyTemperatureCurveCanvas(
                    hourlyTemperature = newTemperatureList,
                    currentTep = temperature.value.toInt(),
                    indexTep = index,
                    hourlyAqi = newAqiList
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            val newSkyList = getNewSkyList(hourly, realtime, currentDate)
            newSkyList.forEach { skyCon ->
                Box(modifier = Modifier.width(64.dp), contentAlignment = Alignment.Center) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = getSky(skyCon.value).icon),
                        contentDescription = null
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            val newWindList = getNewWindList(hourly, realtime, currentDate)
            newWindList.forEach { wind ->
                Row(
                    modifier = Modifier.width(64.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    getWindDirection(wind.direction).icon?.let {
                        Image(
                            painter = painterResource(id = it),
                            contentDescription = null,
                            modifier = Modifier.size(8.dp),
                        )
                    }
                    Spacer(modifier = Modifier.width(3.dp))
                    getWindSpeed(wind.speed).speed?.let {
                        Text(text = it, fontSize = 14.sp)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            val newAqiList = getNewAqiList(hourly, realtime, currentDate)
            newAqiList.forEach { aqi ->
                Box(modifier = Modifier.width(64.dp), contentAlignment = Alignment.Center) {
                    Surface(
                        modifier = Modifier.width(35.dp),
                        color = getAqiInfo(aqi.value.chn).color.copy(0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(4.dp)) {
                            Text(
                                text = stringResource(getAqiInfo(aqi.value.chn).grade),
                                color = getAqiInfo(aqi.value.chn).color,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            val newTemperatureList = getNewTemperatureList(hourly, realtime, currentDate)
            newTemperatureList.forEachIndexed { index, temperature ->
                Column(modifier = Modifier.width(64.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (index == 1)
                            stringResource(R.string.current_time_text) else
                                getHourly(temperature.datetime),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}