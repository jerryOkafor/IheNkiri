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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import me.jerryokafor.core.data.util.ImageUtil
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.OneVerticalSpacer
import me.jerryokafor.core.ds.theme.TwoAndHalfVerticalSpacer
import me.jerryokafor.core.model.Movie
import me.jerryokafor.feature.movies.R
import me.jerryokafor.feature.movies.model.MovieListFilterItem
import me.jerryokafor.feature.movies.viewmodel.MoviesViewModel

const val TITLE_TEST_TAG = "title"
const val SEARCH_TEST_TAG = "search"
const val CHIP_GROUP_TEST_TAG = "chips"
const val GRID_ITEMS_TEST_TAG = "gridItems"

@Suppress("MagicNumber")
@ThemePreviews
@Composable
private fun MoviesScreenPreview() {
    IheNkiriTheme {
        MoviesScreen(
            movies = testMovies().takeLast(4),
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
            onItemSelected = {},
        )
    }
}

@Composable
fun MoviesScreen(viewModel: MoviesViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val onItemSelected: (MovieListFilterItem.FilterType) -> Unit = {
        viewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(it))
    }

    MoviesScreen(
        movies = uiState.value.movies,
        loading = uiState.value.loading,
        filters = uiState.value.availableFilters,
        onItemSelected = onItemSelected,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesScreen(
    movies: List<Movie>,
    loading: Boolean,
    filters: List<MovieListFilterItem> = emptyList(),
    onItemSelected: (MovieListFilterItem.FilterType) -> Unit = {},
) {
    @Suppress("MagicNumber")
    val colorStops = listOf(
        IheNkiri.color.primary,
        Color(0x00000000),
    )

    val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(IheNkiri.color.surface),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colorStops)),
        ) {
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
                )
                SearchBar(
                    modifier = Modifier
                        .testTag(SEARCH_TEST_TAG)
                        .fillMaxWidth(),
                )
                TwoAndHalfVerticalSpacer()
                MovieListFilter(
                    modifier = Modifier.testTag(CHIP_GROUP_TEST_TAG),
                    filters = filters,
                    onItemSelected = onItemSelected,
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
                                ) {}
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
}

fun testMovies(): List<Movie> = listOf(
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
    Movie(
        id = 884605,
        title = "No Hard Feelings",
        overview = """
            On the brink of losing her childhood home, Maddie discovers an intriguing job listing: 
            wealthy helicopter parents looking for someone to “date” their introverted 19-year-old 
            son, Percy, before he leaves for college. To her surprise, Maddie soon discovers the 
            awkward Percy is no sure thing.
        """.trimIndent(),
        backdropPath = "/rRcNmiH55Tz0ugUsDUGmj8Bsa4V.jpg",
        posterPath = "/4K7gQjD19CDEPd7A9KZwr2D9Nco.jpg",
        voteAverage = 7.1,
    ),
    Movie(
        id = 346698,
        title = "Barbie",
        overview = """
            Barbie and Ken are having the time of their lives in the colorful and seemingly 
            perfect world of Barbie Land. However, when they get a chance to go to the real world, 
            they soon discover the joys and perils of living among humans.
        """.trimIndent(),
        backdropPath = "/nHf61UzkfFno5X1ofIhugCPus2R.jpg",
        posterPath = "/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg",
        voteAverage = 7.5,
    ),
    Movie(
        id = 615656,
        title = "Meg 2: The Trench",
        overview = """
            An exploratory dive into the deepest depths of the ocean of a daring research team 
            spirals into chaos when a malevolent mining operation threatens their mission and 
            forces them into a high-stakes battle for survival
        """.trimIndent(),
        backdropPath = "/zN41DPmPhwmgJjHwezALdrdvD0h.jpg",
        posterPath = "/4m1Au3YkjqsxF8iwQy0fPYSxE0h.jpg",
        voteAverage = 7.0,
    ),
    Movie(
        id = 976573,
        title = "Elemental",
        overview = """
            In a city where fire, water, land and air residents live together, a fiery young woman 
            and a go-with-the-flow guy will discover something elemental: how much they have in common.
        """.trimIndent(),
        backdropPath = "/jZIYaISP3GBSrVOPfrp98AMa8Ng.jpg",
        posterPath = "/8riWcADI1ekEiBguVB9vkilhiQm.jpg",
        voteAverage = 7.7,
    ),
    Movie(
        id = 1006462,
        title = "The Flood",
        overview = """
            A horde of giant hungry alligators is unleashed on a group of in-transit 
            prisoners and their guards after a massive hurricane floods Louisiana.
        """.trimIndent(),
        backdropPath = "/bz66a19bR6BKsbY8gSZCM4etJiK.jpg",
        posterPath = "/mvjqqklMpHwOxc40rn7dMhGT0Fc.jpg",
        voteAverage = 6.8,
    ),
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
    Movie(
        id = 884605,
        title = "No Hard Feelings",
        overview = """
            On the brink of losing her childhood home, Maddie discovers an intriguing job listing: 
            wealthy helicopter parents looking for someone to “date” their introverted 19-year-old 
            son, Percy, before he leaves for college. To her surprise, Maddie soon discovers the 
            awkward Percy is no sure thing.
        """.trimIndent(),
        backdropPath = "/rRcNmiH55Tz0ugUsDUGmj8Bsa4V.jpg",
        posterPath = "/4K7gQjD19CDEPd7A9KZwr2D9Nco.jpg",
        voteAverage = 7.1,
    ),
    Movie(
        id = 346698,
        title = "Barbie",
        overview = """
            Barbie and Ken are having the time of their lives in the colorful and seemingly 
            perfect world of Barbie Land. However, when they get a chance to go to the real world, 
            they soon discover the joys and perils of living among humans.
        """.trimIndent(),
        backdropPath = "/nHf61UzkfFno5X1ofIhugCPus2R.jpg",
        posterPath = "/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg",
        voteAverage = 7.5,
    ),
    Movie(
        id = 615656,
        title = "Meg 2: The Trench",
        overview = """
            An exploratory dive into the deepest depths of the ocean of a daring research team 
            spirals into chaos when a malevolent mining operation threatens their mission and 
            forces them into a high-stakes battle for survival
        """.trimIndent(),
        backdropPath = "/zN41DPmPhwmgJjHwezALdrdvD0h.jpg",
        posterPath = "/4m1Au3YkjqsxF8iwQy0fPYSxE0h.jpg",
        voteAverage = 7.0,
    ),
    Movie(
        id = 976573,
        title = "Elemental",
        overview = """
            In a city where fire, water, land and air residents live together, a fiery young woman 
            and a go-with-the-flow guy will discover something elemental: how much they have in common.
        """.trimIndent(),
        backdropPath = "/jZIYaISP3GBSrVOPfrp98AMa8Ng.jpg",
        posterPath = "/8riWcADI1ekEiBguVB9vkilhiQm.jpg",
        voteAverage = 7.7,
    ),
    Movie(
        id = 1006462,
        title = "The Flood",
        overview = """
            A horde of giant hungry alligators is unleashed on a group of in-transit 
            prisoners and their guards after a massive hurricane floods Louisiana.
        """.trimIndent(),
        backdropPath = "/bz66a19bR6BKsbY8gSZCM4etJiK.jpg",
        posterPath = "/mvjqqklMpHwOxc40rn7dMhGT0Fc.jpg",
        voteAverage = 6.8,
    ),
)
