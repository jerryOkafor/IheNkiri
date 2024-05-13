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
@file:Suppress("TooManyFunctions")

package me.jerryokafor.ihenkiri.feature.tvshows.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.coroutines.flow.flowOf
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.data.util.ImageUtil
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.FiveVerticalSpacer
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.model.TVShow
import me.jerryokafor.core.model.TVShowsFilterItem
import me.jerryokafor.core.ui.components.Background
import me.jerryokafor.core.ui.components.MovieListFilter
import me.jerryokafor.core.ui.components.MoviePoster
import me.jerryokafor.core.ui.extension.TrackScrollJank
import me.jerryokafor.ihenkiri.feature.tvshows.R
import me.jerryokafor.ihenkiri.feature.tvshows.viewModel.TVShowsViewModel

const val TITLE_TEST_TAG = "title"
const val CHIP_GROUP_TEST_TAG = "chips"
const val TV_SHOWS_GRID_ITEMS_TEST_TAG = "tv_show_gridItems"
const val SEARCH_TEST_TAG = "search"
const val ASPECT_RATIO = 0.7F
const val FRESH_LOAD_PROGRESS_TEST_TAG = "fresh_load"
const val APPEND_LOAD_PROGRESS_TEST_TAG = "append_load"
const val SEARCH_INLCUDE_VIDEO = "search_include_video"
const val SEARCH_ADULT = "search_include_adult"

@Suppress("MagicNumber")
@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun TVShowsScreenPreview() {
    IheNkiriTheme {
        TVShowsScreen(
            tvShowLazyPagingItems = flowOf(
                PagingData.from(testTvShows()),
            ).collectAsLazyPagingItems(),
            filters = testFilters(),
            onTVShowClick = {},
        ) {}
    }
}

@Composable
@ExcludeFromGeneratedCoverageReport
fun TVShowsScreen(
    viewModel: TVShowsViewModel = hiltViewModel(),
    onRecommendationClick: () -> Unit = {},
    onTVShowClick: (movieId: Long) -> Unit = {},
) {
    val availableFilters by viewModel.tvShowsFilters.collectAsStateWithLifecycle()
    val movieLazyPagingItems = viewModel.tvShows.collectAsLazyPagingItems()
    val onItemSelected: (TVShowsFilterItem.FilterType) -> Unit = {
        viewModel.onFilterChange(it)
    }

    TVShowsScreen(
        tvShowLazyPagingItems = movieLazyPagingItems,
        filters = availableFilters,
        onTVShowClick = onTVShowClick,
        onRecommendationClick = onRecommendationClick,
        onFilterItemSelected = onItemSelected,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TVShowsScreen(
    tvShowLazyPagingItems: LazyPagingItems<TVShow>,
    filters: List<TVShowsFilterItem> = emptyList(),
    onRecommendationClick: () -> Unit = {},
    onTVShowClick: (movieId: Long) -> Unit = {},
    onFilterItemSelected: (TVShowsFilterItem.FilterType) -> Unit = {},
) {
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    val filterItemSelected: (TVShowsFilterItem.FilterType) -> Unit = {
        when (it) {
            TVShowsFilterItem.FilterType.DISCOVER -> {
                onRecommendationClick()
            }

            else -> {
                onFilterItemSelected(it)
            }
        }
    }

    Background {
        Column(modifier = Modifier) {
            CenterAlignedTopAppBar(
                modifier = Modifier.testTag(TITLE_TEST_TAG),
                title = {
                    Text(
                        text = stringResource(R.string.tv_shows_title),
                        style = IheNkiri.typography.titleMedium,
                        color = IheNkiri.color.onPrimary,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
                actions = {
                    Button(onClick = onRecommendationClick) {
                        Icon(
                            painter = painterResource(
                                id = me.jerryokafor.core.ui.R.drawable.search,
                            ),
                            contentDescription = stringResource(
                                R.string.tv_shows_content_description_search,
                            ),
                        )
                    }
                },
            )

            val scrollState = rememberLazyStaggeredGridState()
            TrackScrollJank(
                scrollableState = scrollState,
                stateName = "peopleDetails:screen",
            )

            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .padding(horizontal = IheNkiri.spacing.twoAndaHalf)
                        .testTag(TV_SHOWS_GRID_ITEMS_TEST_TAG)
                        .fillMaxSize(),
                    state = scrollState,
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = IheNkiri.spacing.oneAndHalf,
                    contentPadding = PaddingValues(bottom = IheNkiri.spacing.twoAndaHalf),
                    horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.oneAndHalf),
                    content = {
                        item(span = StaggeredGridItemSpan.FullLine) { FiveVerticalSpacer() }
                        items(count = tvShowLazyPagingItems.itemCount) { index ->
                            val tvShow = tvShowLazyPagingItems[index]!!
                            val path = ImageUtil.buildImageUrl(tvShow.posterPath)
                            MoviePoster(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(ASPECT_RATIO),
                                posterUrl = path,
                                shimmer = shimmerInstance,
                                contentDescription = tvShow.name,
                                onClick = { onTVShowClick(tvShow.id) },
                            )
                        }

                        if (tvShowLazyPagingItems.loadState.append == LoadState.Loading) {
                            item(span = StaggeredGridItemSpan.FullLine) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .wrapContentHeight(),
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .testTag(APPEND_LOAD_PROGRESS_TEST_TAG)
                                            .align(Alignment.Center)
                                            .padding(IheNkiri.spacing.oneAndHalf),
                                    )
                                }
                            }
                        }
                    },
                )

                if (tvShowLazyPagingItems.loadState.refresh == LoadState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .testTag(FRESH_LOAD_PROGRESS_TEST_TAG)
                            .align(Alignment.Center)
                            .size(30.dp)
                            .padding(vertical = IheNkiri.spacing.one),
                        strokeWidth = 1.dp,
                        strokeCap = StrokeCap.Round,
                    )
                }

                MovieListFilter(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .testTag(CHIP_GROUP_TEST_TAG),
                    items = filters.map { Pair(filterLabelFor(it), it.isSelected) },
                    onItemSelected = { filterItemSelected(filters[it].type) },
                )
            }
        }
    }
}

