plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
    id("com.github.johnrengelman.shadow") version "8.+"
}

evaluationDependsOnChildren()

group = "com.atnerix"
version = ""

repositories {
    maven("https://m2.dv8tion.net/releases")
    mavenCentral()
    jcenter()
}

dependencies {
    val coroutinesVersion: String by project
    val serializationVersion: String by project
    val lincheckVersion: String by project

    implementation("ch.qos.logback:logback-classic:1.2.9")

    implementation("net.dv8tion:JDA:5.+")
    implementation("com.sedmelluq:lavaplayer:+")
    implementation("com.jagrosh:jda-utilities:3.+")

    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-common"))
    implementation(kotlinx("coroutines-core", coroutinesVersion))
    implementation(kotlinx("coroutines-core-jvm", coroutinesVersion))
    implementation(kotlinx("serialization-core", serializationVersion))
    implementation(kotlinx("serialization-json", serializationVersion))
    implementation(kotlinxModule("lincheck", lincheckVersion))
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("com.atnerix.alyx.MainKt")
}

fun DependencyHandler.kotlinx(module: String, version: String? = null): Any = kotlinxModule("kotlinx-$module", version)
fun DependencyHandler.kotlinxModule(module: String, version: String? = null): Any = "org.jetbrains.kotlinx:$module${version?.let { ":$version" } ?: ""}"