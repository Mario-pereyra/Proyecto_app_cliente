package com.example.proyectoappcliente.data.network

import com.example.proyectoappcliente.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiClient {

    // --- Autenticación ---
    @POST("client/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("client/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Response<Unit> // No esperamos cuerpo en la respuesta

    // --- Categorías ---
    @GET("categories")
    suspend fun getCategories(): Response<List<Categoria>>

    // --- Trabajadores ---
    @GET("categories/{id}/workers")
    suspend fun getWorkersByCategory(@Path("id") categoryId: Int): Response<List<Trabajador>>

    @GET("workers/{id}")
    suspend fun getWorkerDetail(@Path("id") workerId: Int): Response<TrabajadorDetalle>

    // --- Citas (Appointments) ---
    @GET("appointments")
    suspend fun getAppointments(): Response<List<Cita>>

    @POST("appointments")
    suspend fun createAppointment(@Body crearCitaRequest: CrearCitaRequest): Response<Cita>

    @POST("appointments/{id}/make")
    suspend fun makeAppointment(
        @Path("id") appointmentId: Int,
        @Body concretarCitaRequest: ConcretarCitaRequest
    ): Response<Cita>

    @GET("appointments/{id}")
    suspend fun getAppointmentDetails(@Path("id") appointmentId: Int): Response<Cita>

    // --- Chat ---
    @GET("appointments/{id}/chats")
    suspend fun getChatMessages(@Path("id") appointmentId: Int): Response<List<MensajeChat>>

    @POST("appointments/{id}/chats")
    suspend fun sendChatMessage(
        @Path("id") appointmentId: Int,
        @Body mensajeChatRequest: MensajeChatRequest
    ): Response<Unit>

    // --- Calificación ---
    @POST("appointments/{id}/review")
    suspend fun postReview(
        @Path("id") appointmentId: Int,
        @Body calificarCitaRequest: CalificarCitaRequest
    ): Response<Unit>
}
