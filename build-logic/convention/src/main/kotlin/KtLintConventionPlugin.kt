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

import me.jerryokafor.ihenkiri.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.attributes.Bundling
import org.gradle.api.tasks.JavaExec
import org.gradle.kotlin.dsl.creating
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.named
import org.gradle.language.base.plugins.LifecycleBasePlugin

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

class KtLintConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val ktlintVersion = libs.findVersion("ktlint").get().toString()
            val ktlintConfig by configurations.creating

            dependencies {
                ktlintConfig("com.pinterest:ktlint:$ktlintVersion") {
                    attributes {
                        attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
                    }
                }
                // ktlintConfig(project(":custom-ktlint-ruleset")) // in case of custom ruleset
            }

            val outputDir = "${project.buildDir}/reports/ktlint/"
            val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

            @Suppress("UnusedPrivateMember")
            val ktlint by tasks.creating(JavaExec::class) {
                inputs.files(inputFiles)
                outputs.dir(outputDir)

                group = LifecycleBasePlugin.VERIFICATION_GROUP
                description = "Check Kotlin code style."
                classpath = ktlintConfig
                mainClass.set("com.pinterest.ktlint.Main")
                args(
                    "**/src/**/*.kt",
                    "**.kts",
                    "!**/build/**",
                )
            }

            @Suppress("UnusedPrivateMember")
            val ktlintFormat by tasks.creating(JavaExec::class) {
                inputs.files(inputFiles)
                outputs.dir(outputDir)

                group = LifecycleBasePlugin.VERIFICATION_GROUP
                description = "Fix Kotlin code style deviations."
                classpath = ktlintConfig
                mainClass.set("com.pinterest.ktlint.Main")
                jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
                args(
                    "-F",
                    "**/src/**/*.kt",
                    "**.kts",
                    "!**/build/**",
                )
            }

            @Suppress("UnusedPrivateMember")
            val ktlintAndroidStudio by tasks.creating(JavaExec::class) {
                description = "Setup Android Studio to use the same code style as ktlint."
                classpath = ktlintConfig
                mainClass.set("com.pinterest.ktlint.Main")
                args = listOf("--android", "applyToIDEAProject", "-y")
            }
        }
    }
}
