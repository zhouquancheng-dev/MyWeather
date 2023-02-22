package com.zqc.mdoel.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

/**
 * 屏幕适配：
 * 只能实现对单个方向的适配，无法同时兼顾宽高。之所以只能单个方向，
 * 是因为当前手机屏幕的宽高比并不是按照一个固定的比例进行递增的，4 : 3、16 : 9、甚至其它宽高比都有，
 * 这种背景下我们要达到百分百还原设计稿是不现实的，我们只能选择一个维度来进行适配；
 *
 * 其适配思路基于以下几条换算公式：
 * ```
 *  px = density * dp
 *  density = dpi / 160
 *  px = dp * (dpi / 160)
 *  px = sp * fontScale * density  //自由控制应用内文字的显示大小
 *  ```
 * MyWeather天气app设计稿屏幕尺寸为 1440 * 2880px = 411 * 822dp， densityDpi = 560dpi
 *
```
val displayMetrics = applicationContext.resources.displayMetrics
Log.e("TAG", "densityDpi: ${displayMetrics.densityDpi}")
Log.e("TAG", "density: ${displayMetrics.density}")
Log.e("TAG", "widthPixels: ${displayMetrics.widthPixels}")
Log.e("TAG", "heightPixels: ${displayMetrics.heightPixels}")
Log.e("TAG", "widthDp: ${displayMetrics.widthPixels / displayMetrics.density}")
Log.e("TAG", "heightDp: ${displayMetrics.heightPixels / displayMetrics.density}")
```
 */
@Composable
fun AdaptationScreenHeight(
    content: @Composable () -> Unit
) {
    val fontScale = LocalDensity.current.fontScale
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val heightPixels = displayMetrics.heightPixels
    CompositionLocalProvider(
        LocalDensity provides Density(
            density = heightPixels / 857f,
            fontScale = fontScale
        )
    ) {
        content()
    }
}

@Composable
fun AdaptationScreenWidth(
    content: @Composable () -> Unit
) {
    val fontScale = LocalDensity.current.fontScale
    val displayMetrics = LocalContext.current.resources.displayMetrics
    val widthPixels = displayMetrics.widthPixels
    CompositionLocalProvider(
        LocalDensity provides Density(
            density = widthPixels / 411f,
            fontScale = fontScale
        )
    ) {
        content()
    }
}