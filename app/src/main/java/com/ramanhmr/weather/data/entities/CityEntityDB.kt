package com.ramanhmr.weather.data.entities

import androidx.room.Entity

@Entity(tableName = "fav_cities_table", primaryKeys = ["name", "countryCode"])
data class CityEntityDB(
    val name: String,
    val countryCode: String
)