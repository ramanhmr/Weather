package com.ramanhmr.weather.ui.screens.weather

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramanhmr.weather.R
import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.utils.ConversionUtils
import com.ramanhmr.weather.databinding.ItemForecastWeatherBinding
import java.text.SimpleDateFormat
import java.util.*

class ForecastWeatherAdapter :
    ListAdapter<Weather, ForecastWeatherAdapter.ForecastWeatherViewHolder>(Weather.DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastWeatherViewHolder =
        ForecastWeatherViewHolder(
            ItemForecastWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            SimpleDateFormat(parent.context.getString(R.string.day_time), Locale.getDefault()),
            parent.context.getString(R.string.humidity_value)
        )

    override fun onBindViewHolder(holder: ForecastWeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ForecastWeatherViewHolder(
        private val binding: ItemForecastWeatherBinding,
        private val dateFormat: SimpleDateFormat,
        private val humidityString: String
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherItem: Weather) {
            with(binding) {
                ivIcon.setImageResource(weatherItem.weatherType.iconId)
                tvDate.text = dateFormat.format(weatherItem.date)
                tvMaxTemp.text = ConversionUtils.tempToString(weatherItem.maxTemperature)
                tvMinTemp.text = ConversionUtils.tempToString(weatherItem.minTemperature)
                tvHumidity.text = String.format(humidityString, weatherItem.humidity)
            }
        }
    }
}