package com.example.proyectoappcliente.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.proyectoappcliente.R
import com.example.proyectoappcliente.databinding.FragmentWorkerDetailBinding
import com.example.proyectoappcliente.ui.activities.MainActivity
import com.example.proyectoappcliente.ui.adapters.ResenaAdapter
import com.example.proyectoappcliente.ui.viewmodels.WorkerDetailViewModel
import com.example.proyectoappcliente.ui.viewmodels.factories.ViewModelFactory

class WorkerDetailFragment : Fragment() {

    private var _binding: FragmentWorkerDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WorkerDetailViewModel by viewModels {
        ViewModelFactory(MainActivity.repository)
    }

    private val args: WorkerDetailFragmentArgs by navArgs()
    private lateinit var resenaAdapter: ResenaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkerDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupEventListeners()

        viewModel.fetchWorkerDetails(args.workerId)
    }

    private fun setupRecyclerView() {
        resenaAdapter = ResenaAdapter(emptyList())
        binding.rvReviews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReviews.adapter = resenaAdapter
    }

    private fun setupObservers() {
        viewModel.workerDetails.observe(viewLifecycleOwner) { worker ->
            val fullName = "${worker.user.name} ${worker.user.lastName}"
            binding.txtWorkerName.text = fullName

            // Convertimos el rating de String a Float para mostrarlo
            val rating = worker.averageRating.toFloatOrNull() ?: 0.0f
            binding.txtWorkerStats.text = "${rating}% Calificación - ${worker.reviewsCount} trabajos"

            // Unimos los nombres de las categorías en un solo string
            binding.txtWorkerSpecialties.text = worker.categories.joinToString(", ") { it.name }

            // --- LÓGICA CORREGIDA ---
            val imageUrl = worker.pictureUrl
            Glide.with(this)
                .load(if (imageUrl == "null") null else imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.drawable.ic_launcher_background)
                .into(binding.imgWorkerProfile)

            resenaAdapter.updateData(worker.reviews)
        }

        viewModel.newAppointment.observe(viewLifecycleOwner) { cita ->
            // Navegar al chat con el ID de la nueva cita
            val action = WorkerDetailFragmentDirections.actionWorkerDetailFragmentToChatFragment(cita.id)
            findNavController().navigate(action)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupEventListeners() {
        binding.btnContactar.setOnClickListener {
            viewModel.createAppointment(args.workerId.toString(), args.categoryId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
