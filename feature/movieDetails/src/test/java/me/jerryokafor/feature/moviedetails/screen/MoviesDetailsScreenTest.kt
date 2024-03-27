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

package me.jerryokafor.feature.moviedetails.screen

import android.os.Build
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.test.ext.junit.runners.AndroidJUnit4
import me.jerryokafor.ihenkiri.core.test.rule.assertAreDisplayed
import me.jerryokafor.ihenkiri.core.test.util.MovieDetailsTestData
import me.jerryokafor.ihenkiri.core.test.util.testMovies
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_BOTTOM_BAR
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_CATEGORIES
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_CATEGORIES_ROW
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_COL
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_CREW
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_CREW_ROW
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_MAIN_CAST
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_MAIN_CAST_ROW
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_OVERVIEW
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MOVIE_DETAILS_RECOMMENDATIONS_ROW
import me.jerryokafor.ihenkiri.feature.moviedetails.ui.MoviesDetailsScreen
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
    private var onMovieItemClick = 0

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun moviesDetailsScreen_movieDetailsLoading_progressBarIsShown() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.Loading,
                    movieCreditUiState = MovieCreditUiState.Loading,
                    similarMoviesUiState = SimilarMoviesUiState.Loading,
                    moviesVideoUiState = MoviesVideoUiState.Loading,
                )
            }

            onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun moviesDetailsScreen_otherDetailsLoading_progressBarIsShown() {
        with(composeTestRule) {
            val expectedMovieDetails = MovieDetailsTestData.testMovieDetails(0L)
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.Success(expectedMovieDetails),
                    movieCreditUiState = MovieCreditUiState.Loading,
                    similarMoviesUiState = SimilarMoviesUiState.Loading,
                    moviesVideoUiState = MoviesVideoUiState.Loading,
                )
            }

            onAllNodes(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
                .assertCountEquals(3)
        }
    }

    @Test
    fun moviesDetailsScreen_movieDetailsLoaded_showMovieDetails() {
        with(composeTestRule) {
            val expectedMovieDetails = MovieDetailsTestData.testMovieDetails(0L)
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.Success(expectedMovieDetails),
                    movieCreditUiState = MovieCreditUiState.Loading,
                    similarMoviesUiState = SimilarMoviesUiState.Loading,
                    moviesVideoUiState = MoviesVideoUiState.Loading,
                    onAddToWatchListClick = { onAddToWatchListClick++ },
                    onAddToBookmarkClick = { onAddToBookmarkClick++ },
                    onAddToFavorite = { onAddToFavorite++ },
                    onRateItClick = { onRateItClick++ },
                    onWatchTrailerClick = { _, _ -> onWatchTrailerClick++ },
                    onNavigateUp = { onNavigateUp++ },
                )
            }

            onAllNodesWithText("Fight Club")
                .assertCountEquals(2)
                .assertAreDisplayed()

            onNodeWithText("2hr(s) 19m")
                .assertExists()
                .assertIsDisplayed()

            onNodeWithTag(MOVIE_DETAILS_COL).assertExists()
                .performScrollToNode(hasTestTag(MOVIE_DETAILS_OVERVIEW))

            onNodeWithTag(MOVIE_DETAILS_COL).performTouchInput { swipeUp() }

            // 3 progress showing
            onAllNodes(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
                .assertCountEquals(3)
                .assertAreDisplayed()

            onNodeWithTag(MOVIE_DETAILS_MAIN_CAST).assertExists()
                .assertIsDisplayed()
                .assert(hasText("Main Cast"))

            onNodeWithTag(MOVIE_DETAILS_CREW).assertExists()
                .assertIsDisplayed()
                .assert(hasText("Crew"))

            onNodeWithTag(MOVIE_DETAILS_CATEGORIES).assertExists()
                .assertIsDisplayed()
                .assert(hasText("Categories"))

            onNodeWithTag(MOVIE_DETAILS_CATEGORIES_ROW).assertExists()
                .assertIsDisplayed()
                .onChildren()
                .assertCountEquals(expectedMovieDetails.genres.size)
                .onFirst()
                .assertTextEquals("Drama")
        }
    }

    @Test
    fun moviesDetailsScreen_movieCreditsLoaded_showMainCastsAndCrew() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.Success(
                        MovieDetailsTestData.testMovieDetails(0L),
                    ),
                    movieCreditUiState = MovieCreditUiState.Success(
                        movieCredit = MovieDetailsTestData.testMovieCredit(0L),
                    ),
                    similarMoviesUiState = SimilarMoviesUiState.Loading,
                    moviesVideoUiState = MoviesVideoUiState.Loading,
                )
            }

            onNodeWithTag(MOVIE_DETAILS_COL).assertExists()
                .performTouchInput { swipeUp() }

            // assert main casts
            onNodeWithTag(MOVIE_DETAILS_MAIN_CAST_ROW).assertExists()
                .assertIsDisplayed()
                .assert(hasScrollAction())
                .onChildren()
                .assertCountEquals(2)
                .onFirst()
                .assert(hasContentDescription("Edward Norton"))
                .assert(hasText("Edward"))

            onNodeWithTag(MOVIE_DETAILS_CREW_ROW).assertExists()
                .assertIsDisplayed()
                .assert(hasScrollAction())
                .onChildren()
                .assertCountEquals(2)
                .onFirst()
                .assert(hasContentDescription("Arnon Milchan"))
                .assert(hasText("Arnon"))
        }
    }

    @Test
    fun moviesDetailsScreen_similarMoviesLoaded_showSimilar() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.Success(
                        MovieDetailsTestData.testMovieDetails(0L),
                    ),
                    movieCreditUiState = MovieCreditUiState.Loading,
                    similarMoviesUiState = SimilarMoviesUiState.Success(testMovies()),
                    moviesVideoUiState = MoviesVideoUiState.Loading,
                    onMovieItemClick = { onMovieItemClick++ },
                )
            }

            onNodeWithTag(MOVIE_DETAILS_COL).assertExists()
                .performTouchInput { swipeUp() }

            // assert main casts
            onNodeWithTag(MOVIE_DETAILS_RECOMMENDATIONS_ROW).assertExists()
                .assertIsDisplayed()
                .assert(hasScrollAction())
                .onChildren()
                .assertCountEquals(4)
                .onFirst()
                .assert(hasContentDescription("Transformers: Rise of the Beasts"))
                .assertHasClickAction()
                .performClick()

            assertEquals(expected = 1, actual = onMovieItemClick)
        }
    }

    @Test
    fun moviesDetailsScreen_onAddToWatchListClick_addToWatchList() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.Success(
                        MovieDetailsTestData.testMovieDetails(0L),
                    ),
                    movieCreditUiState = MovieCreditUiState.Loading,
                    similarMoviesUiState = SimilarMoviesUiState.Loading,
                    moviesVideoUiState = MoviesVideoUiState.Loading,
                    onAddToWatchListClick = { onAddToWatchListClick++ },
                )
            }
            onNodeWithTag(MOVIE_DETAILS_BOTTOM_BAR).assertExists()
                .assertIsDisplayed()

            onNodeWithContentDescription("Add to watch list").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()

            assertEquals(1, onAddToWatchListClick)
        }
    }

    @Test
    fun moviesDetailsScreen_onAddToBookmarkClick_addToBookmark() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
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
                    onWatchTrailerClick = { _, _ -> onWatchTrailerClick++ },
                    onNavigateUp = { onNavigateUp++ },
                )
            }

            onNodeWithTag(MOVIE_DETAILS_BOTTOM_BAR).assertExists()
                .assertIsDisplayed()

            onNodeWithContentDescription("Add to bookmark").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
            assertEquals(1, onAddToBookmarkClick)
        }
    }

    @Test
    fun moviesDetailsScreen_onRateItClick_rateMovie() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
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
                    onWatchTrailerClick = { _, _ -> onWatchTrailerClick++ },
                    onNavigateUp = { onNavigateUp++ },
                )
            }
            onNodeWithTag(MOVIE_DETAILS_BOTTOM_BAR).assertExists()
                .assertIsDisplayed()

            onNodeWithContentDescription("Rate it").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
            assertEquals(1, onRateItClick)
        }
    }

    @Test
    fun moviesDetailsScreen_onAddToFavorite_addToFavorite() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
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
                    onWatchTrailerClick = { _, _ -> onWatchTrailerClick++ },
                    onNavigateUp = { onNavigateUp++ },
                )
            }

            onNodeWithTag(MOVIE_DETAILS_BOTTOM_BAR).assertExists()
                .assertIsDisplayed()

            onNodeWithContentDescription("Add to favourite").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
            assertEquals(1, onAddToFavorite)
        }
    }

    @Test
    fun moviesDetailsScreen_onWatchTrailerClick_showMovieTrailer() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.Success(
                        MovieDetailsTestData.testMovieDetails(0L),
                    ),
                    movieCreditUiState = MovieCreditUiState.Loading,
                    similarMoviesUiState = SimilarMoviesUiState.Loading,
                    moviesVideoUiState = MoviesVideoUiState.Success(
                        videos = MovieDetailsTestData.testMovieVideos(movieId = 0L),
                        movieId = 0L,
                    ),
                    onWatchTrailerClick = { _, _ -> onWatchTrailerClick++ },
                )
            }

            onNodeWithTag(MOVIE_DETAILS_BOTTOM_BAR).assertExists()
                .assertIsDisplayed()

            onNodeWithText("Watch Trailer").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
            assertEquals(1, onWatchTrailerClick)
        }
    }

    @Test
    fun moviesDetailsScreen_movieVideosEmpty_hideMovieTrailer() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.Success(
                        MovieDetailsTestData.testMovieDetails(0L),
                    ),
                    movieCreditUiState = MovieCreditUiState.Loading,
                    similarMoviesUiState = SimilarMoviesUiState.Loading,
                    moviesVideoUiState = MoviesVideoUiState.Success(emptyList(), 0L),
                    onWatchTrailerClick = { _, _ -> onWatchTrailerClick++ },
                )
            }

            onNodeWithTag(MOVIE_DETAILS_BOTTOM_BAR).assertExists()
                .assertIsDisplayed()

            onNodeWithText("Watch Trailer").assertDoesNotExist()
        }
    }

    @Test
    fun moviesDetailsScreen_onNavigateUp_backPress() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.Success(
                        MovieDetailsTestData.testMovieDetails(0L),
                    ),
                    movieCreditUiState = MovieCreditUiState.Loading,
                    similarMoviesUiState = SimilarMoviesUiState.Loading,
                    moviesVideoUiState = MoviesVideoUiState.Loading,
                    onNavigateUp = { onNavigateUp++ },
                )
            }

            onNodeWithTag(MOVIE_DETAILS_BOTTOM_BAR).assertExists()
                .assertIsDisplayed()

            onNodeWithContentDescription("Navigate up").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
            assertEquals(1, onNavigateUp)
        }
    }

    @Test
    fun moviesDetailsScreen_movieDetailsLoadFailed_showError() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.LoadFailed(
                        "Error loading movie details",
                    ),
                    movieCreditUiState = MovieCreditUiState.LoadFailed(""),
                    similarMoviesUiState = SimilarMoviesUiState.LoadFailed(""),
                    moviesVideoUiState = MoviesVideoUiState.LoadFailed(""),
                    onNavigateUp = { onNavigateUp++ },
                )
            }

            onNodeWithText("Error loading movie details")
                .assertExists().assertIsDisplayed()
            onNode(hasClickAction() and hasText("Back"))
                .assertIsDisplayed()
                .performClick()
            assertEquals(1, onNavigateUp)
        }
    }

    @Test
    fun moviesDetailsScreen_movieExtraDetailsLoadFailed_showError() {
        with(composeTestRule) {
            setContent {
                MoviesDetailsScreen(
                    movieDetailsUiState = MovieDetailsUiState.Success(
                        MovieDetailsTestData.testMovieDetails(0L),
                    ),
                    movieCreditUiState = MovieCreditUiState.LoadFailed(""),
                    similarMoviesUiState = SimilarMoviesUiState.LoadFailed(""),
                    moviesVideoUiState = MoviesVideoUiState.LoadFailed(""),
                )
            }

            onNodeWithTag(MOVIE_DETAILS_COL).performTouchInput { swipeUp() }

            onAllNodesWithText(text = "please try again", substring = true, ignoreCase = true)
                .assertCountEquals(3)
                .assertAreDisplayed()
            onAllNodes(hasClickAction() and hasText("Back"))
                .assertAreDisplayed()
                .assertCountEquals(3)
        }
    }
}
