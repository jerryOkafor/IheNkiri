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

package me.jerryokafor.ihenkiri.feature.auth

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.rule.IntentsRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import me.jerryokafor.core.common.util.Constants
import me.jerryokafor.ihenkiri.feature.auth.ui.AuthScreen
import me.jerryokafor.uitesthiltmanifest.HiltComponentActivity
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
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
@HiltAndroidTest
class AuthSessionTest {
    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
     */
    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @get:Rule(order = 2)
    val intentsRule = IntentsRule()

    /**
     * Use the primary activity to initialize the app normally.
     */
    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    private var onCompleteLoginClick = 0

    @Before
    fun setUp() {
        ShadowLog.stream = System.out

        hiltRule.inject()
    }

    @Test
    fun testAuth2() {
        val resultData = Intent().apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse(Constants.AUTH_REDIRECT_URL)
        }

//        val uri = Uri.parse(Constants.TMDB_AUTH_ACCESS_URL).buildUpon()
//            .appendQueryParameter(
//                AuthParam.REQUEST_TOKEN,
//                createRequestTokenSuccessResponse().requestToken,
//            )
//            .appendQueryParameter(AuthParam.REDIRECT_TO, Constants.AUTH_REDIRECT_URL)
//            .build()

        val expected = allOf(hasAction(Intent.ACTION_VIEW))
        Intents.intending(expected).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, resultData),
        )

        composeTestRule.apply {
            setContent {
                AuthScreen(
                    onCompleteLogin = { onCompleteLoginClick++ },
                    onShowSnackbar = { _, _ -> true },
                )
            }
            onNodeWithText("Sign In").performClick()
//            onNodeWithText("Continue as Guest").performClick()
//            onRoot(true).printToLog("Testing")
            waitForIdle()
        }

        Intents.intended(expected)
        assertThat(onCompleteLoginClick).isEqualTo(1)
    }
}
