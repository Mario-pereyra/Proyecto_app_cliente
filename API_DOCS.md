# Documentación de API - App Cliente 🌐

## Información General

**Base URL**: `http://trabajos.jmacboy.com/api/`

**Autenticación**: Bearer Token (JWT) en header Authorization

**Formato de datos**: JSON

**Codificación**: UTF-8

## Endpoints de Autenticación 🔐

### POST /client/login
Autentica un cliente y devuelve un token de acceso.

**Request Body:**
```json
{
  "email": "cliente@ejemplo.com",
  "password": "contraseña123"
}
```

**Response (200):**
```json
{
  "access_token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9..."
}
```

**Códigos de respuesta:**
- `200`: Login exitoso
- `401`: Credenciales inválidas
- `422`: Datos de entrada inválidos

---

### POST /client/register
Registra un nuevo cliente en el sistema.

**Request Body:**
```json
{
  "name": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@ejemplo.com",
  "password": "contraseña123"
}
```

**Response (201):**
```json
{
  "id": 1,
  "name": "Juan",
  "email": "juan.perez@ejemplo.com",
  "profile": {
    "id": 1,
    "user_id": 1,
    "phone": null,
    "picture_url": null
  }
}
```

**Códigos de respuesta:**
- `201`: Usuario creado exitosamente
- `422`: Email ya existe o datos inválidos

## Endpoints de Categorías 📋

### GET /categories
Obtiene todas las categorías de servicios disponibles.

**Headers requeridos:**
```
Authorization: Bearer {token}
```

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "Plomería",
    "description": "Servicios de fontanería y plomería"
  },
  {
    "id": 2,
    "name": "Electricidad",
    "description": "Instalaciones y reparaciones eléctricas"
  },
  {
    "id": 3,
    "name": "Carpintería",
    "description": "Trabajos en madera y muebles"
  }
]
```

## Endpoints de Trabajadores 👷

### GET /categories/{id}/workers
Obtiene todos los trabajadores de una categoría específica.

**Parámetros:**
- `id` (integer): ID de la categoría

**Headers requeridos:**
```
Authorization: Bearer {token}
```

**Response (200):**
```json
[
  {
    "id": 1,
    "user_id": 5,
    "picture_url": "https://ejemplo.com/foto1.jpg",
    "user": {
      "id": 5,
      "name": "Carlos",
      "last_name": "González",
      "email": "carlos@ejemplo.com",
      "type": 2
    },
    "categories": [
      {
        "id": 1,
        "name": "Plomería",
        "description": "Servicios de fontanería"
      }
    ]
  }
]
```

---

### GET /workers/{id}
Obtiene información detallada de un trabajador específico.

**Parámetros:**
- `id` (integer): ID del trabajador

**Headers requeridos:**
```
Authorization: Bearer {token}
```

**Response (200):**
```json
{
  "id": 1,
  "user_id": 5,
  "picture_url": "https://ejemplo.com/foto1.jpg",
  "average_rating": "4.5",
  "reviews_count": 25,
  "user": {
    "id": 5,
    "name": "Carlos",
    "last_name": "González",
    "email": "carlos@ejemplo.com",
    "type": 2
  },
  "categories": [
    {
      "id": 1,
      "name": "Plomería",
      "description": "Servicios de fontanería"
    }
  ],
  "reviews": [
    {
      "id": 1,
      "worker_id": 1,
      "user_id": 3,
      "appointment_id": 10,
      "rating": 5,
      "comment": "Excelente trabajo, muy profesional",
      "is_done": 1,
      "user": {
        "id": 3,
        "name": "María",
        "last_name": "López",
        "type": 1
      }
    }
  ]
}
```

## Endpoints de Citas 📅

### GET /appointments
Obtiene todas las citas del cliente autenticado.

**Headers requeridos:**
```
Authorization: Bearer {token}
```

**Response (200):**
```json
[
  {
    "id": 1,
    "worker_id": 5,
    "user_id": 3,
    "appointment_date": "2024-01-15",
    "appointment_time": "14:30:00",
    "category_selected_id": 1,
    "latitude": -25.2637,
    "longitude": -57.5759,
    "status": 1,
    "worker": {
      "id": 5,
      "user_id": 10,
      "picture_url": "https://ejemplo.com/foto5.jpg",
      "user": {
        "id": 10,
        "name": "Carlos",
        "last_name": "González",
        "email": "carlos@ejemplo.com"
      }
    }
  }
]
```

---

### POST /appointments
Crea una nueva cita inicial (solicitud) con un trabajador.

**Headers requeridos:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "worker_id": "5",
  "category_selected_id": 1
}
```

