package com.ramanhmr.weather.data.interfaces.forecast

import com.ramanhmr.weather.data.entities.Weather

interface ExternalWeatherForecastSource : WeatherForecastSource {
    suspend fun getForecastByCoord(latitude: Float, longitude: Float): List<Weather>?
}