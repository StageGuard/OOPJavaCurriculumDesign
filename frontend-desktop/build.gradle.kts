import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "0.4.0"
}


repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
    // Decompose : Decompose
    implementation("com.arkivanov.decompose:decompose:0.2.5")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.2.5")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

compose.desktop {
    application {
        mainClass = "me.stageguard.oopcd.frontend.desktop.DesktopMainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "OOPCDFrontendDesktop"
            packageVersion = "1.0.0"
        }
    }
}