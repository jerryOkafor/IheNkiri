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

package me.jerryokafor.ihenkiri.screens

import android.os.Build
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import me.jerryokafor.ihenkiri.feature.auth.navigation.LANDING_SCREEN_TEST_TAG
import me.jerryokafor.ihenkiri.ui.BOTTOM_NAV_BAR_TEST_TAG
import me.jerryokafor.ihenkiri.ui.GraphRoute
import me.jerryokafor.ihenkiri.ui.IhenkiriApp
import me.jerryokafor.ihenkiri.ui.MAIN_CONTENT_TEST_TAG
import me.jerryokafor.uitesthiltmanifest.HiltComponentActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(
    application = HiltTestApplication::class,
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
)
@HiltAndroidTest
class IhenkiriAppTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    private var onContinueAsGuestClick = 0
    private var onSignInClick = 0
    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun bottomNavbar_verifyNavMenus() {
        with(composeTestRule) {
            setContent {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())

                IhenkiriApp(
                    navController = navController,
                    startDestination = GraphRoute.HOME,
                    onContinueAsGuestClick = { onContinueAsGuestClick++ },
                    onSignInClick = { onSignInClick++ },
                )
            }

            waitUntilNodeCount(hasTestTag(BOTTOM_NAV_BAR_TEST_TAG), 1)

            // Verify /movies
            onNode(isBottomNavItemWithText("Movies")).assertHasClickAction()
                .assertIsDisplayed()
                .performClick()
            val moviesRoute = navController.currentBackStackEntry?.destination?.route
            assertThat(moviesRoute).isEqualTo("/movies")
            onNode(isBottomNavItemWithText("Movies")).assertIsSelected()

            // Verify /tv-shows
            onNode(isBottomNavItemWithText("TV Shows")).assertHasClickAction()
                .assertIsDisplayed()
                .performClick()
            val tvShowsRoute = navController.currentBackStackEntry?.destination?.route
            assertThat(tvShowsRoute).isEqualTo("/tv-shows")
            onNode(isBottomNavItemWithText("TV Shows")).assertIsSelected()

            // Verify route: /people
            onNode(isBottomNavItemWithText("People")).assertHasClickAction()
                .assertIsDisplayed()
                .performClick()
            val peopleRoute = navController.currentBackStackEntry?.destination?.route
            assertThat(peopleRoute).isEqualTo("/people")
            onNode(isBottomNavItemWithText("People")).assertIsSelected()

            // Verify route: /more
            onNode(isBottomNavItemWithText("Settings")).assertHasClickAction()
                .assertIsDisplayed()
                .performClick()
            val moreRtoue = navController.currentBackStackEntry?.destination?.route
            assertThat(moreRtoue).isEqualTo("/settings")
            onNode(isBottomNavItemWithText("Settings")).assertIsSelected()
        }
    }

    @Test
    fun ihenkiriApp_verifyAuhStartDestination() {
        with(composeTestRule) {
            setContent {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())

                IhenkiriApp(
                    navController = navController,
                    startDestination = GraphRoute.AUTH,
                    onContinueAsGuestClick = { onContinueAsGuestClick++ },
                    onSignInClick = { onSignInClick++ },
                )
            }

            onNodeWithTag(LANDING_SCREEN_TEST_TAG).assertExists()
            val route = navController.currentBackStackEntry?.destination?.route
            assertThat(route).isEqualTo("welcome")
        }
    }

    @Test
    fun ihenkiriApp_verifyHomeDestination() {
        with(composeTestRule) {
            setContent {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())

                IhenkiriApp(
                    navController = navController,
                    startDestination = GraphRoute.HOME,
                    onContinueAsGuestClick = { onContinueAsGuestClick++ },
                    onSignInClick = { onSignInClick++ },
                )
            }

            onNodeWithTag(LANDING_SCREEN_TEST_TAG).assertDoesNotExist()
            onNodeWithTag(MAIN_CONTENT_TEST_TAG).assertIsDisplayed()
            val route = navController.currentBackStackEntry?.destination?.route
            assertThat(route).isEqualTo("/movies")
        }
    }
}

private fun isBottomNavItemWithText(text: String): SemanticsMatcher =
    hasText(text) and isSelectable() and hasClickAction()
