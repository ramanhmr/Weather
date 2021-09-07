package com.ramanhmr.weather.data.interfaces

import com.ramanhmr.weather.data.entities.Weather

interface WeatherForecastSource {
    suspend fun getForecastByName(cityName: String): List<Weather>?
    suspend fun getForecastByCoord(latitude: Double, longitude: Double): List<Weather>?
}