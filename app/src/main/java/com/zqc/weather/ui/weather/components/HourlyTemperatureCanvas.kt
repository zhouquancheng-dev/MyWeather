package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.model.weather.HourlyResponse
import com.zqc.mdoel.weather.getAqiInfo
import java.util.*

/**
 * @param hourlyTemperature 24小时温度 List
 * @param currentTep 当前温度值，必须传入Int类型，便于求前后的中点温度值
 * @param indexTep List<HourlyResponse.Temperature> 中当前item的索引
 * @param hourlyAqi List<HourlyResponse.AirQuality.AQI> 传入aqi以达到动态改变曲线颜色着色器
 */
@ExperimentalTextApi
@Composable
fun HourlyTemperatureCurveCanvas(
    hourlyTemperature: List<HourlyResponse.Hourly.Temperature>,
    currentTep: Int,
    indexTep: Int,
    hourlyAqi: List<HourlyResponse.Hourly.AirQuality.AQI>
) {
    //深色模式返回 true 浅色false
    val isDark = isSystemInDarkTheme()
    val textMeasure = rememberTextMeasurer(cacheSize = 8)
    Canvas(
        modifier = Modifier
            .width(64.dp)
            .height(60.dp)
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

            // 找出24小时中的最高和最低温度值
            var maxTep = hourlyTemperature[0].value
            var minTep = hourlyTemperature[0].value
            for (index in hourlyTemperature.indices) {
                if (maxTep < hourlyTemperature[index].value) {
                    maxTep = hourlyTemperature[index].value
                }
                if (minTep > hourlyTemperature[index].value) {
                    minTep = hourlyTemperature[index].value
                }
            }

            // x 轴中心
            val centreX = (size.width / 2)
            // 每度温度所占的高度
            val yAxisInterval = (size.height / (maxTep - minTep))
            // 当前温度所在的 y 轴位置
            val currentTepOffsetY = yAxisInterval * (currentTep - minTep)

            // 前后温度的中间温度值集合
            val tepCentreList = mutableListOf<Float>()
            for (index in 0 until hourlyTemperature.size - 1) {
                tepCentreList.add(index, (hourlyTemperature[index].value.toInt() + hourlyTemperature[index + 1].value.toInt()) / 2f)
            }

            // 曲线着色器
            val colors: MutableList<Color> = mutableListOf()
            if (indexTep == 0) {
                colors.add(0, getAqiInfo(hourlyAqi[indexTep].value.chn).color)
                colors.add(1, getAqiInfo(hourlyAqi[indexTep].value.chn).color)
            } else if (indexTep > 0 && indexTep < hourlyTemperature.size - 1) {
                colors.add(0, getAqiInfo(hourlyAqi[indexTep - 1].value.chn).color)
                colors.add(1, getAqiInfo(hourlyAqi[indexTep].value.chn).color)
                colors.add(2, getAqiInfo(hourlyAqi[indexTep].value.chn).color)
            } else if (indexTep == hourlyTemperature.size - 1) {
                colors.add(0, getAqiInfo(hourlyAqi[indexTep - 1].value.chn).color)
                colors.add(1, getAqiInfo(hourlyAqi[indexTep].value.chn).color)
            }

            // 初始化画笔
            val mPaint = Paint().apply {
                strokeWidth = 1.6f.dp.toPx()
                style = PaintingStyle.Stroke
                isAntiAlias = true //抗锯齿
                shader = LinearGradientShader(Offset(0f,0f), Offset(size.width,0f), colors)
            }

            // 曲线控制点 x轴偏移量
            val offsetX = 60f.toDp().toPx()
            // 曲线控制点 y轴偏移量
            val offsetY = 2f.toDp().toPx()

            // 创建路径
            val linePath = Path()
            // 开始绘制每小时温度曲线
            if (indexTep == 0) {
                if (currentTep > hourlyTemperature[1].value.toInt()) {
                    linePath.moveTo(centreX, currentTepOffsetY)
                    linePath.quadraticBezierTo(
                        x1 = centreX + offsetX,
                        y1 = currentTepOffsetY + offsetY,
                        x2 = size.width,
                        y2 = yAxisInterval * (tepCentreList.first() - minTep)
                    )
                } else if (currentTep < hourlyTemperature[1].value.toInt()) {
                    linePath.moveTo(centreX, currentTepOffsetY)
                    linePath.quadraticBezierTo(
                        x1 = centreX + offsetX,
                        y1 = currentTepOffsetY - offsetY,
                        x2 = size.width,
                        y2 = yAxisInterval * (tepCentreList.first() - minTep)
                    )
                } else if (currentTep == hourlyTemperature[1].value.toInt()) {
                    linePath.moveTo(centreX, currentTepOffsetY)
                    linePath.lineTo(size.width, yAxisInterval * (tepCentreList.first() - minTep))
                }
            } else if (indexTep > 0 && indexTep < hourlyTemperature.size - 1) {
                // 左边曲线
                if (hourlyTemperature[indexTep - 1].value.toInt() > currentTep) {
                    linePath.moveTo(0f, yAxisInterval * (tepCentreList[indexTep - 1] - minTep))
                    linePath.quadraticBezierTo(
                        x1 = centreX - offsetX,
                        y1 = currentTepOffsetY - offsetY,
                        x2 = centreX,
                        y2 = currentTepOffsetY
                    )
                } else if (hourlyTemperature[indexTep - 1].value.toInt() < currentTep) {
                    linePath.moveTo(0f, yAxisInterval * (tepCentreList[indexTep - 1] - minTep))
                    linePath.quadraticBezierTo(
                        x1 = centreX - offsetX,
                        y1 = currentTepOffsetY + offsetY,
                        x2 = centreX,
                        y2 = currentTepOffsetY
                    )
                } else if (hourlyTemperature[indexTep - 1].value.toInt() == currentTep) {
                    linePath.moveTo(0f, yAxisInterval * (tepCentreList[indexTep - 1] - minTep))
                    linePath.lineTo(centreX, currentTepOffsetY)
                }
                // 右边曲线
                if (currentTep > hourlyTemperature[indexTep + 1].value.toInt()) {
                    linePath.moveTo(centreX, currentTepOffsetY)
                    linePath.quadraticBezierTo(
                        x1 = centreX + offsetX,
                        y1 = currentTepOffsetY + offsetY,
                        x2 = size.width,
                        y2 = yAxisInterval * (tepCentreList[indexTep] - minTep)
                    )
                } else if (currentTep < hourlyTemperature[indexTep + 1].value.toInt()) {
                    linePath.moveTo(centreX, currentTepOffsetY)
                    linePath.quadraticBezierTo(
                        x1 = centreX + offsetX,
                        y1 = currentTepOffsetY - offsetY,
                        x2 = size.width,
                        y2 = yAxisInterval * (tepCentreList[indexTep] - minTep)
                    )
                } else if (currentTep == hourlyTemperature[indexTep + 1].value.toInt()) {
                    linePath.moveTo(centreX, currentTepOffsetY)
                    linePath.lineTo(size.width, yAxisInterval * (tepCentreList[indexTep] - minTep))
                }
            } else if (indexTep == hourlyTemperature.size - 1) {
                if (hourlyTemperature[indexTep - 1].value.toInt() > currentTep) {
                    linePath.moveTo(0f, yAxisInterval * (tepCentreList.last() - minTep))
                    linePath.quadraticBezierTo(
                        x1 = centreX - offsetX,
                        y1 = currentTepOffsetY - offsetY,
                        x2 = centreX,
                        y2 = currentTepOffsetY
                    )
                } else if (hourlyTemperature[indexTep - 1].value.toInt() < currentTep) {
                    linePath.moveTo(0f, yAxisInterval * (tepCentreList.last() - minTep))
                    linePath.quadraticBezierTo(
                        x1 = centreX - offsetX,
                        y1 = currentTepOffsetY + offsetY,
                        x2 = centreX,
                        y2 = currentTepOffsetY
                    )
                } else if (hourlyTemperature[indexTep - 1].value.toInt() == currentTep) {
                    linePath.moveTo(0f, yAxisInterval * (tepCentreList.last() - minTep))
                    linePath.lineTo(centreX, currentTepOffsetY)
                }
            }
            canvas.drawPath(linePath, mPaint)
            linePath.close()
            canvas.restore()

            // 绘制现在时间的 点 和 虚线
            if (indexTep == 1) {
                drawLine(
                    color = Color.LightGray,
                    start = Offset(centreX, currentTepOffsetY),
                    end = Offset(centreX, -40f),
                    strokeWidth = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 5f), 1f)
                )
                drawCircle(
                    color = Color.Gray,
                    radius = 3f.dp.toPx(),
                    center = Offset(centreX, currentTepOffsetY)
                )
            }

            canvas.save()
            canvas.scale(1f, -1f)
            val textTep = buildAnnotatedString { append("${currentTep}°") }
            val textLayoutResult = textMeasure.measure(
                text = textTep, style = TextStyle(color = if (isDark) Color.White else Color.Black, fontSize = 14.sp)
            )
            // 绘制温度文字
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = centreX - (textLayoutResult.size.width / 2),
                    y = -yAxisInterval * (currentTep - minTep) - (textLayoutResult.size.height * 1.5f)
                )
            )
            canvas.restore()
        }
    }
}

