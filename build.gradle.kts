// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.4") // Latest compatible Android Gradle Plugin version
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0") // Update Kotlin version to match the modern standard
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.52") // Hilt plugin with latest version
    }
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}
