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

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.isInternal
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.data.injection.LocalStorageBinding
import me.jerryokafor.core.data.repository.LocalStorage
import me.jerryokafor.ihenkiri.core.test.rule.assertAreDisplayed
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.ui.BOTTOM_NAV_BAR_TEST_TAG
import me.jerryokafor.ihenkiri.ui.MainActivity
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
@Config(
    application = HiltTestApplication::class,
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
    qualifiers = "xlarge",
)
@UninstallModules(LocalStorageBinding::class)
@HiltAndroidTest
class MainActivityTest {
    @get:Rule(order = 0)
    val dispatcherRule = MainDispatcherRule()

    @get:Rule(order = 1)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 3)
    val intentsRule = IntentsRule()

    @BindValue
    @JvmField
    val localStorage = mockk<LocalStorage>(relaxed = true) {
        every { isLoggedIn() } returns flowOf(true)
    }

    @Before
    fun setUp() {
        ShadowLog.stream = System.out

        val resultData = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse("https://ihenkiri.jerryokafor.me/auth")
        }

        intending(not(isInternal())).respondWith(
            Instrumentation.ActivityResult(
                Activity.RESULT_OK,
                resultData,
            ),
        )

        hiltRule.inject()
    }

//    @Test
//    fun oauth_e2e() = runTest {
//        every { localStorage.isLoggedIn() } returns flowOf(false)
//
//        val scenario = launchActivity<MainActivity>()
//        scenario.moveToState(Lifecycle.State.CREATED)
//
//        scenario.onActivity {
//            with(composeTestRule) {
//                onNodeWithText("Continue as Guest").assertExists().assertIsDisplayed()
//                onNodeWithText("Sign In").assertExists().assertIsDisplayed().performClick()
//            }
//        }
//        val fakeRequest = createRequestTokenSuccessResponse()
//        val requestToken = fakeRequest.requestToken
//        val redirectTo = "https://ihenkiri.jerryokafor.me/auth"
//        val url = "https://www.themoviedb.org/auth/access?request_token=$requestToken&redirect_to=$redirectTo"
        // verify that the correct intent was sent
//        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(url)))

        // verify that the correct intent was sent using truth assertions
//        val receivedIntent: Intent = Iterables.getOnlyElement(Intents.getIntents())
//        assertThat(receivedIntent).hasData(Uri.parse(url))
//        assertThat(receivedIntent).hasAction(Intent.ACTION_VIEW)
//    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun e2e() = runTest {
        every { localStorage.isLoggedIn() } returns flowOf(true)
        val scenario = launchActivity<MainActivity>()
        scenario.moveToState(Lifecycle.State.CREATED)

        scenario.onActivity {
            with(composeTestRule) {
                waitUntilNodeCount(
                    hasTestTag(BOTTOM_NAV_BAR_TEST_TAG),
                    count = 1,
                    timeoutMillis = 400,
                )

                onNodeWithTag("movies_nav").assertExists()
                    .assertHasClickAction()
                    .assertIsSelected()
                    .performClick()
                onAllNodesWithText("Movies").assertCountEquals(2).assertAreDisplayed()

                onNodeWithTag("tv_shows_nav").assertExists()
                    .assertHasClickAction()
                    .performClick()
                    .assertIsSelected()
                onAllNodesWithText("TV Shows").assertCountEquals(2).assertAreDisplayed()

                onNodeWithTag("people_nav").assertExists()
                    .assertHasClickAction()
                    .performClick()
                    .assertIsSelected()
                onAllNodesWithText("People")
                    .assertCountEquals(2).assertAreDisplayed()

                onNodeWithTag("settings_nav")
                    .assertExists()
                    .assertHasClickAction()
                    .performClick()
                    .assertIsSelected()
                onAllNodesWithText("Settings")
                    .assertCountEquals(2)
                    .assertAreDisplayed()
            }
        }
    }
}
