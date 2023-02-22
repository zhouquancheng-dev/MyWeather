package com.zqc.weather.ui.lifeindex

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.weather.ui.theme.MyWeatherTheme

@Composable
fun BottomLifeIndexContent(
    title: String
) {
    Box(
        modifier = Modifier
            .height(220.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "暂无${title}指数详情", fontSize = 20.sp)
    }
}

@Preview
@Composable
fun BottomLifeWeatherScreenPreview() {
    MyWeatherTheme {
        BottomLifeIndexContent(title = "生活指数")
    }
}