package com.zqc.weather.ui.daily.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.mdoel.view.isDarkMode
import com.zqc.model.weather.DailyResponse
import com.zqc.weather.ui.theme.BrokenLineColor
import com.zqc.weather.ui.theme.MyWeatherTheme
import java.util.*

/**
 * 自定义绘制温度趋势
 * @param dailyTemperature 15日温度 List
 * @param dayCurrentTep 当天当前日间温度
 * @param nightCurrentTep 当天当前夜间温度
 * @param indexTep List<DailyResponse.Temperature> 中当前item的索引
 */
@ExperimentalTextApi
@Composable
fun DailyTemperatureBrokenLineCanvas(
    dailyTemperature: List<DailyResponse.Daily.Temperature>,
    dayCurrentTep: Int,
    nightCurrentTep: Int,
    indexTep: Int,
) {
    val context = LocalContext.current
    //深色模式返回 true 浅色false
    val isDark = context.isDarkMode()
    val textMeasure = rememberTextMeasurer(cacheSize = 8)
    //找出15天中每天中的最高温度和最低温度
    var maxTep = dailyTemperature[0].max
    var minTep = dailyTemperature[0].min
    for (index in dailyTemperature.indices) {
        if (maxTep < dailyTemperature[index].max) {
            maxTep = dailyTemperature[index].max
        }
        if (minTep > dailyTemperature[index].min) {
            minTep = dailyTemperature[index].min
        }
    }
    val temperature = remember { Pair(maxTep, minTep) }
    Canvas(
        modifier = Modifier
            .width(80.dp)
            .height(120.dp)
            .padding(vertical = 10.dp)
    ) {
        drawIntoCanvas { canvas ->
            // 内部提供size来获取自身宽高
            // 变换坐标轴, 使得屏幕左下角为原点(0, 0)
            // 沿x轴镜像变换必须明白最重要的一点,这时候y坐标系向下为正，经过下面scale(1,-1)y轴坐标系向下为负
            canvas.scale(1f, -1f)
            // 向下平移，这时候向下是负方向
            canvas.translate(0f, -size.height)
            canvas.save()

            // x 轴中心
            val centreX = (size.width / 2)
            // 每一度温度所占高度
            val yAxisInterval = (size.height / (temperature.first - temperature.second))
            // 白天温度所在的 y 轴位置
            val tempDayOffsetY = yAxisInterval * (dayCurrentTep - temperature.second)
            // 夜间温度所在的 y 轴位置
            val tempNightOffsetY = yAxisInterval * (nightCurrentTep - temperature.second)

            //高温和低温交接圆点位置集合
            val dataDotList: List<Offset> = listOf(
                Offset(centreX, yAxisInterval * (dayCurrentTep - temperature.second)),
                Offset(centreX, yAxisInterval * (nightCurrentTep - temperature.second))
            )

            // 高温前后的中点温度值集合
            val dataMax = mutableListOf<Float>()
            for (index in 0 until dailyTemperature.size - 1) {
                dataMax.add(index, ((dailyTemperature[index].max.toInt() + dailyTemperature[index + 1].max.toInt()) / 2f))
            }
            // 低温前后的中点温度值集合
            val dataMin = mutableListOf<Float>()
            for (index in 0 until dailyTemperature.size - 1) {
                dataMin.add(index, ((dailyTemperature[index].min.toInt() + dailyTemperature[index + 1].min.toInt()) / 2f))
            }

            // 初始化画笔
            val paint = Paint().apply {
                color = BrokenLineColor
                strokeWidth = 2f.dp.toPx()
                style = PaintingStyle.Stroke
                isAntiAlias = true    //抗锯齿
            }
            // 初始化路径
            val linePath = Path()

            // 开始绘制两条折线
            // 绘制最高温度折线
            if (indexTep == 0) {
                linePath.moveTo(centreX, tempDayOffsetY)
                linePath.lineTo(size.width, yAxisInterval * (dataMax.first() - temperature.second))
            } else if (indexTep > 0 && indexTep < dailyTemperature.size - 1) {
                linePath.moveTo(0f, yAxisInterval * (dataMax[indexTep - 1] - temperature.second))
                linePath.lineTo(centreX, tempDayOffsetY)
                linePath.moveTo(centreX, tempDayOffsetY)
                linePath.lineTo(size.width, yAxisInterval * (dataMax[indexTep] - temperature.second))
            } else if (indexTep == dailyTemperature.size - 1) {
                linePath.moveTo(0f, yAxisInterval * (dataMax.last() - temperature.second))
                linePath.lineTo(centreX, tempDayOffsetY)
            }
            canvas.drawPath(linePath, paint)
            linePath.close()

            // 绘制最低温度折线
            if (indexTep == 0) {
                linePath.moveTo(centreX, tempNightOffsetY)
                linePath.lineTo(size.width, yAxisInterval * (dataMin.first() - temperature.second))
            } else if (indexTep > 0 && indexTep < dailyTemperature.size - 1) {
                linePath.moveTo(0f, yAxisInterval * (dataMin[indexTep - 1] - temperature.second))
                linePath.lineTo(centreX, tempNightOffsetY)
                linePath.moveTo(centreX, tempNightOffsetY)
                linePath.lineTo(size.width, yAxisInterval * (dataMin[indexTep] - temperature.second))
            } else if (indexTep == dailyTemperature.size - 1) {
                linePath.moveTo(0f, yAxisInterval * (dataMin.last() - temperature.second))
                linePath.lineTo(centreX, tempNightOffsetY)
            }
            canvas.drawPath(linePath, paint)
            linePath.close()

            // 以背景色为颜色的实心圆覆盖折线交点拐角
            paint.apply {
                color = if (isDark) Color.Black else Color.White
                style = PaintingStyle.Fill //实心画笔
                strokeWidth = 2f.dp.toPx()
            }
            for (index in dataDotList.indices) {
                canvas.drawCircle(Offset(dataDotList[index].x, dataDotList[index].y), 6.dp.toPx(), paint)
            }

            // 再绘制空心圆点
            paint.apply {
                color = if (isDark) Color.White else Color.Gray
                style = PaintingStyle.Stroke //空心画笔
                strokeWidth = 2f.dp.toPx()
                isAntiAlias = true    //抗锯齿
            }
            for (index in dataDotList.indices) {
                canvas.drawCircle(Offset(dataDotList[index].x, dataDotList[index].y), 3.dp.toPx(), paint)
            }
            canvas.restore()

            canvas.save()
            // 开始绘制温度文字
            canvas.scale(1f, -1f)
            val textDayTep = buildAnnotatedString { append("${dayCurrentTep}°") }
            val textLayoutResultDay = textMeasure.measure(
                text = textDayTep,
                style = TextStyle(color = if (isDark) Color.White else Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            )
            drawText(
                textLayoutResult = textLayoutResultDay,
                topLeft = Offset(
                    x = centreX - (textLayoutResultDay.size.width / 2),
                    y = -tempDayOffsetY - (textLayoutResultDay.size.height * 1.5f)
                )
            )
            canvas.restore()

            canvas.save()
            // 绘制温度文字
            canvas.scale(1f, -1f)
            val textNightTep = buildAnnotatedString { append("${nightCurrentTep}°") }
            val textLayoutResultNight = textMeasure.measure(
                text = textNightTep,
                style = TextStyle(color = if (isDark) Color.White else Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            )
            drawText(
                textLayoutResult = textLayoutResultNight,
                topLeft = Offset(
                    x = centreX - (textLayoutResultNight.size.width / 2),
                    y = -tempNightOffsetY + (textLayoutResultNight.size.height / 1.5f)
                )
            )
            canvas.restore()
        }
    }
}

@ExperimentalTextApi
@Preview(name = "15日温度趋势折线", showBackground = true,
    device = "spec:width=1440px,height=2880px,dpi=560", locale = "zh-rCN", showSystemUi = false)
@Composable
fun DailyTemperatureBrokenLineCanvasPreview() {
    MyWeatherTheme {
        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp)
        ) {
            items(2) {
                DailyTemperatureBrokenLineCanvas(
                    dailyTemperature = listOf(
                        DailyResponse.Daily.Temperature(
                            date = Date(),
                            max = 35f,
                            min = 25f
                        ),
                        DailyResponse.Daily.Temperature(
                            date = Date(),
                            max = 35f,
                            min = 25f
                        )
                    ),
                    34,
                    26,
                    indexTep = 0
                )
            }
        }
    }
}