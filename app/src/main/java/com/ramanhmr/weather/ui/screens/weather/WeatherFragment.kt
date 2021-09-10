package com.ramanhmr.weather.ui.screens.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.ramanhmr.weather.R
import com.ramanhmr.weather.databinding.FragmentWeatherBinding
import com.ramanhmr.weather.ui.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

class WeatherFragment : Fragment() {
    private val viewModel: WeatherViewModel by viewModel()
    private lateinit var binding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater).apply {
            weatherViewModel = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkInternetForViewModel()
        val args: WeatherFragmentArgs by navArgs()
        when (args.actionType) {
            BY_NAME -> {

                viewModel.getCityDataExternal(args.cityName)
            }
            BY_COORD -> {
                viewModel.setCoord(args.lat, args.lon)
            }
        }

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.list_divider, null)!!)
        }
        val adapter = ForecastWeatherAdapter()
        with(binding) {
            rvForecastList.adapter = adapter
            rvForecastList.addItemDecoration(divider)
            tvUpdateTime.setOnClickListener {
                checkInternetForViewModel()
                viewModel.updateDataByName()
            }
            tvLocation.setOnClickListener {
                findNavController().navigate(WeatherFragmentDirections.toLocationsFavourite())
            }
            ivFavourite.setOnClickListener {
                if (viewModel.city.value != "") {
                    viewModel.checkIsFavourite(
                        {
                            viewModel.removeFavourite()
                            favouriteFalse()
                        },
                        {
                            viewModel.addFavourite()
                            favouriteTrue()
                        }
                    )
                }
            }
        }
        viewModel.forecastWeather.observe(viewLifecycleOwner, {
            adapter.submitList(it)
            if (it.isNotEmpty() && it[0].city != "") {
                viewModel.checkIsFavourite({ favouriteTrue() }, { favouriteFalse() })
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun checkInternetForViewModel() {
        viewModel.hasInternet = (requireActivity() as? MainActivity)?.hasInternet() ?: false
    }

    private fun favouriteTrue() {
        binding.ivFavourite.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_favorite_true,
                null
            )
        )
    }

    private fun favouriteFalse() {
        binding.ivFavourite.setImageDrawable(
            resources.getDrawable(
                R.drawable.ic_favorite_false,
                null
            )
        )
    }

    companion object {
        const val BY_NAME = 1
        const val BY_COORD = 2
    }
}