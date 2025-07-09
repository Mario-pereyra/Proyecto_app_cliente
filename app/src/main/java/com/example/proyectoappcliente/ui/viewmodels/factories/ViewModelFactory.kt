package com.example.proyectoappcliente.ui.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.proyectoappcliente.repositories.AppRepository
import com.example.proyectoappcliente.ui.viewmodels.*

class ViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository) as T
            modelClass.isAssignableFrom(CategoriesViewModel::class.java) -> CategoriesViewModel(repository) as T
            modelClass.isAssignableFrom(WorkersViewModel::class.java) -> WorkersViewModel(repository) as T
            modelClass.isAssignableFrom(WorkerDetailViewModel::class.java) -> WorkerDetailViewModel(repository) as T
            modelClass.isAssignableFrom(ChatViewModel::class.java) -> ChatViewModel(repository) as T
            modelClass.isAssignableFrom(AppointmentsViewModel::class.java) -> AppointmentsViewModel(repository) as T
            modelClass.isAssignableFrom(DateTimePickerViewModel::class.java) -> DateTimePickerViewModel(repository) as T
            modelClass.isAssignableFrom(ReviewViewModel::class.java) -> ReviewViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
