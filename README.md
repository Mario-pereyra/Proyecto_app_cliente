# App Cliente - Plataforma de Servicios 📱

## Descripción del Proyecto

Esta aplicación Android forma parte de un sistema de dos aplicaciones (cliente-trabajador) para una plataforma de servicios. La **App Cliente** permite a los usuarios buscar, contactar y contratar trabajadores de diferentes categorías de servicios.

## 🌟 Características Principales

### Autenticación y Sesión
- **Registro de usuarios**: Creación de cuentas para nuevos clientes
- **Inicio de sesión**: Autenticación segura con tokens JWT
- **Persistencia de sesión**: Mantenimiento automático de la sesión del usuario

### Exploración de Servicios
- **Categorías de servicios**: Navegación por diferentes tipos de servicios disponibles
- **Búsqueda de trabajadores**: Filtrado y búsqueda de profesionales por categoría
- **Perfiles detallados**: Visualización completa de información del trabajador, incluyendo:
  - Información personal y profesional
  - Calificaciones y reseñas
  - Especialidades y categorías de trabajo
  - Historial de trabajos completados

### Gestión de Citas
- **Creación de citas**: Solicitud inicial de servicios a trabajadores
- **Programación**: Selección de fecha, hora y ubicación para el servicio
- **Seguimiento**: Visualización del estado de las citas programadas
- **Historial**: Acceso a citas pasadas y futuras

### Comunicación
- **Chat en tiempo real**: Comunicación directa con trabajadores
- **Mensajería contextual**: Chat específico para cada cita
- **Notificaciones**: Actualizaciones sobre mensajes y estados de citas

### Ubicación y Mapas
- **Selección de ubicación**: Uso de Google Maps para definir lugar del servicio
- **Geolocalización**: Obtención automática de la ubicación actual
- **Visualización de mapas**: Interface interactiva para selección precisa

### Sistema de Calificaciones
- **Reseñas**: Calificación y comentarios sobre servicios completados
- **Puntuación**: Sistema de estrellas para evaluar la calidad del trabajo
- **Feedback**: Contribución al sistema de reputación de trabajadores

## 🏗️ Arquitectura Técnica

### Patrón de Arquitectura
- **MVVM (Model-View-ViewModel)**: Separación clara de responsabilidades
- **Repository Pattern**: Centralización del acceso a datos
- **Single Activity**: Navegación basada en fragmentos

### Componentes Principales

#### 📱 Interfaz de Usuario
- **Fragments**: Modularización de pantallas
  - `LoginFragment`: Autenticación de usuarios
  - `CategoriesFragment`: Listado de categorías de servicios
  - `WorkersFragment`: Lista de trabajadores por categoría
  - `WorkerDetailFragment`: Detalle completo del trabajador
  - `ChatFragment`: Interfaz de mensajería
  - `MapFragment`: Selección de ubicación en mapa
  - `AppointmentsFragment`: Gestión de citas
  - `DateTimePickerFragment`: Selección de fecha y hora

- **Adapters**: Gestión de listas y datos
  - `CategoriaAdapter`: Visualización de categorías
  - `TrabajadorAdapter`: Lista de trabajadores
  - `ChatAdapter`: Mensajes del chat
  - `CitaAdapter`: Lista de citas
  - `ResenaAdapter`: Reseñas y calificaciones

#### 🧠 Lógica de Negocio
- **ViewModels**: Gestión de estado y lógica de UI
  - `LoginViewModel`: Autenticación y sesión
  - `CategoriesViewModel`: Gestión de categorías
  - `WorkersViewModel`: Manejo de lista de trabajadores
  - `ChatViewModel`: Lógica de mensajería
  - `AppointmentsViewModel`: Gestión de citas

#### 🔄 Acceso a Datos
- **Repository**: `AppRepository` - Centralización de operaciones de datos
- **API Client**: `ApiClient` - Interface con servicios REST
- **DataStore**: `TokenDataStore` - Almacenamiento seguro de tokens
- **Interceptor**: `AuthInterceptor` - Inyección automática de tokens de autenticación

