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

package me.jerryokafor.ihenkiri.feature.moviedetails.ui

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.data.util.ImageUtil
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.OneAndHalfVerticalSpacer
import me.jerryokafor.core.ds.theme.OneVerticalSpacer
import me.jerryokafor.core.ds.theme.ThreeVerticalSpacer
import me.jerryokafor.core.ds.theme.TwoVerticalSpacer
import me.jerryokafor.core.ui.components.GenreChip
import me.jerryokafor.core.ui.components.MoviePoster
import me.jerryokafor.core.ui.components.TrailerButton
import me.jerryokafor.core.ui.widget.MovieRating
import me.jerryokafor.core.ui.widget.PeoplePoster
import me.jerryokafor.ihenkiri.feature.moviedetails.R
import me.jerryokafor.ihenkiri.feature.moviedetails.viewmodel.MoviesDetailViewModel
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

const val MOVIE_DETAILS_COL = "movies_details_col"
const val MOVIE_DETAILS_OVERVIEW = "movies_details_overview"
const val MOVIE_DETAILS_TRAILER_BUTTON = "movies_details_trailer_button"
const val MOVIE_DETAILS_MAIN_CAST = "movies_details_main_cast"
const val MOVIE_DETAILS_MAIN_CAST_ROW = "movies_details_main_cast_roe"
const val MOVIE_DETAILS_CREW = "movies_details_bottom_crew"
const val MOVIE_DETAILS_CREW_ROW = "movies_details_bottom_crew_row"
const val MOVIE_DETAILS_CATEGORIES = "movies_details_categories"
const val MOVIE_DETAILS_CATEGORIES_ROW = "movies_details_categories_row"
const val MOVIE_DETAILS_RECOMMENDATIONS = "movies_details_recommendations"
const val MOVIE_DETAILS_RECOMMENDATIONS_ROW = "movies_details_recommendations_row"
const val MOVIE_DETAILS_BOTTOM_BAR = "movies_details_bottom_bar"

@ExcludeFromGeneratedCoverageReport
private fun testUIState() = MoviesDetailViewModel.UIState(
    title = "Fight Club",
    overview = """
                A ticking-time-bomb insomniac and a slippery soap salesman channel primal male
                 aggression into a shocking new form of therapy. Their concept catches on, with
                 underground \"fight clubs\" forming in every town, until an eccentric gets in the
                 way and ignites an out-of-control spiral toward oblivion.
    """.trimIndent(),
    releaseDate = "2023/09/15",
    runtime = "1hr 43m",
)

@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun MoviesDetailsPreview() {
    IheNkiriTheme {
        MoviesDetails(uiState = testUIState(), onNavigateUp = {})
    }
}

@Composable
@Suppress("UnusedPrivateMember")
@ExcludeFromGeneratedCoverageReport
fun MoviesDetails(
    viewModel: MoviesDetailViewModel = hiltViewModel(),
    onBackPress: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    val onAddToWatchListClick: () -> Unit = {}
    val onAddToBookmarkClick: () -> Unit = {}
    val onAddToFavorite: () -> Unit = {}
    val onRateItClick: () -> Unit = {}
    val onWatchTrailerClick: () -> Unit = {}

    MoviesDetails(
        uiState = uiState.value,
        onAddToWatchListClick = onAddToWatchListClick,
        onAddToBookmarkClick = onAddToBookmarkClick,
        onAddToFavorite = onAddToFavorite,
        onRateItClick = onRateItClick,
        onWatchTrailerClick = onWatchTrailerClick,
        onNavigateUp = onBackPress,
    )
}

