package com.ramanhmr.weather.data.utils

import com.ramanhmr.weather.data.enums.WeatherType
import com.ramanhmr.weather.exceptions.UnregisteredWeatherTypeException
import kotlin.math.roundToInt

object ConversionUtils {
    fun tempToString(temp: Double): String {
        val tempInt = temp.roundToInt()
        return StringBuilder().append(if (tempInt >= 0) "+" else "")
            .append(tempInt)
            .append("\u2103")
            .toString()
    }

    fun iconOWMToWeatherType(iconOWM: String): WeatherType {
        WeatherType.values().forEach {
            if (it.iconOWM == iconOWM) return it
        }
        throw UnregisteredWeatherTypeException("Weather type \"$iconOWM\" is not found in WeatherTypes for OpenWeatherMap")
    }
}