package com.ramanhmr.weather.data.interfaces

import com.ramanhmr.weather.data.entities.Weather

interface WeatherCurrentSource {
    suspend fun getCurrentByName(cityName: String): Weather?
    suspend fun getCurrentByCoord(latitude: Double, longitude: Double): Weather?
}