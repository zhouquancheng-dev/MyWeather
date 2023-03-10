package com.zqc.weather.ui.weather.components

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zqc.mdoel.weather.timeCompareAfter
import com.zqc.mdoel.weather.timeCompareBefore
import com.zqc.model.weather.DailyResponse
import com.zqc.weather.ui.theme.MyWeatherTheme
import kotlin.math.pow
import com.zqc.utils.R
import com.zqc.weather.ui.theme.SunRiseSetCurveColor

@SuppressLint("NewApi")
@Composable
fun SunRiseSetCurveCanvas(
    sun: DailyResponse.Daily.Astro,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val result = getAccounted(sun.sunrise.time, sun.sunset.time)
    val bitmapSun = BitmapFactory.decodeResource(context.resources, R.drawable.ic_clear_day)
    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
                .padding(bottom = 20.dp)
        ) {
            val startX =
                (1.0 - 0.1).pow(2.0) * (0f - 30.dp.toPx()) + 2 * 0.1 * (1 - 0.1) * (size.width / 2) + 0.1
                    .pow(2.0) * (size.width + 30.dp.toPx())
            val startY =
                (1.0 - 0.1).pow(2.0) * (size.height + 80.dp.toPx()) + 2 * 0.1 * (1 - 0.1) * (-size.height + 50.dp.toPx()) + 0.1
                    .pow(2.0) * (size.height + 80.dp.toPx())

            val endX =
                (1.0 - 0.9).pow(2.0) * (0f - 30.dp.toPx()) + 2 * 0.9 * (1 - 0.9) * (size.width / 2) + 0.9
                    .pow(2.0) * (size.width + 30.dp.toPx())
            val endY =
                (1.0 - 0.9).pow(2.0) * (size.height + 80.dp.toPx()) + 2 * 0.9 * (1 - 0.9) * (-size.height + 50.dp.toPx()) + 0.9
                    .pow(2.0) * (size.height + 80.dp.toPx())

            // ?????????????????????????????????
            val (sunIconX, sunIconY) = bezierPointPair(result.toDouble(), sun.sunrise.time, sun.sunset.time)
            // ??????????????????????????????
            drawImage(
                image = bitmapSun.asImageBitmap(),
                topLeft = Offset(
                    x = sunIconX.toFloat() - bitmapSun.width / 2,
                    y = sunIconY.toFloat() - bitmapSun.height / 2
                )
            )

            // ??????????????????????????????
            val dayPath = Path()
            // ?????????
            dayPath.moveTo(0f - 30.dp.toPx(), size.height + 80.dp.toPx())
            // ?????????????????????????????????
            dayPath.quadraticBezierTo(
                x1 = size.width / 2,
                y1 = -size.height + 50.dp.toPx(),
                x2 = size.width + 30.dp.toPx(),
                y2 = size.height + 80.dp.toPx()
            )
            // ?????????????????????????????????
            drawPath(
                path = dayPath,
                color = SunRiseSetCurveColor,
                style = Stroke(width = 3f)
            )
            dayPath.close()

            // ???????????????
            drawLine(
                color = Color.LightGray,
                start = Offset(startX.toFloat() - 55, startY.toFloat()),
                end = Offset(endX.toFloat() + 55, endY.toFloat()),
                strokeWidth = 0.5f.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 5f), 1f)
            )

            // ????????????????????????????????????
            val points = arrayListOf(
                Offset(startX.toFloat(), startY.toFloat()),
                Offset(endX.toFloat(), endY.toFloat())
            )
            // ??????????????????????????????
            drawPoints(
                points = points,
                pointMode = PointMode.Points,
                color = SunRiseSetCurveColor,
                strokeWidth = 5f.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

/**
 * ??????????????????
 * ????????????????????????????????????
 *
 * ??????????????????????????????:
 *  B(t) = (1 - t)^2 * P0 + 2t(1 - t) * P1 + t^2 * P2, t???[0,1]
 *
 * P0??????????????? ??? P1?????????????????? P2 ????????????
 * P0(x1, y1), P1(x2, y2), P2(x3, y3)
 * x = Math.pow(1-t, 2) * x1 + 2 * t * (1-t) * x2 + Math.pow(t, 2) * x3
 * y = Math.pow(1-t, 2) * y1 + 2 * t * (1-t) * y2 + Math.pow(t, 2) * y3
 */
private fun DrawScope.bezierPointPair(
    sunResult: Double,
    sunRise: String,
    sunSet: String
): Pair<Double, Double> {
    if (timeCompareBefore(sunRise)) {
        // ?????????????????????
        val endX =
            (1.0 - 0.1).pow(2.0) * (0f - 30.dp.toPx()) + 2 * 0.1 * (1 - 0.1) * (size.width / 2) + 0.1
                .pow(2.0) * (size.width + 80.dp.toPx())
        val endY =
            (1.0 - 0.1).pow(2.0) * (size.height + 80.dp.toPx()) + 2 * 0.1 * (1 - 0.1) * (-size.height + 30.dp.toPx()) + 0.1
                .pow(2.0) * (size.height + 80.dp.toPx())
        val x =
            (1.0 - sunResult).pow(2.0) * (0f - 30.dp.toPx()) + 2 * sunResult * (1 - sunResult) * endX + sunResult
                .pow(2.0) * endX
        val y =
            (1.0 - sunResult).pow(2.0) * (size.height + 80.dp.toPx()) + 2 * sunResult * (1 - sunResult) * endY + sunResult
                .pow(2.0) * endY
        return Pair(x, y)
    } else if (timeCompareAfter(sunSet)) {
        // ?????????????????????
        val endX =
            (1.0 - 0.9).pow(2.0) * (0f - 30.dp.toPx()) + 2 * 0.9 * (1 - 0.9) * (size.width / 2) + 0.9
                .pow(2.0) * (size.width + 30.dp.toPx())
        val endY =
            (1.0 - 0.9).pow(2.0) * (size.height + 80.dp.toPx()) + 2 * 0.9 * (1 - 0.9) * (-size.height + 50.dp.toPx()) + 0.9
                .pow(2.0) * (size.height + 80.dp.toPx())
        val x =
            (1.0 - sunResult).pow(2.0) * endX + 2 * sunResult * (1 - sunResult) * (size.width + 30.dp.toPx()) + sunResult
                .pow(2.0) * (size.width + 30.dp.toPx())
        val y =
            (1.0 - sunResult).pow(2.0) * endY + 2 * sunResult * (1 - sunResult) * (size.height + 80.dp.toPx()) + sunResult
                .pow(2.0) * (size.height + 80.dp.toPx())
        return Pair(x, y)
    } else {
        val startX =
            (1.0 - 0.1).pow(2.0) * (0f - 30.dp.toPx()) + 2 * 0.1 * (1 - 0.1) * (size.width / 2) + 0.1
                .pow(2.0) * (size.width + 30.dp.toPx())
        val startY =
            (1.0 - 0.1).pow(2.0) * (size.height + 80.dp.toPx()) + 2 * 0.1 * (1 - 0.1) * (-size.height + 50.dp.toPx()) + 0.1
                .pow(2.0) * (size.height + 80.dp.toPx())

        val endX =
            (1.0 - 0.9).pow(2.0) * (0f - 30.dp.toPx()) + 2 * 0.9 * (1 - 0.9) * (size.width / 2) + 0.9
                .pow(2.0) * (size.width + 30.dp.toPx())
        val endY =
            (1.0 - 0.9).pow(2.0) * (size.height + 80.dp.toPx()) + 2 * 0.9 * (1 - 0.9) * (-size.height + 50.dp.toPx()) + 0.9
                .pow(2.0) * (size.height + 80.dp.toPx())
        // ???????????????????????????
        val x =
            (1.0 - sunResult).pow(2.0) * startX + 2 * sunResult * (1 - sunResult) * (size.width / 2) + sunResult
                .pow(2.0) * endX
        val y =
            (1.0 - sunResult).pow(2.0) * startY + 2 * sunResult * (1 - sunResult) * (-size.height + 80.dp.toPx()) + sunResult
                .pow(2.0) * endY
        return Pair(x, y)
    }
}

/**
 * ????????????????????????????????????????????????????????????
 * @return ????????????????????????????????????????????????????????????
 */
@RequiresApi(Build.VERSION_CODES.N)
private fun getAccounted(sunrise: String?, sunset: String?): Float {
    if (sunrise.isNullOrEmpty() || sunset.isNullOrEmpty()) return 0f
    val calendar = Calendar.getInstance()
    val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    val startMinutes = getMinutes("00:00")
    val endMinutes = getMinutes("24:00")
    val sunriseMinutes = getMinutes(sunrise)    // ????????????????????????
    val sunsetMinutes = getMinutes(sunset)  // ????????????????????????
    val startAccount = (currentMinutes.toFloat() - startMinutes.toFloat()) / (sunriseMinutes.toFloat() - startMinutes.toFloat())
    val endAccount = (currentMinutes.toFloat() - sunsetMinutes.toFloat()) / (endMinutes.toFloat() - sunsetMinutes.toFloat())
    // ????????????????????????????????????????????????????????????
    // (???????????????????????? - ?????????????????????????????????) / (????????????????????????????????? - ?????????????????????????????????)
    val account = (currentMinutes.toFloat() - sunriseMinutes.toFloat()) / (sunsetMinutes.toFloat() - sunriseMinutes.toFloat())
    val result = if (timeCompareBefore(sunrise)) {
        startAccount
    } else if (timeCompareAfter(sunset)) {
        endAccount
    } else {
        if (account > 1f) {
            1f
        } else if (account < 0f) {
            0f
        } else {
            account
        }
    }
    return result
}

/**
 * ???????????????
 * @param sun ??????????????? ?????????????????????18:20??? "06:46"
 * @return ?????? ??????????????? ??????????????? ?????? 18 * 60 + 20
 */
private fun getMinutes(sun: String): Int {
    val hour = sun.substring(0, 2).toInt()
    val minutes = sun.substring(3, 5).toInt()
    return hour * 60 + minutes
}

@Preview(name = "??????????????????")
@Composable
fun SunRiseSetCurveCanvasPreview() {
    MyWeatherTheme {
        SunRiseSetCurveCanvas(
            DailyResponse.Daily.Astro(
                DailyResponse.Daily.Astro.SunDesc("06:50"),
                DailyResponse.Daily.Astro.SunDesc("18:20")
            )
        )
    }
}