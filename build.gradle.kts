// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    id("org.sonarqube") version "7.3.0.8198"
}

sonarqube {
    properties {
        property("sonar.projectKey", "ItxasoUrgoitia_ParkingLKSNext")
        property("sonar.organization", "itxasourgoitia")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.androidVariant", "debug")
    }
}