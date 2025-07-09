package com.example.proyectoappcliente.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectoappcliente.data.model.RegisterRequest
import com.example.proyectoappcliente.repositories.AppRepository
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AppRepository) : ViewModel() {

    // LiveData para el estado del registro
    private val _registrationStatus = MutableLiveData<Boolean>()
    val registrationStatus: LiveData<Boolean> = _registrationStatus

    // LiveData para comunicar errores a la UI
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun register(nombre: String, apellido: String, email: String, pass: String) {
        viewModelScope.launch {
            try {
                val request = RegisterRequest(nombre, apellido, email, pass)
                val response = repository.register(request)
                if (response.isSuccessful) {
                    _registrationStatus.value = true
                } else {
                    _errorMessage.value = "Error en el registro. El email podría ya estar en uso."
                    _registrationStatus.value = false
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error de conexión. Verifique su red."
                _registrationStatus.value = false
            }
        }
    }
}

