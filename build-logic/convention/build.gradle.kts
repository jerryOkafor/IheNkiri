/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 IheNkiri Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "me.jerryokafor.ihenkiri.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
}

tasks.check {
    dependsOn("ktlintCheck", "jacocoTestReport")
}

gradlePlugin {
    plugins {
        register("androidApp") {
            id = "me.jerryokafor.ihenkiri.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "me.jerryokafor.ihenkiri.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidFeature") {
            id = "me.jerryokafor.ihenkiri.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        register("androidTest") {
            id = "me.jerryokafor.ihenkiri.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }

        register("kotlinJVM") {
            id = "me.jerryokafor.ihenkiri.kotlin.jvm"
            implementationClass = "JvmLibraryConventionPlugin"
        }

        register("detekt") {
            id = "me.jerryokafor.ihenkiri.build.detekt"
            implementationClass = "DetektConventionPlugin"
        }

        register("ktlint") {
            id = "me.jerryokafor.ihenkiri.build.ktlint"
            implementationClass = "KtLintConventionPlugin"
        }

        register("androidApplicationJacoco") {
            id = "me.jerryokafor.ihenkiri.application.jacoco"
            implementationClass = "AndroidApplicationJacocoConventionPlugin"
        }

        register("androidLibraryJacoco") {
            id = "me.jerryokafor.ihenkiri.library.jacoco"
            implementationClass = "AndroidLibraryJacocoConventionPlugin"
        }

        register("androidHilt") {
            id = "me.jerryokafor.ihenkiri.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }

        register("androidNavigation") {
            id = "me.jerryokafor.ihenkiri.android.navigation"
            implementationClass = "AndroidNavigationConventionPlugin"
        }

        register("androidAppCompose") {
            id = "me.jerryokafor.ihenkiri.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "me.jerryokafor.ihenkiri.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidScreenshotTest") {
            id = "me.jerryokafor.ihenkiri.android.test.screenshot"
            implementationClass = "AndroidScreenshotConvention"
        }
    }
}
