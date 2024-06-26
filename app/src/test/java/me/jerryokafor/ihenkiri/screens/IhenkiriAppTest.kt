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
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import me.jerryokafor.feature.movies.navigation.Movies
import me.jerryokafor.ihenkiri.feature.auth.navigation.LANDING_SCREEN_TEST_TAG
import me.jerryokafor.ihenkiri.feature.people.navigation.People
import me.jerryokafor.ihenkiri.feature.settings.navigation.Settings
import me.jerryokafor.ihenkiri.feature.tvshows.navigation.TvShows
import me.jerryokafor.ihenkiri.navigation.BOTTOM_NAV_BAR_TEST_TAG
import me.jerryokafor.ihenkiri.ui.IhenkiriApp
import me.jerryokafor.ihenkiri.ui.MAIN_CONTENT_TEST_TAG
import me.jerryokafor.uitesthiltmanifest.HiltComponentActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(
    application = HiltTestApplication::class,
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
    qualifiers = "xlarge",
)
@HiltAndroidTest
class IhenkiriAppTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

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
                navController = TestNavHostController(LocalContext.current).apply {
                    navigatorProvider.addNavigator(ComposeNavigator())
                }
                IhenkiriApp(navController = navController)
            }

            waitUntilNodeCount(hasTestTag(BOTTOM_NAV_BAR_TEST_TAG), count = 1, timeoutMillis = 400)

            // Verify /movies
            onNode(isBottomNavItemWithText("Movies")).assertHasClickAction()
                .assertIsDisplayed()
                .performClick()
            val moviesRoute = navController.currentBackStackEntry?.destination?.route
            assertThat(moviesRoute).isEqualTo(Movies::class.qualifiedName)
            onNode(isBottomNavItemWithText("Movies")).assertIsSelected()

            // Verify /tv-shows
            onNode(isBottomNavItemWithText("TV Shows")).assertHasClickAction()
                .assertIsDisplayed()
                .performClick()
            val tvShowsRoute = navController.currentBackStackEntry?.destination?.route
            assertThat(tvShowsRoute).isEqualTo(TvShows::class.qualifiedName)
            onNode(isBottomNavItemWithText("TV Shows")).assertIsSelected()

            // Verify route: /people
            onNode(isBottomNavItemWithText("People")).assertHasClickAction()
                .assertIsDisplayed()
                .performClick()
            val peopleRoute = navController.currentBackStackEntry?.destination?.route
            assertThat(peopleRoute).isEqualTo(People::class.qualifiedName)
            onNode(isBottomNavItemWithText("People")).assertIsSelected()

            // Verify route: /more
            onNode(isBottomNavItemWithText("Settings")).assertHasClickAction()
                .assertIsDisplayed()
                .performClick()
            val moreRtoue = navController.currentBackStackEntry?.destination?.route
            assertThat(moreRtoue).isEqualTo(Settings::class.qualifiedName)
            onNode(isBottomNavItemWithText("Settings")).assertIsSelected()
        }
    }

    @Test
    fun ihenkiriApp_verifyHomeDestination() {
        with(composeTestRule) {
            setContent {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())

                IhenkiriApp(navController = navController)
            }

            onNodeWithTag(LANDING_SCREEN_TEST_TAG).assertDoesNotExist()
            onNodeWithTag(MAIN_CONTENT_TEST_TAG).assertIsDisplayed()
            val route = navController.currentBackStackEntry?.destination?.route
            assertThat(route).isEqualTo(Movies::class.qualifiedName)
        }
    }
}

private fun isBottomNavItemWithText(text: String): SemanticsMatcher =
    hasText(text) and isSelectable() and hasClickAction()
