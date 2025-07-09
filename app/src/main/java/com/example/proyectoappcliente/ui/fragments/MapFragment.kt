package com.example.proyectoappcliente.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.proyectoappcliente.R
import com.example.proyectoappcliente.databinding.FragmentMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var map: GoogleMap
    private val args: MapFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        // Ubicación inicial: Santa Cruz, Bolivia
        val initialLocation = LatLng(-17.7833, -63.1821)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 12f))

        map.setOnMapClickListener { latLng ->
            map.clear() // Limpia marcadores anteriores
            map.addMarker(MarkerOptions().position(latLng).title("Ubicación Seleccionada"))
            // Navegar a la siguiente pantalla pasando los datos
            val action = MapFragmentDirections.actionMapFragmentToDateTimePickerFragment(
                args.appointmentId,
                latLng.latitude.toFloat(),
                latLng.longitude.toFloat()
            )
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
