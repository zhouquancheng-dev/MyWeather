package com.zqc.model.city

import com.google.gson.annotations.SerializedName

/**
 * 搜索全球城市Bean
 */
data class PlaceResponse(
    val status: String,
    val places: List<PlaceEntity>,
) {
    /**
     * @param id id
     * @param name 城市名
     * @param location 城市经纬度
     * @param address 详细地址
     * @param placeId 城市id
     */
    data class PlaceEntity(
        val id: String,
        val name: String,
        val location: LocationEntity,
        @SerializedName("formatted_address") val address: String,
        @SerializedName("place_id") val placeId: String
    )

    /**
     * @param lat 纬度latitude
     * @param lng 经度longitude
     */
    data class LocationEntity(
        val lat: String,
        val lng: String
    )
}