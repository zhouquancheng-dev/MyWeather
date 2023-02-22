package com.zqc.weather.ui.city.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zqc.mdoel.weather.cityItemsList
import com.zqc.mdoel.weather.getCity
import com.zqc.weather.R
import com.zqc.weather.ui.city.viewmodel.CityViewModel
import kotlinx.coroutines.FlowPreview

@ExperimentalLifecycleComposeApi
@FlowPreview
@Composable
fun CityLazyGrid(
    cityItems: List<String> = cityItemsList,
    cityViewModel: CityViewModel,
    onLocationClick: () -> Unit,
    toDailyClick: (locationName: String, longitude: String, latitude: String) -> Unit
) {
    LazyVerticalGrid(columns = GridCells.Fixed(3)) {
        item(contentType = { cityItems }) {
            LocateItem {
                onLocationClick()
            }
        }
        items(items = cityItems, contentType = { it }) { cityName ->
            val isHasCity by cityViewModel.isHasCity(cityName).collectAsStateWithLifecycle(null)
            CityListItem(
                cityItem = cityName,
                textColor = if (isHasCity != null) MaterialTheme.colors.primary else MaterialTheme.colors.onSurface
            ) {
                toDailyClick(cityName, getCity(cityName).longitude, getCity(cityName).latitude)
            }
        }
    }
}

@Composable
private fun LocateItem(onLocationClick: () -> Unit) {
    val itemModifier = Modifier
        .height(40.dp)
        .wrapContentSize()
    Surface(
        modifier = Modifier
            .width(80.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(60.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
    ) {
        Row(
            modifier = Modifier.clickable { onLocationClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = stringResource(id = R.string.locate_text), modifier = itemModifier, color = MaterialTheme.colors.primary)
            Icon(
                imageVector = Icons.Filled.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colors.primary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun CityListItem(
    cityItem: String,
    textColor: Color,
    toDailyClick: (cityName: String) -> Unit
) {
    val itemModifier = Modifier
        .height(40.dp)
        .wrapContentSize()
    Surface(
        modifier = Modifier
            .width(80.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(60.dp),
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
    ) {
        Box(
            modifier = Modifier.clickable { toDailyClick(cityItem) },
            contentAlignment = Alignment.Center
        ) {
            Text(text = cityItem, modifier = itemModifier, color = textColor)
        }
    }
}