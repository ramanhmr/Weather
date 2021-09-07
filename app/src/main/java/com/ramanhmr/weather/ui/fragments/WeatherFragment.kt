package com.ramanhmr.weather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.ramanhmr.weather.R
import com.ramanhmr.weather.data.viewmodels.WeatherViewModel
import com.ramanhmr.weather.databinding.FragmentWeatherBinding
import com.ramanhmr.weather.ui.adapters.ForecastWeatherAdapter
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
        binding.tvLocation.setOnClickListener { viewModel.changeCity() }

        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.list_divider, null)!!)
        }
        val adapter = ForecastWeatherAdapter()
        with(binding) {
            rvForecastList.adapter = adapter
            rvForecastList.addItemDecoration(divider)
        }
        viewModel.forecastWeather.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        super.onViewCreated(view, savedInstanceState)
    }
}