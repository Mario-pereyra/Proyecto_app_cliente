package com.example.proyectoappcliente.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappcliente.data.model.Cita
import com.example.proyectoappcliente.data.model.MensajeChat
import com.example.proyectoappcliente.data.model.MensajeChatRequest
import com.example.proyectoappcliente.repositories.AppRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: AppRepository) : ViewModel() {

    private val _messages = MutableLiveData<List<MensajeChat>>()
    val messages: LiveData<List<MensajeChat>> = _messages

    private val _appointmentDetails = MutableLiveData<Cita>()
    val appointmentDetails: LiveData<Cita> = _appointmentDetails

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private var pollingJob: Job? = null

    fun startPollingMessages(appointmentId: Int) {
        pollingJob?.cancel() // Cancela cualquier sondeo anterior
        pollingJob = viewModelScope.launch {
            while (true) {
                fetchMessages(appointmentId)
                delay(30000) // Espera 30 segundos
            }
        }
    }

    fun fetchAppointmentDetails(appointmentId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getAppointmentDetails(appointmentId)
                if (response.isSuccessful) {
                    _appointmentDetails.value = response.body()
                } else {
                    _errorMessage.value = "Error al obtener detalles de la cita."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión."
            }
        }
    }

    private fun fetchMessages(appointmentId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getChatMessages(appointmentId)
                if (response.isSuccessful) {
                    _messages.value = response.body()
                } else {
                    _errorMessage.value = "Error al cargar mensajes."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión."
            }
        }
    }

    fun sendMessage(appointmentId: Int, messageText: String, receiverId: Int) {
        viewModelScope.launch {
            val request = MensajeChatRequest(messageText, receiverId)
            try {
                val response = repository.sendChatMessage(appointmentId, request)
                if (response.isSuccessful) {
                    // Refresca los mensajes inmediatamente después de enviar
                    fetchMessages(appointmentId)
                } else {
                    _errorMessage.value = "No se pudo enviar el mensaje."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión."
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        pollingJob?.cancel() // Detiene el sondeo cuando el ViewModel se destruye
    }
}

