package com.example.proyectoappcliente.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappcliente.databinding.FragmentAppointmentsBinding
import com.example.proyectoappcliente.ui.activities.MainActivity
import com.example.proyectoappcliente.ui.adapters.CitaAdapter
import com.example.proyectoappcliente.ui.viewmodels.AppointmentsViewModel
import com.example.proyectoappcliente.ui.viewmodels.factories.ViewModelFactory

class AppointmentsFragment : Fragment() {

    private var _binding: FragmentAppointmentsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AppointmentsViewModel by viewModels {
        ViewModelFactory(MainActivity.repository)
    }

    private lateinit var citaAdapter: CitaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        viewModel.fetchAppointments()
    }

    private fun setupRecyclerView() {
        citaAdapter = CitaAdapter(emptyList()) { cita ->
            val action = AppointmentsFragmentDirections.actionAppointmentsFragmentToChatFragment(cita.id)
            findNavController().navigate(action)
        }
        binding.rvAppointments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAppointments.adapter = citaAdapter
    }

    private fun setupObservers() {
        viewModel.appointments.observe(viewLifecycleOwner) { appointments ->
            citaAdapter.updateData(appointments)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

