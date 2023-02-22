package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.zqc.mdoel.weather.getIconAnimation

@Composable
fun WeatherAnimation(weatherCode: String) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(
            getIconAnimation(weatherCode)
        )
    )

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.size(130.dp)
    )
}