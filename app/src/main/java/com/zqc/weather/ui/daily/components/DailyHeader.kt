package com.zqc.weather.ui.daily.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DailyHeaderContent(
    title: String,
    onBackClick: () -> Unit
) {
    Column(modifier = Modifier.padding(20.dp)) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.Filled.KeyboardBackspace,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = title, fontSize = 30.sp)
    }
}