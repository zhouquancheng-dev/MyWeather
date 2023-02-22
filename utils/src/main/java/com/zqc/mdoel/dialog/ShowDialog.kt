package com.zqc.mdoel.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ShowDialog(
    alertDialog: MutableState<Boolean>,
    title: String,
    content: String,
    cancelString: String,
    confirmString: String,
    onConfirmClick: () -> Unit
) {
    if (!alertDialog.value) return
    Dialog(onDismissRequest = { alertDialog.value = false }) {
        Card(
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Column(
                modifier = Modifier.padding(top = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colors.primary,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Text(
                    text = content,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(top = 12.dp, bottom = 25.dp, start = 20.dp, end = 20.dp)
                )
                Divider()
                Row {
                    //取消按钮
                    TextButton(
                        onClick = { alertDialog.value = false },
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp)
                    ) {
                        Text(
                            text = cancelString,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                    Divider(modifier = Modifier
                        .width(1.dp)
                        .height(45.dp))
                    //确定按钮
                    TextButton(
                        onClick = {
                            alertDialog.value = false
                            onConfirmClick()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(45.dp)
                    ) {
                        Text(
                            text = confirmString,
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Dialog预览", showBackground = true)
@Composable
fun ShowDialogPreview() {
    val alertDialog = remember { mutableStateOf(true) }
    ShowDialog(
        alertDialog = alertDialog,
        title = "对话框标题",
        content = "对话框内容对话框内容对话框内容对话框内容对话框内容对话框内容",
        cancelString = "取消",
        confirmString = "确定",
        onConfirmClick = {}
    )
}