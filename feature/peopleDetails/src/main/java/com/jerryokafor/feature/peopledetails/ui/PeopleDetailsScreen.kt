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

package com.jerryokafor.feature.peopledetails.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jerryokafor.feature.peopledetails.R
import com.jerryokafor.feature.peopledetails.viewModel.PeopleDetailsViewModel
import com.jerryokafor.feature.peopledetails.viewModel.PersonDetailsUiState
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import kotlinx.coroutines.launch
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.data.util.ImageUtil
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.HalfVerticalSpacer
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.OneAndHalfVerticalSpacer
import me.jerryokafor.core.ds.theme.OneVerticalSpacer
import me.jerryokafor.core.ds.theme.ThreeVerticalSpacer
import me.jerryokafor.core.ds.theme.TwoVerticalSpacer
import me.jerryokafor.core.model.Credit
import me.jerryokafor.core.model.PersonDetails
import me.jerryokafor.core.ui.components.Background
import me.jerryokafor.core.ui.components.IheNkiriCircularProgressIndicator
import me.jerryokafor.core.ui.components.IhenkiriCollapsingToolbarHeader
import me.jerryokafor.core.ui.components.IhenkiriCollapsingToolbarTitle
import me.jerryokafor.core.ui.components.IhenkiriCollapsingToolbarToolbar
import me.jerryokafor.core.ui.components.MoviePoster
import me.jerryokafor.core.ui.components.headerHeight
import me.jerryokafor.core.ui.components.toolbarHeight

@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun PeopleDetailsScreenPreview() {
    IheNkiriTheme {
        Background {
            PeopleDetailsScreen(uiState = PersonDetailsUiState.Loading)
        }
    }
}

@Suppress("MagicNumber")
private val timelines = (0..6).mapIndexed { index, item ->
    object : TimelineScope {
        override val title: String
            get() = "202$index"
        override val content: @Composable (BoxScope.(Modifier) -> Unit)
            get() = {
                Surface(
                    modifier = it.padding(top = 16.dp, start = 16.dp),
                    onClick = {},
                ) {
                    Column {
                        Text(
                            modifier = Modifier,
                            text = "This is a timeline",
                        )
                        Text(
                            modifier = Modifier.padding(start = 32.dp),
                            text = "as Tiler",
                        )
                    }
                }
            }
    }
}

@Composable
@Suppress("UnusedPrivateMember")
fun PeopleDetailsScreen(
    viewModel: PeopleDetailsViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val uiState by viewModel.personDetails.collectAsStateWithLifecycle()
    PeopleDetailsScreen(uiState = uiState, onNavigateUp = onNavigateUp)
}

