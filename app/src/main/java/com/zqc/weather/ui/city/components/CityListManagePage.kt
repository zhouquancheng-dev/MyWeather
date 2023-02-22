package com.zqc.weather.ui.city.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.mdoel.swipe.SwipeLayout
import com.zqc.model.room.entity.CityInfo
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun CityListManagePage(
    visibleCity: Boolean,
    cityInfoList: List<CityInfo>,
    toWeatherClick: (Int) -> Unit,
    onDeleteCityClick: (CityInfo) -> Unit
) {
    AnimatedVisibility(
        visible = visibleCity,
        enter = fadeIn(animationSpec = tween(150)),
        exit = fadeOut(animationSpec = tween(100))
    ) {
        if (cityInfoList.isNotEmpty()) {
            CityListPage(
                cityInfoList = cityInfoList,
                toWeatherClick = { toWeatherClick(it) },
                onDeleteCityClick = { onDeleteCityClick(it) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun CityListPage(
    cityInfoList: List<CityInfo>,
    toWeatherClick: (Int) -> Unit,
    onDeleteCityClick: (CityInfo) -> Unit
) {
    LazyColumn(
        modifier = Modifier.height(650.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        itemsIndexed(
            items = cityInfoList,
            contentType = { _, item -> item.address }
        ) {index, citiInfo ->
            CityListItem(
                cityInfo = citiInfo,
                isShowChildItem = citiInfo.isLocation != 1,
                toWeatherClick = { toWeatherClick(index) },
                onDeleteCityClick = { onDeleteCityClick(citiInfo) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun CityListItem(
    cityInfo: CityInfo,
    isShowChildItem: Boolean = true,
    toWeatherClick: () -> Unit,
    onDeleteCityClick: (CityInfo) -> Unit
) {
    val scope = rememberCoroutineScope()
    val swipeState = rememberSwipeableState(0)

    SwipeLayout(
        swipeState = swipeState,
        isShowChildItem = isShowChildItem,
        childItemContent = {
            Surface(
                modifier = Modifier.size(85.dp).padding(8.dp),
                shape = CircleShape,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.05f)
            ) {
                Box(
                    modifier = Modifier
                        .clickable {
                            onDeleteCityClick(cityInfo)
                            scope.launch {
                                swipeState.animateTo(0)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteOutline,
                        contentDescription = null,
                        tint = Color.Red,
                        modifier = Modifier.size(50.dp).padding(8.dp)
                    )
                }
            }
        },
        primaryContent = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(18.dp),
                elevation = 5.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable { toWeatherClick() }
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = cityInfo.address,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(2f),
                        color = if (cityInfo.isLocation == 1) MaterialTheme.colors.primary else Color.Unspecified
                    )
                    if (cityInfo.isLocation == 1) {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier
                                .padding(end = 25.dp)
                                .size(25.dp)
                        )
                    }
                }
            }
        }
    )
}

@ExperimentalMaterialApi
@Preview(name = "城市item", showBackground = true)
@Composable
fun CityListItemPreview() {
    val cityInfo = CityInfo(
        address = "深圳市",
        isLocation = 1
    )
    CityListItem(cityInfo = cityInfo, toWeatherClick = {}, onDeleteCityClick = {})
}