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

        // Configurar opciones de zoom y movimiento
        setupMapOptions()

        // Ubicación inicial: Santa Cruz, Bolivia
        val initialLocation = LatLng(-17.7833, -63.1821)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 14f))

        // Configurar el botón de confirmar ubicación
        setupConfirmButton()
    }

    private fun setupMapOptions() {
        // Habilitar controles de zoom (botones + y -)
        map.uiSettings.isZoomControlsEnabled = true

        // Habilitar todos los gestos para que el usuario pueda mover el mapa libremente
        map.uiSettings.isZoomGesturesEnabled = true
        map.uiSettings.isScrollGesturesEnabled = true
        map.uiSettings.isRotateGesturesEnabled = true
        map.uiSettings.isTiltGesturesEnabled = true

        // Configurar niveles de zoom
        map.setMinZoomPreference(8f)
        map.setMaxZoomPreference(20f)
    }

    private fun setupConfirmButton() {
        binding.btnConfirmLocation.setOnClickListener {
            // Obtener las coordenadas del centro del mapa (donde está el marcador fijo)
            val centerLatLng = map.cameraPosition.target

            // Navegar a la siguiente pantalla pasando las coordenadas del centro como Float
            val action = MapFragmentDirections.actionMapFragmentToDateTimePickerFragment(
                args.appointmentId,
                centerLatLng.latitude.toFloat(), // Convertir a Float
                centerLatLng.longitude.toFloat() // Convertir a Float
            )
            findNavController().navigate(action)
        }
    }

    // Métodos adicionales de zoom que puedes usar
    private fun zoomIn() {
        map.animateCamera(CameraUpdateFactory.zoomIn())
    }

    private fun zoomOut() {
        map.animateCamera(CameraUpdateFactory.zoomOut())
    }

    private fun zoomToLevel(level: Float) {
        map.animateCamera(CameraUpdateFactory.zoomTo(level))
    }

    private fun zoomToLocation(latLng: LatLng, zoomLevel: Float = 15f) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
