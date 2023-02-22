package com.zqc.model.room.dao

import androidx.room.*
import com.zqc.model.room.entity.CityInfo
import com.zqc.model.room.entity.CityLngLatTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface CityInfoDao {

    @Query("SELECT * FROM city_info ORDER BY is_location DESC, id ASC")
    fun loadAllCityInfoFlow(): Flow<List<CityInfo>>

    @Query("SELECT * FROM city_info")
    suspend fun loadAllCityInfo(): List<CityInfo>

    @Query("SELECT longitude, latitude FROM city_info WHERE id = :id")
    suspend fun loadLngLatInfo(id: Int): CityLngLatTuple

    @Query("SELECT * FROM city_info WHERE is_location = :isLocation")
    suspend fun loadIsLocationList(isLocation: Int = 1): List<CityInfo>

    @Query("SELECT 1 FROM city_info where address = :addressName LIMIT 1")
    fun loadHasCityFlow(addressName: String): Flow<Int?>

    @Query("SELECT 1 FROM city_info where address = :addressName LIMIT 1")
    suspend fun loadHasCity(addressName: String): Int?

    @Query("SELECT COUNT(*) FROM city_info")
    suspend fun loadCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(cityInfo: CityInfo): Long

    @Update
    suspend fun updateCity(newCityInfo: CityInfo)

    @Delete
    suspend fun deleteCity(cityInfo: CityInfo): Int

    @Query("DELETE FROM city_info")
    suspend fun deleteAll()

}