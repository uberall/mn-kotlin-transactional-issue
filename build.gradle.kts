plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.23"
    id("com.google.devtools.ksp") version "1.9.23-1.0.19"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.3.8"
}

version = "0.1"
group = "com.example"

val kotlinVersion=project.properties.get("kotlinVersion")

repositories {
    mavenCentral()
}

dependencies {
    // Micronaut
    runtimeOnly("org.yaml:snakeyaml")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    ksp("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut:micronaut-inject-java")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")

    // Micronaut Data
    ksp("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut.data:micronaut-data-jdbc")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("org.postgresql:postgresql")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("jakarta.persistence:jakarta.persistence-api")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.8.0")

    // Testing
    testImplementation("io.micronaut:micronaut-http-client")
}


application {
    mainClass = "com.example.ApplicationKt"
}
java {
    sourceCompatibility = JavaVersion.toVersion("21")
}


graalvmNative.toolchainDetection = false
micronaut {
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
}


tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion = "21"
}


