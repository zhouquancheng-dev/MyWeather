package com.zqc.weather.ui.city.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import com.zqc.model.WeatherUiState
import com.zqc.model.city.PlaceResponse
import com.zqc.mdoel.lce.LcePage
import com.zqc.weather.R
import com.zqc.weather.ui.city.viewmodel.CityViewModel
import kotlinx.coroutines.FlowPreview

@ExperimentalLifecycleComposeApi
@FlowPreview
@Composable
fun CityListOrSearchCityContent(
    visible: Boolean,
    searchState: SearchState,
    cityViewModel: CityViewModel,
    places: WeatherUiState<List<PlaceResponse.PlaceEntity>>,
    onLocationClick: () -> Unit,
    toDailyClick: (locationName: String, longitude: String, latitude: String) -> Unit,
    onRetryClick: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(100)),
        exit = fadeOut(animationSpec = tween(100))
    ) {
        if (searchState.query.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = stringResource(id = R.string.hot_city_text))
                Spacer(modifier = Modifier.height(8.dp))
                CityLazyGrid(
                    cityViewModel = cityViewModel,
                    onLocationClick = onLocationClick,
                    toDailyClick = { locationName, longitude, latitude ->
                        toDailyClick(locationName, longitude, latitude)
                    }
                )
            }
        } else {
            LcePage(uiState = places, onRetryClick = { onRetryClick() }) { result ->
                QueryResultLazyColumnContent(places = result) { locationName, longitude, latitude ->
                    toDailyClick(locationName, longitude, latitude)
                }
            }
        }
    }
}