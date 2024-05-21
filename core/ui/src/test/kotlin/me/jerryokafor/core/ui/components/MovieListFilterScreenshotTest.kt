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

package me.jerryokafor.core.ui.components

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltTestApplication
import me.jerryokafor.core.model.MovieListFilterItem
import me.jerryokafor.ihenkiri.core.test.util.captureMultiTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, sdk = [33], qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class MovieListFilterScreenshotTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun movieListFilter_multipleThemes() {
        composeTestRule.captureMultiTheme("MovieListFilter") { desc: String ->
            Surface {
                MovieListFilter(
                    items = listOf(
                        MovieListFilterItem(
                            isSelected = true,
                            type = MovieListFilterItem.FilterType.NOW_PLAYING,
                        ),
                        MovieListFilterItem(
                            isSelected = false,
                            type = MovieListFilterItem.FilterType.POPULAR,
                        ),
                        MovieListFilterItem(
                            isSelected = false,
                            type = MovieListFilterItem.FilterType.TOP_RATED,
                        ),
                        MovieListFilterItem(
                            isSelected = false,
                            type = MovieListFilterItem.FilterType.UPCOMING,
                        ),
                    ).map { Pair(labelFor(it), it.isSelected) },
                ) {}
            }
        }
    }

    private fun labelFor(filterItem: MovieListFilterItem): String {
        return when (filterItem.type) {
            MovieListFilterItem.FilterType.NOW_PLAYING -> "Now Playing"
            MovieListFilterItem.FilterType.POPULAR -> "Popular"
            MovieListFilterItem.FilterType.TOP_RATED -> "Top Rated"
            MovieListFilterItem.FilterType.UPCOMING -> "Upcoming"
            MovieListFilterItem.FilterType.DISCOVER -> "Discover"
        }
    }
}
