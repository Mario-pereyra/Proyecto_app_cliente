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
import com.bumptech.glide.Glide
import com.example.proyectoappcliente.R
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

        // Inicializar con un valor temporal, se actualizará cuando lleguen los appointmentDetails
        setupRecyclerView(1) // Valor temporal
        setupObservers()
        setupEventListeners()

        // Estrategia robusta: intentar obtener info del trabajador de múltiples fuentes
        loadWorkerInfo()

        // Carga de mensajes y datos de la cita
        viewModel.fetchAppointmentDetails(args.appointmentId)
        viewModel.startPollingMessages(args.appointmentId)

        android.util.Log.d("ChatFragment", "Abriendo chat para appointmentId: ${args.appointmentId}")
    }

    private fun loadWorkerInfo() {
        // Estrategia mejorada: Usar GET /appointments/{id} que ya tiene toda la info
        binding.txtWorkerName.text = "Cargando..."

        android.util.Log.d("ChatFragment", "Obteniendo info completa de la cita ${args.appointmentId}")

        // Esta llamada ya se hace en onViewCreated, el observer se encargará del resto
    }

    private fun updateWorkerInfo() {
        val messages = viewModel.messages.value
        if (!messages.isNullOrEmpty()) {
            // Extraer worker_id del primer mensaje
            val appointment = messages.first().appointment
            val workerId = appointment.workerId
            android.util.Log.d("ChatFragment", "Obteniendo detalles del trabajador ID: $workerId")

            // Llamar al endpoint /workers/{id} para obtener detalles completos
            viewModel.fetchWorkerDetails(workerId)
        } else {
            // No hay mensajes, usar datos por defecto
            binding.txtWorkerName.text = "Chat"
            android.util.Log.d("ChatFragment", "No hay mensajes, usando datos por defecto")
        }
    }

    private fun setupRecyclerView(currentUserId: Int) {
        chatAdapter = ChatAdapter(emptyList(), currentUserId)
        binding.rvChat.layoutManager = LinearLayoutManager(requireContext()).apply {
            stackFromEnd = true // Para que la lista empiece desde abajo
        }
        binding.rvChat.adapter = chatAdapter
    }

    private fun setupObservers() {
        // NUEVO: Observer para los detalles completos de la cita (incluye info del trabajador)
        viewModel.appointmentDetails.observe(viewLifecycleOwner) { cita ->
            android.util.Log.d("ChatFragment", "Observer appointmentDetails activado")

            // LOGS INFORMATIVOS DE IDs
            android.util.Log.d("ChatFragment", "=== INFORMACIÓN DE LA CITA ===")
            android.util.Log.d("ChatFragment", "ID de la cita: ${cita.id}")
            android.util.Log.d("ChatFragment", "ID del CLIENTE: ${cita.userId}")
            android.util.Log.d("ChatFragment", "ID del TRABAJADOR: ${cita.workerId}")
            android.util.Log.d("ChatFragment", "Status de la cita: ${cita.status}")
            android.util.Log.d("ChatFragment", "===============================")

            // ACTUALIZAR EL ADAPTER CON EL USER_ID CORRECTO
            val correctUserId = cita.userId
            android.util.Log.d("ChatFragment", "Actualizando ChatAdapter con userId correcto: $correctUserId")

            // Recrear el adapter con el userId correcto
            setupRecyclerView(correctUserId)

            // Si ya hay mensajes, actualizar el adapter
            val currentMessages = viewModel.messages.value
            if (!currentMessages.isNullOrEmpty()) {
                chatAdapter.updateData(currentMessages)
            }

            // Extraer información del trabajador desde la cita
            val worker = cita.worker
            binding.txtWorkerName.text = "Trabajador" // Nombre por defecto ya que el endpoint no incluye user.name

            // Cargar foto del trabajador con Glide
            val imageUrl = worker.pictureUrl
            Glide.with(this)
                .load(if (imageUrl == "null" || imageUrl?.isEmpty() == true || imageUrl == null) null else imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(binding.imgWorkerPhoto)

            android.util.Log.d("ChatFragment", "Mostrando trabajador desde appointmentDetails con foto: $imageUrl")

            // Configurar botones según el status
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
            android.util.Log.d("ChatFragment", "Observer messages activado. Cantidad: ${messages?.size ?: 0}")
            chatAdapter.updateData(messages)

            if (messages.isNotEmpty()) {
                binding.rvChat.scrollToPosition(messages.size - 1) // Scroll al último mensaje
                android.util.Log.d("ChatFragment", "Mensajes cargados correctamente")
            } else {
                android.util.Log.d("ChatFragment", "No hay mensajes en el chat")
            }
        }

        // NUEVO: Observer para los detalles del trabajador
        viewModel.workerDetails.observe(viewLifecycleOwner) { worker ->
            android.util.Log.d("ChatFragment", "Observer workerDetails activado")
            // Mostrar nombre completo del trabajador
            val fullName = "${worker.user.name} ${worker.user.lastName}"
            binding.txtWorkerName.text = fullName

            // Cargar foto del trabajador con Glide
            val imageUrl = worker.pictureUrl
            Glide.with(this)
                .load(if (imageUrl == "null" || imageUrl?.isEmpty() == true || imageUrl == null) null else imageUrl)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(binding.imgWorkerPhoto)

            android.util.Log.d("ChatFragment", "Mostrando trabajador: $fullName con foto: $imageUrl")
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            android.util.Log.e("ChatFragment", "Error: $it")
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupEventListeners() {
        binding.btnSendMessage.setOnClickListener {
            val message = binding.editTxtMessage.text.toString()
            android.util.Log.d("ChatFragment", "Botón enviar presionado. Mensaje: '$message'")

            if (message.isNotEmpty()) {
                android.util.Log.d("ChatFragment", "Mensaje no está vacío, intentando enviar...")

                // El receiver_id siempre es el workerId ya que esta es la app del cliente
                var receiverId: Int? = null

                // Intentar obtener workerId desde los mensajes existentes primero
                val messages = viewModel.messages.value
                if (!messages.isNullOrEmpty()) {
                    receiverId = messages.first().appointment.workerId
                    android.util.Log.d("ChatFragment", "Worker ID obtenido desde mensajes: $receiverId")
                } else {
                    // Si no hay mensajes, usar appointmentDetails
                    val appointmentDetails = viewModel.appointmentDetails.value
                    if (appointmentDetails != null) {
                        receiverId = appointmentDetails.workerId
                        android.util.Log.d("ChatFragment", "Worker ID obtenido desde appointmentDetails: $receiverId")
                    }
                }

                if (receiverId != null) {
                    android.util.Log.d("ChatFragment", "Enviando mensaje: '$message' al trabajador: $receiverId")
                    viewModel.sendMessage(args.appointmentId, message, receiverId)
                    binding.editTxtMessage.text.clear()
                    android.util.Log.d("ChatFragment", "Campo de texto limpiado")
                } else {
                    android.util.Log.e("ChatFragment", "ERROR: No se pudo determinar el workerId")
                    Toast.makeText(context, "Error: No se puede enviar el mensaje", Toast.LENGTH_SHORT).show()
                }
            } else {
                android.util.Log.w("ChatFragment", "ADVERTENCIA: El mensaje está vacío, no se envía")
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
