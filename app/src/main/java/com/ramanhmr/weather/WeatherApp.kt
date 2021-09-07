package com.ramanhmr.weather

import android.app.Application
import com.ramanhmr.weather.data.interfaces.WeatherCurrentSource
import com.ramanhmr.weather.data.interfaces.WeatherForecastSource
import com.ramanhmr.weather.data.repositories.CurrentRepository
import com.ramanhmr.weather.data.repositories.ForecastRepository
import com.ramanhmr.weather.data.restApi.WeatherApiService
import com.ramanhmr.weather.data.viewmodels.WeatherViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

class WeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@WeatherApp)
            modules(viewModels, repositories, dataAccess)
        }
    }

    private val viewModels = module {
        viewModel { WeatherViewModel(get(), get()) }
    }

    private val repositories = module {
        factory { ForecastRepository(get()) } bind (WeatherForecastSource::class)
        factory { CurrentRepository(get()) } bind (WeatherCurrentSource::class)
    }

    private val dataAccess = module {
        single { WeatherApiService.getWeatherService() }
    }
}