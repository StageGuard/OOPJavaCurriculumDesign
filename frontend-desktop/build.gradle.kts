import org.gradle.jvm.tasks.Jar
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.PrintWriter
import java.io.StringWriter
import java.util.spi.ToolProvider

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "0.4.0"
    kotlin("plugin.serialization") version "1.5.0"
}


repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.2")
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("com.arkivanov.decompose:decompose:0.2.5")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:0.2.5")
    implementation("io.ktor:ktor-client-core:1.6.0")
    implementation("io.ktor:ktor-client-okhttp:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
    kotlinOptions.freeCompilerArgs.apply {
        plus("-Xopt-in=kotlin.RequiresOptIn")
        plus("--add-modules")
        plus("java.instrument")
        plus("--add-modules")
        plus("jdk.unsupported")
    }
}

compose.desktop {
    application {
        mainClass = "me.stageguard.oopcd.frontend.desktop.DesktopMainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Dmg)
            packageName = "OOPCDFrontendDesktop"
            packageVersion = "1.0.0"
        }
    }
}

val printModuleDeps by tasks.creating {
    doLast {
        val uberJar = tasks.named("packageUberJarForCurrentOS", Jar::class)
        val jarFile = uberJar.get().archiveFile.get().asFile

        val jdeps = ToolProvider.findFirst("jdeps").orElseGet { error("Can't find jdeps tool in JDK") }
        val out = StringWriter()
        val pw = PrintWriter(out)
        jdeps.run(pw, pw, "--print-module-deps", "--ignore-missing-deps", jarFile.absolutePath)

        val modules = out.toString()
        println(modules)
        // compose.desktop.application.nativeDistributions.modules.addAll(modules.split(","))
    }
    dependsOn("packageUberJarForCurrentOS")
}