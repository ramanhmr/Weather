package com.ramanhmr.weather.data.enums

import com.ramanhmr.weather.R

enum class WeatherType(
    val iconOWM: String,
    val iconId: Int
) {
    CLEAR_D("01d", R.drawable.ic_clear_d),
    CLEAR_N("01n", R.drawable.ic_clear_n),
    FEW_CLOUDS_D("02d", R.drawable.ic_few_clouds_d),
    FEW_CLOUDS_N("02n", R.drawable.ic_few_clouds_n),
    SCATTERED_CLOUDS_D("03d", R.drawable.ic_scattered_clouds),
    SCATTERED_CLOUDS_N("03n", R.drawable.ic_scattered_clouds),
    BROKEN_CLOUDS_D("04d", R.drawable.ic_broken_clouds),
    BROKEN_CLOUDS_N("04n", R.drawable.ic_broken_clouds),
    SHOWER_RAIN_D("09d", R.drawable.ic_shower_rain),
    SHOWER_RAIN_N("09n", R.drawable.ic_shower_rain),
    RAIN_D("10d", R.drawable.ic_rain_d),
    RAIN_N("10n", R.drawable.ic_rain_n),
    THUNDERSTORM_D("11d", R.drawable.ic_thunderstorm),
    THUNDERSTORM_N("11n", R.drawable.ic_thunderstorm),
    SNOW_D("13d", R.drawable.ic_snow),
    SNOW_N("13n", R.drawable.ic_snow),
    MIST_D("50d", R.drawable.ic_mist),
    MIST_N("50n", R.drawable.ic_mist)
}