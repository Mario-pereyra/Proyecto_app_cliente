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

// Modelo para el usuario que hace la reseña
data class ReviewUser(
    val id: Int,
    val name: String,
    @SerializedName("last_name") val lastName: String,
    val type: Int
)

// Modelo corregido para una reseña
data class Resena(
    val id: Int,
    @SerializedName("worker_id") val workerId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("appointment_id") val appointmentId: Int,
    val rating: Int,
    val comment: String?, // El comentario puede ser nulo
    @SerializedName("is_done") val isDone: Int,
    val user: ReviewUser // Información del usuario que hizo la reseña
)

data class TrabajadorDetalle(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("picture_url") val pictureUrl: String?,
    @SerializedName("average_rating") val averageRating: String, // La API lo manda como String
    @SerializedName("reviews_count") val reviewsCount: Int,
    val user: User, // Reutilizamos la clase User que ya tenemos
    val categories: List<Categoria>, // Ahora es una lista de objetos Categoria
    val reviews: List<Resena> // Usa la nueva clase Resena
)

data class RegisterResponse(
    val id: Int,
    val name: String,
    val email: String,
    val profile: Profile
)

data class Profile(
    val id: Int,
    val name: String,
    @SerializedName("last_name") val lastName: String,
    val type: Int
)

data class ErrorResponse(
    val message: String,
    val errors: Map<String, List<String>>
)
