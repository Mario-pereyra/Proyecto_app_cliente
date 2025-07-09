package com.example.proyectoappcliente.data.model

import com.google.gson.annotations.SerializedName

// Petición para /client/login
data class LoginRequest(
    val email: String,
    val password: String
)

// Respuesta de /client/login
data class LoginResponse(
    @SerializedName("access_token") val accessToken: String
)

// Petición para /client/register
data class RegisterRequest(
    val name: String,
    val lastName: String,
    val email: String,
    val password: String
)

// Petición para /appointments (Crear cita inicial)
data class CrearCitaRequest(
    @SerializedName("worker_id") val workerId: String,
    @SerializedName("category_selected_id") val categorySelectedId: Int
)

// Petición para /appointments/{id}/make
data class ConcretarCitaRequest(
    @SerializedName("appointment_date") val appointmentDate: String,
    @SerializedName("appointment_time") val appointmentTime: String,
    val latitude: Double,
    val longitude: Double
)

// Petición para /appointments/{id}/review
data class CalificarCitaRequest(
    val rating: Int,
    val review: String
)

// Petición para /appointments/{id}/chats
data class MensajeChatRequest(
    val message: String,
    @SerializedName("receiver_id") val receiverId: Int
)

// Modelo para un mensaje individual en la respuesta de /appointments/{id}/chats
data class MensajeChat(
    val id: Int,
    val message: String,
    @SerializedName("sender_id") val senderId: Int,
    @SerializedName("receiver_id") val receiverId: Int,
    @SerializedName("created_at") val createdAt: String
)

data class Resena(
    val rating: Float,
    val comment: String,
    val author: String
)

data class TrabajadorDetalle(
    val id: Int,
    val user: User,
    @SerializedName("picture_url") val pictureUrl: String?,
    @SerializedName("average_rating") val averageRating: Float,
    @SerializedName("reviews_count") val reviewsCount: Int,
    val specialties: List<String>,
    val reviews: List<Resena>
)
