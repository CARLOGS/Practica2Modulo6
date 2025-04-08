import org.jetbrains.kotlin.backend.wasm.ir2wasm.bind

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.dgtic.practica2modulo6"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.dgtic.practica2modulo6"
        minSdk = 24
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        // Debe ser viewBinding en lugar de dataBinding
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    //Para retrofit y Gson
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Adicional para el interceptor
    implementation(libs.logging.interceptor)

    //Glide y Picasso
    implementation(libs.glide)
    implementation(libs.picasso)

    //Para las corrutinas con alcance lifecycle (opcional)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    //Imágenes con bordes redondeados
    implementation(libs.roundedimageview)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}