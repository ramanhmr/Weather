package com.ramanhmr.weather.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ramanhmr.weather.data.entities.WeatherCurrentEntityDB

@Dao
interface WeatherCurrentDao {
    @Query("SELECT * FROM weather_current_table WHERE cityName = :cityName AND countryCode = :countryCode")
    suspend fun getCurrent(cityName: String, countryCode: String): WeatherCurrentEntityDB?

    @Query("SELECT * FROM weather_current_table WHERE cityName = :cityName")
    suspend fun getCurrent(cityName: String): WeatherCurrentEntityDB?

    @Insert(entity = WeatherCurrentEntityDB::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(weatherEntity: WeatherCurrentEntityDB)

    @Insert(entity = WeatherCurrentEntityDB::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(weatherEntities: List<WeatherCurrentEntityDB>)

    @Query("DELETE FROM weather_current_table WHERE cityName = :cityName AND countryCode = :countryCode")
    suspend fun deleteCity(cityName: String, countryCode: String)
}