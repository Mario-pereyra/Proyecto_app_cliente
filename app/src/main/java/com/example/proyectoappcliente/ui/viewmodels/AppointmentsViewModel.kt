package com.example.proyectoappcliente.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappcliente.data.model.Cita
import com.example.proyectoappcliente.repositories.AppRepository
import kotlinx.coroutines.launch

class AppointmentsViewModel(private val repository: AppRepository) : ViewModel() {

    private val _appointments = MutableLiveData<List<Cita>>()
    val appointments: LiveData<List<Cita>> = _appointments

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun fetchAppointments() {
        viewModelScope.launch {
            try {
                val response = repository.getAppointments()
                if (response.isSuccessful) {
                    _appointments.value = response.body()
                } else {
                    _errorMessage.value = "Error al obtener las citas."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión."
            }
        }
    }
}