**Response (201):**
```json
{
  "id": 15,
  "worker_id": 5,
  "user_id": 3,
  "appointment_date": null,
  "appointment_time": null,
  "category_selected_id": 1,
  "latitude": null,
  "longitude": null,
  "status": 0
}
```

**Estados de cita:**
- `0`: Pendiente (recién creada)
- `1`: Confirmada (con fecha, hora y lugar)
- `2`: En progreso
- `3`: Completada
- `4`: Cancelada

---

### POST /appointments/{id}/make
Concreta una cita agregando fecha, hora y ubicación.

**Parámetros:**
- `id` (integer): ID de la cita

**Headers requeridos:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "appointment_date": "2024-01-15",
  "appointment_time": "14:30",
  "latitude": "-25.2637",
  "longitude": "-57.5759"
}
```

**Response (200):**
```json
{
  "id": 15,
  "worker_id": 5,
  "user_id": 3,
  "appointment_date": "2024-01-15",
  "appointment_time": "14:30:00",
  "category_selected_id": 1,
  "latitude": -25.2637,
  "longitude": -57.5759,
  "status": 1
}
```

---

### GET /appointments/{id}
Obtiene detalles completos de una cita específica.

**Parámetros:**
- `id` (integer): ID de la cita

**Headers requeridos:**
```
Authorization: Bearer {token}
```

**Response (200):**
```json
{
  "id": 15,
  "worker_id": 5,
  "user_id": 3,
  "appointment_date": "2024-01-15",
  "appointment_time": "14:30:00",
  "category_selected_id": 1,
  "latitude": -25.2637,
  "longitude": -57.5759,
  "status": 1,
  "worker": {
    "id": 5,
    "user_id": 10,
    "picture_url": "https://ejemplo.com/foto5.jpg",
    "user": {
      "id": 10,
      "name": "Carlos",
      "last_name": "González",
      "email": "carlos@ejemplo.com",
      "type": 2
    }
  }
}
```

## Endpoints de Chat 💬

### GET /appointments/{id}/chats
Obtiene todos los mensajes de chat de una cita específica.

**Parámetros:**
- `id` (integer): ID de la cita

**Headers requeridos:**
```
Authorization: Bearer {token}
```

**Response (200):**
```json
[
  {
    "id": 1,
    "appointment_id": 15,
    "sender_id": 3,
    "receiver_id": 10,
    "date_sent": "2024-01-10 10:30:00",
    "message": "Hola, ¿cuándo podríamos coordinar el trabajo?",
    "appointment": {
      "id": 15,
      "worker_id": 5,
      "user_id": 3,
      "appointment_date": "2024-01-15",
      "appointment_time": "14:30:00",
      "category_selected_id": 1,
      "latitude": -25.2637,
      "longitude": -57.5759,
      "status": 1
    },
    "sender": {
      "id": 3,
      "name": "María",
      "email": "maria@ejemplo.com"
    },
    "receiver": {
      "id": 10,
      "name": "Carlos",
      "email": "carlos@ejemplo.com"
    }
  }
]
```

---

### POST /appointments/{id}/chats
Envía un nuevo mensaje en el chat de una cita.

**Parámetros:**
- `id` (integer): ID de la cita

**Headers requeridos:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "message": "Perfecto, nos vemos a las 14:30",
  "receiver_id": 10
}
```

**Response (201):**
```json
{}
```

**Nota**: La respuesta es vacía. Para obtener el mensaje creado, realizar una nueva consulta GET a `/appointments/{id}/chats`.

## Endpoints de Calificaciones ⭐

### POST /appointments/{id}/review
Califica y comenta sobre un servicio completado.

**Parámetros:**
- `id` (integer): ID de la cita completada

**Headers requeridos:**
```
Authorization: Bearer {token}
Content-Type: application/json
```

**Request Body:**
```json
{
  "rating": 5,
  "review": "Excelente trabajo, muy profesional y puntual. Lo recomiendo totalmente."
}
```

**Response (201):**
```json
{}
```

