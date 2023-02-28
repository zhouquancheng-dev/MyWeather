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
                .width(100.dp)
                .height(100.dp)
                .padding(start = 10.dp, end = 20.dp, top = 10.dp, bottom = 10.dp)
        ) {
            val startX =
                (1.0 - 0.1).pow(2.0) * (0f - 30) + 2 * 0.1 * (1 - 0.1) * (size.width / 2) + 0.1
                    .pow(2.0) * (size.width + 30)
            val startY =
                (1.0 - 0.1).pow(2.0) * (size.height + 80) + 2 * 0.1 * (1 - 0.1) * (-size.height + 50) + 0.1
                    .pow(2.0) * (size.height + 80)

            val endX =
                (1.0 - 0.9).pow(2.0) * (0f - 30) + 2 * 0.9 * (1 - 0.9) * (size.width / 2) + 0.9
                    .pow(2.0) * (size.width + 30)
            val endY =
                (1.0 - 0.9).pow(2.0) * (size.height + 80) + 2 * 0.9 * (1 - 0.9) * (-size.height + 50) + 0.9
                    .pow(2.0) * (size.height + 80)

            // 解构当前时间分钟数位置
            val (sunIconX, sunIconY) = bezierPointPair(result.toDouble(), sun.sunrise.time, sun.sunset.time)
            // 绘制太阳图标升起位置
            drawImage(
                image = bitmapSun.asImageBitmap(),
                topLeft = Offset(
                    x = sunIconX.toFloat() - bitmapSun.width / 2,
                    y = sunIconY.toFloat() - bitmapSun.height / 2
                )
            )

            // 创建白天太阳升起路径
            val dayPath = Path()
            // 起始点
            dayPath.moveTo(0f - 30, size.height + 80)
            // 创建二阶贝塞尔曲线路径
            dayPath.quadraticBezierTo(
                x1 = size.width / 2,
                y1 = -size.height + 50,
                x2 = size.width + 30,
                y2 = size.height + 80
            )
            // 绘制二阶贝塞尔曲线路径
            drawPath(
                path = dayPath,
                color = SunRiseSetCurveColor,
                style = Stroke(width = 3f)
            )
            dayPath.close()

            // 绘制地平线
            drawLine(
                color = Color.LightGray,
                start = Offset(startX.toFloat() - 80, startY.toFloat()),
                end = Offset(endX.toFloat() + 55, endY.toFloat()),
                strokeWidth = 0.5f.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 5f), 1f)
            )

            // 日出日落的首尾点位置数组
            val points = arrayListOf(
                Offset(startX.toFloat(), startY.toFloat()),
                Offset(endX.toFloat(), endY.toFloat())
            )
            // 绘制日出日落的首尾点
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
 * 太阳升起路径
 * 计算二阶贝塞尔曲线上的点
 *
 * 二阶贝塞尔曲线的公式:
 *  B(t) = (1 - t)^2 * P0 + 2t(1 - t) * P1 + t^2 * P2, t∈[0,1]
 *
 * P0（起始点） ， P1（控制点）， P2 （终点）
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
        // 日出之前的路径
        val endX =
            (1.0 - 0.1).pow(2.0) * (0f - 30) + 2 * 0.1 * (1 - 0.1) * (size.width / 2) + 0.1
                .pow(2.0) * (size.width + 80)
        val endY =
            (1.0 - 0.1).pow(2.0) * (size.height + 80) + 2 * 0.1 * (1 - 0.1) * (-size.height + 50) + 0.1
                .pow(2.0) * (size.height + 80)
        val x =
            (1.0 - sunResult).pow(2.0) * (0f - 30) + 2 * sunResult * (1 - sunResult) * (0f - 30) + sunResult
                .pow(2.0) * endX
        val y =
            (1.0 - sunResult).pow(2.0) * (size.height + 80) + 2 * sunResult * (1 - sunResult) * (size.height + 80) + sunResult
                .pow(2.0) * endY
        return Pair(x, y)
    } else if (timeCompareAfter(sunSet)) {
        // 日落之后的路径
        val endX =
            (1.0 - 0.9).pow(2.0) * (0f - 30) + 2 * 0.9 * (1 - 0.9) * (size.width / 2) + 0.9
                .pow(2.0) * (size.width + 30)
        val endY =
            (1.0 - 0.9).pow(2.0) * (size.height + 80) + 2 * 0.9 * (1 - 0.9) * (-size.height + 50) + 0.9
                .pow(2.0) * (size.height + 80)
        val x =
            (1.0 - sunResult).pow(2.0) * endX + 2 * sunResult * (1 - sunResult) * (size.width + 30) + sunResult
                .pow(2.0) * (size.width + 30)
        val y =
            (1.0 - sunResult).pow(2.0) * endY + 2 * sunResult * (1 - sunResult) * (size.height + 80) + sunResult
                .pow(2.0) * (size.height + 80)
        return Pair(x, y)
    } else {
        // 白天太阳升起的路径
        val x =
            (1.0 - sunResult).pow(2.0) * (0f - 30) + 2 * sunResult * (1 - sunResult) * (size.width / 2) + sunResult
                .pow(2.0) * (size.width + 30)
        val y =
            (1.0 - sunResult).pow(2.0) * (size.height + 80) + 2 * sunResult * (1 - sunResult) * (-size.height + 50) + sunResult
                .pow(2.0) * (size.height + 80)
        return Pair(x, y)
    }
}

/**
 * 从日出到日落时间当前时间位置所占的百分比
 * @return 从日出到日落时间当前时间位置所占的百分比
 */
@RequiresApi(Build.VERSION_CODES.N)
private fun getAccounted(sunrise: String?, sunset: String?): Float {
    if (sunrise.isNullOrEmpty() || sunset.isNullOrEmpty()) return 0f
    val calendar = Calendar.getInstance()
    val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    val startMinutes = getMinutes("00:00")
    val endMinutes = getMinutes("24:00")
    val sunriseMinutes = getMinutes(sunrise)    // 日出时间的分钟数
    val sunsetMinutes = getMinutes(sunset)  // 日落时间的分钟数
    val startAccount = (currentMinutes.toFloat() - startMinutes.toFloat()) / (sunriseMinutes.toFloat() - startMinutes.toFloat())
    val endAccount = (currentMinutes.toFloat() - sunsetMinutes.toFloat()) / (endMinutes.toFloat() - sunsetMinutes.toFloat())
    // 从日出到日落时间当前时间位置所占的百分比
    // (当前时间的总分钟 - 当天日出时间的总分钟数) / (当天日落时间的总分钟数 - 当天日出时间的总分钟数)
    val account = (currentMinutes.toFloat() - sunriseMinutes.toFloat()) / (sunsetMinutes.toFloat() - sunriseMinutes.toFloat())
    val result = if (timeCompareBefore(sunrise)) {
        startAccount
    } else if (timeCompareAfter(sunset)) {
        endAccount
    } else {
        account
    }
    return result
}

/**
 * 时间字符串
 * @param sun 时间字符串 必须如格式：“18:20” "06:46"
 * @return 当前 小时和分钟 的总分钟数 例如 18 * 60 + 20
 */
private fun getMinutes(sun: String): Int {
    val hour = sun.substring(0, 2).toInt()
    val minutes = sun.substring(3, 5).toInt()
    return hour * 60 + minutes
}

@Preview(name = "日出日落轨迹")
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