@Composable
@Suppress("UnusedPrivateMember")
fun PeopleDetailsScreen(
    uiState: PersonDetailsUiState,
    onNavigateUp: () -> Unit = {},
) {
    val scroll: ScrollState = rememberScrollState(0)
    val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }

    Surface {
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is PersonDetailsUiState.Error -> {}
                PersonDetailsUiState.Loading -> {
                    IheNkiriCircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is PersonDetailsUiState.Success -> {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(bottom = IheNkiri.spacing.three)
                            .background(MaterialTheme.colorScheme.surface),
                    ) {
                        // header
                        IhenkiriCollapsingToolbarHeader(
                            scroll = scroll,
                            headerHeightPx = headerHeightPx,
                            imagePath = ImageUtil.buildImageUrl(
                                path = uiState.personDetails.profilePath,
                                size = ImageUtil.Size.Profile.H632,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(headerHeight),
                        )

                        // Body
                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scroll),
                            verticalArrangement = Arrangement.spacedBy(IheNkiri.spacing.two),
                        ) {
                            Spacer(Modifier.height(headerHeight))

                            SocialButtonRow()

                            PersonalInfo(uiState.personDetails)

                            Column(modifier = Modifier.padding(horizontal = IheNkiri.spacing.two)) {
                                Text(
                                    text = stringResource(R.string.biography),
                                    style = IheNkiri.typography.titleLarge,
                                    color = contentColorFor(
                                        backgroundColor = IheNkiri.color.inverseOnSurface,
                                    ),
                                )
                                OneAndHalfVerticalSpacer()
                                Text(
                                    text = uiState.personDetails.biography,
                                    style = IheNkiri.typography.bodyLarge,
                                    textAlign = TextAlign.Justify,
                                    color = contentColorFor(IheNkiri.color.inverseOnSurface)
                                        .copy(alpha = 0.75f),
                                )
                            }

                            // Known for
                            KnownForRow(credits = uiState.personDetails.knownFor)
                            TwoVerticalSpacer()

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = IheNkiri.spacing.two),
                            ) {
                                Text(
                                    text = stringResource(R.string.acting_timeline),
                                    style = IheNkiri.typography.titleLarge,
                                    color = contentColorFor(
                                        backgroundColor = IheNkiri.color.inverseOnSurface,
                                    ),
                                )
                                OneAndHalfVerticalSpacer()
                                Timeline(modifier = Modifier, items = timelines)
                            }
                        }

                        // Toolbar
                        IhenkiriCollapsingToolbarToolbar(
                            scroll = scroll,
                            headerHeightPx = headerHeightPx,
                            toolbarHeightPx = toolbarHeightPx,
                            onNavigationIconClick = onNavigateUp,
                        )

                        // Toolbar title
                        IhenkiriCollapsingToolbarTitle(
                            scroll = scroll,
                            name = uiState.personDetails.name,
                            style = IheNkiri.typography.displaySmall,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PersonalInfo(personDetails: PersonDetails) {
    Column(
        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Personal Info",
            style = IheNkiri.typography.titleLarge,
            color = contentColorFor(
                backgroundColor = IheNkiri.color.inverseOnSurface,
            ),
        )
        OneAndHalfVerticalSpacer()

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.oneAndHalf),
        ) {
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = stringResource(id = R.string.known_for),
                    style = IheNkiri.typography.titleMedium,
                    color = contentColorFor(
                        backgroundColor = IheNkiri.color.inverseOnSurface,
                    ),
                )
                HalfVerticalSpacer()
                Text(
                    text = personDetails.knownForDepartment ?: "",
                    style = IheNkiri.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    color = contentColorFor(IheNkiri.color.inverseOnSurface),
                )
            }
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = stringResource(R.string.known_credits),
                    style = IheNkiri.typography.titleMedium,
                    color = contentColorFor(
                        backgroundColor = IheNkiri.color.inverseOnSurface,
                    ),
                )
                HalfVerticalSpacer()
                Text(
                    text = "${personDetails.totalCredits}",
                    style = IheNkiri.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    color = contentColorFor(IheNkiri.color.inverseOnSurface),
                )
            }
        }

        OneVerticalSpacer()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.oneAndHalf),
        ) {
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = stringResource(R.string.gender),
                    style = IheNkiri.typography.titleMedium,
                    color = contentColorFor(
                        backgroundColor = IheNkiri.color.inverseOnSurface,
                    ),
                )
                HalfVerticalSpacer()
                Text(
                    text = personDetails.gender,
                    style = IheNkiri.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    color = contentColorFor(IheNkiri.color.inverseOnSurface),
                )
            }
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = stringResource(R.string.birthday),
                    style = IheNkiri.typography.titleMedium,
                    color = contentColorFor(
                        backgroundColor = IheNkiri.color.inverseOnSurface,
                    ),
                )
                HalfVerticalSpacer()
                Text(
                    text = personDetails.birthday ?: "",
                    style = IheNkiri.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    color = contentColorFor(IheNkiri.color.inverseOnSurface),
                )
            }
        }
        OneVerticalSpacer()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.oneAndHalf),
        ) {
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = stringResource(R.string.place_of_birth),
                    style = IheNkiri.typography.titleMedium,
                    color = contentColorFor(
                        backgroundColor = IheNkiri.color.inverseOnSurface,
                    ),
                )
                HalfVerticalSpacer()
                Text(
                    text = personDetails.placeOfBirth ?: "",
                    style = IheNkiri.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    color = contentColorFor(IheNkiri.color.inverseOnSurface),
                )
            }
            Column(modifier = Modifier.weight(1F)) {
                Text(
                    text = stringResource(R.string.also_known_as),
                    style = IheNkiri.typography.titleMedium,
                    color = contentColorFor(
                        backgroundColor = IheNkiri.color.inverseOnSurface,
                    ),
                )
                HalfVerticalSpacer()
                Text(
                    text = personDetails.alsoKnownAs.firstOrNull() ?: "",
                    style = IheNkiri.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    color = contentColorFor(IheNkiri.color.inverseOnSurface),
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SocialButtonRow() {
    FlowRow(
        modifier = Modifier
            .padding(horizontal = IheNkiri.spacing.two)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        IconButton(onClick = {}) {
            Icon(
                modifier = @Suppress("MagicNumber") Modifier.scale(0.5f),
                painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.ic_facebook),
                contentDescription = "Facebook",
            )
        }
        Spacer(modifier = Modifier.width(IheNkiri.spacing.oneAndHalf))
        IconButton(onClick = {}) {
            Icon(
                modifier = @Suppress("MagicNumber") Modifier.scale(0.5f),
                painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.ic_twitter),
                contentDescription = "Facebook",
            )
        }
        Spacer(modifier = Modifier.width(IheNkiri.spacing.oneAndHalf))
        IconButton(onClick = {}) {
            Icon(
                modifier = @Suppress("MagicNumber") Modifier.scale(0.5f),
                painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.ic_instagram),
                contentDescription = "Facebook",
            )
        }
        Spacer(modifier = Modifier.width(IheNkiri.spacing.oneAndHalf))
        IconButton(onClick = {}) {
            Icon(
                modifier = @Suppress("MagicNumber") Modifier.scale(0.5f),
                painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.ic_tiktok),
                contentDescription = "Facebook",
            )
        }
    }
}

