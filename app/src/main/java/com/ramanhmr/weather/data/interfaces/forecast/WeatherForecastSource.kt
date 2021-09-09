package com.ramanhmr.weather.data.interfaces.forecast

import com.ramanhmr.weather.data.entities.Weather

interface WeatherForecastSource {
    suspend fun getForecastByCityCountry(cityName: String, countryCode: String): List<Weather>?
    suspend fun getForecastByName(cityName: String): List<Weather>?
}