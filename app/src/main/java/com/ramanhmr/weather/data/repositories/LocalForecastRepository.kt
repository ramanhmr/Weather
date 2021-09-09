package com.ramanhmr.weather.data.repositories

import com.ramanhmr.weather.data.database.WeatherForecastDao
import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.entities.WeatherForecastEntityDB
import com.ramanhmr.weather.data.interfaces.forecast.LocalWeatherForecastSource
import com.ramanhmr.weather.data.utils.ConversionUtils
import java.util.*

class LocalForecastRepository(
    private val forecastDao: WeatherForecastDao
) : LocalWeatherForecastSource {
    override suspend fun getForecastByCityCountry(
        cityName: String,
        countryCode: String
    ): List<Weather>? = forecastDao.getForecasts(cityName, countryCode)?.map { it.toWeather() }

    override suspend fun getForecastByName(cityName: String): List<Weather>? =
        forecastDao.getForecasts(cityName)?.map { it.toWeather() }

    override suspend fun saveForecast(weather: Weather) {
        forecastDao.insertForecast(weather.toForecastEntity())
    }

    override suspend fun saveForecast(weatherList: List<Weather>) {
        forecastDao.insertForecast(weatherList.map { it.toForecastEntity() })
    }

    override suspend fun deleteCity(cityName: String, countryCode: String) {
        forecastDao.deleteCity(cityName, countryCode)
    }

    override suspend fun deleteBeforeTime(timeLong: Long) {
        forecastDao.deleteBeforeTime(timeLong)
    }

    private fun WeatherForecastEntityDB.toWeather() = Weather(
        this.cityName,
        this.countryCode,
        Date(this.time),
        this.temperature,
        this.maxTemperature,
        this.minTemperature,
        this.humidity,
        ConversionUtils.iconOWMToWeatherType(this.weatherTypeId)
    )

    private fun Weather.toForecastEntity() = WeatherForecastEntityDB(
        this.city,
        this.countryCode,
        this.date.time,
        this.temperature,
        this.maxTemperature,
        this.minTemperature,
        this.humidity,
        this.weatherType.iconOWM
    )
}