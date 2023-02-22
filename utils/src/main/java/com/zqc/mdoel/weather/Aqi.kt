package com.zqc.mdoel.weather

import androidx.annotation.ColorLong
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.zqc.utils.R
import com.zqc.mdoel.theme.*

class Aqi(@StringRes val grade: Int, @ColorLong val color: Color)

fun getAqiInfo(aqi: Int?): Aqi {
    return when (aqi) {
        null -> Aqi(R.string.airQuality_null, AqiNoneColor)
        in 0..50 -> Aqi(R.string.airQuality_excellent, AqiExcellentColor)
        in 51..100 -> Aqi(R.string.airQuality_good, AqiGoodColor)
        in 101..150 -> Aqi(R.string.airQuality_mild, AqiMildColor)
        in 151..200 -> Aqi(R.string.airQuality_moderate, AqiModerateColor)
        in 201..300 -> Aqi(R.string.airQuality_severe, AqiSevereColor)
        else -> Aqi(R.string.airQuality_serious, AqiSeriousColor)
    }
}