@Composable
fun KnownForRow(credits: List<Credit>) {
    val shimmer = rememberShimmer(shimmerBounds = ShimmerBounds.Window)

    Column(modifier = Modifier) {
        ThreeVerticalSpacer()
        Text(
            modifier = Modifier
                .padding(horizontal = IheNkiri.spacing.two),
            style = IheNkiri.typography.titleLarge,
            color = contentColorFor(
                backgroundColor = IheNkiri.color.inverseOnSurface,
            ),
            text = stringResource(R.string.known_for),
        )
        OneAndHalfVerticalSpacer()
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = IheNkiri.spacing.two),
            horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
        ) {
            items(credits) {
                val path = ImageUtil.buildImageUrl(
                    path = it.posterPath,
                    size = ImageUtil.Size.Poster.W154,
                )
                @Suppress("MagicNumber")
                MoviePoster(
                    modifier = Modifier
                        .width(120.dp)
                        .aspectRatio(0.7F),
                    posterUrl = path,
                    contentDescription = it.title ?: "",
                    shimmer = shimmer,
                    onClick = { },
                )
            }
        }
    }
}

data class TimelineNodeConfig(
    val indicatorColor: Color,
    val circleRadius: Dp,
    val circleStrokeWidth: Dp,
    val lineStrokeWidth: Dp,
    val titleTextStyle: TextStyle,
)

object TimelineNodeDefaults {
    val indicatorColor: Color = Color.White
    val circleRadius: Dp = 8.dp
    val circleStrokeWidth: Dp = 3.0.dp
    val lineStrokeWidth: Dp = 1.0.dp
    val titleTextStyle: TextStyle = TextStyle(fontSize = 14.sp)