### 🌐 Integración con Backend

#### URL Base de la API
```
http://trabajos.jmacboy.com/api/
```

#### Endpoints Principales

**Autenticación**
- `POST /client/login` - Inicio de sesión
- `POST /client/register` - Registro de nuevo usuario

**Categorías**
- `GET /categories` - Obtener todas las categorías

**Trabajadores**
- `GET /categories/{id}/workers` - Trabajadores por categoría
- `GET /workers/{id}` - Detalle específico de trabajador

**Citas**
- `GET /appointments` - Lista de citas del usuario
- `POST /appointments` - Crear nueva cita
- `POST /appointments/{id}/make` - Concretar cita con detalles
- `GET /appointments/{id}` - Obtener detalles de cita específica

**Chat**
- `GET /appointments/{id}/chats` - Mensajes de una cita
- `POST /appointments/{id}/chats` - Enviar nuevo mensaje

**Calificaciones**
- `POST /appointments/{id}/review` - Enviar reseña y calificación

## 🚀 Configuración del Proyecto

### Requisitos Previos
- **Android Studio** Arctic Fox o superior
- **SDK Android** 24 (mínimo) - 36 (objetivo)
- **Java** 11 o superior
- **Kotlin** 1.8+

### Dependencias Principales
```kotlin
// UI y Navegación
implementation "androidx.navigation:navigation-fragment-ktx"
implementation "androidx.navigation:navigation-ui-ktx"

// Arquitectura MVVM
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx"
implementation "androidx.lifecycle:lifecycle-livedata-ktx"

// Networking
implementation "com.squareup.retrofit2:retrofit"
implementation "com.squareup.retrofit2:converter-gson"

// Almacenamiento
implementation "androidx.datastore:datastore-preferences"
implementation "androidx.room:room-runtime"

// UI adicional
implementation "com.github.bumptech.glide:glide"
implementation "com.google.android.gms:play-services-maps"
```

### Configuración de Google Maps

