package com.zqc.weather.ui.city.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardBackspace
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.weather.R

@Composable
fun CityHeaderContent(
    visibleCity: Boolean,
    onBackClick: () -> Unit
) {
    AnimatedVisibility(
        visible = visibleCity,
        enter = fadeIn(animationSpec = tween(100)),
        exit = fadeOut(animationSpec = tween(100))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardBackspace,
                    contentDescription = "back",
                    modifier = Modifier.size(35.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.city_manage_text), fontSize = 30.sp)
        }
    }
}