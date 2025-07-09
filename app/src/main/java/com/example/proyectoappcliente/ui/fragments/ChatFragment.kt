package com.example.proyectoappcliente.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyectoappcliente.databinding.FragmentChatBinding
import com.example.proyectoappcliente.ui.activities.MainActivity
import com.example.proyectoappcliente.ui.adapters.ChatAdapter
import com.example.proyectoappcliente.ui.viewmodels.ChatViewModel
import com.example.proyectoappcliente.ui.viewmodels.factories.ViewModelFactory

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels {
        ViewModelFactory(MainActivity.repository)
    }

    private val args: ChatFragmentArgs by navArgs()
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Asumimos que el ID del usuario actual es 1. En un caso real,
        // este ID se obtendría de DataStore o de un objeto de sesión.
        val currentUserId = 1

        setupRecyclerView(currentUserId)
        setupObservers()
        setupEventListeners()

        // Carga inicial de datos
        viewModel.fetchAppointmentDetails(args.appointmentId)
        viewModel.startPollingMessages(args.appointmentId)
    }

    private fun setupRecyclerView(currentUserId: Int) {
        chatAdapter = ChatAdapter(emptyList(), currentUserId)
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true // Para que la lista empiece desde abajo
        }
        binding.rvChat.adapter = chatAdapter
    }

    private fun setupObservers() {
        viewModel.appointmentDetails.observe(viewLifecycleOwner) { cita ->
            (activity as AppCompatActivity).supportActionBar?.title = "Chat con ${cita.worker.user?.name}"

            // Lógica para mostrar/ocultar botones
            when (cita.status) {
                3 -> { // COMPLETED
                    binding.btnCalificar.visibility = View.VISIBLE
                    binding.btnConcretar.visibility = View.GONE
                }
                else -> {
                    binding.btnCalificar.visibility = View.GONE
                    binding.btnConcretar.visibility = View.VISIBLE
                }
            }
        }

        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            chatAdapter.updateData(messages)
            binding.rvChat.scrollToPosition(messages.size - 1) // Scroll al último mensaje
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupEventListeners() {
        binding.btnSendMessage.setOnClickListener {
            val message = binding.editTxtMessage.text.toString()
            val receiverId = viewModel.appointmentDetails.value?.worker?.userId ?: -1
            if (message.isNotEmpty() && receiverId != -1) {
                viewModel.sendMessage(args.appointmentId, message, receiverId)
                binding.editTxtMessage.text.clear()
            }
        }

        binding.btnConcretar.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Concretar Cita")
                .setMessage("¿Está seguro que desea concretar una cita?")
                .setPositiveButton("Si") { _, _ ->
                    // Navegar al mapa
                    val action = ChatFragmentDirections.actionChatFragmentToMapFragment(args.appointmentId)
                    findNavController().navigate(action)
                }
                .setNegativeButton("No", null)
                .show()
        }

        binding.btnCalificar.setOnClickListener {
            val dialog = ReviewDialogFragment.newInstance(args.appointmentId)
            dialog.show(childFragmentManager, "ReviewDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