1. Obtener API Key de Google Maps:
   - Visitar [Google Cloud Console](https://console.cloud.google.com/)
   - Crear proyecto y habilitar Maps SDK para Android
   - Generar API Key

2. Configurar en `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="TU_API_KEY_AQUI" />
```

### Permisos Requeridos
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

## 📱 Flujo de Usuario

### 1. Autenticación
```
Pantalla de Login → Registro (si es necesario) → Categorías Principales
```

### 2. Búsqueda de Servicios
```
Categorías → Lista de Trabajadores → Detalle del Trabajador → Crear Cita
```

### 3. Proceso de Contratación
```
Detalle del Trabajador → Crear Cita → Chat → Seleccionar Ubicación → 
Elegir Fecha/Hora → Confirmar Cita
```

### 4. Seguimiento
```
Lista de Citas → Chat con Trabajador → Calificar Servicio (al completar)
```

## 🔧 Instalación y Ejecución

### Clonar el Repositorio
```bash
git clone https://github.com/Mario-pereyra/Proyecto_app_cliente.git
cd Proyecto_app_cliente
```

### Compilar y Ejecutar
```bash
# Compilar el proyecto
./gradlew assembleDebug

# Instalar en dispositivo conectado
./gradlew installDebug

# Ejecutar tests
./gradlew test
```

### Configuración del Entorno
1. Abrir el proyecto en Android Studio
2. Sincronizar dependencias de Gradle
3. Configurar API Key de Google Maps
4. Verificar conectividad con el backend
5. Ejecutar en emulador o dispositivo físico

## 📊 Modelos de Datos

### Usuario y Autenticación
```kotlin
data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val name: String, val lastName: String, val email: String, val password: String)
data class LoginResponse(val accessToken: String)
```

### Trabajadores y Servicios
```kotlin
data class Categoria(val id: Int, val name: String, val description: String?)
data class Trabajador(val id: Int, val userId: Int, val user: User, val categories: List<Categoria>)
data class TrabajadorDetalle(val id: Int, val averageRating: String, val reviewsCount: Int, val reviews: List<Resena>)
```

### Citas y Comunicación
```kotlin
data class Cita(val id: Int, val workerId: Int, val userId: Int, val appointmentDate: String?, val status: Int)
data class MensajeChat(val id: Int, val appointmentId: Int, val message: String, val sender: ChatUser)
```

## 🤝 Integración con App Trabajador

Esta aplicación cliente se complementa con una **App Trabajador** que permite a los profesionales:
- Recibir y gestionar solicitudes de citas
- Comunicarse con clientes
- Actualizar disponibilidad
- Gestionar perfil profesional

Ambas aplicaciones comparten:
- Backend común y base de datos
- Sistema de autenticación
- Protocolo de comunicación
- Estándares de calidad

## 🔒 Seguridad

### Autenticación
- **JWT Tokens**: Autenticación basada en tokens seguros
- **Interceptor HTTP**: Inyección automática de headers de autorización
- **Almacenamiento seguro**: Tokens guardados en Android DataStore

### Privacidad
- **Validación de entrada**: Sanitización de datos de usuario
- **Comunicación encriptada**: HTTPS para todas las comunicaciones
- **Permisos mínimos**: Solicitud solo de permisos necesarios

## 🛠️ Desarrollo y Mantenimiento

### Estructura del Código
```
app/src/main/java/com/example/proyectoappcliente/
├── data/
│   ├── datastore/          # Almacenamiento local
│   ├── model/              # Modelos de datos
│   └── network/            # Cliente HTTP y API
├── repositories/           # Patrón Repository
├── ui/
│   ├── activities/         # MainActivity
│   ├── adapters/           # Adaptadores RecyclerView
│   ├── fragments/          # Pantallas de la aplicación
│   └── viewmodels/         # Lógica de presentación
└── MapsActivity.kt         # Actividad independiente para mapas
```

### Buenas Prácticas Implementadas
- **Separación de responsabilidades**: Cada clase tiene un propósito específico
- **Inyección de dependencias**: Repository pattern para gestión de datos
- **Manejo de errores**: Try-catch y estados de error apropiados
- **Lifecycle awareness**: ViewModels conscientes del ciclo de vida
- **Navegación declarativa**: Navigation Component para flujo de pantallas

## 🐛 Resolución de Problemas Comunes

### Error de Conectividad
- Verificar conexión a internet
- Confirmar que el backend esté funcionando
- Revisar configuración de proxy si aplica

### Problemas con Google Maps
- Verificar que la API Key sea válida
- Confirmar que esté habilitado Maps SDK
- Revisar permisos de ubicación

### Errores de Autenticación
- Verificar credenciales de usuario
- Confirmar que el token no haya expirado
- Revisar headers de autorización

## 📈 Futuras Mejoras

### Funcionalidades Propuestas
- **Notificaciones push**: Alertas en tiempo real
- **Pagos integrados**: Procesamiento de pagos dentro de la app
- **Historial avanzado**: Análisis detallado de servicios
- **Favoritos**: Sistema de trabajadores preferidos
- **Compartir**: Recomendación de trabajadores a otros usuarios

### Optimizaciones Técnicas
- **Caché inteligente**: Almacenamiento local de datos frecuentes
- **Offline mode**: Funcionalidad básica sin conexión
- **Performance**: Optimización de carga de imágenes y datos
- **Testing**: Cobertura completa de tests unitarios y UI

---

## 📞 Contacto y Soporte

Para dudas, sugerencias o reportes de errores relacionados con esta aplicación cliente, contactar al equipo de desarrollo del proyecto.

**Nota**: Esta documentación describe la aplicación cliente del sistema. Para información sobre la aplicación trabajador complementaria, consultar su repositorio correspondiente.
