# Guía de Configuración para Desarrolladores 🛠️

## Prerrequisitos del Sistema

### Software Requerido
- **Android Studio** Electric Eel (2022.1.1) o superior
- **JDK** 11 o superior
- **Android SDK** con las siguientes versiones:
  - API Level 24 (Android 7.0) - Mínimo
  - API Level 36 - Target
- **Git** para control de versiones
- **Gradle** 8.0+ (incluido con Android Studio)

### Hardware Recomendado
- **RAM**: 8GB mínimo, 16GB recomendado
- **Almacenamiento**: 20GB espacio libre
- **CPU**: Procesador de 64 bits
- **Dispositivo Android** o emulador para testing

## Configuración del Entorno de Desarrollo

### 1. Instalación de Android Studio

#### Windows:
```bash
# Descargar desde https://developer.android.com/studio
# Instalar con configuración por defecto
# Agregar al PATH del sistema
```

#### macOS:
```bash
# Usar Homebrew
brew install --cask android-studio

# O descargar desde el sitio oficial
```

#### Linux (Ubuntu/Debian):
```bash
# Instalar dependencias
sudo apt update
sudo apt install openjdk-11-jdk

# Descargar Android Studio desde el sitio oficial
# Extraer y ejecutar studio.sh
```

### 2. Configuración del SDK de Android

Abrir Android Studio y configurar:

```bash
# SDK Platforms necesarios:
- Android 14.0 (API 34)
- Android 7.0 (API 24)

# SDK Tools:
- Android SDK Build-Tools 34.0.0
- Android Emulator
- Android SDK Platform-Tools
- Google Play services
```

### 3. Configuración de Variables de Entorno

#### Agregar al archivo de perfil (~/.bashrc, ~/.zshrc, etc.):
```bash
export ANDROID_HOME="$HOME/Android/Sdk"
export PATH="$PATH:$ANDROID_HOME/emulator"
export PATH="$PATH:$ANDROID_HOME/platform-tools"
export PATH="$PATH:$ANDROID_HOME/tools"
export PATH="$PATH:$ANDROID_HOME/tools/bin"
```

## Configuración del Proyecto

### 1. Clonar el Repositorio
```bash
git clone https://github.com/Mario-pereyra/Proyecto_app_cliente.git
cd Proyecto_app_cliente
```

### 2. Configuración de API Keys

