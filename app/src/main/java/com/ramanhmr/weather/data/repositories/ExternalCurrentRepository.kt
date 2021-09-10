package com.ramanhmr.weather.data.repositories

import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.entities.WeatherCurrentResponseEntity
import com.ramanhmr.weather.data.interfaces.current.ExternalWeatherCurrentSource
import com.ramanhmr.weather.data.restApi.WeatherApi
import com.ramanhmr.weather.data.utils.ConversionUtils
import java.util.*

class ExternalCurrentRepository(private val weatherApi: WeatherApi) : ExternalWeatherCurrentSource {
    override suspend fun hasCity(cityName: String): Boolean =
        weatherApi.getCurrentByCity(cityName).body() != null

    override suspend fun getCurrentByName(cityName: String): Weather? =
        weatherApi.getCurrentByCity(cityName).body()?.toWeather()

    override suspend fun getCurrentByCityCountry(cityName: String, countryCode: String): Weather? =
        weatherApi.getCurrentByCity("$cityName,$countryCode").body()?.toWeather()

    override suspend fun getCurrentByCoord(latitude: Float, longitude: Float): Weather? =
        weatherApi.getCurrentByCoord(latitude, longitude).body()?.toWeather()

    private fun WeatherCurrentResponseEntity.toWeather() =
        Weather(
            this.name ?: "",
            this.sys.country ?: "",
            Date(this.dt.toLong() * 1000),
            this.main.temp,
            this.main.tempMax,
            this.main.tempMin,
            this.main.humidity,
            ConversionUtils.iconOWMToWeatherType(this.weather[0].icon)
        )
}