@ExperimentalTextApi
@Preview(name = "24小时温度趋势曲线", showBackground = true,
    device = "spec:width=1440px,height=2880px,dpi=560", locale = "zh-rCN", showSystemUi = false
)
@Composable
fun HourlyTemperatureCurveCanvasPreview() {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(5) {
            HourlyTemperatureCurveCanvas(
                hourlyTemperature = listOf(
                    HourlyResponse.Hourly.Temperature(datetime = Date(), value = 30f),
                    HourlyResponse.Hourly.Temperature(datetime = Date(), value = 29f),
                    HourlyResponse.Hourly.Temperature(datetime = Date(), value = 28f),
                    HourlyResponse.Hourly.Temperature(datetime = Date(), value = 27f),
                    HourlyResponse.Hourly.Temperature(datetime = Date(), value = 25f)
                ),
                currentTep = 28,
                indexTep = 0,
                hourlyAqi = listOf(
                    HourlyResponse.Hourly.AirQuality.AQI(Date(), HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(50)),
                    HourlyResponse.Hourly.AirQuality.AQI(Date(), HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(100)),
                    HourlyResponse.Hourly.AirQuality.AQI(Date(), HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(150)),
                    HourlyResponse.Hourly.AirQuality.AQI(Date(), HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(200)),
                    HourlyResponse.Hourly.AirQuality.AQI(Date(), HourlyResponse.Hourly.AirQuality.AQI.AQIDesc(300)),
                )
            )
        }
    }
}