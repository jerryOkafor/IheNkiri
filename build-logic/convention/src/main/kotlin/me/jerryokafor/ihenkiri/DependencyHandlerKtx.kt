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

package me.jerryokafor.ihenkiri

import org.gradle.api.artifacts.dsl.DependencyHandler

internal fun DependencyHandler.implementation(provider: Any) {
    add("implementation", provider)
}

internal fun DependencyHandler.debugImplementation(provider: Any) {
    add("debugImplementation", provider)
}

internal fun DependencyHandler.testImplementation(provider: Any) {
    add("testImplementation", provider)
}

internal fun DependencyHandler.androidTestImplementation(provider: Any) {
    add("androidTestImplementation", provider)
}

internal fun DependencyHandler.detekt(provider: Any) {
    add("detektPlugins", provider)
}

internal fun DependencyHandler.kapt(provider: Any) {
    add("kapt", provider)
}

internal fun DependencyHandler.ksp(provider: Any) {
    add("ksp", provider)
}

internal fun DependencyHandler.kaptTest(provider: Any) {
    add("kaptTest", provider)
}

internal fun DependencyHandler.kaptAndroidTest(provider: Any) {
    add("kaptAndroidTest", provider)
}
