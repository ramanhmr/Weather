package com.ramanhmr.weather.data.background

import android.content.Context
import android.net.ConnectivityManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ramanhmr.weather.data.entities.City
import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.interfaces.current.ExternalWeatherCurrentSource
import com.ramanhmr.weather.data.interfaces.current.LocalWeatherCurrentSource
import com.ramanhmr.weather.data.interfaces.forecast.ExternalWeatherForecastSource
import com.ramanhmr.weather.data.interfaces.forecast.LocalWeatherForecastSource
import com.ramanhmr.weather.data.interfaces.location.FavouriteLocationsListSource
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class UpdateWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams), KoinComponent {

    override fun doWork(): Result {
        val locationsSource: FavouriteLocationsListSource by inject()
        val currentExternalSource: ExternalWeatherCurrentSource by inject()
        val forecastExternalSource: ExternalWeatherForecastSource by inject()
        val currentLocalSource: LocalWeatherCurrentSource by inject()
        val forecastLocalSource: LocalWeatherForecastSource by inject()
        val cm =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (cm.activeNetworkInfo?.isConnected == true) {
            val cities = mutableListOf<City>()
            val newCurrentData = mutableListOf<Weather>()
            val newForecastData = mutableListOf<Weather>()
            runBlocking { cities.addAll(locationsSource.getCities()) }
            runBlocking {
                launch {
                    cities.forEach { city ->
                        currentExternalSource.getCurrentByCityCountry(
                            city.name,
                            city.countryCode
                        )?.let { newCurrentData.add(it) }
                    }
                }
                launch {
                    cities.forEach { city ->
                        forecastExternalSource.getForecastByCityCountry(
                            city.name,
                            city.countryCode
                        )?.let { newForecastData.addAll(it) }
                    }
                }
            }
            runBlocking {
                launch { currentLocalSource.saveCurrent(newCurrentData) }
                launch { forecastLocalSource.saveForecast(newForecastData) }
            }
            return Result.success()
        } else {
            return Result.retry()
        }
    }
}