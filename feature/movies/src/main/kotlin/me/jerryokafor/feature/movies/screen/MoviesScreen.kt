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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import me.jerryokafor.core.data.util.ImageUtil
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.EightVerticalSpacer
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.TwoAndHalfVerticalSpacer
import me.jerryokafor.core.model.Movie
import me.jerryokafor.feature.movies.viewmodel.MoviesViewModel

const val SEARCH_TEST_TAG = "search"
const val CHIP_GROUP_TEST_TAG = "chips"
const val GRID_ITEMS_TEST_TAG = "gridItems"

@ThemePreviews
@Composable
private fun MoviesScreenPreview() {
    IheNkiriTheme {
        MoviesScreen(
            movies = testMovies(),
            filters = listOf(
                Chip(label = "Now Playing", isSelected = true, type = Chip.FilterType.NOW_PLAYING),
                Chip(label = "Popular", isSelected = false, type = Chip.FilterType.POPULAR),
                Chip(label = "Top Rated", isSelected = false, type = Chip.FilterType.TOP_RATED),
                Chip(label = "Upcoming", isSelected = false, type = Chip.FilterType.UPCOMING),
            ),
            onItemSelected = {},
        )
    }
}

@Composable
fun MoviesScreen(viewModel: MoviesViewModel = viewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val onItemSelected: (Chip.FilterType) -> Unit = {
        viewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(it))
    }

    MoviesScreen(
        movies = uiState.value.movies,
        filters = uiState.value.availableFilters,
        onItemSelected = onItemSelected,
    )
}

@Composable
fun MoviesScreen(
    movies: List<Movie>,
    filters: List<Chip> = emptyList(),
    onItemSelected: (Chip.FilterType) -> Unit = {},
) {
    @Suppress("MagicNumber")
    val colorStops = listOf(
        IheNkiri.color.primary,
        Color(0x00000000),
    )
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
                    .systemBarsPadding()
                    .padding(IheNkiri.spacing.twoAndaHalf),
            ) {
                SearchBar(
                    modifier = Modifier
                        .testTag(SEARCH_TEST_TAG)
                        .fillMaxWidth(),
                )
                TwoAndHalfVerticalSpacer()
                ChipGroup(
                    modifier = Modifier.testTag(CHIP_GROUP_TEST_TAG),
                    filters = filters,
                    onItemSelected = onItemSelected,
                )
                TwoAndHalfVerticalSpacer()

                val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
                LazyVerticalStaggeredGrid(
                    modifier = Modifier
                        .testTag(GRID_ITEMS_TEST_TAG)
                        .fillMaxSize(),
                    columns = StaggeredGridCells.Fixed(2),
                    verticalItemSpacing = IheNkiri.spacing.oneAndHalf,
                    horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.oneAndHalf),
                    content = {
                        itemsIndexed(
                            items = movies,
                            key = { index, item -> "${item.id}$index" },
                        ) { _, item ->
                            val path = ImageUtil.buildImageUrl(item.posterPath)
                            Poster(
                                path = path,
                                shimmer = shimmerInstance,
                                contentDescription = item.title,
                            ) {}
                        }
                        item { EightVerticalSpacer() }
                    },
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun PosterPreview() {
    IheNkiriTheme {
        Column(modifier = Modifier.padding(IheNkiri.spacing.twoAndaHalf)) {
            Poster(
                path = "https://example.com/image.jpg",
                contentDescription = "Image",
                shimmer = rememberShimmer(
                    shimmerBounds = ShimmerBounds.Window,
                ),
            ) {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Poster(
    modifier: Modifier = Modifier,
    path: String,
    contentDescription: String,
    shimmer: Shimmer,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = IheNkiri.shape.medium,
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current).data(path).crossfade(true).build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = contentDescription,
        ) {
            val state = painter.state
            val showShimmer = state is AsyncImagePainter.State.Loading
            val shimmerModifier = if (showShimmer) {
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(
                        @Suppress("MagicNumber")
                        listOf(0.6f, 0.8f, 1.5f)
                            .shuffled()
                            .first(),
                    )
            } else {
                Modifier.fillMaxWidth()
            }

            ShimmerBox(
                modifier = shimmerModifier,
                shimmer = shimmer,
                showShimmer = showShimmer,
            ) {
                if (state !is AsyncImagePainter.State.Loading) {
                    SubcomposeAsyncImageContent()
                } else {
                    // Show empty place holder here
                }
            }
        }
    }
}

@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    showShimmer: Boolean,
    shimmer: Shimmer,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.background(Color.Transparent)) {
        if (showShimmer) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .shimmer(shimmer)
                    .background(Color.Black.copy(alpha = 0.8f)),
            )
        } else {
            content()
        }
    }
}

