plugins {
    id("java")
    id("org.openjfx.javafxplugin") version "0.0.13"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    
    // Dipendenze esplicite per il Language Server dell'IDE
    implementation("org.openjfx:javafx-controls:17.0.6")
    implementation("org.openjfx:javafx-graphics:17.0.6")
    implementation("org.openjfx:javafx-base:17.0.6")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}