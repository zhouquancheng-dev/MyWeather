package com.zqc.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zqc.model.room.dao.CityInfoDao
import com.zqc.model.room.entity.CityInfo

@Database(
    entities = [CityInfo::class],
    version = 1
)
abstract class MyWeatherDatabase : RoomDatabase() {

    abstract fun cityInfoDao(): CityInfoDao

    //单例防止同时打开多个数据库实例
    companion object {

        private var instance: MyWeatherDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): MyWeatherDatabase {
            instance?.let {
                return it
            }
            return Room.databaseBuilder(
                context.applicationContext,
                MyWeatherDatabase::class.java,
                "my_weather_database"
            ).build().apply {
                instance = this
            }
        }
    }
}