@Composable
private fun filterLabelFor(filterItem: TVShowsFilterItem): String {
    return when (filterItem.type) {
        TVShowsFilterItem.FilterType.AIRING_TODAY -> stringResource(R.string.airing_today)
        TVShowsFilterItem.FilterType.ON_THE_AIR -> stringResource(R.string.on_the_air)
        TVShowsFilterItem.FilterType.POPULAR -> stringResource(R.string.popular)
        TVShowsFilterItem.FilterType.TOP_RATED -> stringResource(R.string.top_rated)
        TVShowsFilterItem.FilterType.DISCOVER -> stringResource(R.string.discover)
    }
}

@ExcludeFromGeneratedCoverageReport
private fun testFilters() = listOf(
    TVShowsFilterItem(
        isSelected = false,
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

@ExcludeFromGeneratedCoverageReport
private fun testTvShows() = listOf(
    TVShow(
        id = 667538,
        name = "Transformers: Rise of the Beasts",
        overview =
            """
                When a new threat capable of destroying the entire planet emerges, Optimus Prime and 
                the Autobots must team up with a powerful faction known as the Maximals. With the 
                fate of humanity hanging in the balance, humans Noah and Elena will do whatever it takes 
                to help the Transformers as they engage in the ultimate battle to save Earth.
            """.trimIndent(),
        backdropPath = "/bz66a19bR6BKsbY8gSZCM4etJiK.jpg",
        posterPath = "/2vFuG6bWGyQUzYS9d69E5l85nIz.jpg",
        voteAverage = 7.5,
    ),
    TVShow(
        id = 298618,
        name = "The Flash",
        overview =
            """
                When his attempt to save his family inadvertently alters the future, 
                Barry Allen becomes trapped in a reality in which General Zod has returned and 
                there are no Super Heroes to turn to. In order to save the world that he is in and 
                return to the future that he knows, Barry's only hope is to race for his life. But 
                will making the ultimate sacrifice be enough to reset the universe
            """.trimIndent(),
        backdropPath = "/yF1eOkaYvwiORauRCPWznV9xVvi.jpg",
        posterPath = "/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
        voteAverage = 6.5,
    ),
)
