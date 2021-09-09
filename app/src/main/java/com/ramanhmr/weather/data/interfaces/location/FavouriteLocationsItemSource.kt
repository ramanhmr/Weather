package com.ramanhmr.weather.data.interfaces.location

import com.ramanhmr.weather.data.entities.City

interface FavouriteLocationsItemSource : FavouriteLocationsBasicSource {
    suspend fun hasCity(city: City): Boolean
}