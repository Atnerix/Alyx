pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }

    val kVersion: String by settings

    plugins {
        kotlin("jvm") version kVersion
        kotlin("plugin.serialization") version kVersion
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "AlyxBot"