@ThemePreviews
@Composable
private fun SearchBarPreview() {
    IheNkiriTheme {
        Column(modifier = Modifier.padding(IheNkiri.spacing.twoAndaHalf)) {
            SearchBar()
        }
    }
}

@Composable
private fun SearchBar(modifier: Modifier = Modifier, onSearch: (searchQuery: String) -> Unit = {}) {
    val searching by remember { mutableStateOf(false) }
    val trailingIcon = if (searching) {
        me.jerryokafor.core.ui.R.drawable.baseline_cancel_24
    } else {
        me.jerryokafor.core.ui.R.drawable.search
    }
    val contentDescription = if (searching) "Close" else "Search"
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = modifier,
        value = searchQuery,
        placeholder = { Text(text = "the end game") },
        onValueChange = { searchQuery = it },
        shape = IheNkiri.shape.pill,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(searchQuery) }),
        trailingIcon = {
            Icon(
                painter = painterResource(id = trailingIcon),
                tint = IheNkiri.color.tertiaryContainer,
                contentDescription = contentDescription,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = IheNkiri.color.onPrimaryContainer.copy(alpha = 0.7f),
            unfocusedContainerColor = IheNkiri.color.onPrimaryContainer.copy(alpha = 0.7f),
            focusedPlaceholderColor = IheNkiri.color.onPrimary.copy(alpha = 0.5F),
            unfocusedPlaceholderColor = IheNkiri.color.onPrimary.copy(alpha = 0.8f),
            focusedTextColor = IheNkiri.color.onPrimary,
            unfocusedTextColor = IheNkiri.color.onPrimary.copy(alpha = 0.4F),
        ),
    )
}

@ThemePreviews
@Composable
private fun ChipGroupPreview() {
    IheNkiriTheme {
        ChipGroup(
            filters = listOf(
                Chip(label = "Now Playing", isSelected = true, type = Chip.FilterType.NOW_PLAYING),
                Chip(label = "Popular", isSelected = false, type = Chip.FilterType.POPULAR),
                Chip(label = "Top Rated", isSelected = false, type = Chip.FilterType.TOP_RATED),
                Chip(label = "Upcoming", isSelected = false, type = Chip.FilterType.UPCOMING),
            ),
        ) {}
    }
}

data class Chip(val label: String, val isSelected: Boolean, val type: FilterType) {
    enum class FilterType {
        NOW_PLAYING, POPULAR, TOP_RATED, UPCOMING
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChipGroup(
    modifier: Modifier = Modifier,
    filters: List<Chip>,
    onItemSelected: (Chip.FilterType) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.twoAndaHalf),
    ) {
        items(filters) {
            FilterChip(
                selected = it.isSelected,
                onClick = { onItemSelected(it.type) },
                label = { Text(text = it.label) },
                shape = IheNkiri.shape.pill,
                colors = FilterChipDefaults.filterChipColors(
                    labelColor = IheNkiri.color.onPrimary.copy(alpha = 0.7f),
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = IheNkiri.color.onPrimary.copy(alpha = 0.7f),
                ),
            )
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
