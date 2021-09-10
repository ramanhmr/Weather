package com.ramanhmr.weather

import android.app.Application
import androidx.work.BackoffPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.ramanhmr.weather.data.background.UpdateWorker
import com.ramanhmr.weather.data.database.WeatherDatabase
import com.ramanhmr.weather.data.database.WeatherDatabaseConstructor
import com.ramanhmr.weather.data.interfaces.current.ExternalWeatherCurrentSource
import com.ramanhmr.weather.data.interfaces.current.LocalWeatherCurrentSource
import com.ramanhmr.weather.data.interfaces.forecast.ExternalWeatherForecastSource
import com.ramanhmr.weather.data.interfaces.forecast.LocalWeatherForecastSource
import com.ramanhmr.weather.data.interfaces.location.FavouriteLocationsItemSource
import com.ramanhmr.weather.data.interfaces.location.FavouriteLocationsListSource
import com.ramanhmr.weather.data.repositories.*
import com.ramanhmr.weather.data.restApi.WeatherApiService
import com.ramanhmr.weather.ui.screens.locations.LocationsFavouriteViewModel
import com.ramanhmr.weather.ui.screens.weather.WeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.KoinApiExtension
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

@KoinApiExtension
class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApp)
            modules(viewModels, repositories, dataAccess)
        }
        val updateRequest = PeriodicWorkRequestBuilder<UpdateWorker>(1, TimeUnit.HOURS)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 30, TimeUnit.MINUTES)
            .build()
        WorkManager
            .getInstance(this)
            .enqueue(updateRequest)
    }

    private val viewModels = module {
        viewModel { WeatherViewModel(get(), get(), get(), get(), get()) }
        viewModel { LocationsFavouriteViewModel(get(), get(), get()) }
    }

    private val repositories = module {
        factory { ExternalForecastRepository(get()) } bind (ExternalWeatherForecastSource::class)
        factory { ExternalCurrentRepository(get()) } bind (ExternalWeatherCurrentSource::class)
        factory { LocalForecastRepository(get()) } bind (LocalWeatherForecastSource::class)
        factory { LocalCurrentRepository(get()) } bind (LocalWeatherCurrentSource::class)
        factory { LocationsFavouriteRepository(get()) } binds (arrayOf(
            FavouriteLocationsListSource::class,
            FavouriteLocationsItemSource::class
        ))
    }

    private val dataAccess = module {
        factory { WeatherApiService.getWeatherService() }
        single { WeatherDatabaseConstructor.create(get()) }
        factory { get<WeatherDatabase>().weatherForecastDao() }
        factory { get<WeatherDatabase>().weatherCurrentDao() }
        factory { get<WeatherDatabase>().cityDao() }
    }
}