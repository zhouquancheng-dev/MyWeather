package com.zqc.mdoel.weather

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.zqc.utils.R

class Sky(@StringRes val info: Int, @DrawableRes val icon: Int, @DrawableRes val bg: Int)

fun getSky(skyCon: String): Sky {
    return (sky[skyCon] ?: sky["CLEAR_DAY"])!!
}

/**
 * 主要天气现象的优先级：降雪 > 降雨 > 雾 > 沙尘 > 浮尘 > 雾霾 > 大风 > 阴 > 多云 > 晴
 */
private val sky: Map<String, Sky> = mapOf(
    "CLEAR_DAY" to Sky(R.string.sky_clear, R.drawable.ic_clear_day, R.drawable.bg_clear_day),
    "CLEAR_NIGHT" to Sky(R.string.sky_clear, R.drawable.ic_clear_night, R.drawable.bg_clear_night),
    "PARTLY_CLOUDY_DAY" to Sky(R.string.sky_partly_cloudy, R.drawable.ic_partly_cloud_day, R.drawable.bg_partly_cloudy_day),
    "PARTLY_CLOUDY_NIGHT" to Sky(R.string.sky_partly_cloudy, R.drawable.ic_partly_cloud_night, R.drawable.bg_partly_cloudy_night),
    "CLOUDY" to Sky(R.string.sky_cloudy, R.drawable.ic_cloudy, R.drawable.bg_cloudy),
    "WIND" to Sky(R.string.sky_strong_wind, R.drawable.ic_wind, R.drawable.bg_wind),
    "LIGHT_RAIN" to Sky(R.string.sky_light_rain, R.drawable.ic_light_rain, R.drawable.bg_rain),
    "MODERATE_RAIN" to Sky(R.string.sky_moderate_rain, R.drawable.ic_moderate_rain, R.drawable.bg_rain),
    "HEAVY_RAIN" to Sky(R.string.sky_heavy_rain, R.drawable.ic_heavy_rain, R.drawable.bg_rain),
    "STORM_RAIN" to Sky(R.string.sky_rainstorm, R.drawable.ic_storm_rain, R.drawable.bg_rain),
    "THUNDER_SHOWER" to Sky(R.string.sky_thunderstorm, R.drawable.ic_thunder_shower, R.drawable.bg_rain),
    "LIGHT_SNOW" to Sky(R.string.sky_light_snow, R.drawable.ic_light_snow, R.drawable.bg_snow),
    "MODERATE_SNOW" to Sky(R.string.sky_moderate_snow, R.drawable.ic_moderate_snow, R.drawable.bg_snow),
    "HEAVY_SNOW" to Sky(R.string.sky_heavy_snow, R.drawable.ic_heavy_snow, R.drawable.bg_snow),
    "STORM_SNOW" to Sky(R.string.sky_blizzard, R.drawable.ic_heavy_snow, R.drawable.bg_snow),
    "LIGHT_HAZE" to Sky(R.string.sky_mild_haze, R.drawable.ic_light_haze, R.drawable.bg_fog),
    "MODERATE_HAZE" to Sky(R.string.sky_moderate_smog, R.drawable.ic_moderate_haze, R.drawable.bg_fog),
    "HEAVY_HAZE" to Sky(R.string.sky_heavy_smog, R.drawable.ic_heavy_haze, R.drawable.bg_fog),
    "FOG" to Sky(R.string.sky_fog, R.drawable.ic_fog, R.drawable.bg_fog),
    "DUST" to Sky(R.string.sky_dust, R.drawable.ic_fog, R.drawable.bg_fog),
    "SAND" to Sky(R.string.sky_sand, R.drawable.ic_fog, R.drawable.bg_fog)
)