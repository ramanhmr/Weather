package com.ramanhmr.weather.data.restApi

import com.ramanhmr.weather.BuildConfig
import com.ramanhmr.weather.data.entities.WeatherCurrentResponseEntity
import com.ramanhmr.weather.data.entities.WeatherForecastResponseEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("/data/2.5/forecast?appid=${BuildConfig.WEATHER_API_KEY}&units=${WeatherApiService.UNITS_METRIC}")
    suspend fun getForecastByName(
        @Query("q") cityName: String
    ): Response<WeatherForecastResponseEntity>

    @GET("/data/2.5/forecast?appid=${BuildConfig.WEATHER_API_KEY}&units=${WeatherApiService.UNITS_METRIC}")
    suspend fun getForecastByCoord(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<WeatherForecastResponseEntity>

    @GET("/data/2.5/weather?appid=${BuildConfig.WEATHER_API_KEY}&units=${WeatherApiService.UNITS_METRIC}")
    suspend fun getCurrentByName(
        @Query("q") cityName: String,
    ): Response<WeatherCurrentResponseEntity>

    @GET("/data/2.5/weather?appid=${BuildConfig.WEATHER_API_KEY}&units=${WeatherApiService.UNITS_METRIC}")
    suspend fun getCurrentByCoord(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<WeatherCurrentResponseEntity>
}