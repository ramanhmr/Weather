package com.ramanhmr.weather.ui.screens.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ramanhmr.weather.R
import com.ramanhmr.weather.data.entities.Weather
import com.ramanhmr.weather.data.utils.ConversionUtils
import com.ramanhmr.weather.databinding.ItemLocationFavouriteBinding

class LocationsWeatherAdapter :
    ListAdapter<Weather, LocationsWeatherAdapter.LocationWeatherViewHolder>(Weather.DiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationWeatherViewHolder =
        LocationWeatherViewHolder(
            ItemLocationFavouriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            parent.resources.getString(R.string.city_country),
            parent.resources.getString(R.string.humidity_value)
        ) {
            parent.findNavController()
                .navigate(LocationsFavouriteFragmentDirections.toWeatherByName(it))
        }

    override fun onBindViewHolder(holder: LocationWeatherViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LocationWeatherViewHolder(
        private val binding: ItemLocationFavouriteBinding,
        private val locationString: String,
        private val humidityString: String,
        private val onClick: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherItem: Weather) {
            with(binding) {
                ivIcon.setImageResource(weatherItem.weatherType.iconId)
                tvCityCountry.text =
                    String.format(locationString, weatherItem.city, weatherItem.countryCode)
                tvTemp.text = ConversionUtils.tempToString(weatherItem.temperature)
                tvHumidity.text = String.format(humidityString, weatherItem.humidity)
                root.setOnClickListener { onClick(weatherItem.city) }
            }
        }
    }
}