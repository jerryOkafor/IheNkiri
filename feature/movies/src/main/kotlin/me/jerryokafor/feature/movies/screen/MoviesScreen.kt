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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
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
import me.jerryokafor.core.ds.theme.FillingSpacer
import me.jerryokafor.core.ds.theme.FiveVerticalSpacer
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.TwoVerticalSpacer
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
const val SEARCH_TEST_TAG = "search"
const val ASPECT_RATIO = 0.7F
const val FRESH_LOAD_PROGRESS_TEST_TAG = "fresh_load"
const val APPEND_LOAD_PROGRESS_TEST_TAG = "append_load"

@Suppress("MagicNumber")
@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun MoviesScreenPreview() {
    IheNkiriTheme {
        MoviesScreen(
            movieLazyPagingItems = flowOf(PagingData.from(testMovies())).collectAsLazyPagingItems(),
            filters = testFilters(),
            onMovieClick = {},
        ) {}
    }
}

@Composable
@ExcludeFromGeneratedCoverageReport
fun MoviesScreen(
    viewModel: MoviesViewModel = hiltViewModel(),
    onMovieClick: (movieId: Long) -> Unit,
) {
    val availableFilters by viewModel.availableFilters.collectAsStateWithLifecycle()
    val movieLazyPagingItems = viewModel.movies.collectAsLazyPagingItems()
    val onItemSelected: (MovieListFilterItem.FilterType) -> Unit = {
        viewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(it))
    }

    MoviesScreen(
        movieLazyPagingItems = movieLazyPagingItems,
        filters = availableFilters,
        onMovieClick = onMovieClick,
        onFilterItemSelected = onItemSelected,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    movieLazyPagingItems: LazyPagingItems<Movie>,
    filters: List<MovieListFilterItem> = emptyList(),
    onMovieClick: (movieId: Long) -> Unit,
    onFilterItemSelected: (MovieListFilterItem.FilterType) -> Unit = {},
) {
    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
    var query by rememberSaveable { mutableStateOf("") }
    var showSearch by rememberSaveable { mutableStateOf(false) }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    val onSearchClick: () -> Unit = {
        showSearch = true
        isSearchActive = true
    }

    val filterItemSelected: (MovieListFilterItem.FilterType) -> Unit = {
        when (it) {
            MovieListFilterItem.FilterType.DISCOVER -> {
                showSearch = true
                isSearchActive = true
            }

            else -> {
                showSearch = false
                onFilterItemSelected(it)
            }
        }
    }

    Background {
        Column(modifier = Modifier) {
            Column(modifier = Modifier.padding(horizontal = IheNkiri.spacing.twoAndaHalf)) {
                Crossfade(targetState = showSearch, label = "toggleSearch") { show ->
                    when {
                        show -> {
                            SearchBarRow(
                                modifier = Modifier.statusBarsPadding(),
                                isSearchActive = isSearchActive,
                                query = query,
                                onQueryChange = { query = it },
                                onSearch = { isSearchActive = false },
                                onActiveChange = { showSearch = it },
                                onCancel = { showSearch = false },
                            )
                        }

                        else -> {
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
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .padding(horizontal = IheNkiri.spacing.twoAndaHalf)
                        .testTag(GRID_ITEMS_TEST_TAG)
                        .fillMaxSize(),
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = IheNkiri.spacing.oneAndHalf,
                    contentPadding = PaddingValues(bottom = IheNkiri.spacing.twoAndaHalf),
                    horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.oneAndHalf),
                    content = {
                        item(span = StaggeredGridItemSpan.FullLine) { FiveVerticalSpacer() }
                        items(count = movieLazyPagingItems.itemCount) { index ->
                            val movie = movieLazyPagingItems[index]!!
                            val path = ImageUtil.buildImageUrl(movie.posterPath)
                            MoviePoster(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(ASPECT_RATIO),
                                posterUrl = path,
                                shimmer = shimmerInstance,
                                contentDescription = movie.title,
                                onClick = { onMovieClick(movie.id) },
                            )
                        }

                        if (movieLazyPagingItems.loadState.append == LoadState.Loading) {
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

                if (movieLazyPagingItems.loadState.refresh == LoadState.Loading) {
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
                    items = filters.map { Pair(it.label, it.isSelected) },
                    onItemSelected = { filterItemSelected(filters[it].type) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarRow(
    modifier: Modifier = Modifier,
    isSearchActive: Boolean = false,
    isSearching: Boolean = false,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    onCancel: () -> Unit = {},
) {
    var includeAdult by rememberSaveable { mutableStateOf(false) }
    var includeVideo by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = modifier
            .testTag(SEARCH_TEST_TAG)
            .semantics { traversalIndex = -1f },
        query = query,
        shape = IheNkiri.shape.large,
        tonalElevation = SearchBarDefaults.Elevation,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = isSearchActive,
        onActiveChange = onActiveChange,
        colors = SearchBarDefaults.colors(
            containerColor = IheNkiri.color.primary,
            inputFieldColors = TextFieldDefaults.colors(
                focusedTextColor = contentColorFor(backgroundColor = IheNkiri.color.primary),
                unfocusedTextColor = contentColorFor(backgroundColor = IheNkiri.color.primary),
            ),
        ),
        placeholder = {
            Text(
                text = "Search ....",
                color = contentColorFor(backgroundColor = IheNkiri.color.primary),
            )
        },
        leadingIcon = {
            IconButton(onClick = { onSearch(query) }) {
                Icon(
                    painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.search),
                    contentDescription = "perform search",
                    tint = contentColorFor(IheNkiri.color.primary),
                )
            }
        },
        trailingIcon = {
            IconButton(onClick = onCancel) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "close search",
                    tint = contentColorFor(IheNkiri.color.primary),
                )
            }
        },
    ) {
        AnimatedVisibility(visible = isSearching) {
            LinearProgressIndicator(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth(),
                strokeCap = StrokeCap.Round,
            )
        }

        TwoVerticalSpacer()
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Row(
                modifier = Modifier
                    .padding(
                        horizontal = IheNkiri.spacing.two,
                    )
                    .border(
                        1.dp,
                        color = IheNkiri.color.onPrimary.copy(alpha = 0.5f),
                        shape = IheNkiri.shape.medium,
                    )
                    .padding(
                        horizontal = IheNkiri.spacing.two,
                    )
                    .weight(1F),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.include_adult),
                    style = IheNkiri.typography.titleSmall,
                )
                FillingSpacer()
                Switch(checked = includeAdult, onCheckedChange = { includeAdult = it })
            }

            Row(
                modifier = Modifier
                    .padding(
                        horizontal = IheNkiri.spacing.two,
                    )
                    .border(
                        1.dp,
                        color = IheNkiri.color.onPrimary.copy(alpha = 0.5f),
                        shape = IheNkiri.shape.medium,
                    )
                    .padding(
                        horizontal = IheNkiri.spacing.two,
                    )
                    .weight(1F),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.include_video),
                    style = IheNkiri.typography.titleSmall,
                )
                FillingSpacer()
                Switch(checked = includeVideo, onCheckedChange = { includeVideo = it })
            }
        }
    }
}

@ExcludeFromGeneratedCoverageReport
private fun testFilters() = listOf(
    MovieListFilterItem(
        label = "Discover",
        isSelected = true,
        type = MovieListFilterItem.FilterType.NOW_PLAYING,
    ),
    MovieListFilterItem(
        label = "Now Playing",
        isSelected = false,
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

@ExcludeFromGeneratedCoverageReport
private fun testMovies() = listOf(
    Movie(
        id = 667538,
        title = "Transformers: Rise of the Beasts",
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
    Movie(
        id = 298618,
        title = "The Flash",
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
        voteAverage = 7.0,
    ),
)
