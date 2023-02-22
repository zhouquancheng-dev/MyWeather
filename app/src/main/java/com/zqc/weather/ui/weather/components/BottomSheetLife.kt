package com.zqc.weather.ui.weather.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zqc.model.weather.DailyResponse
import com.zqc.utils.R

fun LazyListScope.bottomSheet(
    lifeIndex: DailyResponse.Daily,
    toBottomSheetClick: (title: String) -> Unit
) {
    item(contentType = { lifeIndex.lifeIndex }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(horizontal = 15.dp, vertical = 20.dp),
            shape = RoundedCornerShape(20.dp),
        ) {
            LifeIndexContent(lifeIndex = lifeIndex, toBottomSheetClick = { toBottomSheetClick(it) })
        }
    }
}

@Composable
private fun LifeIndexContent(
    lifeIndex: DailyResponse.Daily,
    toBottomSheetClick: (title: String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val modifier = Modifier
            .weight(1f)
            .padding(5.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 60.dp, top = 8.dp)
        ) {
            Ultraviolet(
                lifeIndex = lifeIndex.lifeIndex,
                modifier = modifier,
                toBottomSheetClick = { title -> toBottomSheetClick(title) }
            )
            Dressing(
                lifeIndex = lifeIndex.lifeIndex,
                modifier = modifier,
                toBottomSheetClick = { title -> toBottomSheetClick(title) }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(start = 60.dp, bottom = 8.dp)
        ) {
            CarWashing(
                lifeIndex = lifeIndex.lifeIndex,
                modifier = modifier,
                toBottomSheetClick = { title -> toBottomSheetClick(title) }
            )
            ColdRisk(
                lifeIndex = lifeIndex.lifeIndex,
                modifier = modifier,
                toBottomSheetClick = { title -> toBottomSheetClick(title) }
            )
        }
    }
}

@Composable
private fun Ultraviolet(
    lifeIndex: DailyResponse.Daily.LifeIndex,
    modifier: Modifier = Modifier,
    toBottomSheetClick: (title: String) -> Unit
) {
    Row(
        modifier = modifier.clickable { toBottomSheetClick("紫外线") },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_uv),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = stringResource(id = R.string.life_ultraviolet), fontSize = 12.sp)
            Text(
                text = lifeIndex.ultraviolet[0].desc,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}

@Composable
private fun Dressing(
    lifeIndex: DailyResponse.Daily.LifeIndex,
    modifier: Modifier = Modifier,
    toBottomSheetClick: (title: String) -> Unit
) {
    Row(
        modifier = modifier.clickable { toBottomSheetClick("穿衣") },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_clothes),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = stringResource(id = R.string.life_dressing), fontSize = 12.sp)
            Text(
                text = lifeIndex.dressing[0].desc,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}

@Composable
private fun CarWashing(
    lifeIndex: DailyResponse.Daily.LifeIndex,
    modifier: Modifier = Modifier,
    toBottomSheetClick: (title: String) -> Unit
) {
    Row(
        modifier = modifier.clickable { toBottomSheetClick("洗车") },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_car),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = stringResource(id = R.string.life_car_wash), fontSize = 12.sp)
            Text(
                text = lifeIndex.carWashing[0].desc,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}

@Composable
private fun ColdRisk(
    lifeIndex: DailyResponse.Daily.LifeIndex,
    modifier: Modifier = Modifier,
    toBottomSheetClick: (title: String) -> Unit
) {
    Row(
        modifier = modifier.clickable { toBottomSheetClick("感冒") },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cold),
            contentDescription = null,
            modifier = Modifier.size(30.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = stringResource(id = R.string.life_cold), fontSize = 12.sp)
            Text(
                text = lifeIndex.coldRisk[0].desc,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}