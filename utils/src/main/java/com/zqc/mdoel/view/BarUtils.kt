package com.zqc.mdoel.view

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * 透明状态栏
 */
fun Activity.transparentStatusBar() {
    //第二参数decorFitsSystemWindows表示是否沉浸，false表示沉浸，true表示不沉浸
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

/**
 * 状态栏
 */
@Composable
fun BarsLightDarkTheme(useDarkIcons: Boolean = !isSystemInDarkTheme()) {
    val systemUiController = rememberSystemUiController()
    // 判断系统是否深色模式，动态状态栏颜色
    // isSystemInDarkTheme 深色返回 true, 浅色false
    DisposableEffect(systemUiController, useDarkIcons) {
        systemUiController.setSystemBarsColor(Color.Transparent, useDarkIcons)
        systemUiController.isNavigationBarContrastEnforced = false
        onDispose { }
    }
}
