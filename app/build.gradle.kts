plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.caminalibre"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.caminalibre"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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


    // Librerías de CameraX (Versión estable)
    val camerax_version = "1.3.4"

    // Núcleo de CameraX y su implementación de camera2
    implementation("androidx.camera:camera-core:${camerax_version}")
    implementation("androidx.camera:camera-camera2:${camerax_version}")

    // Librería para que CameraX sepa cuándo abrirse/cerrarse con el ciclo de vida del fragmento
    implementation("androidx.camera:camera-lifecycle:${camerax_version}")

    // Vista PreviewView para que el usuario pueda ver lo que enfoca la cámara
    implementation("androidx.camera:camera-view:${camerax_version}")

    ///------/*************      ///////

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    testImplementation("androidx.room:room-testing:2.6.1")

    // Añade esto para garantizar el acceso a FileProvider y utilidades del sistema
    implementation("androidx.core:core:1.12.0")
}