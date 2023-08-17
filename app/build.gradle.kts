/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 IheNkiri Project
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

import me.jerryokafor.ihenkiri.Config

plugins {
    id("me.jerryokafor.ihenkiri.android.application")
    id("me.jerryokafor.ihenkiri.android.application.compose")
    id("me.jerryokafor.ihenkiri.android.hilt")
    id("me.jerryokafor.ihenkiri.android.navigation")
    id("me.jerryokafor.ihenkiri.application.jacoco")
//    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

android {
    namespace = "me.jerryokafor.ihenkiri"

    defaultConfig {
        applicationId = "me.jerryokafor.ihenkiri"
        versionCode = Config.versionCode
        versionName = Config.versionName

        testInstrumentationRunner = "me.jerryokafor.ihenkiri.core.test.IheNkiriTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {}
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core:network"))
    implementation(project(":core:ds"))
    implementation(project(":core:ui"))
    implementation(project(":feature:movies"))

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.com.google.android.material)

    implementation(libs.androidx.browser)

    // retrofit
    implementation(libs.com.squareup.retrofit2)
    implementation(libs.com.google.code.gson)
    implementation(libs.com.squareup.retrofit2.converter.gson)
    implementation(libs.com.squareup.okhttp3.logging.interceptor)

    // compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.com.google.accompanist.systemuicontroller)

    testImplementation(project(":core:test"))
    testImplementation(libs.junit4)
    testImplementation(libs.io.mockk.android)
    testImplementation(libs.io.mockk.agent)

    androidTestImplementation(project(":core:test"))
    androidTestImplementation(libs.io.mockk.android)
    androidTestImplementation(libs.io.mockk.agent)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
