package com.zqc.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @param id id
 * @param isLocation 定位id
 * @param longitude 经度
 * @param latitude 纬度
 * @param address 全地址 如：广东省深圳市罗湖区……
 * @param province 省信息 如：广东省
 * @param city 城市 如：深圳市
 * @param district 城区 如：罗湖区
 * @param street 街道 如：清水河街道泥岗路
 */
@Entity(tableName = "city_info")
data class CityInfo(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "is_location") val isLocation: Int = 0,
    @ColumnInfo(name = "longitude") val longitude: String = "",
    @ColumnInfo(name = "latitude") val latitude: String = "",
    @ColumnInfo(name = "address") val address: String = "",
    @ColumnInfo(name = "province") val province: String? = "",
    @ColumnInfo(name = "city") val city: String? = "",
    @ColumnInfo(name = "district") val district: String? = "",
    @ColumnInfo(name = "street") val street: String? = ""
)
