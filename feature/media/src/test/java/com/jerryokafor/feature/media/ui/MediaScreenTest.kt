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
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.jerryokafor.ihenkiri.core.test.util.MovieDetailsTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
@Config(
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
    qualifiers = "xlarge",
)
class MediaScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var onNavigateUp = 0

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun mediaScreen_loadingMovieVideos_progressIndicatorShown() {
        with(composeTestRule) {
            setContent {
                MediaScreen(
                    uiState = MediaUiState.Loading,
                    title = "Test Movie title",
                    onBackClick = { onNavigateUp++ },
                )
            }

            onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun mediaScreen_loadMovieVideos_movieVideosIsShown() {
        val testMovies = MovieDetailsTestData.testMovieVideos(0L)
        with(composeTestRule) {
            setContent {
                MediaScreen(
                    uiState = MediaUiState.Success(testMovies),
                    title = "Test Movie title",
                    onBackClick = { onNavigateUp++ },
                )
            }
            onNodeWithText("Test Movie title").assertIsDisplayed()
            onNodeWithContentDescription("Back")
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
            assertEquals(onNavigateUp, 1)

            onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
                .assertDoesNotExist()

            onNode(hasScrollAction() and hasTestTag(VIDEO_LIST))
                .assertIsDisplayed()
                .onChildren()
                .assertCountEquals(2)

            onAllNodes(hasContentDescription("YouTube", ignoreCase = true) and hasClickAction())
                .assertCountEquals(1)
                .onFirst()
                .performClick()

            waitForIdle()
            onNodeWithTag(YOUTUBE_PLAYER)
                .assertIsDisplayed()

            onAllNodes(hasContentDescription("Vimeo", ignoreCase = true) and hasClickAction())
                .assertCountEquals(1)
                .onFirst()
                .performClick()

            waitForIdle()
            onNodeWithTag(VIMEO_PLAYER)
                .assertIsDisplayed()

            onNodeWithText("Fight Club (1999) Trailer", substring = true)
                .assertIsDisplayed()
            onNodeWithText(testMovies.first().publishedAt.formatDate(), substring = true)
                .assertIsDisplayed()

            onNodeWithText("#TBT Trailer").assertIsDisplayed()
            onNodeWithText(testMovies.last().publishedAt.formatDate(), substring = true)
                .assertIsDisplayed()
        }
    }

    @Test
    fun mediaScreen_loadMovieUnknownVideo_showUnknownProvider() {
        val testMovies = MovieDetailsTestData.testUnknownProviderVideo()
        with(composeTestRule) {
            setContent {
                MediaScreen(
                    uiState = MediaUiState.Success(testMovies),
                    title = "Test Movie title",
                    onBackClick = { onNavigateUp++ },
                )
            }

            onNodeWithText("Provider: Unknown not supported")
                .assertIsDisplayed()
        }
    }

    @Test
    fun mediaScreen_loadMovieFailed_showFailureScreen() {
        with(composeTestRule) {
            setContent {
                MediaScreen(
                    uiState = MediaUiState.LoadFailed("Error loading movie videos"),
                    title = "Test Movie title",
                    onBackClick = { onNavigateUp++ },
                )
            }

            onNodeWithText("Error loading movie videos")
                .assertIsDisplayed()
        }
    }
}
