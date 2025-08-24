# Documentación Técnica - App Cliente 🔧

## Arquitectura Detallada

### Patrón MVVM Implementado

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   UI Layer      │    │  Business Logic │    │   Data Layer    │
│   (Fragments)   │◄──►│   (ViewModels)  │◄──►│  (Repository)   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         ▼                       ▼                       ▼
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│  - LoginFrag    │    │ - LoginViewModel│    │ - AppRepository │
│  - CategoriesFrag│    │ - CategoriesVM  │    │ - ApiClient     │
│  - WorkersFragment│   │ - WorkersVM     │    │ - TokenDataStore│
│  - ChatFragment │    │ - ChatViewModel │    │ - AuthInterceptor│
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### Flujo de Datos

1. **Fragment** → Interacción del usuario
2. **ViewModel** → Procesa la lógica de negocio
3. **Repository** → Coordina fuentes de datos
4. **ApiClient** → Comunicación con backend
5. **DataStore** → Persistencia local

## Componentes Detallados

### 🎯 UI Layer (Fragments)

#### LoginFragment
```kotlin
Responsabilidades:
- Validación de formularios de login
- Gestión de estados de autenticación
- Navegación post-login
- Manejo de errores de credenciales

Observable:
- sessionStatus: Boolean
- loginResult: LoginResponse?
- errorMessage: String?
```

#### CategoriesFragment
```kotlin
Responsabilidades:
- Mostrar grid/lista de categorías
- Implementar búsqueda y filtrado
- Navegación a lista de trabajadores
- Gestión del menú principal

Observable:
- categories: List<Categoria>
- filteredCategories: List<Categoria>
- isLoading: Boolean
```

#### WorkersFragment
```kotlin
Responsabilidades:
- Lista de trabajadores por categoría
- Implementar paginación
- Filtros de búsqueda avanzada
- Navegación a detalle de trabajador

Observable:
- workers: List<Trabajador>
- isLoading: Boolean
- errorMessage: String?
```

#### ChatFragment
```kotlin
Responsabilidades:
- Interfaz de chat en tiempo real
- Envío y recepción de mensajes
- Polling automático de mensajes
- Navegación a mapa para concretar cita

Observable:
- messages: List<MensajeChat>
- appointmentDetails: Cita
- workerDetails: TrabajadorDetalle
```

### 🧠 Business Logic (ViewModels)

#### LoginViewModel
```kotlin
class LoginViewModel(private val repository: AppRepository) : ViewModel() {
    
    // Estados observables
    private val _sessionStatus = MutableLiveData<Boolean>()
    val sessionStatus: LiveData<Boolean> = _sessionStatus
    
    // Métodos principales
    fun login(email: String, password: String)
    fun checkSession()
    fun logout()
    
    // Validaciones
    private fun validateCredentials(email: String, password: String): Boolean
}
```

#### ChatViewModel con Polling
```kotlin
class ChatViewModel(private val repository: AppRepository) : ViewModel() {
    
    private var pollingJob: Job? = null
    
    fun startPollingMessages(appointmentId: Int) {
        pollingJob?.cancel()
        pollingJob = viewModelScope.launch {
            while (true) {
                fetchMessages(appointmentId)
                delay(30000) // Polling cada 30 segundos
            }
        }
    }
    
    fun sendMessage(appointmentId: Int, message: String, receiverId: Int) {
        viewModelScope.launch {
            // Enviar mensaje y refrescar automáticamente
            repository.sendChatMessage(appointmentId, request)
            fetchMessages(appointmentId)
        }
    }
}
```

### 💾 Data Layer

