package com.zqc.weather.ui.daily.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zqc.weather.R

@Composable
fun AddButton(
    toWeatherClick: () -> Unit
) {
    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
       IconButton(onClick = { toWeatherClick() }) {
           Icon(
               imageVector = Icons.Filled.Add,
               contentDescription = null,
               modifier = Modifier.size(45.dp)
           )
       }
        Text(text = stringResource(id = R.string.add_city_text), modifier = Modifier.padding(top = 8.dp))
    }
}