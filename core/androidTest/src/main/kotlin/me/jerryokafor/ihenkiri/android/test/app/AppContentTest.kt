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

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import me.jerryokafor.ihenkiri.ui.AppContent
import me.jerryokafor.ihenkiri.ui.LANDING_SCREEN_TEST_TAG
import me.jerryokafor.ihenkiri.ui.MAIN_CONTENT_TEST_TAG
import me.jerryokafor.ihenkiri.ui.navigation.GraphRoute
import me.jerryokafor.uitesthiltmanifest.HiltComponentActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AppContentTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    private lateinit var navController: TestNavHostController

    private var onContinueAsGuestClick = 0
    private var onSignInClick = 0

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun setUpNavController(content: @Composable () -> Unit) {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator(),
            )

            // dump content
            content()
        }
    }

    @Test
    fun appContent_verifyAuhStartDestinatoin() {
        setUpNavController {
            AppContent(
                navHostController = navController,
                startDestination = GraphRoute.AUTH,
                isDarkTheme = false,
                isDynamicColor = false,
                onContinueAsGuestClick = { onContinueAsGuestClick++ },
                onSignInClick = { onSignInClick++ },
            )
        }

        composeTestRule.onNodeWithTag(LANDING_SCREEN_TEST_TAG).assertExists()
        composeTestRule.onNodeWithTag(MAIN_CONTENT_TEST_TAG).assertDoesNotExist()
        val route = navController.currentBackStackEntry?.destination?.route
        assertThat(route).isEqualTo("welcome")
    }

    @Test
    fun appContent_verifyHomeDestination() {
        setUpNavController {
            AppContent(
                navHostController = navController,
                startDestination = GraphRoute.HOME,
                isDarkTheme = false,
                isDynamicColor = false,
                onContinueAsGuestClick = { onContinueAsGuestClick++ },
                onSignInClick = { onSignInClick++ },
            )
        }
        composeTestRule.onNodeWithTag(LANDING_SCREEN_TEST_TAG).assertDoesNotExist()
        composeTestRule.onNodeWithTag(MAIN_CONTENT_TEST_TAG).assertIsDisplayed()

        val route = navController.currentBackStackEntry?.destination?.route
        assertThat(route).isEqualTo("home-graph")
    }
}