    @Composable
    fun timelineConfig(
        indicatorColor: Color = TimelineNodeDefaults.indicatorColor,
        circleRadius: Dp = TimelineNodeDefaults.circleRadius,
        circleStrokeWidth: Dp = TimelineNodeDefaults.circleStrokeWidth,
        lineStrokeWidth: Dp = TimelineNodeDefaults.lineStrokeWidth,
        titleTextStyle: TextStyle = TimelineNodeDefaults.titleTextStyle,
    ): TimelineNodeConfig = TimelineNodeConfig(
        indicatorColor = indicatorColor,
        circleRadius = circleRadius,
        circleStrokeWidth = circleStrokeWidth,
        lineStrokeWidth = lineStrokeWidth,
        titleTextStyle = titleTextStyle,
    )
}

interface TimelineScope {
    val title: String
    val content: @Composable BoxScope.(Modifier) -> Unit
}

@Composable
fun <T : TimelineScope> Timeline(
    modifier: Modifier = Modifier,
    items: List<T>,
) {
    val lineAnim = remember { Animatable(0f) }
    val circleAnim = remember { Animatable(0f) }

    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        items.forEach { item ->
            TimelineNode(
                title = item.title,
                lineAnim = lineAnim.value,
                circleAnim = circleAnim.value,
            ) {
                item.content(this, it)
            }
        }
    }

    LaunchedEffect(Unit, block = {
        launch {
            lineAnim.animateTo(
                targetValue = 1f,
                animationSpec = @Suppress("MagicNumber")
                tween(2000, easing = LinearEasing),
            )
        }
        launch {
            circleAnim.animateTo(
                targetValue = 1f,
                animationSpec = @Suppress("MagicNumber")
                tween(2000, easing = LinearEasing),
            )
        }
    })
}

@Composable
fun TimelineNode(
    modifier: Modifier = Modifier,
    title: String,
    lineAnim: Float,
    circleAnim: Float,
    config: TimelineNodeConfig = TimelineNodeDefaults.timelineConfig(),
    content: @Composable BoxScope.(Modifier) -> Unit,
) {
    val textMeasurer = rememberTextMeasurer()
    val linePadding = 8.dp

    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(start = 8.dp, top = 8.dp)
            .drawWithCache {
                val textLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(title),
                    constraints = Constraints.fixedWidth(size.width.toInt()),
                    style = config.titleTextStyle,
                )

                onDrawBehind {
                    val circleRadiusPx = config.circleRadius.toPx()
                    val linePaddingPx = linePadding.toPx()
                    val circleStrokeSizePx = config.circleStrokeWidth.toPx()
                    val lineStrokeSizePx = config.lineStrokeWidth.toPx()
                    val timelineColors = listOf(Color.Cyan, Color.LightGray)

                    drawArc(
                        color = config.indicatorColor,
                        startAngle = 270f,
                        sweepAngle =
                            @Suppress("MagicNumber")
                            360f
                                *
                                circleAnim,
                        useCenter = false,
                        topLeft = Offset(x = 0f, y = 0f),
                        size = Size(width = circleRadiusPx * 2, height = circleRadiusPx * 2),
                        style = Stroke(width = circleStrokeSizePx),
                    )

                    drawText(
                        topLeft = Offset(circleRadiusPx * 2 + linePaddingPx, 0F),
                        textLayoutResult = textLayoutResult,
                        color = Color.White,
                    )

                    clipRect(bottom = size.height * lineAnim) {
                        drawLine(
                            brush = Brush.verticalGradient(timelineColors),
                            start = Offset(
                                x = circleRadiusPx,
                                y = circleRadiusPx * 2 + linePaddingPx,
                            ),
                            end = Offset(
                                x = circleRadiusPx,
                                y = size.height,
                            ),
                            cap = StrokeCap.Round,
                            strokeWidth = lineStrokeSizePx,
                        )
                    }
                }
            },
        content = { content(Modifier) },
    )
}
