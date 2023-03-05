val npmToken: String by project

allprojects {
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
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

//    Native example.
//    linuxX64 {
//        binaries { sharedLib{} }
//    }
//    mingwX64 {
//        binaries { sharedLib{} }
//    }
}

ktlint.filter {
    exclude { it.file.path.contains("generated") }
}

npmPublish {
    readme.set(File("./README.md"))
    packages.getByName("js") {
        files.from.add("./LICENSE.md")
        packageJson {
            license.set("LGPL-2.1-only")
            description.set("Library used by CLiMAT tool")
            repository
            repository {
                type.set("git")
                url.set("https://github.com/climat-project/climat-lib")
            }
            homepage.set("https://climat-project.github.io")
            author {
                name.set("Marius Aiordăchioaei")
            }
            keywords.addAll("cli", "macros")
        }
    }
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
