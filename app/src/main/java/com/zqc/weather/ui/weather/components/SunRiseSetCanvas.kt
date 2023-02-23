package com.zqc.weather.ui.weather.components

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zqc.model.weather.DailyResponse
import com.zqc.weather.ui.theme.MyWeatherTheme
import kotlin.math.pow
import com.zqc.utils.R
import com.zqc.weather.ui.theme.SunRiseSetCurveColor

@Composable
fun SunRiseSetCurveCanvas(
    sun: DailyResponse.Daily.Astro
) {
    val context = LocalContext.current
    val result = getAccounted(sun.sunrise.time, sun.sunset.time)
    val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.ic_clear_day)
    Box(contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .width(100.dp)
                .height(80.dp)
                .padding(start = 5.dp, end = 12.dp, top = 10.dp, bottom = 10.dp)
        ) {
            // 创建路径
            val path = Path()
            // 起始点
            path.moveTo(0f, size.height)
            // 解构当前时间分钟数位置
            val (sunIconX, sunIconY) = bezierPointPair(result.toDouble())
            // 绘制太阳图标升起位置
            drawImage(
                image = bitmap.asImageBitmap(),
                topLeft = Offset(
                    x = sunIconX.toFloat() - bitmap.width / 2,
                    y = sunIconY.toFloat() - bitmap.height / 2
                )
            )
            // 创建二阶贝塞尔曲线路径
            path.quadraticBezierTo(
                x1 = size.width / 2,
                y1 = -size.height + 35,
                x2 = size.width,
                y2 = size.height
            )
            // 绘制二阶贝塞尔曲线路径
            drawPath(
                path = path,
                color = SunRiseSetCurveColor,
                style = Stroke(width = 3f)
            )
            // 日出日落的首尾点位置数组
            val points = arrayListOf(
                Offset(0f, size.height),
                Offset(size.width, size.height)
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
private fun DrawScope.bezierPointPair(sunResult: Double): Pair<Double, Double> {
    val x =
        (1.0 - sunResult).pow(2.0) * 0f + 2 * sunResult * (1 - sunResult) * (size.width / 2) + sunResult
            .pow(2.0) * size.width
    val y =
        (1.0 - sunResult).pow(2.0) * size.height + 2 * sunResult * (1 - sunResult) * (-size.height + 35) + sunResult
            .pow(2.0) * size.height
    return Pair(x, y)
}

/**
 * 从日出到日落时间当前时间位置所占的百分比
 * @return 从日出到日落时间当前时间位置所占的百分比
 */
@SuppressLint("NewApi")
private fun getAccounted(sunrise: String?, sunset: String?): Float {
    if (sunrise.isNullOrEmpty() || sunset.isNullOrEmpty()) return 0.5f
    val calendar = Calendar.getInstance()
    val currentMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
    val sunriseMinutes = getMinutes(sunrise)    // 日出时间的分钟数
    val sunsetMinutes = getMinutes(sunset)  // 日落时间的分钟数
    // 从日出到日落时间当前时间位置所占的百分比
    // (当前时间的总分钟 - 当天日出时间的总分钟数) / (当天日落时间的总分钟数 - 当天日出时间的总分钟数)
    val account =
        (currentMinutes.toFloat() - sunriseMinutes.toFloat()) / (sunsetMinutes.toFloat() - sunriseMinutes.toFloat())
    val result = if (account > 1) {
        1f
    } else if (account < 0) {
        0f
    } else {
        account
    }
    return result
}

/**
 * 时间字符串
 * @param sunrise 时间字符串 必须如格式：“18:20” "06:50"
 * @return 当前 小时和分钟 的总分钟数 例如 18 * 60 + 20
 */
private fun getMinutes(sunrise: String): Int {
    val hour = sunrise.substring(0, 2).toInt()
    val minutes = sunrise.substring(3, 5).toInt()
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