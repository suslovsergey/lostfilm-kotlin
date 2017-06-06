import groovy.lang.GroovyObject

import org.jfrog.gradle.plugin.artifactory.dsl.PublisherConfig
import org.jfrog.gradle.plugin.artifactory.dsl.ResolverConfig
import org.jfrog.gradle.plugin.artifactory.task.ArtifactoryTask
import org.gradle.api.tasks.SourceSet
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.HasConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
val kotlinVersion = "1.1.2-2"
val jacksonVersion = "2.8.7"
val shadowVersion = "2.0.0"
val junitVersion = "4.11"


buildscript {
    val kotlinVersion = "1.1.2-2"
    val jacksonVersion = "2.8.7"
    val shadowVersion = "2.0.0"
    val junitVersion = "4.11"

    repositories {
        maven { url = uri("https://jitpack.io") }
        mavenCentral()
        jcenter()
    }


    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.github.jengelman.gradle.plugins:shadow:$shadowVersion")
    }
}

dependencies {
    val kotlinVersion = "1.1.2-2"
    val jacksonVersion = "2.8.7"
    val shadowVersion = "2.0.0"
    val junitVersion = "4.11"

    compile(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = jacksonVersion)
    compile(group = "com.fasterxml.jackson.module", name = "jackson-module-kotlin", version = jacksonVersion)
    compile(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-yaml", version = jacksonVersion)
    compile(group = "org.jetbrains.kotlin", name = "kotlin-reflect", version = kotlinVersion)
    compile(group = "org.jetbrains.kotlin", name = "kotlin-stdlib", version = kotlinVersion)
    compile(group = "me.tongfei", name = "progressbar", version = "0.5.5")
    compile(group = "org.jsoup", name = "jsoup", version = "1.10.2")
    compile(group = "ninja.sakib", name = "PultusORM", version = "v1.4")
    compile(group = "com.github.kittinunf.fuel", name = "fuel", version = "1.7.0")
}

repositories {
    maven { url = uri("https://jitpack.io") }
    mavenCentral()
    jcenter()
}



apply {
    plugin("kotlin")
    plugin("application")
    plugin("java")
    plugin("com.github.johnrengelman.shadow")
}

plugins {
    base
    id("com.jfrog.artifactory") version "4.1.1"
}



allprojects {
    group = "lostfilm"
    version = "0.0.1-SNAPSHOT"
}
val sourceSets = java.sourceSets

sourceSets {
    main {
        java.sourceDirs = files("src")
        kotlin.sourceDirs = files("src")
        resources.sourceDirs = files("src/res")
    }
    test {
        java.sourceDirs = files("test")
        kotlin.sourceDirs = files("test")
        resources.sourceDirs = files("test/res")
    }
}