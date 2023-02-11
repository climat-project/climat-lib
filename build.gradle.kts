val githubUser: String by project
val githubPassword: String by project
val npmToken: String by project

allprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")

        maven {
            url = uri("https://maven.pkg.github.com/wilversings/kotlinx-cli")
            credentials {
                username = githubUser
                password = githubPassword
            }
        }
    }
}

buildscript {
    repositories {
        maven("https://jitpack.io")
        mavenCentral()
    }
    dependencies {
        classpath("com.strumenta.antlr-kotlin:antlr-kotlin-gradle-plugin:b5135079b8")
    }
}

plugins {
    kotlin("multiplatform") version "1.8.0"
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
    id("dev.petuska.npm.publish") version "3.2.0"
    id("org.barfuin.gradle.taskinfo") version "1.0.5"
}

kotlin {
    sourceSets {
        val commonAntlr by creating {
            kotlin.srcDir("build/generated-src/commonAntlr/kotlin")
            dependencies {
                api(kotlin("stdlib-common"))
                api("com.strumenta.antlr-kotlin:antlr-kotlin-runtime:b5135079b8")
            }
        }

        val commonMain by getting {
            dependsOn(commonAntlr)
            kotlin.srcDir("src/main/kotlin")
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5-develop-0")
            }
        }

        val commonTest by getting {
            kotlin.srcDir("src/test/kotlin")
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }

    js(IR) {
        binaries.library()
        browser {
            testTask {
                useMocha()
            }
            webpackTask {
                outputFileName = "main.js"
                output.libraryTarget = "commonjs2"
            }
        }
        useCommonJs()
    }
    jvm {
    }
}

ktlint.filter {
    exclude { it.file.path.contains("generated") }
}

npmPublish {
    registries {
        npmjs {
            authToken.set(npmToken)
        }
    }
}

tasks.register<com.strumenta.antlrkotlin.gradleplugin.AntlrKotlinTask>("generateKotlinCommonGrammarSource") {
    antlrClasspath = configurations.detachedConfiguration(
        project.dependencies.create("com.strumenta.antlr-kotlin:antlr-kotlin-target:b5135079b8")
    )
    maxHeapSize = "64m"
    packageName = "climat.lang"
    arguments = listOf("-no-visitor", "-no-listener")
    source = project.objects
        .sourceDirectorySet("antlr", "antlr")
        .srcDir("src/antlr").apply {
            include("**/*.g4")
        }
    outputDirectory = File("build/generated-src/commonAntlr/kotlin")

    dependsOn("compileCommonAntlrKotlinMetadata")
}

// Doesn't work: https://github.com/gradle/gradle/issues/9331
// tasks.getByName("compileCommonMainKotlinMetadata").dependsOn("generateKotlinCommonGrammarSource")

tasks.getByName("compileKotlinJvm").dependsOn("generateKotlinCommonGrammarSource")
tasks.getByName("compileKotlinJs").dependsOn("generateKotlinCommonGrammarSource")
