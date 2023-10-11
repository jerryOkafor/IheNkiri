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

package me.jerryokafor.ihenkiri.android.test.movies

import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import me.jerryokafor.core.model.MovieListFilterItem
import me.jerryokafor.core.ui.components.MOVIE_POSTER_TEST_TAG
import me.jerryokafor.feature.movies.screen.CHIP_GROUP_TEST_TAG
import me.jerryokafor.feature.movies.screen.GRID_ITEMS_TEST_TAG
import me.jerryokafor.feature.movies.screen.MoviesScreen
import me.jerryokafor.ihenkiri.core.test.util.testMovies
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MoviesScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testFilters = listOf(
        MovieListFilterItem(
            label = "Now Playing",
            isSelected = true,
            type = MovieListFilterItem.FilterType.NOW_PLAYING,
        ),
        MovieListFilterItem(
            label = "Popular",
            isSelected = false,
            type = MovieListFilterItem.FilterType.POPULAR,
        ),
        MovieListFilterItem(
            label = "Top Rated",
            isSelected = false,
            type = MovieListFilterItem.FilterType.TOP_RATED,
        ),
        MovieListFilterItem(
            label = "Upcoming",
            isSelected = false,
            type = MovieListFilterItem.FilterType.UPCOMING,
        ),
    )

    private var onSearchClickCounter = 0
    private var onMovieClickCounter = 0
    private var onFilterItemSelectedCounter = 0

    fun setUp(filters: List<MovieListFilterItem>) {
        composeTestRule.setContent {
            MoviesScreen(
                loading = false,
                filters = filters,
                movies = testMovies(),
                onSearchClick = { onSearchClickCounter++ },
                onMovieClick = { onMovieClickCounter++ },
                onFilterItemSelected = { onFilterItemSelectedCounter++ },
            )
        }
    }

    @Test
    fun moviesScreen_SearchButtonClick_SearchViewShown() {
        setUp(testFilters)
        composeTestRule.onNodeWithContentDescription("Search")
            .assertExists()
            .assertIsDisplayed()
            .performClick()

        assertThat(onSearchClickCounter).isEqualTo(1)
    }

    @Test
    fun moviesScreen_NowPlayingMoviesFilterSelected_NowPlayingMoviesShown() {
        setUp(testFilters.selectItem("Now Playing"))

        composeTestRule.onNodeWithTag(CHIP_GROUP_TEST_TAG)
            .assertExists()
            .assertIsDisplayed()

        with(composeTestRule.onNode(hasText("Now Playing") and hasClickAction())) {
            assertExists()
            assertIsSelected()
            assertIsDisplayed()
            performClick()
        }

        assertThat(onFilterItemSelectedCounter).isEqualTo(1)
    }

    @Test
    fun moviesScreen_PopularMoviesFilterSelected_PopularMoviesShown() {
        setUp(testFilters.selectItem("Popular"))

        composeTestRule.onNodeWithTag(CHIP_GROUP_TEST_TAG)
            .assertExists()
            .assertIsDisplayed()
        with(composeTestRule.onNode(hasText("Popular") and hasClickAction())) {
            assertExists()
            assertIsSelected()
            assertIsDisplayed()
            performClick()
        }

        assertThat(onFilterItemSelectedCounter).isEqualTo(1)
    }

    @Test
    fun moviesScreen_TopRatedMoviesFilterSelected_TopRatedMoviesShown() {
        setUp(testFilters.selectItem("Top Rated"))

        composeTestRule.onNodeWithTag(CHIP_GROUP_TEST_TAG)
            .assertExists()
            .assertIsDisplayed()
        with(composeTestRule.onNode(hasText("Top Rated") and hasClickAction())) {
            assertExists()
            assertIsSelected()
            assertIsDisplayed()
            performClick()
        }

        assertThat(onFilterItemSelectedCounter).isEqualTo(1)
    }

    @Test
    fun moviesScreen_UpcomingMoviesFilterSelected_UpcomingMoviesShown() {
        setUp(testFilters.selectItem("Upcoming"))

        composeTestRule.onNodeWithTag(CHIP_GROUP_TEST_TAG)
            .assertExists()
            .assertIsDisplayed()
            .performScrollToIndex(3)
        with(composeTestRule.onNode(hasText("Upcoming") and hasClickAction())) {
            assertExists()
            assertIsSelected()
            assertIsDisplayed()
            performClick()
        }

        assertThat(onFilterItemSelectedCounter).isEqualTo(1)
    }

    @Test
    fun moviesScreen_MovieItemClick_MovieDetailsScreenShown() {
        setUp(testFilters.selectItem("Now Playing"))

        composeTestRule.onNodeWithTag(GRID_ITEMS_TEST_TAG, useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
            .assert(hasScrollAction())
            .onChildren()
            .assertCountEquals(4)

        composeTestRule.onAllNodesWithTag(MOVIE_POSTER_TEST_TAG, useUnmergedTree = true)
            .assertCountEquals(4)
            .onFirst()
            .assertHasClickAction()
            .performClick()

        assertThat(onMovieClickCounter).isEqualTo(1)
    }
}

private fun List<MovieListFilterItem>.selectItem(whereLabel: String) =
    map { if (it.label == whereLabel) it.copy(isSelected = true) else it.copy(isSelected = false) }
