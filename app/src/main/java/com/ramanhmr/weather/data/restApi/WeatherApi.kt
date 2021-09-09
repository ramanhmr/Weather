package com.ramanhmr.weather.data.restApi

import com.ramanhmr.weather.BuildConfig
import com.ramanhmr.weather.data.entities.WeatherCurrentResponseEntity
import com.ramanhmr.weather.data.entities.WeatherForecastResponseEntity
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    //Forecasts
    @GET("/data/2.5/forecast?appid=${BuildConfig.WEATHER_API_KEY}&units=${WeatherApiService.UNITS_METRIC}")
    suspend fun getForecastByCity(
        @Query("q") cityName: String
    ): Response<WeatherForecastResponseEntity>

    @GET("/data/2.5/forecast?appid=${BuildConfig.WEATHER_API_KEY}&units=${WeatherApiService.UNITS_METRIC}")
    suspend fun getForecastByCityCountry(
        @Query("q") cityName: String,
        @Query("q") countryCode: String
    ): Response<WeatherForecastResponseEntity>

    @GET("/data/2.5/forecast?appid=${BuildConfig.WEATHER_API_KEY}&units=${WeatherApiService.UNITS_METRIC}")
    suspend fun getForecastByCoord(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float
    ): Response<WeatherForecastResponseEntity>

    //Current
    @GET("/data/2.5/weather?appid=${BuildConfig.WEATHER_API_KEY}&units=${WeatherApiService.UNITS_METRIC}")
    suspend fun getCurrentByCity(
        @Query("q") cityName: String
    ): Response<WeatherCurrentResponseEntity>

    @GET("/data/2.5/weather?appid=${BuildConfig.WEATHER_API_KEY}&units=${WeatherApiService.UNITS_METRIC}")
    suspend fun getCurrentByCityCountry(
        @Query("q") cityName: String,
        @Query("q") countryCode: String
    ): Response<WeatherCurrentResponseEntity>

    @GET("/data/2.5/weather?appid=${BuildConfig.WEATHER_API_KEY}&units=${WeatherApiService.UNITS_METRIC}")
    suspend fun getCurrentByCoord(
        @Query("lat") latitude: Float,
        @Query("lon") longitude: Float
    ): Response<WeatherCurrentResponseEntity>
}