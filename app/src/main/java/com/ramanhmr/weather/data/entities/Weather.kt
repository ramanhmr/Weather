package com.ramanhmr.weather.data.entities

import androidx.recyclerview.widget.DiffUtil
import com.ramanhmr.weather.data.enums.WeatherType
import java.util.*

data class Weather(
    val date: Date,
    val temperature: Double,
    val maxTemperature: Double,
    val minTemperature: Double,
    val humidity: Int,
    val description: String,
    val weatherType: WeatherType
) {
    class DiffUtilCallback : DiffUtil.ItemCallback<Weather>() {
        override fun areItemsTheSame(oldItem: Weather, newItem: Weather): Boolean =
            oldItem.date == newItem.date

        override fun areContentsTheSame(oldItem: Weather, newItem: Weather): Boolean =
            oldItem == newItem
    }
}