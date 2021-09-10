package com.ramanhmr.weather.ui.screens.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ramanhmr.weather.databinding.FragmentMapBinding

class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private lateinit var binding: FragmentMapBinding
    private lateinit var map: GoogleMap
    private var chosenCoord: LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(layoutInflater)
        binding.mapView.onCreate(savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.mapView.getMapAsync(this)
        binding.btnThisLocation.setOnClickListener {
            if (chosenCoord != null) {
                findNavController().navigate(
                    MapFragmentDirections.toWeatherByCoord(
                        chosenCoord!!.latitude.toFloat(),
                        chosenCoord!!.longitude.toFloat()
                    )
                )
            }
        }
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap.apply {
            setOnMapClickListener(this@MapFragment)
        }
    }

    override fun onMapClick(coord: LatLng) {
        map.clear()
        map.addMarker(MarkerOptions().position(coord))
        chosenCoord = coord
    }
}