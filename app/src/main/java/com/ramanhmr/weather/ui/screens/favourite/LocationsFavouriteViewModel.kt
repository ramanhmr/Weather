package com.ramanhmr.weather.ui.screens.favourite

import androidx.lifecycle.*
import com.ramanhmr.weather.data.entities.City
import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.interfaces.current.ExternalWeatherCurrentSource
import com.ramanhmr.weather.data.interfaces.current.LocalWeatherCurrentSource
import com.ramanhmr.weather.data.interfaces.location.FavouriteLocationsListSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LocationsFavouriteViewModel(
    locationsSource: FavouriteLocationsListSource,
    private val currentExternalSource: ExternalWeatherCurrentSource,
    private val currentLocalSource: LocalWeatherCurrentSource
) : ViewModel() {
    val cityWeatherLiveData by lazy { MutableLiveData<List<Weather>>() }
    var hasInternet = false

    private val cityLiveData: LiveData<List<City>> =
        locationsSource.getCityListFlow().asLiveData(Dispatchers.IO)
    private val citiesObserver = Observer<List<City>> {
        viewModelScope.launch(Dispatchers.IO) {
            cityWeatherLiveData.postValue(listCurrentCitiesWeather(it))
        }
    }

    init {
        cityLiveData.observeForever(citiesObserver)
    }

    override fun onCleared() {
        cityLiveData.removeObserver(citiesObserver)
        super.onCleared()
    }

    fun checkCityAndCall(
        cityName: String,
        actionTrue: (String) -> Unit,
        actionFalse: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = async(Dispatchers.IO) { currentExternalSource.hasCity(cityName) }
            if (result.await()) actionTrue(cityName) else actionFalse(cityName)
        }
    }

    fun updateWeather() {
        if (cityLiveData.value != null) {
            viewModelScope.launch(Dispatchers.IO) {
                cityWeatherLiveData.postValue(listCurrentCitiesWeather(cityLiveData.value!!))
            }
        }
    }

    private suspend fun listCurrentCitiesWeather(cities: List<City>): List<Weather> {
        val result = mutableListOf<Weather>()
        if (hasInternet) {
            cities.map { city ->
                currentExternalSource.getCurrentByCityCountry(city.name, city.countryCode)?.let {
                    result.add(it)
                }
            }
            currentLocalSource.saveCurrent(result)
        } else {
            cities.map { city ->
                currentLocalSource.getCurrentByCityCountry(city.name, city.countryCode)?.let {
                    result.add(it)
                }
            }
        }
        return result
    }
}