#### Google Maps API Key:
1. Ir a [Google Cloud Console](https://console.cloud.google.com/)
2. Crear proyecto nuevo o usar existente
3. Habilitar "Maps SDK for Android"
4. Crear credenciales (API Key)
5. Restringir la key por package name

#### Configurar en el proyecto:
```xml
<!-- En app/src/main/AndroidManifest.xml -->
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="TU_GOOGLE_MAPS_API_KEY" />
```

### 3. Configuración de local.properties
Crear archivo `local.properties` en la raíz del proyecto:
```properties
# SDK path
sdk.dir=/path/to/your/Android/Sdk

# API Keys
GOOGLE_MAPS_API_KEY=tu_api_key_aqui

# Backend URLs
DEV_BASE_URL="http://10.0.2.2:8000/api/"
PROD_BASE_URL="http://trabajos.jmacboy.com/api/"
```

### 4. Configuración de Gradle

#### Verificar gradle.properties:
```properties
# Optimización de memoria
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true

# AndroidX
android.useAndroidX=true
android.enableJetifier=true

# Kotlin
kotlin.code.style=official
```

## Estructura del Proyecto Detallada

```
Proyecto_app_cliente/
├── app/
│   ├── build.gradle.kts           # Configuración del módulo app
│   ├── proguard-rules.pro         # Reglas de ofuscación
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/proyectoappcliente/
│       │   │   ├── data/
│       │   │   │   ├── datastore/          # Almacenamiento local
│       │   │   │   ├── model/              # Modelos de datos
│       │   │   │   └── network/            # Red y API
│       │   │   ├── repositories/           # Capa de datos
│       │   │   ├── ui/
│       │   │   │   ├── activities/         # MainActivity
│       │   │   │   ├── adapters/           # Adaptadores
│       │   │   │   ├── fragments/          # Pantallas
│       │   │   │   └── viewmodels/         # Lógica de negocio
│       │   │   └── MapsActivity.kt
│       │   └── res/                        # Recursos (layouts, strings, etc.)
│       ├── androidTest/                    # Tests instrumentados
│       └── test/                           # Tests unitarios
├── build.gradle.kts                        # Configuración del proyecto
├── gradle.properties                       # Propiedades globales
├── settings.gradle.kts                     # Configuración de módulos
└── local.properties                        # Configuración local (no versionado)
```

## Dependencias del Proyecto

### Dependencias Principales
```kotlin
dependencies {
    // Core Android
    implementation "androidx.core:core-ktx:1.12.0"
    implementation "androidx.appcompat:appcompat:1.6.1"
    implementation "com.google.android.material:material:1.11.0"
    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
    
    // Architecture Components
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.7.0"
    
    // Navigation
    implementation "androidx.navigation:navigation-fragment-ktx:2.7.6"
    implementation "androidx.navigation:navigation-ui-ktx:2.7.6"
    
    // Network
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.squareup.retrofit2:converter-gson:2.9.0"
    implementation "com.squareup.okhttp3:logging-interceptor:4.12.0"
    
    // Local Storage
    implementation "androidx.datastore:datastore-preferences:1.0.0"
    implementation "androidx.room:room-runtime:2.6.1"
    implementation "androidx.room:room-ktx:2.6.1"
    kapt "androidx.room:room-compiler:2.6.1"
    
    // Image Loading
    implementation "com.github.bumptech.glide:glide:4.16.0"
    
    // Maps
    implementation "com.google.android.gms:play-services-maps:18.2.0"
    implementation "com.google.android.gms:play-services-location:21.0.1"
    
    // Testing
    testImplementation "junit:junit:4.13.2"
    testImplementation "org.mockito:mockito-core:5.1.1"
    testImplementation "androidx.arch.core:core-testing:2.2.0"
    testImplementation "org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3"
    
    androidTestImplementation "androidx.test.ext:junit:1.1.5"
    androidTestImplementation "androidx.test.espresso:espresso-core:3.5.1"
    androidTestImplementation "androidx.test:runner:1.5.2"
    androidTestImplementation "androidx.test:rules:1.5.0"
}
```

## Configuración de Build Variants

### Build Types
```kotlin
android {
    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8000/api/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "true")
        }
        
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "BASE_URL", "\"https://trabajos.jmacboy.com/api/\"")
            buildConfigField("boolean", "ENABLE_LOGGING", "false")
        }
        
        staging {
            initWith(getByName("debug"))
            isDebuggable = false
            applicationIdSuffix = ".staging"
            buildConfigField("String", "BASE_URL", "\"https://staging.trabajos.jmacboy.com/api/\"")
        }
    }
}
```

### Product Flavors (Opcional)
```kotlin
android {
    flavorDimensions += "environment"
    
    productFlavors {
        create("dev") {
            dimension = "environment"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        
        create("prod") {
            dimension = "environment"
        }
    }
}
```

## Configuración de Testing

### Tests Unitarios
```kotlin
// En app/src/test/java/
class LoginViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    
    @Test
    fun `login with valid credentials should succeed`() {
        // Test implementation
    }
}
```

### Tests Instrumentados
```kotlin
// En app/src/androidTest/java/
@RunWith(AndroidJUnit4::class)
class LoginFragmentTest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun loginWithValidCredentials() {
        // UI test implementation
    }
}
```

## Comandos de Desarrollo

### Compilación
```bash
# Compilar en modo debug
./gradlew assembleDebug

# Compilar en modo release
./gradlew assembleRelease

# Limpiar proyecto
./gradlew clean
```

### Testing
```bash
# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests instrumentados
./gradlew connectedAndroidTest

# Ejecutar todos los tests
./gradlew check

# Generar reporte de cobertura
./gradlew jacocoTestReport
```

### Instalación
```bash
# Instalar en dispositivo conectado (debug)
./gradlew installDebug

# Instalar variante específica
./gradlew installDebug

# Desinstalar
./gradlew uninstallDebug
```

### Análisis de Código
```bash
# Lint check
./gradlew lint

# Ktlint (si está configurado)
./gradlew ktlintCheck

# Detekt (si está configurado)
./gradlew detekt
```

## Configuración de IDE

### Android Studio Plugins Recomendados
- **Kotlin** (incluido por defecto)
- **Git Toolbox**: Mejor integración con Git
- **ADB Idea**: Comandos ADB desde IDE
- **JSON to Kotlin Class**: Generar clases desde JSON
- **Rainbow Brackets**: Mejor visualización de código

### Configuración de Code Style
```kotlin
// En Settings > Editor > Code Style > Kotlin
- Use default Kotlin style guide
- Import order: android.*, androidx.*, com.*, java.*, javax.*, kotlin.*, *
- Blank lines: Keep maximum 2
```

### Live Templates Útiles
```kotlin
// Crear Fragment básico
frag

// Crear ViewModel
vm

// Crear Adapter
adapter
```

## Debugging y Profiling

### Configuración de Logging
```kotlin
// En build.gradle.kts (debug)
buildConfigField("boolean", "ENABLE_LOGGING", "true")

// En código
if (BuildConfig.ENABLE_LOGGING) {
    Log.d("AppCliente", "Debug message")
}
```

### Android Profiler
- **CPU Profiler**: Analizar performance
- **Memory Profiler**: Detectar memory leaks
- **Network Profiler**: Monitorear tráfico de red
- **Energy Profiler**: Optimizar consumo de batería

### Debug Database
```kotlin
// Configurar para inspeccionar Room database
debugImplementation "com.facebook.flipper:flipper:0.164.0"
debugImplementation "com.facebook.flipper:flipper-network-plugin:0.164.0"
```

## CI/CD Configuration (Opcional)

### GitHub Actions
```yaml
# .github/workflows/android.yml
name: Android CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
        
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Run tests
      run: ./gradlew test
      
    - name: Build debug APK
      run: ./gradlew assembleDebug
```

## Troubleshooting

### Problemas Comunes

#### Build Failures
```bash
# Limpiar cache de Gradle
./gradlew clean

# Invalidar caché de Android Studio
File > Invalidate Caches and Restart

# Verificar versions de SDK
Tools > SDK Manager
```

#### Problemas de Red en Emulador
```bash
# Usar 10.0.2.2 para localhost en emulador
# O configurar proxy si es necesario
```

#### OutOfMemoryError
```properties
# En gradle.properties
org.gradle.jvmargs=-Xmx4096m -Dfile.encoding=UTF-8
```

### Logs Útiles
```bash
# Ver logs en tiempo real
adb logcat | grep "AppCliente"

# Logs de crash
adb logcat AndroidRuntime:E *:S

# Logs de red
adb logcat okhttp:D *:S
```

## Mejores Prácticas

### Organización del Código
- **Usar packages por feature**, no por tipo de clase
- **Mantener clases pequeñas** y con responsabilidad única
- **Documentar código complejo** con comentarios
- **Usar constantes** en lugar de strings hardcodeados

### Git Workflow
```bash
# Crear feature branch
git checkout -b feature/nueva-funcionalidad

# Commits descriptivos
git commit -m "feat: agregar funcionalidad de chat"

# Pull request a develop
# Review de código antes de merge
```

### Testing
- **Escribir tests primero** para funcionalidades críticas
- **Mockear dependencias** externas
- **Tests de integración** para flujos completos
- **Coverage mínimo del 70%**

---

Con esta configuración tendrás un entorno de desarrollo completo y funcional para trabajar en la aplicación cliente. Recuerda mantener todas las dependencias actualizadas y seguir las mejores prácticas de desarrollo Android.