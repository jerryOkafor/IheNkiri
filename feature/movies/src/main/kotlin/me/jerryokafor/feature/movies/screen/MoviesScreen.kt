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
@file:Suppress("TooManyFunctions")

package me.jerryokafor.feature.movies.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import me.jerryokafor.core.common.annotation.ExcludeFromJacocoGeneratedReport
import me.jerryokafor.core.data.util.ImageUtil
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.OneVerticalSpacer
import me.jerryokafor.core.ds.theme.TwoAndHalfVerticalSpacer
import me.jerryokafor.core.model.Movie
import me.jerryokafor.core.model.MovieListFilterItem
import me.jerryokafor.core.ui.components.Background
import me.jerryokafor.core.ui.components.MovieListFilter
import me.jerryokafor.core.ui.components.MoviePoster
import me.jerryokafor.feature.movies.R
import me.jerryokafor.feature.movies.viewmodel.MoviesViewModel

const val TITLE_TEST_TAG = "title"
const val CHIP_GROUP_TEST_TAG = "chips"
const val GRID_ITEMS_TEST_TAG = "gridItems"

@Suppress("MagicNumber")
@ThemePreviews
@Composable
@ExcludeFromJacocoGeneratedReport
fun MoviesScreenPreview() {
    IheNkiriTheme {
        MoviesScreen(
            movies = listOf(
                Movie(
                    id = 667538,
                    title = "Transformers: Rise of the Beasts",
                    overview = """
        When a new threat capable of destroying the entire planet emerges, Optimus Prime and 
        the Autobots must team up with a powerful faction known as the Maximals. With the 
        fate of humanity hanging in the balance, humans Noah and Elena will do whatever it takes 
        to help the Transformers as they engage in the ultimate battle to save Earth.
                    """.trimIndent(),
                    backdropPath = "/bz66a19bR6BKsbY8gSZCM4etJiK.jpg",
                    posterPath = "/2vFuG6bWGyQUzYS9d69E5l85nIz.jpg",
                    voteAverage = 7.5,
                ),
                Movie(
                    id = 298618,
                    title = "The Flash",
                    overview = """
            When his attempt to save his family inadvertently alters the future, 
            Barry Allen becomes trapped in a reality in which General Zod has returned and 
            there are no Super Heroes to turn to. In order to save the world that he is in and 
            return to the future that he knows, Barry's only hope is to race for his life. But 
            will making the ultimate sacrifice be enough to reset the universe
                    """.trimIndent(),
                    backdropPath = "/yF1eOkaYvwiORauRCPWznV9xVvi.jpg",
                    posterPath = "/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
                    voteAverage = 7.0,
                ),
            ),
            loading = false,
            filters = listOf(
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
            ),
            onSearchClick = {},
            onMovieClick = {},
            onFilterItemSelected = {},
        )
    }
}

@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    onSearchClick: () -> Unit,
    onMovieClick: (movieId: Long) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val onItemSelected: (MovieListFilterItem.FilterType) -> Unit = {
        viewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(it))
    }

    MoviesScreen(
        movies = uiState.value.movies,
        loading = uiState.value.loading,
        filters = uiState.value.availableFilters,
        onSearchClick = onSearchClick,
        onMovieClick = onMovieClick,
        onFilterItemSelected = onItemSelected,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    movies: List<Movie>,
    loading: Boolean,
    filters: List<MovieListFilterItem> = emptyList(),
    onSearchClick: () -> Unit,
    onMovieClick: (movieId: Long) -> Unit,
    onFilterItemSelected: (MovieListFilterItem.FilterType) -> Unit = {},
) {
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    Background {
        Column(
            modifier = Modifier
                .padding(horizontal = IheNkiri.spacing.twoAndaHalf)
                .padding(bottom = IheNkiri.spacing.twoAndaHalf),
        ) {
            CenterAlignedTopAppBar(
                modifier = Modifier.testTag(TITLE_TEST_TAG),
                title = {
                    Text(
                        text = stringResource(R.string.movies_title),
                        style = IheNkiri.typography.titleMedium,
                        color = IheNkiri.color.onPrimary,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
                actions = {
                    Button(onClick = onSearchClick) {
                        Icon(
                            painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.search),
                            contentDescription = stringResource(R.string.movies_content_description_search),
                        )
                    }
                },
            )
            MovieListFilter(
                modifier = Modifier.testTag(CHIP_GROUP_TEST_TAG),
                filters = filters,
                onItemSelected = onFilterItemSelected,
            )
            TwoAndHalfVerticalSpacer()
            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .testTag(GRID_ITEMS_TEST_TAG)
                        .fillMaxSize(),
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = IheNkiri.spacing.oneAndHalf,
                    contentPadding = PaddingValues(bottom = IheNkiri.spacing.twoAndaHalf),
                    horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.oneAndHalf),
                    content = {
                        itemsIndexed(
                            items = movies,
                            key = { index, item -> "${item.id}$index" },
                        ) { _, item ->
                            val path = ImageUtil.buildImageUrl(item.posterPath)
                            MoviePoster(
                                path = path,
                                shimmer = shimmerInstance,
                                contentDescription = item.title,
                                onClick = { onMovieClick(item.id) },
                            )
                        }

                        item { OneVerticalSpacer() }
                    },
                )
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
