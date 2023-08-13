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

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.testing.jacoco.plugins.JacocoPluginExtension
import org.gradle.testing.jacoco.plugins.JacocoTaskExtension
import org.gradle.testing.jacoco.tasks.JacocoReport
import java.util.Locale

private val coverageExclusions = listOf(
    // Android
    "**/R.class",
    "**/R\$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/databinding/*Binding.*",
    "**/R.class",
    "**/R$*.class",
    "**/BuildConfig.*",
    "**/Manifest*.*",
    "**/*Test*.*",
    "android/**/*.*",

    // kotlin
    "**/*MapperImpl*.*",
    "**/*\$ViewInjector*.*",
    "**/*\$ViewBinder*.*",
    "**/BuildConfig.*",
    "**/*Component*.*",
    "**/*BR*.*",
    "**/Manifest*.*",
    "**/*\$Lambda$*.*",
    "**/*Companion*.*",
    "**/*Module*.*",
    "**/*Dagger*.*",
    "**/*Hilt*.*",
    "**/*MembersInjector*.*",
    "**/*_MembersInjector.class",
    "**/*_Factory*.*",
    "**/*_Provide*Factory*.*",
    "**/*Extensions*.*",
)

private fun String.capitalize() = replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}

internal fun Project.configureJacoco(
    commonExtension: CommonExtension<*, *, *, *, *>,
    androidComponentsExtension: AndroidComponentsExtension<*, *, *>,
) {
    // set tool version
    configure<JacocoPluginExtension> {
        toolVersion = libs.findVersion("jacoco").get().toString()
    }

    // parent tasks that depends on the sub tasks
    val jacocoTestCoverageReport = tasks.create("jacocoTestCoverageReport") {
        group = "Reporting"
        description = "Generate Jacoco coverage reports for all variants after unit test"
    }
//    val jacocoTestCoverageVerification = tasks.create("jacocoTestCoverageVerification")

    commonExtension.apply {
        buildTypes {
            getByName("debug") {
                // allow coverage report only on debug build types
                enableUnitTestCoverage = true
                enableAndroidTestCoverage = true
            }

            getByName("release") {
                // disable coverage report on release build
                enableUnitTestCoverage = false
                enableAndroidTestCoverage = false
            }
        }
    }

    androidComponentsExtension.onVariants { variant ->
        // set up for all variants
        // original test task name for yhe given variant
        val testTaskName = "test${variant.name.capitalize()}UnitTest"

        // register variants report task
        val coverageTaskName = "jacoco${testTaskName.capitalize()}Coverage"

        @Suppress("UnusedPrivateMember")
        val coverageTask =
            tasks.register(coverageTaskName, JacocoReport::class) {
                group = "Reporting"
                description =
                    "Generate Jacoco coverage reports for the ${variant.name.capitalize()} build."

                // run this task after the testTask has finished running
                dependsOn(testTaskName)

                // configure output formats
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                }

                classDirectories.setFrom(
                    fileTree("$buildDir/tmp/kotlin-classes/${variant.name}") {
                        exclude(coverageExclusions)
                    },
                )

                // configure sources
                sourceDirectories.setFrom(
                    files(
                        "$projectDir/src/main/java",
                        "$projectDir/src/${variant.name}/java",
                        "$projectDir/src/main/kotlin",
                        "$projectDir/src/${variant.name}/kotlin",
                    ),
                )

                // set outputs
                executionData.setFrom(
                    file(
                        "$buildDir/outputs/unit_test_code_coverage/${variant.name}UnitTest/$testTaskName.exec",
                    ),
                )
            }

        // add unit test verification for all variant
//        val verificationTask = tasks.register(
//            "jacoco${testTaskName.capitalize()}CoverageVerification",
//            JacocoCoverageVerification::class.java,
//        ) {
//            group = "Reporting"
//            description = "Verifies Jacoco coverage for the ${variant.name.capitalize()} build."
//
//            // run this task after report task has finished running
//            dependsOn(coverageTaskName)
//
//            violationRules {
//                rule {
//                    limit {
//                        minimum = 0.3.toBigDecimal()
//                    }
//                }
//                rule {
//                    element = "BUNDLE"
//                    limit {
//                        counter = "LINE"
//                        value = "COVEREDRATIO"
//                        minimum = 0.3.toBigDecimal()
//                    }
//                }
//            }
//
//            classDirectories.setFrom(
//                fileTree("$buildDir/tmp/kotlin-classes/${variant.name}") {
//                    exclude(coverageExclusions)
//                },
//            )
//
//            // configure sources
//            sourceDirectories.setFrom(
//                files(
//                    "$projectDir/src/main/java",
//                    "$projectDir/src/${variant.name}/java",
//                    "$projectDir/src/main/kotlin",
//                    "$projectDir/src/${variant.name}/kotlin",
//                ),
//            )
//
//            // set outputs
//            executionData.setFrom(file("$buildDir/outputs/jacoco/$testTaskName.exec"))
//        }

        jacocoTestCoverageReport.dependsOn(coverageTask)
//        jacocoTestCoverageVerification.dependsOn(verificationTask)
    }

    tasks.withType<Test>().configureEach {
        configure<JacocoTaskExtension> {
            // Required for JaCoCo + Robolectric
            // https://github.com/robolectric/robolectric/issues/2230
            @Suppress("ForbiddenComment")
            // TODO: Consider removing if not we don't add Robolectric
            isIncludeNoLocationClasses = true

            // Required for JDK 11 with the above
            // https://github.com/gradle/gradle/issues/5184#issuecomment-391982009
            excludes = listOf("jdk.internal.*")
        }
    }
}
