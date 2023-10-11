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

package me.jerryokafor.ihenkiri.feature.moviedetails

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.coroutines.delay
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.data.util.ImageUtil
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.FourVerticalSpacer
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.OneAndHalfVerticalSpacer
import me.jerryokafor.core.ds.theme.OneVerticalSpacer
import me.jerryokafor.core.ds.theme.SixVerticalSpacer
import me.jerryokafor.core.ds.theme.ThreeVerticalSpacer
import me.jerryokafor.core.ds.theme.TwoVerticalSpacer
import me.jerryokafor.core.ui.components.GenreChip
import me.jerryokafor.core.ui.components.MoviePoster
import me.jerryokafor.core.ui.components.TrailerButton
import me.jerryokafor.core.ui.widget.MovieRating
import me.jerryokafor.core.ui.widget.PeoplePoster
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun MoviesDetailsPreview() {
    IheNkiriTheme {
        MoviesDetails(uiState = MoviesDetailViewModel.UIState())
    }
}

@Composable
@Suppress("UnusedPrivateMember")
fun MoviesDetails(viewModel: MoviesDetailViewModel = hiltViewModel(), movieId: Long) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    MoviesDetails(uiState = uiState.value)

    LaunchedEffect(movieId) {
        viewModel.setMovieId(movieId)
    }
}

@Composable
@Suppress("UnusedPrivateMember")
fun MoviesDetails(uiState: MoviesDetailViewModel.UIState) {
    var showBottomAppBar by remember { mutableStateOf(false) }
    val primaryTextColor = contentColorFor(IheNkiri.color.inverseOnSurface)

    @Suppress("MagicNumber")
    val secondaryTextColor = primaryTextColor.copy(0.7F)
    val state = rememberCollapsingToolbarScaffoldState()
    val enabled by remember { mutableStateOf(true) }
    var drawable by remember { mutableStateOf<Drawable?>(null) }

    val view = LocalView.current
    val window = (view.context as Activity).window

    LaunchedEffect(drawable) {
        drawable?.let {
            Palette.Builder(it.toBitmap())
                .generate { palette ->
                    val vibrantSwatch = palette?.vibrantSwatch
                    val lightVibrantSwatch = palette?.lightVibrantSwatch
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
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

                val colorStops = listOf(
                    Color(0x00000000),
                    IheNkiri.color.inverseOnSurface,
                )

                @Suppress("MagicNumber")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .parallax(0.5f)
                        .height(400.dp)
                        .fillMaxWidth(),
                ) {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = state.toolbarState.progress
                            },
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(ImageUtil.buildImageUrl(uiState.postPath))
                            .crossfade(true)
                            .build(),
//                        placeholder = painterResource(R.drawable.sample_banner),
                        contentDescription = uiState.title,
                        contentScale = ContentScale.FillWidth,
                        onSuccess = {
                            drawable = it.result.drawable
                        },
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
                            MovieRating(modifier = Modifier.size(57.dp), rating = 0.45F)
                            Column(
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                            ) {
                                Text(
                                    text = uiState.title,
                                    style = IheNkiri.typography.titleMedium,
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

                    Row(
                        modifier = Modifier.statusBarsPadding(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.two),
                    ) {
                        IconButton(onClick = {
                            // preview videos here
                        }, enabled = uiState.videos.isNotEmpty()) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Text(uiState.title, style = IheNkiri.typography.titleMedium)
                    }
                }
            },
        ) {
            Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(IheNkiri.color.inverseOnSurface),
                ) {
                    TwoVerticalSpacer()
                    Text(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        text = uiState.overview,
                        textAlign = TextAlign.Justify,
                        color = secondaryTextColor,
                    )

                    ThreeVerticalSpacer()
                    TrailerButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = IheNkiri.spacing.two),
                    )

                    // Main cast
                    ThreeVerticalSpacer()
                    Text(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        style = IheNkiri.typography.titleMedium,
                        text = stringResource(R.string.title_main_cast),
                    )
                    OneVerticalSpacer()
                    LazyRow(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
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

                    // Crew
                    ThreeVerticalSpacer()
                    Text(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        style = IheNkiri.typography.titleMedium,
                        text = stringResource(R.string.title_crew),
                    )
                    OneVerticalSpacer()
                    LazyRow(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
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
                                    size = ImageUtil.Size.Profile.H632,
                                ),
                            )
                        }
                    }

                    // Categories
                    ThreeVerticalSpacer()
                    Text(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        style = IheNkiri.typography.titleMedium,
                        text = stringResource(R.string.title_categories),
                    )
                    OneAndHalfVerticalSpacer()
                    LazyRow(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                    ) {
                        items(uiState.categories) { GenreChip(text = it) }
                    }

                    // recommendations
                    ThreeVerticalSpacer()
                    Text(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        style = IheNkiri.typography.titleMedium,
                        text = stringResource(R.string.title_recommendations),
                    )
                    OneVerticalSpacer()
                    LazyRow(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                    ) {
                        items(uiState.recommendations) {
                            val path = ImageUtil.buildImageUrl(it.posterPath)
                            Log.d("Testing: ", "Binding: $path")

                            @Suppress("MagicNumber")
                            MoviePoster(
                                modifier = Modifier
                                    .width(120.dp)
                                    .aspectRatio(0.8F),
                                path = path,
                                contentDescription = it.title,
                                shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window),
                                onClick = {},
                            )
                        }
                    }

                    SixVerticalSpacer()
                    SixVerticalSpacer()
                    FourVerticalSpacer()
                }
            }
        }

        val density = LocalDensity.current
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = showBottomAppBar,
            enter = slideInVertically {
                with(density) { -40.dp.roundToPx() }
            } + expandHorizontally(
                expandFrom = Alignment.End,
            ),
            exit = fadeOut(),
        ) {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Add to list",
                        )
                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            painterResource(id = me.jerryokafor.core.ui.R.drawable.baseline_bookmark_add_24),
                            contentDescription = "Add to watch list",
                        )
                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rate it",
                        )
                    }
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = { /* do something */ },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                    ) {
                        Icon(Icons.Filled.FavoriteBorder, "Add to favourite")
                    }
                },
            )
        }
    }
    LaunchedEffect(Unit) {
        @Suppress("MagicNumber")
        delay(600)
        showBottomAppBar = true
    }
}
