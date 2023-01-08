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
}

plugins {
    kotlin("multiplatform") version "1.7.21"
    kotlin("plugin.serialization") version "1.7.20"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

group = "me.marius"
version = "1.0-SNAPSHOT"

kotlin {
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5-develop-0")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            }
        }
        val commonTest by getting {
            kotlin.srcDir("src/test/kotlin")
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }

    js {
        binaries.library()
        browser {
            webpackTask {
                outputFileName = "main.js"
            }
        }
        useCommonJs()
    }
    jvm {
    }
}
