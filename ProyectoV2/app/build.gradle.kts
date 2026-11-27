import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

// Valores locales de cada integrante (local.properties NO se sube a Git):
//   API_BASE_URL=http://192.168.x.x/recetas_api/
//   USDA_API_KEY=tu_clave
val localProps = Properties().apply {
    val f = rootProject.file("local.properties")
    if (f.exists()) f.inputStream().use { load(it) }
}
fun localProp(name: String, default: String): String =
    (localProps.getProperty(name) ?: default).trim().replace("\"", "\\\"")

/**
 * URL del API sin espacios y terminada en "/". Si no tiene la forma
 * http(s)://host/..., la compilación se detiene con un mensaje claro
 * (antes la app se cerraba al abrir: Retrofit no acepta URLs inválidas).
 */
fun apiBaseUrl(): String {
    var url = localProp("API_BASE_URL", "http://10.0.2.2/recetas_api/").replace(Regex("\\s+"), "")
    if (!url.endsWith("/")) url += "/"
    require(Regex("^https?://[A-Za-z0-9.\\-]+(:\\d+)?/.*").matches(url)) {
        "API_BASE_URL en local.properties no es válida: \"$url\". Ejemplo: http://192.168.1.10/recetas_api/"
    }
    return url
}

android {
    namespace = "com.sergio.proyectov1"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.sergio.proyectov1"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // 10.0.2.2 = la computadora vista desde el emulador de Android
        buildConfigField("String", "API_BASE_URL", "\"${apiBaseUrl()}\"")
        buildConfigField("String", "USDA_API_KEY",
            "\"${localProp("USDA_API_KEY", "DEMO_KEY")}\"")
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.activity.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.ext.junit)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.4")
}