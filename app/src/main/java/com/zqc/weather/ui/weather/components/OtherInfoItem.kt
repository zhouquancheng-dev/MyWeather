package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.mdoel.popupWindow.ShowDropdownMenu
import com.zqc.mdoel.view.noRippleClickable
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
        Row {
            Card(
                modifier = Modifier
                    .height(160.dp)
                    .weight(1f)
                    .padding(start = 15.dp, end = 2.5.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box {
                    SunRiseSetContent(
                        sun = other.daily.astro[0],
                        modifier = Modifier.padding(8.dp).align(Alignment.TopStart)
                    )
                    SunRiseSetCurveCanvas(
                        sun = other.daily.astro[0],
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
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

@Composable
private fun SunRiseSetContent(
    sun: DailyResponse.Daily.Astro,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
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
                fontSize = 14.sp
            )
        }
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
                fontSize = 14.sp
            )
        }
    }
}

@Composable
private fun ApparentContent(
    temp: RealtimeResponse.Realtime
) {
    val showPopupWindowWind = remember { mutableStateOf(false) }
    val showPopupWindowHumidity = remember { mutableStateOf(false) }
    val showPopupWindowTemp = remember { mutableStateOf(false) }
    val showPopupWindowPressure = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .noRippleClickable { showPopupWindowWind.value = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShowDropdownMenu(
                showPopupWindow = showPopupWindowWind,
                title = stringResource(id = R.string.weather_description_wind_title),
                content = stringResource(id = R.string.weather_description_wind_info)
            )
            Text(
                text = "${getWindDirection(temp.wind.direction).direction}",
                fontSize = 14.sp,
                modifier = Modifier.weight(3f)
            )
            Text(
                text = "${getWindSpeed(temp.wind.speed).speed}",
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .noRippleClickable { showPopupWindowHumidity.value = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShowDropdownMenu(
                showPopupWindow = showPopupWindowHumidity,
                title = stringResource(id = R.string.weather_description_humidity_title),
                content = stringResource(id = R.string.weather_description_humidity_info)
            )
            Text(
                text = stringResource(id = com.zqc.weather.R.string.humidity_text),
                fontSize = 14.sp,
                modifier = Modifier.weight(3f)
            )
            Text(
                text = "${(temp.humidity * 100).toInt()}%",
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .noRippleClickable { showPopupWindowTemp.value = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShowDropdownMenu(
                showPopupWindow = showPopupWindowTemp,
                title = stringResource(id = R.string.weather_description_apparent_temperature_title),
                content = stringResource(id = R.string.weather_description_apparent_temperature_info)
            )
            Text(
                text = stringResource(id = com.zqc.weather.R.string.apparent_temperature_text),
                fontSize = 14.sp,
                modifier = Modifier.weight(3f)
            )
            Text(
                text = "${temp.apparentTemperature.toInt()}°",
                fontSize = 18.sp
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .noRippleClickable { showPopupWindowPressure.value = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ShowDropdownMenu(
                showPopupWindow = showPopupWindowPressure,
                title = stringResource(id = R.string.weather_description_pressure_title),
                content = stringResource(id = R.string.weather_description_pressure_info)
            )
            Text(
                text = stringResource(id = com.zqc.weather.R.string.pressure_text),
                fontSize = 14.sp,
                modifier = Modifier.weight(3f)
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