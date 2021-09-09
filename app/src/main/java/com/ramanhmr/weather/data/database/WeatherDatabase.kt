package com.ramanhmr.weather.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ramanhmr.weather.data.entities.CityEntityDB
import com.ramanhmr.weather.data.entities.WeatherCurrentEntityDB
import com.ramanhmr.weather.data.entities.WeatherForecastEntityDB

@Database(
    entities = [CityEntityDB::class, WeatherForecastEntityDB::class, WeatherCurrentEntityDB::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun cityDao(): FavouriteCityDao
    abstract fun weatherForecastDao(): WeatherForecastDao
    abstract fun weatherCurrentDao(): WeatherCurrentDao
}