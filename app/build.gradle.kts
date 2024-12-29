plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.sudokuwave"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.sudokuwave"
        minSdk = 26
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
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation (libs.androidx.fragment.ktx)
    implementation (libs.androidx.appcompat)
    implementation(libs.material)
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.livedata.ktx)
    implementation (libs.androidx.runtime.livedata)

//    implementation (libs.androidx.ui.v160)
//    implementation (libs.androidx.material3.v160)
//    implementation (libs.androidx.lifecycle.runtime.compose)
//    implementation (libs.androidx.datastore.preferences)
//    implementation (libs.retrofit)
//    implementation (libs.converter.gson)



    // Jetpack Compose
    implementation (libs.androidx.compose.ui.ui.v152.x4) // Vérifiez la version la plus récente

    // Compose ViewModel
    implementation (libs.androidx.lifecycle.viewmodel.compose.v260) // Vérifiez la version la plus récente

    // Autres dépendances utiles pour Compose (optionnelles selon vos besoins)
    implementation (libs.androidx.material)
    implementation (libs.ui.tooling)
    implementation (libs.androidx.activity.compose.v172)






    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}