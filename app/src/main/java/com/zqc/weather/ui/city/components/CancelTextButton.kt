package com.zqc.weather.ui.city.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.zqc.mdoel.view.noRippleClickable
import com.zqc.weather.R

@Composable
fun CancelTextButton(
    visible: Boolean,
    onCloseQuery: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(100)),
        exit = fadeOut(animationSpec = tween(100))
    ) {
        Text(
            text = stringResource(id = R.string.cancel_text_button),
            color = MaterialTheme.colors.primary,
            fontSize = 15.sp,
            maxLines = 1,
            modifier = Modifier.noRippleClickable { onCloseQuery() }
        )
    }
}