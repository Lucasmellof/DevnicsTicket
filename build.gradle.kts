import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
val exposedVersion: String by project
plugins {
    java
    kotlin("jvm") version "1.4.30"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "wtf.lucasmellof.devnics"
version = "1.0"

repositories {
    mavenCentral()
    jcenter()
    maven { setUrl("https://jitpack.io") }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.2")

    // Database
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("mysql:mysql-connector-java:8.0.23")
    implementation("com.zaxxer:HikariCP:3.4.5")

    // Discord
    implementation("net.dv8tion:JDA:4.2.0_229")
    implementation("com.github.Devoxin:Flight:2.0.8")

    // Utils
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.github.salomonbrys.kotson:kotson:2.5.0")
    implementation("io.github.microutils:kotlin-logging:2.0.4")
    implementation("ch.qos.logback:logback-classic:1.3.0-alpha5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
val jar by tasks.getting(Jar::class) {
    manifest {
        attributes["Main-Class"] = "wtf.lucasmellof.devnics.DevnicsLauncher"
    }
}