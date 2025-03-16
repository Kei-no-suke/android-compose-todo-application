plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "2.1.10-1.0.31"
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.20-RC3"
}

android {
    namespace = "com.keinosuke.todoapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.keinosuke.todoapplication"
        minSdk = 26
        targetSdk = 34
        versionCode = 4
        versionName = "4.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15"
    }
    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.15.0")
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.1.10"))
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation(platform("androidx.compose:compose-bom:2025.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Room
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:${room_version}")
    annotationProcessor("androidx.room:room-compiler:${room_version}")

    // To use Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:${room_version}")

    implementation("androidx.room:room-ktx:${room_version}")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // ViewModelScope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.7")

    // Navigation
    val nav_version = "2.8.8"

    implementation("androidx.navigation:navigation-fragment-ktx:${nav_version}")
    implementation("androidx.navigation:navigation-ui-ktx:${nav_version}")

    implementation("androidx.navigation:navigation-compose:${nav_version}")

    implementation("androidx.compose.material3:material3:1.3.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2025.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}