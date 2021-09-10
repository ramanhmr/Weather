package com.ramanhmr.weather.ui.screens.weather

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramanhmr.weather.data.entities.City
import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.interfaces.current.ExternalWeatherCurrentSource
import com.ramanhmr.weather.data.interfaces.current.LocalWeatherCurrentSource
import com.ramanhmr.weather.data.interfaces.forecast.ExternalWeatherForecastSource
import com.ramanhmr.weather.data.interfaces.forecast.LocalWeatherForecastSource
import com.ramanhmr.weather.data.interfaces.location.FavouriteLocationsItemSource
import com.ramanhmr.weather.data.utils.ConversionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(
    private val currentExternalSource: ExternalWeatherCurrentSource,
    private val currentLocalSource: LocalWeatherCurrentSource,
    private val forecastExternalSource: ExternalWeatherForecastSource,
    private val forecastLocalSource: LocalWeatherForecastSource,
    private val cityItemSource: FavouriteLocationsItemSource
) : ViewModel() {
    val city: MutableLiveData<String> = MutableLiveData()
    val forecastWeather: LiveData<List<Weather>>
        get() = _forecastWeather

    //Binding fields
    val updateTime: ObservableField<String> by lazy { ObservableField("") }
    val currentTemperature: ObservableField<String> by lazy { ObservableField("") }
    val currentHumidity: ObservableField<Int> by lazy { ObservableField(0) }
    val currentIconId: ObservableField<Int> by lazy { ObservableField(0) }

    var hasInternet = false

    private val currentWeather = MutableLiveData<Weather>()
    private val _city: MutableLiveData<String> by lazy { MutableLiveData() }
    private val _coord: MutableLiveData<Pair<Float, Float>> by lazy { MutableLiveData() }
    private val _forecastWeather = MutableLiveData<List<Weather>>()
    private val currentWeatherObserver = Observer<Weather> {
        updateTime.set(SimpleDateFormat("HH:mm dd.MM", Locale.getDefault()).format(it.date))
        currentTemperature.set(ConversionUtils.tempToString(it.temperature))
        currentHumidity.set(it.humidity)
        currentIconId.set(it.weatherType.iconId)
        city.postValue(it.city)
    }
    private val cityObserver = Observer<String> {
        updateDataByName()
    }
    private val coordObserver = Observer<Pair<Float, Float>> {
        updateDataByCoord()
    }

    init {
        _city.observeForever(cityObserver)
        currentWeather.observeForever(currentWeatherObserver)
        _coord.observeForever(coordObserver)
    }

    override fun onCleared() {
        _city.removeObserver(cityObserver)
        currentWeather.removeObserver(currentWeatherObserver)
        _coord.removeObserver(coordObserver)
        super.onCleared()
    }

    fun getCityDataExternal(cityName: String) {
        _city.postValue(cityName)
    }

    fun setCoord(lat: Float, lon: Float) {
        _coord.postValue(Pair(lat, lon))
    }

    fun checkIsFavourite(actionTrue: () -> Unit, actionFalse: () -> Unit) {
        viewModelScope.launch {
            if (forecastWeather.value != null) {
                val result = async(Dispatchers.IO) {
                    cityItemSource.hasCity(
                        City(
                            forecastWeather.value!![0].city,
                            forecastWeather.value!![0].countryCode
                        )
                    )
                }
                if (result.await()) actionTrue() else actionFalse()
            }
        }
    }

    fun addFavourite() {
        viewModelScope.launch(Dispatchers.IO) {
            if (currentWeather.value != null && forecastWeather.value != null) {
                cityItemSource.addCity(
                    City(currentWeather.value!!.city, currentWeather.value!!.countryCode)
                )
                currentLocalSource.saveCurrent(currentWeather.value!!)
                forecastLocalSource.saveForecast(forecastWeather.value!!)
            }
        }
    }

    fun removeFavourite() {
        viewModelScope.launch(Dispatchers.IO) {
            if (currentWeather.value != null) {
                cityItemSource.deleteCity(
                    City(currentWeather.value!!.city, currentWeather.value!!.countryCode)
                )
                currentLocalSource.deleteCity(
                    currentWeather.value!!.city,
                    currentWeather.value!!.countryCode
                )
                forecastLocalSource.deleteCity(
                    currentWeather.value!!.city,
                    currentWeather.value!!.countryCode
                )
            }
        }
    }

    fun updateDataByName() {
        viewModelScope.launch(Dispatchers.IO) {
            if (hasInternet) {
                updateCurrentWeatherByNameExternal()
                updateForecastWeatherByNameExternal()
            } else {
                updateForecastWeatherByNameLocal()
                updateCurrentWeatherByNameLocal()
            }
        }
    }

    private suspend fun updateCurrentWeatherByNameExternal(): Boolean {
        val current = currentExternalSource.getCurrentByName(city.value ?: _city.value!!)
        return if (current != null) {
            currentWeather.postValue(current!!)
            currentLocalSource.saveCurrent(current)
            true
        } else false
    }

    private suspend fun updateForecastWeatherByNameExternal(): Boolean {
        val forecast = forecastExternalSource.getForecastByName(city.value ?: _city.value!!)
        return if (forecast != null) {
            _forecastWeather.postValue(forecast!!)
            forecastLocalSource.saveForecast(forecast)
            true
        } else false
    }

    private suspend fun updateCurrentWeatherByNameLocal(): Boolean {
        val current = currentLocalSource.getCurrentByName(city.value ?: _city.value!!)
        return if (current != null) {
            currentWeather.postValue(current!!)
            true
        } else false
    }

    private suspend fun updateForecastWeatherByNameLocal(): Boolean {
        val forecast = forecastLocalSource.getForecastByName(city.value ?: _city.value!!)
        return if (forecast != null) {
            _forecastWeather.postValue(forecast!!)
            true
        } else false
    }

    private fun updateDataByCoord() {
        viewModelScope.launch(Dispatchers.IO) {
            updateCurrentWeatherByCoord()
            updateForecastWeatherByCoord()
        }
    }

    private suspend fun updateCurrentWeatherByCoord() {
        currentWeather.postValue(
            currentExternalSource.getCurrentByCoord(_coord.value!!.first, _coord.value!!.second)
        )
    }

    private suspend fun updateForecastWeatherByCoord() {
        _forecastWeather.postValue(
            forecastExternalSource.getForecastByCoord(_coord.value!!.first, _coord.value!!.second)
        )
    }
}