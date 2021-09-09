package com.ramanhmr.weather.data.database

import androidx.room.*
import com.ramanhmr.weather.data.entities.CityEntityDB
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCityDao {

    @Query("SELECT * FROM fav_cities_table")
    fun getCityListFlow(): Flow<List<CityEntityDB>>

    @Query("SELECT * FROM fav_cities_table")
    suspend fun getCityList(): List<CityEntityDB>

    @Query("SELECT EXISTS(SELECT * FROM fav_cities_table WHERE name = :cityName AND countryCode = :countryCode)")
    suspend fun hasCity(cityName: String, countryCode: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCity(city: CityEntityDB)

    @Delete
    suspend fun deleteCity(city: CityEntityDB)
}