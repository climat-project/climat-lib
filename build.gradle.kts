import net.pwall.json.kotlin.codegen.gradle.JSONSchemaCodegen // only required if "configure" block included
import net.pwall.json.kotlin.codegen.gradle.JSONSchemaCodegenPlugin

val mavenUser: String by project
val mavenPassword: String by project

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/wilversings/kotlinx-cli")
        credentials {
            username = mavenUser
            password = mavenPassword
        }
    }
    // Needed for kotlinx-nodejs
    jcenter()
}

buildscript {
    dependencies {
        classpath("net.pwall.json:json-kotlin-gradle:0.85")
    }
}

plugins {
    kotlin("js") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}
apply<JSONSchemaCodegenPlugin>()

configure<JSONSchemaCodegen> {
    inputs {
        inputFile(file("src/main/resources/toolchain-schema.json"))
    }
    outputDir.set(file("src/main/kotlin"))
}

group = "me.marius"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5-develop-0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")

    testImplementation(kotlin("test"))
}

kotlin {
    js {
        binaries.library()
//      nodejs()
        browser {
            webpackTask {
                outputFileName = "main.js"
            }
            testTask {
                useKarma {
                    useSourceMapSupport()
                    useFirefoxHeadless()
                }
            }
        }
        useCommonJs()
    }
}
