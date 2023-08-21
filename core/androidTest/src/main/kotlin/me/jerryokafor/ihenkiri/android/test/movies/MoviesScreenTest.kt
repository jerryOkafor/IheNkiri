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

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import me.jerryokafor.feature.movies.model.MovieListFilterItem
import me.jerryokafor.feature.movies.screen.GRID_ITEMS_TEST_TAG
import me.jerryokafor.feature.movies.screen.MoviesScreen
import me.jerryokafor.feature.movies.screen.SEARCH_TEST_TAG
import me.jerryokafor.ihenkiri.core.test.test.data.testMovies
import org.junit.Rule
import org.junit.Test

class MoviesScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val testFilters = listOf(
        MovieListFilterItem(
            label = "Now Playing",
            isSelected = true,
            type = MovieListFilterItem.FilterType.NOW_PLAYING
        ),
        MovieListFilterItem(
            label = "Popular",
            isSelected = false,
            type = MovieListFilterItem.FilterType.POPULAR
        ),
        MovieListFilterItem(
            label = "Top Rated",
            isSelected = false,
            type = MovieListFilterItem.FilterType.TOP_RATED
        ),
        MovieListFilterItem(
            label = "Upcoming",
            isSelected = false,
            type = MovieListFilterItem.FilterType.UPCOMING
        ),
    )

    @Test
    fun testMoviesScreen() {
        composeTestRule.setContent {
            MoviesScreen(
                filters = testFilters,
                movies = testMovies(),
            )
        }

        composeTestRule.onNodeWithTag(SEARCH_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(SEARCH_TEST_TAG).assertIsDisplayed()
        composeTestRule.onNodeWithTag(GRID_ITEMS_TEST_TAG).assertIsDisplayed()
    }
}
