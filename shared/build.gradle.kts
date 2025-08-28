import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinCocoapods)
    id("co.touchlab.skie") version "0.10.4"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    iosX64()

    cocoapods {
        version = "2.0"
        summary = "Some description for a Kotlin/Native module"
        homepage = "Link to a Kotlin/Native module homepage"

        ios.deploymentTarget = "16.0"
        
        // Force use of stable iOS SDK instead of beta
        extraSpecAttributes["ios.deployment_target"] = "16.0"

        framework {
            baseName = "SharedFramework"
        }

        pod("MediaPipeTasksGenAIC") {
            version = "0.10.14"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
        pod("MediaPipeTasksGenAI") {
            version = "0.10.14"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")

            // Core of Orbit, providing state management and unidirectional data flow (multiplatform)
            implementation(libs.orbit.core)

            // Integrates Orbit with Android and Common ViewModel for lifecycle-aware state handling (Android, iOS, desktop)
            implementation(libs.orbit.viewmodel)

            // put your Multiplatform dependencies here
            api(libs.koin.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            implementation("com.google.mediapipe:tasks-genai:0.10.24")
        }
    }
}

android {
    namespace = "com.genaillm.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
