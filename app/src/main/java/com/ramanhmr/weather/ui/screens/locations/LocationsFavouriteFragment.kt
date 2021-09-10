package com.ramanhmr.weather.ui.screens.locations

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.ramanhmr.weather.R
import com.ramanhmr.weather.databinding.FragmentLocationsFavouriteBinding
import com.ramanhmr.weather.ui.MainActivity
import org.koin.android.viewmodel.ext.android.viewModel

class LocationsFavouriteFragment : Fragment() {
    private lateinit var binding: FragmentLocationsFavouriteBinding
    private val viewModel: LocationsFavouriteViewModel by viewModel()
    private lateinit var inputManager: InputMethodManager
    private lateinit var locationManager: LocationManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationsFavouriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkInternetForViewModel()
        viewModel.updateWeather()
        inputManager =
            (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        locationManager =
            (requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager)
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL).apply {
            setDrawable(ResourcesCompat.getDrawable(resources, R.drawable.list_divider, null)!!)
        }
        val adapter = LocationsWeatherAdapter()
        with(binding) {
            rvFavouriteList.addItemDecoration(divider)
            rvFavouriteList.adapter = adapter
            etCityName.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    viewModel.checkCityAndCall(
                        etCityName.text.toString(),
                        { toWeather(it) },
                        { cityNotFound(it) }
                    )
                    etCityName.text.clear()
                    inputManager.hideSoftInputFromWindow(
                        requireActivity().currentFocus!!.windowToken,
                        InputMethodManager.HIDE_NOT_ALWAYS
                    )
                    return@setOnEditorActionListener true
                } else return@setOnEditorActionListener false
            }
            btnGo.setOnClickListener {
                etCityName.onEditorAction(EditorInfo.IME_ACTION_DONE)
            }
            btnThisLocation.setOnClickListener {
                toWeatherWithLocation()
            }
            btnMap.setOnClickListener {
                toMap()
            }
        }
        viewModel.cityWeatherLiveData.observe(viewLifecycleOwner, {
            adapter.submitList(it.sortedBy { weather -> weather.city })
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun checkInternetForViewModel() {
        viewModel.hasInternet = (requireActivity() as? MainActivity)?.hasInternet() ?: false
    }

    private fun toWeather(lat: Float, lon: Float) {
        findNavController().navigate(
            LocationsFavouriteFragmentDirections.toWeatherByCoord(
                lat,
                lon
            )
        )
    }

    private fun toWeather(cityName: String) {
        findNavController().navigate(LocationsFavouriteFragmentDirections.toWeatherByName(cityName))
    }

    fun toWeatherWithLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            checkInternet {
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        ?.let { location ->
                            toWeather(
                                location.latitude.toFloat(),
                                location.longitude.toFloat()
                            )
                        }
                } else {
                    Toast.makeText(context, "Location is disabled", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            (requireActivity() as? MainActivity)?.requestLocationPermission()
        }
    }

    private fun toMap() {
        checkInternet {
            findNavController().navigate(LocationsFavouriteFragmentDirections.toMap())
        }
    }

    private fun cityNotFound(cityName: String) {
        Toast.makeText(
            context,
            String.format(resources.getString(R.string.city_not_found), cityName),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkInternet(action: () -> Unit) {
        if ((requireActivity() as? MainActivity)?.hasInternet() == true) action()
        else {
            Toast.makeText(context, "No Internet connection", Toast.LENGTH_SHORT).show()
        }
    }
}