@Composable
@Suppress("UnusedPrivateMember")
fun MoviesDetails(
    uiState: MoviesDetailViewModel.UIState,
    onAddToWatchListClick: () -> Unit = {},
    onAddToBookmarkClick: () -> Unit = {},
    onAddToFavorite: () -> Unit = {},
    onRateItClick: () -> Unit = {},
    onWatchTrailerClick: () -> Unit = {},
    onNavigateUp: () -> Unit,
) {
    var showBottomAppBar by remember { mutableStateOf(false) }
    val primaryTextColor = contentColorFor(IheNkiri.color.inverseOnSurface)

    @Suppress("MagicNumber")
    val secondaryTextColor = primaryTextColor.copy(0.7F)
    val state = rememberCollapsingToolbarScaffoldState()
    val enabled by remember { mutableStateOf(true) }
    val colorStops = listOf(
        Color(0x00000000),
        IheNkiri.color.inverseOnSurface.copy(alpha = 0.1F),
        IheNkiri.color.inverseOnSurface.copy(alpha = 0.5F),
        IheNkiri.color.inverseOnSurface.copy(alpha = 0.8F),
        IheNkiri.color.inverseOnSurface,
    )

    val colorStops2 = listOf(
        IheNkiri.color.inverseOnSurface.copy(alpha = 0.5F),
        IheNkiri.color.inverseOnSurface.copy(alpha = 0.8F),
        IheNkiri.color.inverseOnSurface.copy(alpha = 0.9F),
        IheNkiri.color.inverseOnSurface,
    )

    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val isLocalInspection = LocalInspectionMode.current

    val posterImageLoader = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(ImageUtil.buildImageUrl(uiState.postPath))
            .crossfade(true)
            .build(),
        contentScale = ContentScale.FillBounds,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    Box(modifier = Modifier.fillMaxSize()) {
        CollapsingToolbarScaffold(
            modifier = Modifier.fillMaxSize(),
            state = state,
            scrollStrategy = ScrollStrategy.EnterAlways,
            toolbarModifier = Modifier.background(IheNkiri.color.primary),
            enabled = enabled,
            toolbar = {
                // Collapsing toolbar collapses its size as small as the that of
                // a smallest child. To make the toolbar collapse to 50dp, we create
                // a dummy Spacer composable.
                // You may replace it with TopAppBar or other preferred composable.
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                )

                @Suppress("MagicNumber")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .parallax(0.5f)
                        .height(450.dp)
                        .fillMaxWidth(),
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = state.toolbarState.progress
                            },
                        painter = if (isError.not() && !isLocalInspection) {
                            posterImageLoader
                        } else {
                            painterResource(me.jerryokafor.core.ui.R.drawable.sample_poster)
                        },
                        contentDescription = uiState.title,
                        contentScale = ContentScale.FillWidth,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.verticalGradient(colorStops)),
                    )

                    Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = IheNkiri.spacing.two),
                            horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.two),
                        ) {
                            MovieRating(modifier = Modifier.size(57.dp), rating = uiState.rating)
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(IheNkiri.spacing.half),
                            ) {
                                Text(
                                    text = uiState.title,
                                    style = IheNkiri.typography.titleLarge,
                                    color = primaryTextColor,
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = uiState.releaseDate,
                                        color = secondaryTextColor,
                                    )
                                    Text(text = "●", color = secondaryTextColor)
                                    Icon(
                                        painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.ic_clock),
                                        contentDescription = "runtime",
                                        tint = secondaryTextColor,
                                    )
                                    Text(
                                        text = uiState.runtime,
                                        color = secondaryTextColor,
                                    )
                                }
                            }
                        }

                        TwoVerticalSpacer()
                        Divider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(secondaryTextColor.copy(alpha = 0.1F)),
                        )
                    }

                    // Fancy top nav bar
                    Row(
                        modifier = Modifier.statusBarsPadding(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.two),
                    ) {
                        Surface(
                            modifier = Modifier.padding(IheNkiri.spacing.one),
                            color = Color.LightGray.copy(alpha = 0.3F),
                            shape = CircleShape,
                        ) {
                            IconButton(
                                onClick = onNavigateUp,
                                enabled = uiState.videos.isNotEmpty(),
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }
                        }
                        Text(uiState.title, style = IheNkiri.typography.titleLarge)
                    }
                }
            },
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .matchParentSize()
                    .background(Brush.verticalGradient(colorStops2))
                    .testTag(MOVIE_DETAILS_COL),
                //                    .background(IheNkiri.color.inverseOnSurface),
            ) {
                // Overview
                item {
                    TwoVerticalSpacer()
                    Text(
                        modifier = Modifier
                            .testTag(MOVIE_DETAILS_OVERVIEW)
                            .padding(horizontal = IheNkiri.spacing.two),
                        text = uiState.overview,
                        style = IheNkiri.typography.bodyMedium,
                        textAlign = TextAlign.Justify,
                        color = secondaryTextColor,
                    )
                }

                // Trailer button
                item {
                    ThreeVerticalSpacer()
                    TrailerButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(MOVIE_DETAILS_TRAILER_BUTTON)
                            .padding(horizontal = IheNkiri.spacing.two),
                        onClick = onWatchTrailerClick,
                    )
                }

                // Main cast
                item {
                    ThreeVerticalSpacer()
                    Text(
                        modifier = Modifier
                            .testTag(MOVIE_DETAILS_MAIN_CAST)
                            .padding(horizontal = IheNkiri.spacing.two),
                        style = IheNkiri.typography.titleMedium,
                        color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
                        text = stringResource(R.string.title_main_cast),
                    )
                    OneVerticalSpacer()
                    LazyRow(
                        modifier = Modifier
                            .testTag(MOVIE_DETAILS_MAIN_CAST_ROW)
                            .padding(horizontal = IheNkiri.spacing.two),
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                    ) {
                        items(uiState.cast) {
                            val (firstName, lastName) = it.name.split(" ")
                            PeoplePoster(
                                modifier = Modifier,
                                size = 80.dp,
                                firstName = firstName,
                                lastName = lastName,
                                imageUrl = ImageUtil.buildImageUrl(
                                    path = it.profilePath ?: "",
                                    size = ImageUtil.Size.Profile.H632,
                                ),
                            )
                        }
                    }
                }

                // Crew
                item {
                    ThreeVerticalSpacer()
                    Text(
                        modifier = Modifier
                            .testTag(MOVIE_DETAILS_CREW)
                            .padding(horizontal = IheNkiri.spacing.two),
                        style = IheNkiri.typography.titleMedium,
                        color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
                        text = stringResource(R.string.title_crew),
                    )
                    OneVerticalSpacer()
                    LazyRow(
                        modifier = Modifier
                            .testTag(MOVIE_DETAILS_CREW_ROW)
                            .padding(horizontal = IheNkiri.spacing.two),
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                    ) {
                        items(uiState.crew) {
                            val (firstName, lastName) = it.name.split(" ")
                            PeoplePoster(
                                modifier = Modifier,
                                size = 80.dp,
                                firstName = firstName,
                                lastName = lastName,
                                imageUrl = ImageUtil.buildImageUrl(
                                    path = it.profilePath ?: "",
                                    size = ImageUtil.Size.Profile.W185,
                                ),
                            )
                        }
                    }
                }

                // Categories
                item {
                    ThreeVerticalSpacer()
                    Text(
                        modifier = Modifier
                            .testTag(MOVIE_DETAILS_CATEGORIES)
                            .padding(horizontal = IheNkiri.spacing.two),
                        style = IheNkiri.typography.titleMedium,
                        color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
                        text = stringResource(R.string.title_categories),
                    )
                    OneAndHalfVerticalSpacer()
                    LazyRow(
                        modifier = Modifier
                            .testTag(MOVIE_DETAILS_CATEGORIES_ROW)
                            .padding(horizontal = IheNkiri.spacing.two),
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                    ) {
                        items(uiState.categories) { GenreChip(text = it) }
                    }
                }

                // recommendations
                item {
                    ThreeVerticalSpacer()
                    Text(
                        modifier = Modifier
                            .testTag(MOVIE_DETAILS_RECOMMENDATIONS)
                            .padding(horizontal = IheNkiri.spacing.two),
                        style = IheNkiri.typography.titleMedium,
                        color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
                        text = stringResource(R.string.title_recommendations),
                    )
                    OneVerticalSpacer()
                    LazyRow(
                        modifier =
                        Modifier
                            .testTag(MOVIE_DETAILS_RECOMMENDATIONS_ROW)
                            .padding(horizontal = IheNkiri.spacing.two),
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                    ) {
                        items(uiState.recommendations) {
                            val path = ImageUtil.buildImageUrl(it.posterPath)
                            Log.d("Testing: ", "Binding: $path")

                            @Suppress("MagicNumber")
                            MoviePoster(
                                modifier = Modifier
                                    .width(120.dp)
                                    .aspectRatio(0.7F),
                                posterUrl = path,
                                contentDescription = it.title,
                                shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window),
                                onClick = {},
                            )
                        }
                    }
                }

                // Bottom padding
                item {
                    Spacer(modifier = Modifier.height(130.0.dp))
                }
            }
        }

        val density = LocalDensity.current
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = showBottomAppBar,
            enter = slideInVertically() + expandHorizontally(expandFrom = Alignment.End),
            exit = fadeOut(),
        ) {
            BottomAppBar(
                modifier = Modifier.testTag(MOVIE_DETAILS_BOTTOM_BAR),
                actions = {
                    IconButton(onClick = onAddToWatchListClick) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = stringResource(R.string.add_to_watch_list),
                        )
                    }
                    IconButton(onClick = onAddToBookmarkClick) {
                        Icon(
                            painterResource(id = me.jerryokafor.core.ui.R.drawable.baseline_bookmark_add_24),
                            contentDescription = stringResource(R.string.add_to_bookmark),
                        )
                    }
                    IconButton(onClick = onRateItClick) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = stringResource(R.string.rate_it),
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onAddToFavorite,
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(R.string.add_to_favourite),
                        )
                    }
                },
            )
        }
    }

    LaunchedEffect(Unit) {
        showBottomAppBar = true
    }
}