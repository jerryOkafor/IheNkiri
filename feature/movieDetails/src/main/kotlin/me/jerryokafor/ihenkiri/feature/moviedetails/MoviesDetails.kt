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
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.coroutines.delay
import me.jerryokafor.core.common.annotation.ExcludeFromJacocoGeneratedReport
import me.jerryokafor.core.data.util.ImageUtil
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
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
@ExcludeFromJacocoGeneratedReport
fun MoviesDetailsPreview() {
    IheNkiriTheme {
        MoviesDetails(uiState = MoviesDetailViewModel.UIState())
    }
}

@Composable
@Suppress("UnusedPrivateMember")
fun MoviesDetails(viewModel: MoviesDetailViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    MoviesDetails(uiState = uiState.value)
}

@Composable
@Suppress("UnusedPrivateMember")
fun MoviesDetails(uiState: MoviesDetailViewModel.UIState) {
    var showBottomAppBar by remember { mutableStateOf(false) }
    val primaryTextColor = contentColorFor(IheNkiri.color.inverseOnSurface)

    @Suppress("MagicNumber")
    val secondaryTextColor = primaryTextColor.copy(0.7F)
    val state = rememberCollapsingToolbarScaffoldState()
    var enabled by remember { mutableStateOf(true) }

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
                    Image(
                        painter = painterResource(id = R.drawable.sample_banner),
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                alpha = state.toolbarState.progress
                            },
                        contentScale = ContentScale.FillWidth,
                        contentDescription = null,
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
                                    text = "Imperdoável (2021)",
                                    style = IheNkiri.typography.titleMedium,
                                    color = primaryTextColor,
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(
                                        text = "10/12/2021 (BR)",
                                        color = secondaryTextColor,
                                    )
                                    Text(text = "●", color = secondaryTextColor)
                                    Icon(
                                        painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.ic_clock),
                                        contentDescription = "Time laps",
                                        tint = secondaryTextColor,
                                    )
                                    Text(
                                        text = "1h 53m",
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
                        IconButton(onClick = {}) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                        Text("Enable collapse/expand", style = IheNkiri.typography.titleMedium)
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
                        text = """
            Após cumprir pena por um crime violento, Ruth volta ao convívio na sociedade, que se recusa a perdoar seu passado. Discriminada no lugar que já chamou de lar, sua única esperança é encontrar a irmã, que ela havia sido forçada a deixar para trás.
                        """.trimIndent(),
                        color = secondaryTextColor,
                    )

                    ThreeVerticalSpacer()
                    TrailerButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = IheNkiri.spacing.two),
                    )

                    TwoVerticalSpacer()
                    Text(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        text = stringResource(R.string.title_main_cast),
                    )
                    TwoVerticalSpacer()
                    LazyRow(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                    ) {
                        @Suppress("MagicNumber")
                        items(6) {
                            PeoplePoster(
                                modifier = Modifier,
                                size = 80.dp,
                                firstName = "Sandra",
                                lastName = "Bullock",
                                imageUrl = ImageUtil.buildImageUrl(
                                    path = "/lldeQ91GwIVff43JBrpdbAAeYWj.jpg",
                                    size = ImageUtil.Size.Profile.H632,
                                ),
                            )
                        }
                    }

                    TwoVerticalSpacer()
                    Text(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        text = stringResource(R.string.title_categories),
                    )
                    TwoVerticalSpacer()
                    LazyRow(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                    ) {
                        @Suppress("MagicNumber")
                        items(3) {
                            GenreChip(text = "Drama")
                        }
                    }

                    TwoVerticalSpacer()
                    Text(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        text = stringResource(R.string.title_recommendations),
                    )
                    TwoVerticalSpacer()
                    LazyRow(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
                        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
                    ) {
                        @Suppress("MagicNumber")
                        items(5) {
                            @Suppress("MagicNumber")
                            MoviePoster(
                                modifier = Modifier
                                    .width(120.dp)
                                    .aspectRatio(0.8F),
                                path = ImageUtil.buildImageUrl(path = "/8pjWz2lt29KyVGoq1mXYu6Br7dE.jpg"),
                                contentDescription = "",
                                shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window),
                                onClick = {},
                            )
                        }
                    }
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
