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

package com.jerryokafor.feature.media.ui

import android.os.Build
import android.webkit.WebView
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jerryokafor.feature.media.ui.vimeo.VimeoPlayerController
import com.jerryokafor.feature.media.ui.vimeo.VimeoPlayerEvent
import com.jerryokafor.feature.media.ui.vimeo.VimeoPlayerState
import com.jerryokafor.feature.media.ui.vimeo.VimeoPlayerView
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import org.robolectric.shadows.ShadowWebView
import kotlin.test.Test
import kotlin.time.Duration.Companion.milliseconds

@RunWith(AndroidJUnit4::class)
@Config(
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
    qualifiers = "xlarge",
)
class VimeoPlayerViewTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    val testScheduler = TestCoroutineScheduler()
    val testDispatcher = StandardTestDispatcher(testScheduler)
    val testScope = TestScope(testDispatcher)

    private lateinit var webView: WebView
    private lateinit var shadowWebView: ShadowWebView

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
        webView = WebView(RuntimeEnvironment.getApplication())
        shadowWebView = shadowOf(webView)
    }

    @Test
    fun shouldRecordLastLoadedUrl() = runTest {
        val playerController = VimeoPlayerController(
            autoPlay = true,
            initialVideoId = "",
            coroutineScope = testScope,
        )
        val playerState = VimeoPlayerState()

        composeTestRule.setContent {
            VimeoPlayerView(
                playerController = playerController,
                playerState = playerState,
                factory = { webView },
            )
        }

        playerState.events.test(timeout = 10.milliseconds) {
            assertThat(awaitItem()).isNull()

            playerState.events.update { VimeoPlayerEvent.OnPlayerReady("", 0F, "") }
            assertThat(awaitItem()).isInstanceOf(VimeoPlayerEvent.OnPlayerReady::class.java)

            playerState.events.update { VimeoPlayerEvent.OnTimeUpdate(0f) }
            assertThat(awaitItem()).isInstanceOf(VimeoPlayerEvent.OnTimeUpdate::class.java)

            playerState.events.update { VimeoPlayerEvent.OnPlay(0f) }
            assertThat(awaitItem()).isInstanceOf(VimeoPlayerEvent.OnPlay::class.java)

            playerState.events.update { VimeoPlayerEvent.OnPause(0f) }
            assertThat(awaitItem()).isInstanceOf(VimeoPlayerEvent.OnPause::class.java)

            playerState.events.update { VimeoPlayerEvent.OnEnded(0f) }
            assertThat(awaitItem()).isInstanceOf(VimeoPlayerEvent.OnEnded::class.java)

            playerState.events.update { VimeoPlayerEvent.OnVolumeChange(0f) }
            assertThat(awaitItem()).isInstanceOf(VimeoPlayerEvent.OnVolumeChange::class.java)

            playerState.events.update { VimeoPlayerEvent.OnTextTrackChange("", "", "") }
            assertThat(awaitItem()).isInstanceOf(VimeoPlayerEvent.OnTextTrackChange::class.java)

            playerState.events.update { VimeoPlayerEvent.OnInitFailed }
            assertThat(awaitItem()).isInstanceOf(VimeoPlayerEvent.OnInitFailed::class.java)

            playerState.events.update { VimeoPlayerEvent.OnError("", "", "") }
            assertThat(awaitItem()).isInstanceOf(VimeoPlayerEvent.OnError::class.java)

            cancelAndConsumeRemainingEvents()
        }
    }
}
