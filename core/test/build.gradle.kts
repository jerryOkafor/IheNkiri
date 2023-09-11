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

plugins {
    id("me.jerryokafor.ihenkiri.android.library")
    id("me.jerryokafor.ihenkiri.android.hilt")
    id("me.jerryokafor.ihenkiri.android.library.compose")
}

android {
    namespace = "me.jerryokafor.ihenkiri.core.test"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.network)
    implementation(projects.core.ds)

    // retrofit
    api(libs.com.squareup.retrofit2)
    api(libs.com.google.code.gson)
    api(libs.com.squareup.retrofit2.converter.gson)
    api(libs.com.squareup.okhttp3.logging.interceptor)
    api(libs.com.squareup.okhttp3.mockwebserver)

    api(libs.junit4)
    api(libs.androidx.test.core)
    api(libs.androidx.test.core.ktx)
    api(libs.androidx.test.rules)
    api(libs.androidx.test.runner)

    api(libs.app.cash.turbine)
    api(libs.com.google.truth)
    api(libs.androidx.hilt.android.testing)
    api(libs.org.jetbrains.kotlinx.coroutines.test)
    api(libs.androidx.compose.ui.test.junit4)
    api(libs.androidx.compose.ui.test.manifest)

    api(libs.io.mockk.android)
    api(libs.io.mockk.agent)

    api(libs.org.robolectric)
    api(libs.org.robolectric.shadows)
    api(libs.io.github.takahirom.roborazzi)
    api(libs.com.google.accompanist.testharness)

    api(libs.androidx.test.ext.junit)
    api(libs.androidx.test.espresso.core)
}
