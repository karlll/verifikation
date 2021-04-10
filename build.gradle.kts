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
    implementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")
    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
}