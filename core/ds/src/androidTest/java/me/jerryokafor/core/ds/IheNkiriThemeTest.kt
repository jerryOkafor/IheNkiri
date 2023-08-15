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

package me.jerryokafor.core.ds

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.junit4.createComposeRule
import me.jerryokafor.core.ds.theme.DarkColors
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

/**
 * Tests [IheNkiriTheme] for dark and light team and dynamic color
 */
class IheNkiriThemeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun test_DarkColors_When_isDarkThemeTrue_and_isDynamicColorFalse() {
        composeTestRule.setContent {
            IheNkiriTheme(isDarkTheme = true, isDynamicColor = false) {
                val expectedColorScheme = DarkColors
                assertColorSchemesEqual(expectedColorScheme, MaterialTheme.colorScheme)
            }
        }
    }
}

private fun assertColorSchemesEqual(
    expectedColorScheme: ColorScheme,
    actualColorScheme: ColorScheme,
) {
    assertEquals(expectedColorScheme.primary, actualColorScheme.primary)
    assertEquals(expectedColorScheme.onPrimary, actualColorScheme.onPrimary)
    assertEquals(expectedColorScheme.primaryContainer, actualColorScheme.primaryContainer)
    assertEquals(expectedColorScheme.onPrimaryContainer, actualColorScheme.onPrimaryContainer)
    assertEquals(expectedColorScheme.secondary, actualColorScheme.secondary)
    assertEquals(expectedColorScheme.onSecondary, actualColorScheme.onSecondary)
    assertEquals(expectedColorScheme.secondaryContainer, actualColorScheme.secondaryContainer)
    assertEquals(
        expectedColorScheme.onSecondaryContainer,
        actualColorScheme.onSecondaryContainer,
    )
    assertEquals(expectedColorScheme.tertiary, actualColorScheme.tertiary)
    assertEquals(expectedColorScheme.onTertiary, actualColorScheme.onTertiary)
    assertEquals(expectedColorScheme.tertiaryContainer, actualColorScheme.tertiaryContainer)
    assertEquals(expectedColorScheme.onTertiaryContainer, actualColorScheme.onTertiaryContainer)
    assertEquals(expectedColorScheme.error, actualColorScheme.error)
    assertEquals(expectedColorScheme.onError, actualColorScheme.onError)
    assertEquals(expectedColorScheme.errorContainer, actualColorScheme.errorContainer)
    assertEquals(expectedColorScheme.onErrorContainer, actualColorScheme.onErrorContainer)
    assertEquals(expectedColorScheme.background, actualColorScheme.background)
    assertEquals(expectedColorScheme.onBackground, actualColorScheme.onBackground)
    assertEquals(expectedColorScheme.surface, actualColorScheme.surface)
    assertEquals(expectedColorScheme.onSurface, actualColorScheme.onSurface)
    assertEquals(expectedColorScheme.surfaceVariant, actualColorScheme.surfaceVariant)
    assertEquals(expectedColorScheme.onSurfaceVariant, actualColorScheme.onSurfaceVariant)
    assertEquals(expectedColorScheme.inverseSurface, actualColorScheme.inverseSurface)
    assertEquals(expectedColorScheme.inverseOnSurface, actualColorScheme.inverseOnSurface)
    assertEquals(expectedColorScheme.outline, actualColorScheme.outline)
}
