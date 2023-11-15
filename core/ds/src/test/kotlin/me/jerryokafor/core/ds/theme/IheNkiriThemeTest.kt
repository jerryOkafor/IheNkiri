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

package me.jerryokafor.core.ds.theme

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Tests [IheNkiriTheme] for dark and light team and dynamic color
 */

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
)
class IheNkiriThemeTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_DarkColors_When_isDarkThemeTrue_and_isDynamicColorFalse() {
        composeTestRule.setContent {
            IheNkiriTheme(isDarkTheme = true, isDynamicColor = false) {
                val expectedColorScheme = DarkColors
                assertColorSchemesEqual(expectedColorScheme, MaterialTheme.colorScheme)

                Assert.assertEquals(
                    iheNKiriDarkColorScheme().extraColor,
                    LocalIheNkiriColorScheme.current.extraColor,
                )

                // assert spacing
                Assert.assertEquals(defaultSpacing(), LocalIheNkiriSpacing.current)

                // assert shape
                assertEqualsShapesEqual(defaultShape(), MaterialTheme.shapes)
            }
        }
    }

    @Test
    fun test_LightColors_When_isDarkThemeFalse_and_isDynamicColorFalse() {
        composeTestRule.setContent {
            IheNkiriTheme(isDarkTheme = false, isDynamicColor = false) {
                val expectedColorScheme = LightColors
                assertColorSchemesEqual(expectedColorScheme, MaterialTheme.colorScheme)

                Assert.assertEquals(
                    iheNKiriLightColorScheme().extraColor,
                    LocalIheNkiriColorScheme.current.extraColor,
                )

                // assert spacing
                Assert.assertEquals(defaultSpacing(), LocalIheNkiriSpacing.current)

                // assert shape
                assertEqualsShapesEqual(defaultShape(), MaterialTheme.shapes)
            }
        }
    }

    @Test
    fun test_DynamicDarkColors_When_isDarkThemeTrue_and_isDynamicColorTrue() {
        composeTestRule.setContent {
            IheNkiriTheme(isDarkTheme = true, isDynamicColor = true) {
                val expectedColorScheme = dynamicDarkColorSchemeWithFallback()
                assertColorSchemesEqual(expectedColorScheme, MaterialTheme.colorScheme)

                Assert.assertEquals(
                    iheNKiriLightColorScheme().extraColor,
                    LocalIheNkiriColorScheme.current.extraColor,
                )

                // assert spacing
                Assert.assertEquals(defaultSpacing(), LocalIheNkiriSpacing.current)

                // assert shape
                assertEqualsShapesEqual(defaultShape(), MaterialTheme.shapes)
            }
        }
    }

    @Test
    fun test_DynamicLightColors_When_isDarkThemeFalse_and_isDynamicColorTrue() {
        composeTestRule.setContent {
            IheNkiriTheme(isDarkTheme = false, isDynamicColor = true) {
                val expectedColorScheme = dynamicLightColorSchemeWithFallback()
                assertColorSchemesEqual(expectedColorScheme, MaterialTheme.colorScheme)

                Assert.assertEquals(
                    iheNKiriLightColorScheme().extraColor,
                    LocalIheNkiriColorScheme.current.extraColor,
                )

                // assert spacing
                Assert.assertEquals(defaultSpacing(), LocalIheNkiriSpacing.current)

                // assert shape
                assertEqualsShapesEqual(defaultShape(), MaterialTheme.shapes)
            }
        }
    }
}

private fun defaultShape(): IhenkiriShape = IhenkiriShape()

private fun defaultSpacing(): IheNkiriSpacing = IheNkiriSpacing()

@Composable
private fun dynamicLightColorSchemeWithFallback(): ColorScheme {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicLightColorScheme(LocalContext.current)
    } else {
        LightColors
    }
}

@Composable
private fun dynamicDarkColorSchemeWithFallback(): ColorScheme {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        dynamicDarkColorScheme(LocalContext.current)
    } else {
        DarkColors
    }
}

private fun assertColorSchemesEqual(
    expectedColorScheme: ColorScheme,
    actualColorScheme: ColorScheme,
) {
    Assert.assertEquals(expectedColorScheme.primary, actualColorScheme.primary)
    Assert.assertEquals(expectedColorScheme.onPrimary, actualColorScheme.onPrimary)
    Assert.assertEquals(expectedColorScheme.primaryContainer, actualColorScheme.primaryContainer)
    Assert.assertEquals(
        expectedColorScheme.onPrimaryContainer,
        actualColorScheme.onPrimaryContainer,
    )
    Assert.assertEquals(expectedColorScheme.secondary, actualColorScheme.secondary)
    Assert.assertEquals(expectedColorScheme.onSecondary, actualColorScheme.onSecondary)
    Assert.assertEquals(
        expectedColorScheme.secondaryContainer,
        actualColorScheme.secondaryContainer,
    )
    Assert.assertEquals(
        expectedColorScheme.onSecondaryContainer,
        actualColorScheme.onSecondaryContainer,
    )
    Assert.assertEquals(expectedColorScheme.tertiary, actualColorScheme.tertiary)
    Assert.assertEquals(expectedColorScheme.onTertiary, actualColorScheme.onTertiary)
    Assert.assertEquals(expectedColorScheme.tertiaryContainer, actualColorScheme.tertiaryContainer)
    Assert.assertEquals(
        expectedColorScheme.onTertiaryContainer,
        actualColorScheme.onTertiaryContainer,
    )
    Assert.assertEquals(expectedColorScheme.error, actualColorScheme.error)
    Assert.assertEquals(expectedColorScheme.onError, actualColorScheme.onError)
    Assert.assertEquals(expectedColorScheme.errorContainer, actualColorScheme.errorContainer)
    Assert.assertEquals(expectedColorScheme.onErrorContainer, actualColorScheme.onErrorContainer)
    Assert.assertEquals(expectedColorScheme.background, actualColorScheme.background)
    Assert.assertEquals(expectedColorScheme.onBackground, actualColorScheme.onBackground)
    Assert.assertEquals(expectedColorScheme.surface, actualColorScheme.surface)
    Assert.assertEquals(expectedColorScheme.onSurface, actualColorScheme.onSurface)
    Assert.assertEquals(expectedColorScheme.surfaceVariant, actualColorScheme.surfaceVariant)
    Assert.assertEquals(expectedColorScheme.onSurfaceVariant, actualColorScheme.onSurfaceVariant)
    Assert.assertEquals(expectedColorScheme.inverseSurface, actualColorScheme.inverseSurface)
    Assert.assertEquals(expectedColorScheme.inverseOnSurface, actualColorScheme.inverseOnSurface)
    Assert.assertEquals(expectedColorScheme.outline, actualColorScheme.outline)
}

private fun assertEqualsShapesEqual(
    defaultShape: IhenkiriShape,
    shapes: Shapes,
) {
    val toMaterialShapes = defaultShape.toMaterialShapes()
    Assert.assertEquals(toMaterialShapes.small, shapes.small)
    Assert.assertEquals(toMaterialShapes.medium, shapes.medium)
    Assert.assertEquals(toMaterialShapes.large, shapes.large)
}
