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

package me.jerryokafor.ihenkiri.android.test.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import me.jerryokafor.ihenkiri.ui.AppContent
import me.jerryokafor.ihenkiri.ui.LANDING_SCREEN_TEST_TAG
import me.jerryokafor.ihenkiri.ui.MAIN_CONTENT_TEST_TAG
import org.junit.Rule
import org.junit.Test

class AppContentTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var onContinueAsGuestClick = 0
    private var onSignInClick = 0

    @Test
    fun when_isLoggedInFalse_Expect_SignScreenToBeShown() {
        composeTestRule.setContent {
            AppContent(
                isLoggedIn = false,
                isDarkTheme = false,
                isDynamicColor = false,
                onContinueAsGuestClick = { onContinueAsGuestClick++ },
                onSignInClick = { onSignInClick++ },
            )
        }

        composeTestRule.onNodeWithTag(LANDING_SCREEN_TEST_TAG).assertExists()
        composeTestRule.onNodeWithTag(MAIN_CONTENT_TEST_TAG).assertDoesNotExist()
    }

    @Test
    fun when_isLoggedInTrue_Expect_MainContentToBeShown() {
        composeTestRule.setContent {
            AppContent(
                isLoggedIn = true,
                isDarkTheme = false,
                isDynamicColor = false,
                onContinueAsGuestClick = { onContinueAsGuestClick++ },
                onSignInClick = { onSignInClick++ },
            )
        }

        composeTestRule.onNodeWithTag(LANDING_SCREEN_TEST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(MAIN_CONTENT_TEST_TAG).assertIsDisplayed()
    }
}
