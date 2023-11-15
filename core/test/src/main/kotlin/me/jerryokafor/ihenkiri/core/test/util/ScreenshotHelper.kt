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

package me.jerryokafor.ihenkiri.core.test.util

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import com.google.accompanist.testharness.TestHarness
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import org.robolectric.RuntimeEnvironment

val DefaultRoborazziOptions =
    RoborazziOptions(
        compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0f),
        recordOptions = RoborazziOptions.RecordOptions(resizeScale = 0.5),
    )

enum class DefaultTestDevices(val description: String, val spec: String) {
    PHONE(
        description = "phone",
        spec = "spec:shape=Normal,width=640,height=360,unit=dp,dpi=480",
    ),
    FOLDABLE(
        description = "foldable",
        spec = "spec:shape=Normal,width=673,height=841,unit=dp,dpi=480",
    ),
    TABLET(
        description = "tablet",
        spec = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480",
    ),
}

fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureMultiDevice(
    screenshotName: String,
    body: @Composable () -> Unit,
) {
    DefaultTestDevices.values().forEach {
        this.captureForDevice(it.description, it.spec, screenshotName, body = body)
    }
}

@Suppress("LongParameterList")
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureForDevice(
    deviceName: String,
    deviceSpec: String,
    screenshotName: String,
    roborazziOptions: RoborazziOptions = DefaultRoborazziOptions,
    darkMode: Boolean = false,
    body: @Composable () -> Unit,
) {
    val (width, height, dpi) = extractSpecs(deviceSpec)

    // Set qualifiers from specs
    RuntimeEnvironment.setQualifiers("w${width}dp-h${height}dp-${dpi}dpi")

    this.activity.setContent {
        CompositionLocalProvider(
            LocalInspectionMode provides true,
        ) {
            TestHarness(darkMode = darkMode) {
                body()
            }
        }
    }
    this.onRoot().captureRoboImage(
        "src/test/screenshots/${screenshotName}_$deviceName.png",
        roborazziOptions = roborazziOptions,
    )
}

/**
 * Takes six screenshots combining light/dark and default/Android themes and whether dynamic color
 * is enabled.
 */
fun <A : ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>.captureMultiTheme(
    name: String,
    overrideFileName: String? = null,
    shouldCompareDarkMode: Boolean = true,
    shouldCompareDynamicColor: Boolean = true,
    content: @Composable (desc: String) -> Unit,
) {
    val darkModeValues = if (shouldCompareDarkMode) listOf(true, false) else listOf(false)
    val dynamicThemingValues = if (shouldCompareDynamicColor) listOf(true, false) else listOf(false)

    var darkMode by mutableStateOf(true)
    var dynamicTheming by mutableStateOf(false)

    this.setContent {
        CompositionLocalProvider(
            LocalInspectionMode provides true,
        ) {
            IheNkiriTheme(
                isDarkTheme = darkMode,
                isDynamicColor = dynamicTheming,
            ) {
                // Keying is necessary in some cases (e.g. animations)
                key(darkMode, dynamicTheming) {
                    val description =
                        generateDescription(
                            shouldCompareDarkMode,
                            darkMode,
                            shouldCompareDynamicColor,
                            dynamicTheming,
                        )
                    content(description)
                }
            }
        }
    }

    // Create permutations
    darkModeValues.forEach { isDarkMode ->
        darkMode = isDarkMode
        val darkModeDesc = if (isDarkMode) "dark" else "light"
        dynamicThemingValues.forEach dynamicTheme@{ isDynamicTheming ->

            dynamicTheming = isDynamicTheming
            val dynamicThemingDesc = if (isDynamicTheming) "dynamic" else "notDynamic"

            val filename = overrideFileName ?: name

            this.onRoot().captureRoboImage(
                "src/test/screenshots/" + "$name/$filename" + "_$darkModeDesc" + "_$dynamicThemingDesc" + ".png",
                roborazziOptions = DefaultRoborazziOptions,
            )
        }
    }
}

@Composable
private fun generateDescription(
    shouldCompareDarkMode: Boolean,
    darkMode: Boolean,
    shouldCompareDynamicColor: Boolean,
    dynamicTheming: Boolean,
): String {
    val description =
        "" +
            if (shouldCompareDarkMode) {
                if (darkMode) "Dark" else "Light"
            } else {
                ""
            } +
            if (shouldCompareDynamicColor) {
                if (dynamicTheming) " Dynamic" else ""
            } else {
                ""
            }

    return description.trim()
}

/**
 * Extracts some properties from the spec string. Note that this function is not exhaustive.
 */
private fun extractSpecs(deviceSpec: String): TestDeviceSpecs {
    val specs =
        deviceSpec.substringAfter("spec:").split(",").map { it.split("=") }
            .associate { it[0] to it[1] }
    val width = specs["width"]?.toInt() ?: 640
    val height = specs["height"]?.toInt() ?: 480
    val dpi = specs["dpi"]?.toInt() ?: 480
    return TestDeviceSpecs(width, height, dpi)
}

data class TestDeviceSpecs(val width: Int, val height: Int, val dpi: Int)
