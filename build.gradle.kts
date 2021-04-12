plugins {
    kotlin("jvm") version "1.4.32"
}

group = "com.ninjacontrol"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(kotlin("test-junit5"))
    implementation("org.junit.platform:junit-platform-launcher:1.8.0-M1")
    implementation("org.junit.platform:junit-platform-engine:1.8.0-M1")
    testImplementation("org.junit.platform:junit-platform-testkit:1.8.0-M1")
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation("io.github.classgraph:classgraph:4.8.104")
    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
}
tasks.register<Copy>("spi-deploy") {
    from("src/main/resources/spi-template-deploy")
    into("$buildDir/resources/main/META-INF/services")
    rename("spi-template-deploy", "org.junit.platform.engine.TestEngine")
}
tasks.register<Copy>("spi-test") {
    from("src/main/resources/spi-template-test")
    into("$buildDir/resources/main/META-INF/services")
    rename("spi-template-test", "org.junit.platform.engine.TestEngine")
}
val compileKotlin: org.jetbrains.kotlin.gradle.tasks.KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()

tasks.named("test") {
    dependsOn("spi-test")
}
tasks.named("jar") {
    dependsOn("spi-deploy")
}
