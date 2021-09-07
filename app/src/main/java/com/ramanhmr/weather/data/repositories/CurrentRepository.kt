package com.ramanhmr.weather.data.repositories

import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.entities.WeatherCurrentResponseEntity
import com.ramanhmr.weather.data.interfaces.WeatherCurrentSource
import com.ramanhmr.weather.data.restApi.WeatherApi
import com.ramanhmr.weather.data.utils.ConversionUtils
import java.util.*

class CurrentRepository(private val weatherApi: WeatherApi) : WeatherCurrentSource {

    override suspend fun getCurrentByName(cityName: String): Weather? =
        weatherApi.getCurrentByName(cityName).body()?.toWeather()

    override suspend fun getCurrentByCoord(latitude: Double, longitude: Double): Weather? =
        weatherApi.getCurrentByCoord(latitude, longitude).body()?.toWeather()

    private fun WeatherCurrentResponseEntity.toWeather() =
        Weather(
            Date(this.dt.toLong()*1000),
            this.main.temp,
            this.main.tempMax,
            this.main.tempMin,
            this.main.humidity,
            this.weather[0].description,
            ConversionUtils.iconOWMToWeatherType(this.weather[0].icon)
        )
}