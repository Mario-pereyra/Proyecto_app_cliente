package com.example.proyectoappcliente.repositories

import com.example.proyectoappcliente.data.datastore.TokenDataStore
import com.example.proyectoappcliente.data.model.*
import com.example.proyectoappcliente.data.network.ApiClient
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val apiClient: ApiClient,
    private val tokenDataStore: TokenDataStore
) {
    // --- Autenticación y Sesión ---
    suspend fun login(loginRequest: LoginRequest) = apiClient.login(loginRequest)
    suspend fun register(registerRequest: RegisterRequest) = apiClient.register(registerRequest)
    suspend fun saveToken(token: String) = tokenDataStore.saveToken(token)
    suspend fun clearToken() = tokenDataStore.clearToken()
    fun getToken(): Flow<String?> = tokenDataStore.getToken()

    // --- Categorías ---
    suspend fun getCategories() = apiClient.getCategories()

    // --- Trabajadores ---
    suspend fun getWorkersByCategory(categoryId: Int) = apiClient.getWorkersByCategory(categoryId)
    suspend fun getWorkerDetail(workerId: Int) = apiClient.getWorkerDetail(workerId)

    // --- Citas (Appointments) ---
    suspend fun getAppointments() = apiClient.getAppointments()
    suspend fun createAppointment(request: CrearCitaRequest) = apiClient.createAppointment(request)
    suspend fun makeAppointment(id: Int, request: ConcretarCitaRequest) = apiClient.makeAppointment(id, request)
    suspend fun getAppointmentDetails(appointmentId: Int) = apiClient.getAppointmentDetails(appointmentId)

    // --- Chat ---
    suspend fun getChatMessages(appointmentId: Int) = apiClient.getChatMessages(appointmentId)
    suspend fun sendChatMessage(id: Int, request: MensajeChatRequest) = apiClient.sendChatMessage(id, request)

    // --- Calificación (Review) ---
    suspend fun postReview(id: Int, request: CalificarCitaRequest) = apiClient.postReview(id, request)
}