#### AppRepository - Coordinador Central
```kotlin
class AppRepository(
    private val apiClient: ApiClient,
    private val tokenDataStore: TokenDataStore
) {
    // Autenticación
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse>
    suspend fun saveToken(token: String)
    fun getToken(): Flow<String?>
    
    // Categorías y Trabajadores
    suspend fun getCategories(): Response<List<Categoria>>
    suspend fun getWorkersByCategory(categoryId: Int): Response<List<Trabajador>>
    suspend fun getWorkerDetail(workerId: Int): Response<TrabajadorDetalle>
    
    // Citas
    suspend fun createAppointment(request: CrearCitaRequest): Response<Cita>
    suspend fun makeAppointment(id: Int, request: ConcretarCitaRequest): Response<Cita>
    suspend fun getAppointments(): Response<List<Cita>>
    
    // Chat
    suspend fun getChatMessages(appointmentId: Int): Response<List<MensajeChat>>
    suspend fun sendChatMessage(id: Int, request: MensajeChatRequest): Response<Unit>
    
    // Reviews
    suspend fun postReview(id: Int, request: CalificarCitaRequest): Response<Unit>
}
```

#### TokenDataStore - Gestión Segura de Tokens
```kotlin
class TokenDataStore(private val context: Context) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
    }
    
    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }
    
    fun getToken(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }
    }
    
    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}
```

#### AuthInterceptor - Inyección Automática de Tokens
```kotlin
class AuthInterceptor(context: Context) : Interceptor {
    private val tokenDataStore = TokenDataStore(context)
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            tokenDataStore.getToken().first()
        }
        
        val request = if (!token.isNullOrBlank()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        
        return chain.proceed(request)
    }
}
```

## Estados y Navegación

### Navigation Graph
```xml
<!-- Flujo Principal -->
LoginFragment → CategoriesFragment → WorkersFragment → WorkerDetailFragment
                      ↓                                        ↓
               AppointmentsFragment ← ChatFragment ← CreateAppointment
                      ↓
               ChatFragment → MapFragment → DateTimePickerFragment
```

### Gestión de Estados

#### Estados de Loading
```kotlin
sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
```

#### Estados de Citas
```kotlin
object AppointmentStatus {
    const val PENDING = 0      // Cita creada, esperando confirmación
    const val CONFIRMED = 1    // Cita confirmada con fecha/hora/lugar
    const val IN_PROGRESS = 2  // Trabajo en progreso
    const val COMPLETED = 3    // Trabajo completado
    const val CANCELLED = 4    // Cita cancelada
}
```

## Adaptadores y RecyclerViews

### ChatAdapter - Mensajes Diferenciados
```kotlin
class ChatAdapter(
    private var messages: List<MensajeChat>,
    private val currentUserId: Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    
    companion object {
        private const val VIEW_TYPE_SENT = 1      // Mensaje enviado por el usuario
        private const val VIEW_TYPE_RECEIVED = 2  // Mensaje recibido del trabajador
    }
    
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == currentUserId) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }
}
```

### TrabajadorAdapter - Lista de Trabajadores
```kotlin
class TrabajadorAdapter(
    private var workers: List<Trabajador>,
    private val onWorkerClick: (Trabajador) -> Unit
) : RecyclerView.Adapter<TrabajadorAdapter.ViewHolder>() {
    
    inner class ViewHolder(private val binding: ItemTrabajadorBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(worker: Trabajador) {
            binding.txtWorkerName.text = "${worker.user.name} ${worker.user.lastName}"
            binding.txtSpecialties.text = worker.categories.joinToString { it.name }
            
            // Cargar imagen con Glide
            Glide.with(binding.root.context)
                .load(worker.pictureUrl)
                .placeholder(R.drawable.placeholder_user)
                .into(binding.imgWorkerPhoto)
                
            binding.root.setOnClickListener { onWorkerClick(worker) }
        }
    }
}
```

## Manejo de Errores

### Estrategia Global de Errores
```kotlin
sealed class NetworkResult<T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val message: String) : NetworkResult<T>()
    data class Exception<T>(val e: Throwable) : NetworkResult<T>()
}

// Extension function para manejo consistente
suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResult<T> {
    return try {
        val response = apiCall()
        if (response.isSuccessful) {
            NetworkResult.Success(response.body()!!)
        } else {
            NetworkResult.Error("Error ${response.code()}: ${response.message()}")
        }
    } catch (e: Exception) {
        NetworkResult.Exception(e)
    }
}
```

