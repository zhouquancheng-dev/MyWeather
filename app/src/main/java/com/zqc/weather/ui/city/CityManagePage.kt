package com.zqc.weather.ui.city

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.animation.core.Spring.DampingRatioMediumBouncy
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zqc.model.WeatherLoading
import com.zqc.model.room.entity.CityInfo
import com.zqc.weather.ui.city.components.*
import com.zqc.weather.ui.city.viewmodel.CityViewModel
import kotlinx.coroutines.FlowPreview

enum class SearchOnOff { OPEN, ClOSE }

@ExperimentalMaterialApi
@ExperimentalLifecycleComposeApi
@FlowPreview
@Composable
fun CityManagePage(
    cityViewModel: CityViewModel,
    searchState: SearchState = rememberSearchState(),
    onBackClick: () -> Unit,
    onLocationClick: () -> Unit,
    toWeatherClick: (Int) -> Unit,
    toDailyClick: (locationName: String, longitude: String, latitude: String) -> Unit,
    onDeleteCityClick: (CityInfo) -> Unit
) {
    val places by cityViewModel.searchLiveData.collectAsStateWithLifecycle(WeatherLoading)
    val cityInfo by cityViewModel.cityInfoList.collectAsStateWithLifecycle(emptyList())

    val focusStateManger = LocalFocusManager.current
    val backHandlingEnabled = rememberSaveable { mutableStateOf(false) }
    val visible = rememberSaveable { mutableStateOf(true) }
    val searchOnOffChange = rememberSaveable { mutableStateOf(SearchOnOff.ClOSE) }
    val transition = updateTransition(targetState = searchOnOffChange, label = "")
    val searchOffset by transition.animateOffset(
        label = "",
        transitionSpec = { spring(DampingRatioMediumBouncy) }) {
        when (it.value) {
            SearchOnOff.OPEN -> Offset(0f, 0f)
            SearchOnOff.ClOSE -> Offset(0f, 110f)
        }
    }
    val searchWidth by transition.animateFloat(
        label = "",
        transitionSpec = { spring(DampingRatioMediumBouncy) }) {
        when (it.value) {
            SearchOnOff.OPEN -> 0.88f
            SearchOnOff.ClOSE -> 1f
        }
    }
    if (visible.value && (searchOnOffChange.value == SearchOnOff.ClOSE)) {
        if (searchState.focused) {
            backHandlingEnabled.value = true
            searchOnOffChange.value = SearchOnOff.OPEN
            visible.value = false
        } else {
            backHandlingEnabled.value = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 15.dp).systemBarsPadding()
    ) {
        CityHeaderContent(visibleCity = visible.value, onBackClick = onBackClick)
        CityListContentVisibility(
            modifier = Modifier.offset(x = searchOffset.x.dp, y = searchOffset.y.dp),
            cityViewModel = cityViewModel,
            searchState = searchState,
            places = places,
            cityInfo = cityInfo,
            visibleSearch = !visible.value,
            searchWidth = { searchWidth },
            onCloseQuery = {
                if (searchState.query.isEmpty()) {
                    searchState.query = ""
                    searchState.focused = false
                    focusStateManger.clearFocus()
                    searchOnOffChange.value = SearchOnOff.ClOSE
                    visible.value = true
                } else {
                    searchState.query = ""
                }
            },
            onLocationClick = { onLocationClick() },
            toWeatherClick = { toWeatherClick(it) },
            toDailyClick = { locationName, longitude, latitude ->
                toDailyClick(locationName, longitude, latitude)
            },
            onDeleteCityClick = { cityInfo ->
                onDeleteCityClick(cityInfo)
            }
        )
    }

    //处理PlaceScreen页的系统返回按钮的效果
    BackHandler(backHandlingEnabled.value) {
        if (searchState.focused || (!visible.value && searchOnOffChange.value == SearchOnOff.OPEN)) {
            if (searchState.query.isNotEmpty()) {
                searchState.query = ""
            } else {
                searchState.focused = false
                focusStateManger.clearFocus()
                searchOnOffChange.value = SearchOnOff.ClOSE
                visible.value = true
            }
        } else {
            onBackClick()
        }
    }
}