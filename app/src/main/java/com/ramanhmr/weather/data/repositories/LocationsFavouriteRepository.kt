package com.ramanhmr.weather.data.repositories

import com.ramanhmr.weather.data.database.FavouriteCityDao
import com.ramanhmr.weather.data.entities.City
import com.ramanhmr.weather.data.entities.CityEntityDB
import com.ramanhmr.weather.data.interfaces.location.FavouriteLocationsItemSource
import com.ramanhmr.weather.data.interfaces.location.FavouriteLocationsListSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocationsFavouriteRepository(private val cityDao: FavouriteCityDao) :
    FavouriteLocationsListSource, FavouriteLocationsItemSource {
    override fun getCityListFlow(): Flow<List<City>> =
        cityDao.getCityListFlow().map {
            it.map { cityDB -> cityDB.toCity() }
        }

    override suspend fun getCities(): List<City> =
        cityDao.getCityList().map { it.toCity() }

    override suspend fun hasCity(city: City): Boolean = cityDao.hasCity(city.name, city.countryCode)

    override suspend fun addCity(city: City) {
        cityDao.addCity(city.toCityEntityDB())
    }

    override suspend fun deleteCity(city: City) {
        cityDao.deleteCity(city.toCityEntityDB())
    }

    private fun City.toCityEntityDB() = CityEntityDB(this.name, this.countryCode)
    private fun CityEntityDB.toCity() = City(this.name, this.countryCode)
}