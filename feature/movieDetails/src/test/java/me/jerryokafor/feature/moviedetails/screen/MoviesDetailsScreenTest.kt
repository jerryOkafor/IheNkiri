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

package me.jerryokafor.feature.moviedetails.screen

import android.os.Build
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.ApplicationProvider
import coil.Coil
import me.jerryokafor.feature.moviedetails.viewmodel.TEST_ID
import me.jerryokafor.ihenkiri.core.test.rule.assertAreDisplayed
import me.jerryokafor.ihenkiri.core.test.util.FakeImageLoader
import me.jerryokafor.ihenkiri.core.test.util.MovieDetailsTestData
import me.jerryokafor.ihenkiri.feature.moviedetails.MOVIE_DETAILS_BOTTOM_BAR
import me.jerryokafor.ihenkiri.feature.moviedetails.MOVIE_DETAILS_COL
import me.jerryokafor.ihenkiri.feature.moviedetails.MOVIE_DETAILS_MAIN_CAST
import me.jerryokafor.ihenkiri.feature.moviedetails.MOVIE_DETAILS_OVERVIEW
import me.jerryokafor.ihenkiri.feature.moviedetails.MoviesDetailViewModel
import me.jerryokafor.ihenkiri.feature.moviedetails.MoviesDetails
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import kotlin.test.assertEquals

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
)
class MoviesDetailsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var onAddToWatchListClick = 0
    private var onAddToBookmarkClick = 0
    private var onAddToFavorite = 0
    private var onRateItClick = 0
    private var onNavigateUp = 0
    private var onWatchTrailerClick = 0

    private val testUIState =
        MoviesDetailViewModel.UIState(
            loading = false,
            title = "Fight Club",
            overview = "A ticking-time-bomb insomniac and a slippery",
            postPath = "",
            releaseDate = "1999/10/15",
            runtime = "1hr 50m",
            rating = 0.85F,
            cast = MovieDetailsTestData.testMovieCredit(TEST_ID).cast,
            crew = MovieDetailsTestData.testMovieCredit(TEST_ID).crew,
            categories = listOf("Action", "Drama", "Adventure", "Animation"),
            recommendations = listOf(),
            videos = listOf(),
        )

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out // Redirect Logcat to console
        Coil.setImageLoader(FakeImageLoader(ApplicationProvider.getApplicationContext()))
    }

    @Test
    fun `moviesDetails_onLoad_verifyUiState`() {
        with(composeTestRule) {
            setContent {
                MoviesDetails(
                    uiState = testUIState,
                    onAddToWatchListClick = { onAddToWatchListClick++ },
                    onAddToBookmarkClick = { onAddToBookmarkClick++ },
                    onAddToFavorite = { onAddToFavorite++ },
                    onRateItClick = { onRateItClick++ },
                    onWatchTrailerClick = { onWatchTrailerClick++ },
                    onNavigateUp = { onNavigateUp++ },
                )
            }
            onAllNodesWithText("Fight Club")
                .assertCountEquals(2)
                .assertAreDisplayed()

            onNodeWithTag(MOVIE_DETAILS_COL).assertExists()
                .performScrollToNode(hasTestTag(MOVIE_DETAILS_OVERVIEW))

            onNodeWithTag(MOVIE_DETAILS_MAIN_CAST).assertExists()

            onNodeWithTag(MOVIE_DETAILS_BOTTOM_BAR).assertExists()
                .assertIsDisplayed()

            onNodeWithContentDescription("Add to watch list").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
            onNodeWithContentDescription("Add to bookmark").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
            onNodeWithContentDescription("Rate it").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
            onNodeWithContentDescription("Add to favourite").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()

            assertEquals(1, onAddToWatchListClick)
            assertEquals(1, onAddToBookmarkClick)
            assertEquals(1, onRateItClick)
            assertEquals(1, onAddToFavorite)
        }
    }
}
