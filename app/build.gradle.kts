plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.serialization)
    id("kotlin-parcelize")
}

android {
    namespace = "com.icris.utils"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.icris.utils"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    implementation(platform(libs.compose.bom))
    api(libs.bundles.base)

    implementation(libs.bundles.utils)

//    implementation(project(":res"))
//    implementation(project(":nav"))
    ksp(libs.compose.destinations.ksp)

    api(libs.bundles.lifecycle)
    api(libs.bundles.navigation)
    api(libs.bundles.paging)
    api(libs.bundles.kotlinx)
    api(libs.bundles.retrofit)
    api(libs.bundles.mmkv)
    api(libs.bundles.coil)
    api(libs.compose.destinations.core)
    api(libs.kotlinx.datetime)


    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.test.android)
    debugImplementation(libs.bundles.debug)

}