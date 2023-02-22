package com.zqc.mdoel.swipe

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

@ExperimentalMaterialApi
@Composable
fun SwipeLayout(
    modifier: Modifier = Modifier,
    swipeState: SwipeableState<Int>,
    isShowChildItem: Boolean = true,
    swipeStyle: SwipeStyle = SwipeStyle.EndToStart,
    childItemContent: @Composable () -> Unit,
    primaryContent: @Composable () -> Unit
) {
    val deleteWidth = remember { mutableStateOf(1) }
    val contentHeight = remember { mutableStateOf(1) }

    val childHeight = with(LocalDensity.current) { contentHeight.value.toDp() }
    val anchors = mapOf(deleteWidth.value.toFloat() to 1, 0.7f to 0)
    Box(
        modifier = modifier
            .swipeable(
                state = swipeState,
                anchors = anchors,
                orientation = Orientation.Horizontal,
                reverseDirection = swipeStyle == SwipeStyle.EndToStart,
                thresholds = { _, _ ->
                    FractionalThreshold(0.7f)
                }
        )
    ) {
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    deleteWidth.value = it.size.width
                }
                .height(childHeight)
                .align(getDeleteAlign(swipeStyle = swipeStyle)),
            contentAlignment = getDeleteAlign(swipeStyle = swipeStyle)
        ) {
            childItemContent()
        }

        val offsetX: Int =
            if (isShowChildItem) {
                if (swipeStyle == SwipeStyle.EndToStart) {
                    -swipeState.offset.value.roundToInt()
                } else {
                    swipeState.offset.value.roundToInt()
                }
            } else {
                0
            }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    contentHeight.value = it.size.height
                }
                .offset { IntOffset(x = offsetX, y = 0) }
        ) {
            primaryContent()
        }
    }
}

@Composable
private fun getDeleteAlign(swipeStyle: SwipeStyle) =
    if (swipeStyle == SwipeStyle.EndToStart) Alignment.CenterEnd else Alignment.CenterStart