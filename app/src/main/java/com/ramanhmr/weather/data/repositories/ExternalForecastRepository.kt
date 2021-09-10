package com.ramanhmr.weather.data.repositories

import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.entities.WeatherForecastResponseEntity
import com.ramanhmr.weather.data.interfaces.forecast.ExternalWeatherForecastSource
import com.ramanhmr.weather.data.restApi.WeatherApi
import com.ramanhmr.weather.data.utils.ConversionUtils
import java.util.*

class ExternalForecastRepository(private val weatherApi: WeatherApi) :
    ExternalWeatherForecastSource {

    override suspend fun getForecastByName(cityName: String): List<Weather>? =
        weatherApi.getForecastByCity(cityName).body()?.toWeatherList()

    override suspend fun getForecastByCityCountry(
        cityName: String,
        countryCode: String
    ): List<Weather>? =
        weatherApi.getForecastByCity("$cityName, $countryCode").body()?.toWeatherList()

    override suspend fun getForecastByCoord(latitude: Float, longitude: Float): List<Weather>? =
        weatherApi.getForecastByCoord(latitude, longitude).body()?.toWeatherList()

    private fun WeatherForecastResponseEntity.toWeatherList(): List<Weather> {
        val forecastList = mutableListOf<Weather>()
        val currentTime = System.currentTimeMillis()
        this.list.forEach {
            if (it.dt * 1000L > currentTime)
                forecastList.add(
                    Weather(
                        this.city.name ?: "",
                        this.city.country ?: "",
                        Date(it.dt * 1000L),
                        it.main.temp,
                        it.main.tempMax,
                        it.main.tempMin,
                        it.main.humidity,
                        ConversionUtils.iconOWMToWeatherType(it.weather[0].icon)
                    )
                )
        }
        return forecastList
    }
}