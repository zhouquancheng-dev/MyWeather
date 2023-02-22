package com.zqc.weather.ui.city.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.zqc.model.WeatherUiState
import com.zqc.model.city.PlaceResponse
import com.zqc.model.room.entity.CityInfo
import com.zqc.weather.ui.city.viewmodel.CityViewModel
import kotlinx.coroutines.FlowPreview

@ExperimentalLifecycleComposeApi
@ExperimentalMaterialApi
@FlowPreview
@Composable
fun CityListContentVisibility(
    modifier: Modifier = Modifier,
    cityViewModel: CityViewModel,
    searchState: SearchState,
    places: WeatherUiState<List<PlaceResponse.PlaceEntity>>,
    cityInfo: List<CityInfo>,
    visibleSearch: Boolean,
    searchWidth: () -> Float,
    onCloseQuery: () -> Unit,
    onLocationClick: () -> Unit,
    toWeatherClick: (Int) -> Unit,
    toDailyClick: (locationName: String, longitude: String, latitude: String) -> Unit,
    onDeleteCityClick: (CityInfo) -> Unit
) {
    if (searchState.query.isNotEmpty()) {
        LaunchedEffect(searchState.query) {
            cityViewModel.searchPlace(searchState.query)
        }
    }
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SearchContent(searchState = searchState, modifier = Modifier.fillMaxWidth(searchWidth()))
            CancelTextButton(visible = visibleSearch, onCloseQuery = onCloseQuery)
        }
        Spacer(modifier = Modifier.height(12.dp))
        CityListOrSearchCityContent(
            visible = visibleSearch,
            searchState = searchState,
            places = places,
            cityViewModel = cityViewModel,
            onLocationClick = { onLocationClick() },
            toDailyClick = { locationName, longitude, latitude ->
                toDailyClick(locationName, longitude, latitude)
            },
            onRetryClick = { cityViewModel.searchPlace(searchState.query) }
        )
        CityListManagePage(
            visibleCity = !visibleSearch,
            cityInfoList = cityInfo,
            toWeatherClick = { toWeatherClick(it) },
            onDeleteCityClick = { cityInfo ->
                onDeleteCityClick(cityInfo)
            }
        )
    }
}