package com.ramanhmr.weather.data.repositories

import com.ramanhmr.weather.data.database.WeatherCurrentDao
import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.entities.WeatherCurrentEntityDB
import com.ramanhmr.weather.data.interfaces.current.LocalWeatherCurrentSource
import com.ramanhmr.weather.data.utils.ConversionUtils
import java.util.*

class LocalCurrentRepository(private val currentDao: WeatherCurrentDao) :
    LocalWeatherCurrentSource {
    override suspend fun getCurrentByName(cityName: String): Weather? =
        currentDao.getCurrent(cityName)?.toWeather()

    override suspend fun getCurrentByCityCountry(cityName: String, countryCode: String): Weather? =
        currentDao.getCurrent(cityName, countryCode)?.toWeather()

    override suspend fun saveCurrent(weather: Weather) {
        currentDao.insertForecast(weather.toCurrentEntity())
    }

    override suspend fun saveCurrent(weatherList: List<Weather>) {
        currentDao.insertForecast(weatherList.map { it.toCurrentEntity() })
    }

    override suspend fun deleteCity(cityName: String, countryCode: String) {
        currentDao.deleteCity(cityName, countryCode)
    }

    private fun WeatherCurrentEntityDB.toWeather() = Weather(
        this.cityName,
        this.countryCode,
        Date(this.time),
        this.temperature,
        this.maxTemperature,
        this.minTemperature,
        this.humidity,
        ConversionUtils.iconOWMToWeatherType(this.weatherTypeId)
    )

    private fun Weather.toCurrentEntity() = WeatherCurrentEntityDB(
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