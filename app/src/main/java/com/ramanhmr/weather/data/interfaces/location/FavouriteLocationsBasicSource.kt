package com.ramanhmr.weather.data.interfaces.location

import com.ramanhmr.weather.data.entities.City

interface FavouriteLocationsBasicSource {
    suspend fun addCity(city: City)
    suspend fun deleteCity(city: City)
}