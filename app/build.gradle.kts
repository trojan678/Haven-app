plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    //id("com.android.application")
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    //id("androidx.navigation.safeargs.kotlin") version "2.5.3" apply false

}
apply(plugin = "com.google.gms.google-services")


android {
    namespace = "com.example.haven"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.haven"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        //buildConfigField("String", "BASE_URL", "https://api.yourdomain.com/")
        //buildConfigField("boolean",LOG_REQUESTS,"true")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
                //buildConfig true
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
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose.v277)
    //implementation(libs.kotlinx.coroutines.play.services)
    implementation (libs.androidx.foundation)
    implementation (libs.androidx.material3.v120)
    implementation (libs.coil.compose)
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt ("com.google.dagger:hilt-android-compiler:2.44")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")

}