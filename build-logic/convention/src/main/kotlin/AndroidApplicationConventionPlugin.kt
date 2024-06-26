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

import com.android.build.api.dsl.ApplicationExtension
import me.jerryokafor.ihenkiri.Config
import me.jerryokafor.ihenkiri.androidTestImplementation
import me.jerryokafor.ihenkiri.configureKotlinAndroid
import me.jerryokafor.ihenkiri.libs
import me.jerryokafor.ihenkiri.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(target.pluginManager) {
                apply("com.android.application")
                apply("kotlin-android")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)

                defaultConfig.targetSdk = Config.targetSdkVersion

                buildTypes {
                    getByName("release") {
                        // disable coverage report on release build
                        enableUnitTestCoverage = false
                        enableAndroidTestCoverage = false
                    }
                }

                lint {
                    baseline = file("lint-baseline.xml")
//                    quiet = true
                    abortOnError = false // fix your lint issue
                    ignoreWarnings = true
                    checkDependencies = true
                }

                packaging {
                    resources.excludes += "DebugProbesKt.bin"
                }

                target.tasks.register("versionCode") {
                    println(defaultConfig.versionCode)
                }

                target.tasks.register("versionName") {
                    println(defaultConfig.versionName)
                }
            }

            configurations.configureEach {
                resolutionStrategy {
                    force(libs.findLibrary("junit4").get())
                    // Temporary workaround for https://issuetracker.google.com/174733673
                    force("org.objenesis:objenesis:2.6")
                }
            }
            dependencies {
                androidTestImplementation(kotlin("test"))
                testImplementation(kotlin("test"))
            }
        }
    }
}
