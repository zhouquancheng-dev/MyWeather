package com.zqc.weather.ui.city.components

import androidx.compose.runtime.*

@Stable
class SearchState(
    query: String,
    focused: Boolean,
) {
    var query by mutableStateOf(query)
    var focused by mutableStateOf(focused)
}

@Composable
fun rememberSearchState(
    query: String = "",
    focused: Boolean = false,
): SearchState {
    return remember {
        SearchState(
            query = query,
            focused = focused,
        )
    }
}