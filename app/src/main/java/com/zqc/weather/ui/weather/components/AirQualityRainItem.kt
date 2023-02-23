package com.zqc.weather.ui.weather.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zqc.model.weather.RealtimeResponse
import com.zqc.mdoel.view.noRippleClickable
import com.zqc.weather.R

fun LazyListScope.airQualityRainItem(
    air: RealtimeResponse.Realtime.AirQuality
) {
    item(contentType = { air.aqi.chn }) {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 15.dp, end = 2.5.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .height(45.dp)
                        .noRippleClickable {
                            Toast
                                .makeText(context, "抱歉，暂无空气质量详情", Toast.LENGTH_SHORT)
                                .show()
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f)
                    ) {
                        Image(
                            painter = painterResource(id = com.zqc.utils.R.drawable.ic_aqi),
                            contentDescription = "aqi",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(8.dp),
                        )
                    }
                    Text(
                        text = "${stringResource(R.string.air_text)}${air.description.chn} ${air.aqi.chn}",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 2.5.dp, end = 15.dp),
                shape = RoundedCornerShape(15.dp)
            ) {
                Row(
                    modifier = Modifier
                        .height(45.dp)
                        .noRippleClickable {
                            Toast
                                .makeText(context, "抱歉，暂无降水详情", Toast.LENGTH_SHORT)
                                .show()
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f),
                    ) {
                        Image(
                            painter = painterResource(id = com.zqc.utils.R.drawable.ic_precipitation),
                            contentDescription = "rain",
                            modifier = Modifier
                                .size(32.dp)
                                .padding(8.dp),
                        )
                    }
                    Text(text = "降水数据", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}