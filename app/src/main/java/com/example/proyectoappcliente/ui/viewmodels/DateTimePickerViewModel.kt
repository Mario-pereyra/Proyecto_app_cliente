package com.example.proyectoappcliente.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappcliente.data.model.ConcretarCitaRequest
import com.example.proyectoappcliente.repositories.AppRepository
import kotlinx.coroutines.launch

class DateTimePickerViewModel(private val repository: AppRepository) : ViewModel() {

    private val _appointmentMadeStatus = MutableLiveData<Boolean>()
    val appointmentMadeStatus: LiveData<Boolean> = _appointmentMadeStatus

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun makeAppointment(id: Int, request: ConcretarCitaRequest) {
        viewModelScope.launch {
            try {
                val response = repository.makeAppointment(id, request)
                if (response.isSuccessful) {
                    _appointmentMadeStatus.value = true
                } else {
                    _errorMessage.value = "Error al concretar la cita."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión."
            }
        }
    }
}

