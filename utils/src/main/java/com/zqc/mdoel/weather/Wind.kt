package com.zqc.mdoel.weather

import androidx.annotation.DrawableRes
import com.zqc.utils.R

class Wind(val speed: String?, val direction: String?, @DrawableRes val icon: Int?)

fun getWindSpeed(speed: Float): Wind {
    return when (speed) {
        in 1f..5f -> Wind("1级", null, null)
        in 6f..11f -> Wind("2级", null, null)
        in 12f..19f -> Wind("3级", null, null)
        in 20f..28f -> Wind("4级", null, null)
        in 29f..38f -> Wind("5级", null, null)
        in 39f..49f -> Wind("6级", null, null)
        in 50f..61f -> Wind("7级", null, null)
        in 62f..74f -> Wind("8级", null, null)
        in 75f..88f -> Wind("9级", null, null)
        in 89f..102f -> Wind("10级", null, null)
        in 103f..117f -> Wind("11级", null, null)
        in 118f..133f -> Wind("12级", null, null)
        in 134f..149f -> Wind("13级", null, null)
        in 150f..166f -> Wind("14级", null, null)
        in 167f..183f -> Wind("15级", null, null)
        in 184f..201f -> Wind("16级", null, null)
        in 202f..220f -> Wind("17级", null, null)
        else -> Wind("1级", null, null)
    }
}

fun getWindDirection(direction: Float): Wind {
    return when (direction) {
        in 11.26f..33.75f -> Wind(null, "北东北风", R.drawable.ic_bottom_start)
        in 33.76f..56.25f -> Wind(null, "东北风", R.drawable.ic_bottom_start)
        in 56.26f..78.75f -> Wind(null, "东东北风", R.drawable.ic_bottom_start)
        in 78.76f..101.25f -> Wind(null, "东风", R.drawable.ic_start)
        in 101.26f..123.75f -> Wind(null, "东东南风", R.drawable.ic_top_start)
        in 123.76f..146.25f -> Wind(null, "东南风", R.drawable.ic_top_start)
        in 146.26f..168.75f -> Wind(null, "南东南风", R.drawable.ic_top_start)
        in 168.76f..191.25f -> Wind(null, "南风", R.drawable.ic_top)
        in 191.26f..213.75f -> Wind(null, "南西南风", R.drawable.ic_top_end)
        in 213.76f..236.25f -> Wind(null, "西南风", R.drawable.ic_top_end)
        in 236.26f..258.75f -> Wind(null, "西西南风", R.drawable.ic_top_end)
        in 258.76f..281.25f -> Wind(null, "西风", R.drawable.ic_end)
        in 281.26f..303.75f -> Wind(null, "西西北风", R.drawable.ic_bottom_end)
        in 303.76f..326.25f -> Wind(null, "西北风", R.drawable.ic_bottom_end)
        in 326.26f..348.75f -> Wind(null, "北西北风", R.drawable.ic_bottom_end)
        else -> Wind(null, "北风", R.drawable.ic_bottom)
    }
}