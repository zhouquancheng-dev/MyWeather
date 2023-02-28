package com.zqc.mdoel.popupWindow

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ShowDropdownMenu(
    showPopupWindow: MutableState<Boolean>,
    title: String,
    content: String,
) {
    if (!showPopupWindow.value) return
    DropdownMenu(
        expanded = showPopupWindow.value,
        onDismissRequest = { showPopupWindow.value = false },
        modifier = Modifier
            .width(300.dp)
            .padding(horizontal = 15.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content)
        }
    }
}