package com.ramanhmr.weather.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ramanhmr.weather.data.entities.WeatherForecastEntityDB

@Dao
interface WeatherForecastDao {

    @Query("SELECT * FROM weather_forecast_table WHERE cityName = :cityName AND countryCode = :countryCode")
    suspend fun getForecasts(cityName: String, countryCode: String): List<WeatherForecastEntityDB>?

    @Query("SELECT * FROM weather_forecast_table WHERE cityName = :cityName")
    suspend fun getForecasts(cityName: String): List<WeatherForecastEntityDB>?

    @Insert(entity = WeatherForecastEntityDB::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(weatherEntity: WeatherForecastEntityDB)

    @Insert(entity = WeatherForecastEntityDB::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForecast(weatherEntities: List<WeatherForecastEntityDB>)

    @Query("DELETE FROM weather_forecast_table WHERE cityName = :cityName AND countryCode = :countryCode")
    suspend fun deleteCity(cityName: String, countryCode: String)

    @Query("DELETE FROM weather_forecast_table WHERE time < :timeLong")
    suspend fun deleteBeforeTime(timeLong: Long)
}