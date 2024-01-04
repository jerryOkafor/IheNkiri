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

package me.jerryokafor.ihenkiri.feature.auth.screen

import android.os.Build
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertAll
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.isNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import me.jerryokafor.ihenkiri.feature.auth.ui.AuthScreen
import me.jerryokafor.ihenkiri.feature.auth.viewmodel.AuthUiState
import me.jerryokafor.ihenkiri.feature.auth.viewmodel.GuestSessionUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
    qualifiers = "xlarge",
)
class AuthScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var onContinueAsGuestClick = 0
    private var onSignInClick = 0

    @Test
    fun authScreen_initialized_authScreenIsShown() {
        composeTestRule.setContent {
            AuthScreen(
                authState = null,
                guestSessionUiState = null,
                onSignInClick = { onSignInClick++ },
            ) { onContinueAsGuestClick++ }
        }

        composeTestRule.onNodeWithText("Sign In")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithText("Continue as Guest")
            .assertIsDisplayed()
            .assertHasClickAction()
            .performClick()
        composeTestRule.onNodeWithText("Continue as Guest")
            .assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("TMDB logo")
            .assertIsDisplayed()

        assertEquals(onContinueAsGuestClick, 1)
        assertEquals(onSignInClick, 1)
    }

    @Test
    fun authScreen_isLoading_showButtonLoading() {
        composeTestRule.setContent {
            AuthScreen(
                authState = AuthUiState.Loading,
                guestSessionUiState = null,
                onSignInClick = { onSignInClick++ },
            ) { onContinueAsGuestClick++ }
        }

        // One progress indicator is shoeing
        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertExists()
            .assertIsDisplayed()

        // two buttons is disabled
        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(2)
            .assertAll(isNotEnabled())
    }

    @Test
    fun authScreen_isGuestSessionLoading_showButtonLoading() {
        composeTestRule.setContent {
            AuthScreen(
                authState = null,
                guestSessionUiState = GuestSessionUiState.Loading,
                onSignInClick = { onSignInClick++ },
            ) { onContinueAsGuestClick++ }
        }

        // One progress indicator is shoeing
        composeTestRule.onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
            .assertExists()
            .assertIsDisplayed()

        // two buttons is disabled
        composeTestRule.onAllNodes(hasClickAction()).assertCountEquals(2)
            .assertAll(isNotEnabled())
    }
}
