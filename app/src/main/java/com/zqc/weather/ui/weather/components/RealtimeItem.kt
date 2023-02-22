package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.model.weather.RealtimeResponse
import com.zqc.mdoel.weather.getSky

fun LazyListScope.realtimeItem(
    realtime: RealtimeResponse.Realtime
) {
    item(contentType = { realtime }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(420.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row {
                Text(
                    text = realtime.temperature.toInt().toString(),
                    fontSize = 120.sp,
                    modifier = Modifier.padding(start = 25.dp),
                    color = Color.White
                )
                Text(
                    text = "℃",
                    fontSize = 28.sp,
                    modifier = Modifier
                        .align(Alignment.Top)
                        .padding(start = 5.dp, top = 20.dp),
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(getSky(realtime.skycon).info),
                fontSize = 25.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(5.dp))
            WeatherAnimation(weatherCode = realtime.skycon)
        }
    }
}