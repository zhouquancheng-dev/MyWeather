package com.zqc.mdoel.lce

import androidx.compose.runtime.Composable
import com.zqc.model.*

/**
 * 根据state的页面封装
 * @param uiState 数据状态
 * @param onRetryClick 重试点击事件
 * @param content 显示数据加载成功的 Composable
 */
@Composable
fun <T> LcePage(
    uiState: WeatherUiState<T>,
    onRetryClick: () -> Unit,
    content: @Composable (T) -> Unit
) {
    when (uiState) {
        WeatherLoading -> {
            LoadingContent()
        }
        is WeatherError -> {
            ErrorContent(onErrorClick = onRetryClick)
        }
        is WeatherNoContent -> {
            NoContent()
        }
        is WeatherSuccess -> {
            content(uiState.data)
        }
    }
}