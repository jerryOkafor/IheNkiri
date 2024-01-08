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

package me.jerryokafor.ihenkiri.feature.tvshows.ui

import android.os.Build
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasScrollAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import me.jerryokafor.core.model.TVShow
import me.jerryokafor.core.model.TVShowsFilterItem
import me.jerryokafor.ihenkiri.core.test.util.TVShowsTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
    qualifiers = "xlarge",
)
class TVShowsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var onTVShowClick = 0
    private var onFilterItemSelected = 0

    private fun testFilters() = listOf(
        TVShowsFilterItem(
            isSelected = true,
            type = TVShowsFilterItem.FilterType.AIRING_TODAY,
        ),
        TVShowsFilterItem(
            isSelected = false,
            type = TVShowsFilterItem.FilterType.ON_THE_AIR,
        ),
        TVShowsFilterItem(
            isSelected = false,
            type = TVShowsFilterItem.FilterType.POPULAR,
        ),
        TVShowsFilterItem(
            isSelected = false,
            type = TVShowsFilterItem.FilterType.TOP_RATED,
        ),
        TVShowsFilterItem(
            isSelected = false,
            type = TVShowsFilterItem.FilterType.DISCOVER,
        ),
    )

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun tvShowsScreen_tvShowsLoading_showLoadingProgress() {
        composeTestRule.apply {
            setContent {
                TVShowsScreen(
                    tvShowLazyPagingItems = flowOf(
                        PagingData.from(
                            data = emptyList<TVShow>(),
                            sourceLoadStates = LoadStates(
                                refresh = LoadState.Loading,
                                append = LoadState.NotLoading(false),
                                prepend = LoadState.NotLoading(false),
                            ),
                        ),
                    ).collectAsLazyPagingItems(),
                    filters = testFilters(),
                    onTVShowClick = { onTVShowClick++ },
                    onFilterItemSelected = { onFilterItemSelected++ },
                )
            }

            onNodeWithTag(FRESH_LOAD_PROGRESS_TEST_TAG).assert(
                hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate),
            ).assertIsDisplayed()
        }
    }

    @Test
    fun tvShowsScreen_appendTvShowsLoading_showLoadingProgress() {
        composeTestRule.apply {
            setContent {
                TVShowsScreen(
                    tvShowLazyPagingItems = flowOf(
                        PagingData.from(
                            data = emptyList<TVShow>(),
                            sourceLoadStates = LoadStates(
                                refresh = LoadState.NotLoading(false),
                                append = LoadState.Loading,
                                prepend = LoadState.NotLoading(false),
                            ),
                        ),
                    ).collectAsLazyPagingItems(),
                    filters = testFilters(),
                    onTVShowClick = { onTVShowClick++ },
                    onFilterItemSelected = { onFilterItemSelected++ },
                )
            }

            onNodeWithTag(APPEND_LOAD_PROGRESS_TEST_TAG).assert(
                hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate),
            ).assertIsDisplayed()
        }
    }

    @Test
    fun tvShowsScreen_tvShowsLoaded_tvShowsAreshown() {
        composeTestRule.apply {
            setContent {
                TVShowsScreen(
                    tvShowLazyPagingItems = flowOf(
                        PagingData.from(
                            data = TVShowsTestData.testTVShows(),
                            sourceLoadStates = LoadStates(
                                refresh = LoadState.NotLoading(false),
                                append = LoadState.NotLoading(false),
                                prepend = LoadState.NotLoading(false),
                            ),
                        ),
                    ).collectAsLazyPagingItems(),
                    filters = testFilters(),
                    onTVShowClick = { onTVShowClick++ },
                    onFilterItemSelected = { onFilterItemSelected++ },
                )
            }

            composeTestRule.onNodeWithTag(TV_SHOWS_GRID_ITEMS_TEST_TAG)
                .assertExists()
                .assert(hasScrollAction())
                .onChildren()
                .onFirst()
                .performClick()

            assertThat(onTVShowClick).isEqualTo(1)
        }
    }

    @Test
    fun tvShowsScreen_airingTodayFilterSelected_correctFilterIsSelected() {
        composeTestRule.apply {
            setContent {
                TVShowsScreen(
                    tvShowLazyPagingItems = flowOf(
                        PagingData.from(
                            data = emptyList<TVShow>(),
                            sourceLoadStates = LoadStates(
                                refresh = LoadState.NotLoading(false),
                                append = LoadState.NotLoading(false),
                                prepend = LoadState.NotLoading(false),
                            ),
                        ),
                    ).collectAsLazyPagingItems(),
                    filters = testFilters().selectItem(TVShowsFilterItem.FilterType.AIRING_TODAY),
                    onTVShowClick = { onTVShowClick++ },
                    onFilterItemSelected = { onFilterItemSelected++ },
                )
            }

            composeTestRule.onNodeWithTag(CHIP_GROUP_TEST_TAG)
                .assertExists()
                .assert(hasScrollAction())
                .assertIsDisplayed()

            composeTestRule.onNode(hasText("Airing Today") and hasClickAction())
                .assertExists()
                .assertIsSelected()
                .assertIsDisplayed()
                .performClick()

            assertThat(onFilterItemSelected).isEqualTo(1)
        }
    }

    @Test
    fun tvShowsScreen_onTheAirFilterSelected_correctFilterIsSelected() {
        composeTestRule.apply {
            setContent {
                TVShowsScreen(
                    tvShowLazyPagingItems = flowOf(
                        PagingData.from(
                            data = emptyList<TVShow>(),
                            sourceLoadStates = LoadStates(
                                refresh = LoadState.NotLoading(false),
                                append = LoadState.NotLoading(false),
                                prepend = LoadState.NotLoading(false),
                            ),
                        ),
                    ).collectAsLazyPagingItems(),
                    filters = testFilters().selectItem(TVShowsFilterItem.FilterType.ON_THE_AIR),
                    onTVShowClick = { onTVShowClick++ },
                    onFilterItemSelected = { onFilterItemSelected++ },
                )
            }

            composeTestRule.onNodeWithTag(CHIP_GROUP_TEST_TAG)
                .assertExists()
                .assert(hasScrollAction())
                .assertIsDisplayed()

            composeTestRule.onNode(hasText("On The Air") and hasClickAction())
                .assertExists()
                .assertIsSelected()
                .assertIsDisplayed()
                .performClick()

            assertThat(onFilterItemSelected).isEqualTo(1)
        }
    }

    @Test
    fun tvShowsScreen_popularFilterSelected_correctFilterIsSelected() {
        composeTestRule.apply {
            setContent {
                TVShowsScreen(
                    tvShowLazyPagingItems = flowOf(
                        PagingData.from(
                            data = emptyList<TVShow>(),
                            sourceLoadStates = LoadStates(
                                refresh = LoadState.NotLoading(false),
                                append = LoadState.NotLoading(false),
                                prepend = LoadState.NotLoading(false),
                            ),
                        ),
                    ).collectAsLazyPagingItems(),
                    filters = testFilters().selectItem(TVShowsFilterItem.FilterType.POPULAR),
                    onTVShowClick = { onTVShowClick++ },
                    onFilterItemSelected = { onFilterItemSelected++ },
                )
            }

            composeTestRule.onNodeWithTag(CHIP_GROUP_TEST_TAG)
                .assertExists()
                .assert(hasScrollAction())
                .assertIsDisplayed()

            composeTestRule.onNode(hasText("Popular") and hasClickAction())
                .assertExists()
                .assertIsSelected()
                .assertIsDisplayed()
                .performClick()

            assertThat(onFilterItemSelected).isEqualTo(1)
        }
    }

    @Test
    fun tvShowsScreen_topRatedFilterSelected_correctFilterIsSelected() {
        composeTestRule.apply {
            setContent {
                TVShowsScreen(
                    tvShowLazyPagingItems = flowOf(
                        PagingData.from(
                            data = emptyList<TVShow>(),
                            sourceLoadStates = LoadStates(
                                refresh = LoadState.NotLoading(false),
                                append = LoadState.NotLoading(false),
                                prepend = LoadState.NotLoading(false),
                            ),
                        ),
                    ).collectAsLazyPagingItems(),
                    filters = testFilters().selectItem(TVShowsFilterItem.FilterType.TOP_RATED),
                    onTVShowClick = { onTVShowClick++ },
                    onFilterItemSelected = { onFilterItemSelected++ },
                )
            }

            composeTestRule.onNodeWithTag(CHIP_GROUP_TEST_TAG)
                .assertExists()
                .assert(hasScrollAction())
                .assertIsDisplayed()

            composeTestRule.onNode(hasText("Top Rated") and hasClickAction())
                .assertExists()
                .assertIsSelected()
                .assertIsDisplayed()
                .performClick()

            assertThat(onFilterItemSelected).isEqualTo(1)
        }
    }

    @Test
    fun tvShowsScreen_discoverFilterSelected_correctFilterIsSelected() {
        composeTestRule.apply {
            setContent {
                TVShowsScreen(
                    tvShowLazyPagingItems = flowOf(
                        PagingData.from(
                            data = emptyList<TVShow>(),
                            sourceLoadStates = LoadStates(
                                refresh = LoadState.NotLoading(false),
                                append = LoadState.NotLoading(false),
                                prepend = LoadState.NotLoading(false),
                            ),
                        ),
                    ).collectAsLazyPagingItems(),
                    filters = testFilters().selectItem(TVShowsFilterItem.FilterType.DISCOVER),
                    onTVShowClick = { onTVShowClick++ },
                    onFilterItemSelected = { onFilterItemSelected++ },
                )
            }

            composeTestRule.onNodeWithTag(CHIP_GROUP_TEST_TAG)
                .assertExists()
                .assert(hasScrollAction())
                .assertIsDisplayed()

            composeTestRule.onNode(hasText("Discover") and hasClickAction())
                .assertExists()
                .assertIsSelected()
                .assertIsDisplayed()
                .performClick()

            onNodeWithTag(SEARCH_TEST_TAG).assertIsDisplayed()
            onNodeWithText("Search ....").assertIsDisplayed()
            onNodeWithContentDescription("perform search").assertIsDisplayed()
            onNodeWithContentDescription("close search").assertIsDisplayed()
            onNodeWithText("Adult").assertIsDisplayed()
            onNodeWithText("Video").assertIsDisplayed()
            onNodeWithTag(SEARCH_ADULT).assertIsDisplayed()
            onNodeWithTag(SEARCH_INLCUDE_VIDEO).assertIsDisplayed()
            assertThat(onFilterItemSelected).isEqualTo(0)
        }
    }
}

private fun List<TVShowsFilterItem>.selectItem(whereType: TVShowsFilterItem.FilterType) = map {
    if (it.type == whereType) {
        it.copy(isSelected = true)
    } else {
        it.copy(isSelected = false)
    }
}
