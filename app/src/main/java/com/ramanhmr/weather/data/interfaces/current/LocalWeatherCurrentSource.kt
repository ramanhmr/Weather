package com.ramanhmr.weather.data.interfaces.current

import com.ramanhmr.weather.data.entities.Weather

interface LocalWeatherCurrentSource : WeatherCurrentSource {
    suspend fun saveCurrent(weather: Weather)
    suspend fun saveCurrent(weatherList: List<Weather>)
    suspend fun deleteCity(cityName: String, countryCode: String)
}