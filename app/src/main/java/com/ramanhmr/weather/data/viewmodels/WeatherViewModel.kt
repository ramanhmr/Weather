package com.ramanhmr.weather.data.viewmodels

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.interfaces.WeatherCurrentSource
import com.ramanhmr.weather.data.interfaces.WeatherForecastSource
import com.ramanhmr.weather.data.utils.ConversionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(
    private val currentSource: WeatherCurrentSource,
    private val forecastSource: WeatherForecastSource
) : ViewModel() {
    val city: LiveData<String>
        get() = _city
    val currentWeather: LiveData<Weather>
        get() = _currentWeather
    val forecastWeather: LiveData<List<Weather>>
        get() = _forecastWeather

    private val _city: MutableLiveData<String> = MutableLiveData("Minsk")
    private val _currentWeather = MutableLiveData<Weather>()
    private val _forecastWeather = MutableLiveData<List<Weather>>()

    //Binding fields
    var updateTime: ObservableField<String> = ObservableField("")
    var currentTemperature: ObservableField<String> = ObservableField("")
    var currentHumidity: ObservableField<String> = ObservableField("")
    var currentIconId: ObservableField<Int> = ObservableField(0)

    init {
        updateData()
        currentWeather.observeForever {
            updateTime.set(SimpleDateFormat("HH:mm dd.MM", Locale.getDefault()).format(it.date))
            currentTemperature.set(ConversionUtils.tempToString(it.temperature))
            currentHumidity.set("${it.humidity}%")
            currentIconId.set(it.weatherType.iconId)
        }
    }

    fun changeCity() {
        _city.postValue("Kabul")
        updateData()
    }

    fun updateData() {
        viewModelScope.launch(Dispatchers.IO) {
            updateCurrentWeather()
            updateForecastWeather()
        }
    }

    private suspend fun updateCurrentWeather() {
        _currentWeather.postValue(currentSource.getCurrentByName(city.value!!))
    }

    private suspend fun updateForecastWeather() {
        _forecastWeather.postValue(forecastSource.getForecastByName(city.value!!))
    }
}