package com.ramanhmr.weather.data.restApi

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherApiService {
    private const val BASE_WEATHER_URL = "https://api.openweathermap.org"

    const val UNITS_METRIC = "metric"

    fun getWeatherService(): WeatherApi = getWeatherRetrofit().create(WeatherApi::class.java)

    private fun getWeatherRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_WEATHER_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .build()
        )
        .build()
}