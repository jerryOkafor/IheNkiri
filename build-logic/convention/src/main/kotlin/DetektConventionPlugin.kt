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

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import me.jerryokafor.ihenkiri.detekt
import me.jerryokafor.ihenkiri.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.named
import org.gradle.kotlin.dsl.register

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

class DetektConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            val detektVersion = libs.findVersion("detekt").get().toString()
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            extensions.getByType<DetektExtension>().apply {
                toolVersion = detektVersion
                source = files("$rootDir/")
                parallel = true
                config = files("$rootDir/config/detekt/detekt-config.yml")
                buildUponDefaultConfig = true
                allRules = false
                baseline = file("$rootDir/config/detekt/detekt-baseline.xml")
                // dependencies {
                //     "detektPlugins"(libs.findLibrary("detekt-formatting").get())
                // }
            }

            tasks.named<Detekt>("detekt").configure {
                description = "Runs Detekt on the whole project at once."
                reports {
                    html.required.set(true)
                    html.outputLocation.set(
                        file("$rootDir/build/reports/detekt.html"),
                    )
                }
                parallel = true
                setSource(projectDir)
                include("**/*.kt", "**/*.kts")
                exclude("**/resources/**", "**/build/**")

                // exclude other custom generated files
            }

            tasks.register("detektProjectBaseline", DetektCreateBaselineTask::class) {
                description = "Overrides current baseline."
                buildUponDefaultConfig.set(true)
                ignoreFailures.set(true)
                parallel.set(true)
                setSource(files(rootDir))
                config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
                baseline.set(file("$rootDir/config/detekt/baseline.xml"))
                include("**/*.kt")
                include("**/*.kts")
                exclude("**/resources/**")
                exclude("**/build/**")
            }

            dependencies {
                detekt("io.gitlab.arturbosch.detekt:detekt-cli:$detektVersion")
            }
        }
    }
}
