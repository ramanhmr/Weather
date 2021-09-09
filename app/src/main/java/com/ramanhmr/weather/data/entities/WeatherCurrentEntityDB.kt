package com.ramanhmr.weather.data.entities

import androidx.room.Entity

@Entity(tableName = "weather_current_table", primaryKeys = ["cityName", "countryCode"])
data class WeatherCurrentEntityDB(
    val cityName: String,
    val countryCode: String,
    val time: Long,
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val humidity: Int,
    val weatherTypeId: String
)