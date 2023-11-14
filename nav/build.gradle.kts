plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("maven-publish")
}

android {
    namespace = "com.icris.nav"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

//    implementation(libs.core.ktx)
    api(libs.compose.runtime)
    api(libs.compose.destinations.core)
//    implementation(libs.appcompat)
//    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}


val GROUP_ID = "com.gitee.icris.utils"
val ARTIFACT_ID = "nav"
val VERSION = latestGitTag().ifEmpty { "1.0.0-SNAPSHOT" }

fun latestGitTag(): String {
    val process = ProcessBuilder("git", "describe", "--tags", "--abbrev=0").start()
    return  process.inputStream.bufferedReader().use {bufferedReader ->
        bufferedReader.readText().trim()
    }
}


publishing { // 发布配置
    publications { // 发布的内容
        register<MavenPublication>("release") { // 注册一个名字为 release 的发布内容
            groupId = GROUP_ID
            artifactId = ARTIFACT_ID
            version = VERSION

            afterEvaluate { // 在所有的配置都完成之后执行
                // 从当前 module 的 release 包中发布
                from(components["release"])
            }
        }
    }
}