**Validaciones:**
- `rating`: Debe ser un entero entre 1 y 5
- `review`: Texto descriptivo del servicio recibido
- Solo se puede calificar citas con estado "completada"

## Códigos de Error Comunes 🚨

### 401 - Unauthorized
```json
{
  "message": "Unauthenticated."
}
```

**Causas:**
- Token no proporcionado
- Token expirado
- Token inválido

### 403 - Forbidden
```json
{
  "message": "This action is unauthorized."
}
```

**Causas:**
- Intentar acceder a recursos de otro usuario
- Permisos insuficientes

### 404 - Not Found
```json
{
  "message": "The requested resource was not found."
}
```

**Causas:**
- ID de recurso inexistente
- Endpoint incorrecto

### 422 - Unprocessable Entity
```json
{
  "message": "The given data was invalid.",
  "errors": {
    "email": [
      "The email field is required."
    ],
    "password": [
      "The password must be at least 6 characters."
    ]
  }
}
```

**Causas:**
- Datos de entrada inválidos
- Violación de reglas de validación

### 500 - Internal Server Error
```json
{
  "message": "Internal Server Error"
}
```

**Causas:**
- Error en el servidor
- Problema con la base de datos

## Límites y Restricciones 📏

### Rate Limiting
- **Autenticación**: 5 intentos por minuto por IP
- **API General**: 60 requests por minuto por usuario autenticado
- **Chat**: 20 mensajes por minuto por cita

### Tamaños Máximos
- **Mensaje de chat**: 1000 caracteres
- **Comentario de review**: 500 caracteres
- **Nombre de usuario**: 50 caracteres

### Formatos de Fecha y Hora
- **Fecha**: `YYYY-MM-DD` (ej: "2024-01-15")
- **Hora**: `HH:MM` (ej: "14:30")
- **Timestamp**: `YYYY-MM-DD HH:MM:SS` (ej: "2024-01-10 10:30:00")

## Ejemplos de Integración 🔧

### Flujo Completo de Autenticación
```kotlin
// 1. Login
val loginRequest = LoginRequest("usuario@ejemplo.com", "password123")
val loginResponse = apiClient.login(loginRequest)

// 2. Guardar token
if (loginResponse.isSuccessful) {
    val token = loginResponse.body()?.accessToken
    tokenDataStore.saveToken(token)
}

// 3. Usar token en requests posteriores (automático con AuthInterceptor)
val categories = apiClient.getCategories()
```

### Flujo de Creación de Cita
```kotlin
// 1. Crear cita inicial
val createRequest = CrearCitaRequest("5", 1)
val appointmentResponse = apiClient.createAppointment(createRequest)

// 2. Concretar cita con detalles
val appointmentId = appointmentResponse.body()?.id
val makeRequest = ConcretarCitaRequest(
    appointmentDate = "2024-01-15",
    appointmentTime = "14:30",
    latitude = "-25.2637",
    longitude = "-57.5759"
)
val finalAppointment = apiClient.makeAppointment(appointmentId, makeRequest)
```

### Implementación de Chat con Polling
```kotlin
class ChatRepository {
    suspend fun startChatPolling(appointmentId: Int) {
        while (true) {
            try {
                val messages = apiClient.getChatMessages(appointmentId)
                if (messages.isSuccessful) {
                    // Actualizar UI con nuevos mensajes
                    updateMessages(messages.body())
                }
                delay(30000) // Esperar 30 segundos
            } catch (e: Exception) {
                // Manejar errores de conexión
                handleConnectionError(e)
            }
        }
    }
}
```

## Consideraciones de Seguridad 🔒

### Tokens JWT
- **Expiración**: Los tokens tienen una duración limitada
- **Renovación**: Implementar refresh token para sesiones largas
- **Almacenamiento**: Usar DataStore (Android) para almacenamiento seguro

### Validación de Datos
- **Sanitización**: Limpiar input del usuario antes de enviar
- **Validación**: Verificar formatos y rangos en el cliente
- **Escape**: Prevenir inyección de código en mensajes

### HTTPS
- **Conexión segura**: Todas las comunicaciones deben usar HTTPS en producción
- **Certificados**: Verificar certificados SSL válidos
- **Pinning**: Implementar certificate pinning para mayor seguridad

---

Esta documentación de API está diseñada para facilitar la integración y el desarrollo de la aplicación cliente, proporcionando todos los detalles necesarios para una implementación exitosa.