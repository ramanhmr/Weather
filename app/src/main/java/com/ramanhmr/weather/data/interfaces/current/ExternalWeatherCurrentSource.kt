package com.ramanhmr.weather.data.interfaces.current

import com.ramanhmr.weather.data.entities.Weather

interface ExternalWeatherCurrentSource : WeatherCurrentSource {
    suspend fun hasCity(cityName: String): Boolean
    suspend fun getCurrentByCoord(latitude: Float, longitude: Float): Weather?
}