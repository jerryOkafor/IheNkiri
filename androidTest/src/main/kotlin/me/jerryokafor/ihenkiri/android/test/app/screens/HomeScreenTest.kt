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
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import me.jerryokafor.ihenkiri.ui.navigation.BOTTOM_NAV_BAR_TEST_TAG
import me.jerryokafor.ihenkiri.ui.screens.HomeScreen
import me.jerryokafor.uitesthiltmanifest.HiltComponentActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
@HiltAndroidTest
class HomeScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setUp() {
        hiltRule.inject()

        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(
                ComposeNavigator(),
            )
            HomeScreen(navController = navController)
        }
    }

    @Test
    fun homeScreen_verifyNavMenus() {
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(BOTTOM_NAV_BAR_TEST_TAG))

        composeTestRule.onNode(isBottomNavItemWithText("Movies")).assertHasClickAction()
            .assertIsDisplayed()
        composeTestRule.onNode(isBottomNavItemWithText("TV Shows")).assertHasClickAction()
            .assertIsDisplayed()
        composeTestRule.onNode(isBottomNavItemWithText("People")).assertHasClickAction()
            .assertIsDisplayed()
        composeTestRule.onNode(isBottomNavItemWithText("More")).assertHasClickAction()
            .assertIsDisplayed()
    }

    @Test
    fun homeScreen_verifyMoviesNavMenuClick() {
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(BOTTOM_NAV_BAR_TEST_TAG))
        composeTestRule.onNode(isBottomNavItemWithText("Movies"))
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        assertThat(route).isEqualTo("/movies")
        composeTestRule.onNode(isBottomNavItemWithText("Movies")).assertIsSelected()
    }

    @Test
    fun homeScreen_verifyTvShowsNavMenuClick() {
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(BOTTOM_NAV_BAR_TEST_TAG))
        composeTestRule.onNode(isBottomNavItemWithText("TV Shows"))
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        assertThat(route).isEqualTo("/tv-shows")
        composeTestRule.onNode(isBottomNavItemWithText("TV Shows")).assertIsSelected()
    }

    @Test
    fun homeScreen_verifyPeopleNavMenuClick() {
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(BOTTOM_NAV_BAR_TEST_TAG))
        composeTestRule.onNode(isBottomNavItemWithText("People"))
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        assertThat(route).isEqualTo("/people")
        composeTestRule.onNode(isBottomNavItemWithText("People")).assertIsSelected()
    }

    @Test
    fun homeScreen_verifyMoreNavMenuClick() {
        composeTestRule.waitUntilExactlyOneExists(hasTestTag(BOTTOM_NAV_BAR_TEST_TAG))
        composeTestRule.onNode(isBottomNavItemWithText("More"))
            .assertIsDisplayed()
            .performClick()
        val route = navController.currentBackStackEntry?.destination?.route
        assertThat(route).isEqualTo("/more")
        composeTestRule.onNode(isBottomNavItemWithText("More")).assertIsSelected()
    }
}

private fun isBottomNavItemWithText(text: String): SemanticsMatcher =
    hasText(text) and isSelectable() and hasClickAction()
