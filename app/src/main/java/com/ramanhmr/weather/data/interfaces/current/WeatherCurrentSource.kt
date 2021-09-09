package com.ramanhmr.weather.data.interfaces.current

import com.ramanhmr.weather.data.entities.Weather

interface WeatherCurrentSource {
    suspend fun getCurrentByName(cityName: String): Weather?
    suspend fun getCurrentByCityCountry(cityName: String, countryCode: String): Weather?
}