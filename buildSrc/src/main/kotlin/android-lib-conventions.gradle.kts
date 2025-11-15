/*
 * Copyright (c) 2015-2023 Uli Bubenheimer. All rights reserved.
 */

import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode.NO_COMPATIBILITY
import org.jetbrains.kotlin.gradle.dsl.JvmTarget.Companion.fromTarget

plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
}

group = "org.bubenheimer"
version = "1.0-SNAPSHOT"

val libs = versionCatalogs.named("libs")

kotlin {
    jvmToolchain(libs.findVersion("java.toolchain").get().toString().toInt())

    explicitApi()

    compilerOptions {
        jvmTarget = fromTarget(libs.findVersion("java.source").get().toString())
        progressiveMode = true
        jvmDefault = NO_COMPATIBILITY
        verbose = false
        extraWarnings = true
        freeCompilerArgs.add("-Xconsistent-data-class-copy-visibility")
    }
}

android {
    libs.findVersion("android.sdk.compile").get().toString().let {
        /**
         * Matches: 36, 36.1, 36-ext18, 36ext18, 36.1-ext20, 36.1ext20
         */
        fun String.toAndroidVersion() =
            """\s*(?<major>\d+)(?:\.(?<minor>\d+))?(?:-?ext(?<ext>\d+))?\s*"""
                .toRegex()
                .matchEntire(this)
                ?.destructured
                ?.let { (major, minor, ext) ->
                    Triple(major.toInt(), minor.toIntOrNull(), ext.toIntOrNull())
                }

        it.toAndroidVersion()?.let { (major, minor, ext) ->
            compileSdk = major
            compileSdkMinor = minor
            compileSdkExtension = ext
        } ?: run { compileSdkPreview = it }
    }

    defaultConfig {
        minSdk = libs.findVersion("android.sdk.min").get().toString().toInt()
    }

    buildTypes {
        getByName("debug") {
        }
        getByName("release") {
            isShrinkResources = false
            isJniDebuggable = false
        }
    }

    compileOptions {
        JavaVersion.toVersion(libs.findVersion("java.source").get().toString()).let {
            sourceCompatibility = it
            targetCompatibility = it
        }
    }

    buildFeatures {
        buildConfig = false
    }

    publishing {
        multipleVariants {
            allVariants()
            withSourcesJar()
        }
    }

    lint {
        quiet = false
        abortOnError = false
        checkReleaseBuilds = true
        ignoreWarnings = false
        absolutePaths = true
        checkAllWarnings = true
        warningsAsErrors = true
        // if true, don't include source code lines in the error output
        noLines = false
        // if true, show all locations for an error, do not truncate lists, etc.
        showAll = true
        // whether lint should include full issue explanations in the text error output
        explainIssues = true
        textReport = false
        xmlReport = false
        htmlReport = true
        // optional path to HTML report (default will be lint-results.html in the builddir)
        //htmlOutput file("$reportsDir/lint-report.html")
        checkTestSources = true
        ignoreTestSources = false
        checkGeneratedSources = false
        checkDependencies = false
    }
}

publishing {
    publications {
        create<MavenPublication>(name = "library") {
            afterEvaluate {
                from(components["default"])
            }
        }
    }
}
