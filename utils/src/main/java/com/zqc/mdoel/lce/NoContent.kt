package com.zqc.mdoel.lce

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.zqc.model.WeatherNoContent
import com.zqc.utils.R

@Composable
fun NoContent(weatherNoContent: WeatherNoContent = WeatherNoContent("当前无内容")) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.noweather_world))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(300.dp)
        )
        Text(text = weatherNoContent.reason, modifier = Modifier.padding(8.dp))
    }
}