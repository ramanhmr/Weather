package com.ramanhmr.weather.data.interfaces.location

import com.ramanhmr.weather.data.entities.City
import kotlinx.coroutines.flow.Flow

interface FavouriteLocationsListSource : FavouriteLocationsBasicSource {
    fun getCityListFlow(): Flow<List<City>>
    suspend fun getCities(): List<City>
}