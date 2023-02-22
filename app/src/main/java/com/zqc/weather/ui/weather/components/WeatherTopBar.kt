package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.zqc.model.room.entity.CityInfo

@ExperimentalPagerApi
@Composable
fun WeatherTopBar(
    cityInfo: CityInfo,
    iconColor: Color,
    toCityClick: () -> Unit,
    toSettingClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(
            onClick = { toCityClick() }
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "add_location",
                modifier = Modifier.size(30.dp),
                tint = iconColor
            )
        }

        val address = if (cityInfo.isLocation == 1) {
            "${cityInfo.city}${cityInfo.district}${cityInfo.street}"
        } else {
            cityInfo.address
        }
        Text(
            text = address,
            fontSize = 21.sp,
            maxLines = 1,
            color = Color.White
        )

        IconButton(
            onClick = { toSettingClick() }
        ) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "setting",
                modifier = Modifier.size(30.dp),
                tint = iconColor
            )
        }
    }
}