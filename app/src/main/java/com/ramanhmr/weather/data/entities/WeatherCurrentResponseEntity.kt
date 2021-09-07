package com.ramanhmr.weather.data.entities

import com.google.gson.annotations.SerializedName

data class WeatherCurrentResponseEntity(
    @SerializedName("coord") val coord: Coord,
    @SerializedName("weather") val weather: List<EntityWeather>,
    @SerializedName("base") val base: String,
    @SerializedName("main") val main: Main,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("wind") val wind: Wind,
    @SerializedName("clouds") val clouds: Clouds,
    @SerializedName("rain") val rain: Rain,
    @SerializedName("snow") val snow: Snow,
    @SerializedName("dt") val dt: Int,
    @SerializedName("sys") val sys: Sys,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("cod") val cod: Int
) {
    data class Coord(
        @SerializedName("lon") val lon: Double,
        @SerializedName("lat") val lat: Double
    )

    data class EntityWeather(
        @SerializedName("id") val id: Int,
        @SerializedName("main") val main: String,
        @SerializedName("description") val description: String,
        @SerializedName("icon") val icon: String
    )

    data class Main(
        @SerializedName("temp") val temp: Double,
        @SerializedName("feels_like") val feelsLike: Double,
        @SerializedName("temp_min") val tempMin: Double,
        @SerializedName("temp_max") val tempMax: Double,
        @SerializedName("pressure") val pressure: Int,
        @SerializedName("humidity") val humidity: Int,
        @SerializedName("sea_level") val seaLevel: Int,
        @SerializedName("grnd_level") val grndLevel: Int
    )

    data class Wind(
        @SerializedName("speed") val speed: Double,
        @SerializedName("deg") val deg: Int,
        @SerializedName("gust") val gust: Double
    )

    data class Clouds(
        @SerializedName("all") val all: Int
    )

    data class Rain(
        @SerializedName("1h") val h1: Double,
        @SerializedName("3h") val h3: Double
    )

    data class Snow(
        @SerializedName("1h") val h1: Double,
        @SerializedName("3h") val h3: Double
    )

    data class Sys(
        @SerializedName("type") val type: Int,
        @SerializedName("id") val id: Int,
        @SerializedName("message") val message: Double,
        @SerializedName("country") val country: String,
        @SerializedName("sunrise") val sunrise: Long,
        @SerializedName("sunset") val sunset: Long
    )
}

