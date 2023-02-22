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
import androidx.compose.ui.unit.dp
import com.zqc.model.weather.RealtimeResponse
import com.zqc.mdoel.view.noRippleClickable
import com.zqc.utils.R

fun LazyListScope.airQualityRainItem(
    air: RealtimeResponse.Realtime.AirQuality
) {
    item(contentType = { air.aqi.chn }) {
        val context = LocalContext.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
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
                            painter = painterResource(id = R.drawable.ic_aqi),
                            contentDescription = "aqi",
                            modifier = Modifier.size(32.dp).padding(8.dp),
                        )
                    }
                    Text(
                        text = "空气${air.description.chn} ${air.aqi.chn}",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
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
                            painter = painterResource(id = R.drawable.ic_precipitation),
                            contentDescription = "rain",
                            modifier = Modifier.size(32.dp).padding(8.dp),
                        )
                    }
                    Text(text = "降水数据占位测试", modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}