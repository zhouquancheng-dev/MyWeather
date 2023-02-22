package com.zqc.weather.ui.city.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.model.city.PlaceResponse

@Composable
fun QueryResultLazyColumnContent(
    places: List<PlaceResponse.PlaceEntity>,
    toDailyClick: (locationName: String, longitude: String, latitude: String) -> Unit
) {
    LazyColumn {
        items(items = places, contentType = { it.name + it.address }) {
            PlacesResultItem(places = it) { locationName, longitude, latitude ->
                toDailyClick(locationName, longitude, latitude)
            }
        }
    }
}

@Composable
private fun PlacesResultItem(
    places: PlaceResponse.PlaceEntity,
    toDailyClick: (locationName: String, longitude: String, latitude: String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable {
                    toDailyClick(places.name, places.location.lng, places.location.lat)
                    Log.i("PlacesResultItem", "PlacesResultItem: ${places.name} ${places.location.lng} ${places.location.lat}")
                }
                .padding(start = 20.dp, end = 20.dp, top = 25.dp)
        ) {
            Text(text = places.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = places.address, fontSize = 18.sp)
        }
    }
}