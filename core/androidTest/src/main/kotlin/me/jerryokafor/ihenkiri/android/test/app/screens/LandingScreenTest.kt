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

package me.jerryokafor.ihenkiri.android.test.app.screens

import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.ihenkiri.ui.screens.LandingScreen
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class LandingScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private var onContinueAsGuestClick = 0
    private var onSignInClick = 0

    @Test
    fun testLandingScreen() {
        composeTestRule.setContent {
            IheNkiriTheme {
                LandingScreen(
                    onContinueAsGuestClick = { onContinueAsGuestClick++ },
                    onSignInClick = { onSignInClick++ },
                )
            }
        }

        composeTestRule.onNodeWithText("Sign In").assertIsDisplayed().assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithText("Continue as Guest").assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithText("Continue as Guest").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("TMDB logo").assertIsDisplayed()

        assertEquals(onContinueAsGuestClick, 1)
        assertEquals(onSignInClick, 1)
    }
}
