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
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.jerryokafor.ihenkiri.core.test.rule.assertAreDisplayed
import me.jerryokafor.ihenkiri.core.test.util.MovieDetailsTestData
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_BOTTOM_BAR
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_COL
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_MAIN_CAST
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_OVERVIEW
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MoviesDetails
import me.jerryokafor.ihenkiri.feature.moviedetails.viewmodel.MovieCreditUiState
import me.jerryokafor.ihenkiri.feature.moviedetails.viewmodel.MovieDetailsUiState
import me.jerryokafor.ihenkiri.feature.moviedetails.viewmodel.MoviesVideoUiState
import me.jerryokafor.ihenkiri.feature.moviedetails.viewmodel.SimilarMoviesUiState
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
class MoviesDetailsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var onAddToWatchListClick = 0
    private var onAddToBookmarkClick = 0
    private var onAddToFavorite = 0
    private var onRateItClick = 0
    private var onNavigateUp = 0
    private var onWatchTrailerClick = 0

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun `moviesDetails_onLoad_verifyUiState`() {
        with(composeTestRule) {
            setContent {
                MoviesDetails(
                    movieDetailsUiState = MovieDetailsUiState.Success(
                        MovieDetailsTestData.testMovieDetails(0L),
                    ),
                    movieCreditUiState = MovieCreditUiState.Loading,
                    similarMoviesUiState = SimilarMoviesUiState.Loading,
                    moviesVideoUiState = MoviesVideoUiState.Loading,
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