### Manejo de Errores en ViewModels
```kotlin
class BaseViewModel : ViewModel() {
    protected val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    protected fun handleError(result: NetworkResult<*>) {
        when (result) {
            is NetworkResult.Error -> _errorMessage.value = result.message
            is NetworkResult.Exception -> _errorMessage.value = "Error de conexión"
            else -> { /* Success case */ }
        }
    }
}
```

## Optimizaciones de Performance

### Carga de Imágenes con Glide
```kotlin
// Configuración global en Application o MainActivity
val requestOptions = RequestOptions()
    .placeholder(R.drawable.placeholder_loading)
    .error(R.drawable.placeholder_error)
    .diskCacheStrategy(DiskCacheStrategy.ALL)
    .transform(CenterCrop(), RoundedCorners(16))

Glide.with(context)
    .setDefaultRequestOptions(requestOptions)
```

### Lazy Loading en RecyclerViews
```kotlin
class WorkersFragment : Fragment() {
    
    private var isLoading = false
    private var hasMoreData = true
    
    private fun setupPagination() {
        binding.rvWorkers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                
                if (!isLoading && hasMoreData) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                        loadMoreWorkers()
                    }
                }
            }
        })
    }
}
```

## Testing y Debugging

### Logging Estructurado
```kotlin
object AppLogger {
    private const val TAG = "AppCliente"
    
    fun logApiCall(endpoint: String, method: String) {
        Log.d(TAG, "API Call: $method $endpoint")
    }
    
    fun logUserAction(action: String, details: String = "") {
        Log.i(TAG, "User Action: $action $details")
    }
    
    fun logError(error: String, throwable: Throwable? = null) {
        Log.e(TAG, "Error: $error", throwable)
    }
}
```

### Tests de ViewModels
```kotlin
@Test
fun `when login is successful, should save token and navigate`() = runTest {
    // Given
    val loginRequest = LoginRequest("test@email.com", "password")
    val loginResponse = LoginResponse("fake_token")
    
    coEvery { repository.login(loginRequest) } returns Response.success(loginResponse)
    coEvery { repository.saveToken("fake_token") } returns Unit
    
    // When
    viewModel.login("test@email.com", "password")
    
    // Then
    assertEquals(true, viewModel.loginSuccess.value)
    coVerify { repository.saveToken("fake_token") }
}
```

## Configuración de Desarrollo

### Build Variants
```kotlin
android {
    buildTypes {
        debug {
            buildConfigField "String", "BASE_URL", "\"http://trabajos.jmacboy.com/api/\""
            buildConfigField "boolean", "ENABLE_LOGGING", "true"
        }
        release {
            buildConfigField "String", "BASE_URL", "\"https://trabajos.jmacboy.com/api/\""
            buildConfigField "boolean", "ENABLE_LOGGING", "false"
            isMinifyEnabled = true
        }
    }
}
```

### ProGuard Rules
```proguard
# Retrofit
-keep class com.example.proyectoappcliente.data.model.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule

# Google Maps
-keep class com.google.android.gms.maps.** { *; }
```

## Mejores Prácticas Aplicadas

### 1. Separación de Responsabilidades
- **Fragments**: Solo UI y eventos de usuario
- **ViewModels**: Lógica de negocio y estado
- **Repository**: Coordinación de datos
- **DataSources**: Acceso específico a datos

### 2. Gestión de Memoria
- **ViewBinding**: Eliminación de findViewById
- **Lifecycle awareness**: ViewModels sobreviven rotaciones
- **Coroutines**: Gestión eficiente de hilos

### 3. Seguridad
- **No hardcoding**: Configuraciones en BuildConfig
- **Tokens seguros**: DataStore en lugar de SharedPreferences
- **Validación**: Input sanitization

### 4. UX/UI
- **Loading states**: Feedback visual durante operaciones
- **Error handling**: Mensajes informativos para el usuario
- **Navegación intuitiva**: Back stack management

---

Esta documentación técnica complementa el README principal y está dirigida a desarrolladores que trabajen en el mantenimiento o extensión de la aplicación cliente.