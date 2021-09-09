package com.ramanhmr.weather.data.database

import android.content.Context
import androidx.room.Room

object WeatherDatabaseConstructor {
    fun create(context: Context): WeatherDatabase =
        Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            "weather_database"
        ).build()
}