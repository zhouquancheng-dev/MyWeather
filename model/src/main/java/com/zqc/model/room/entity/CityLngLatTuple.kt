package com.zqc.model.room.entity

import androidx.room.ColumnInfo

data class CityLngLatTuple(
    @ColumnInfo(name = "longitude") val longitude: String,
    @ColumnInfo(name = "latitude") val latitude: String
)
