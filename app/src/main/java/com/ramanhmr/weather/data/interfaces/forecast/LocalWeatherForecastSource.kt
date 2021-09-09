package com.ramanhmr.weather.data.interfaces.forecast

import com.ramanhmr.weather.data.entities.Weather

interface LocalWeatherForecastSource : WeatherForecastSource {
    suspend fun saveForecast(weather: Weather)
    suspend fun saveForecast(weatherList: List<Weather>)
    suspend fun deleteCity(cityName: String, countryCode: String)
    suspend fun deleteBeforeTime(timeLong: Long)
}