package com.ramanhmr.weather.data.repositories

import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.entities.WeatherForecastResponseEntity
import com.ramanhmr.weather.data.interfaces.WeatherForecastSource
import com.ramanhmr.weather.data.restApi.WeatherApi
import com.ramanhmr.weather.data.utils.ConversionUtils
import java.util.*

class ForecastRepository(private val weatherApi: WeatherApi) : WeatherForecastSource {

    override suspend fun getForecastByName(cityName: String): List<Weather>? =
        weatherApi.getForecastByName(cityName).body()?.toWeatherList()

    override suspend fun getForecastByCoord(latitude: Double, longitude: Double): List<Weather>? =
        weatherApi.getForecastByCoord(latitude, longitude).body()?.toWeatherList()

    private fun WeatherForecastResponseEntity.toWeatherList(): List<Weather> {
        val forecastList = mutableListOf<Weather>()
        this.list.forEach {
            forecastList.add(it.toWeather())
        }
        return forecastList
    }

    private fun WeatherForecastResponseEntity.ListInResponse.toWeather() =
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