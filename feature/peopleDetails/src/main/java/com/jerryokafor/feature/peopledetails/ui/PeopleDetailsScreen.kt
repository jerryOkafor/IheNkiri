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

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jerryokafor.feature.peopledetails.R
import com.jerryokafor.feature.peopledetails.viewModel.PeopleDetailsViewModel
import com.jerryokafor.feature.peopledetails.viewModel.PersonDetailsUiState
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.data.util.ImageUtil
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.FillingSpacer
import me.jerryokafor.core.ds.theme.HalfVerticalSpacer
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.OneAndHalfVerticalSpacer
import me.jerryokafor.core.ds.theme.OneHorizontalSpacer
import me.jerryokafor.core.ds.theme.OneVerticalSpacer
import me.jerryokafor.core.ds.theme.ThreeVerticalSpacer
import me.jerryokafor.core.model.KnownFor
import me.jerryokafor.core.model.PersonDetails
import me.jerryokafor.core.model.Timeline
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
                is PersonDetailsUiState.Error -> {
                    PeopleDetailsScreenError(message = uiState.message, onClose = onNavigateUp)
                }

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

                            // Social Buttons
                            SocialButtonRow()

                            // Personal Details
                            PersonalInfo(uiState.personDetails)

                            // Biography
                            Biography(biography = uiState.personDetails.biography)

                            // Known for
                            KnownForRow(
                                knownFors = uiState.personDetails.knownFor
                                    .filter { it.title != null },
                            )

                            var timeline by remember(uiState.personDetails.timeline) {
                                mutableStateOf(uiState.personDetails.timeline)
                            }
                            var selectedTimelineType by remember {
                                mutableStateOf(
                                    Timeline.Type.ALL,
                                )
                            }
                            var selectedTimelineDept by remember {
                                mutableStateOf(
                                    Timeline.Department.ALL,
                                )
                            }

                            // Timeline
                            TimelineView(
                                timeline = timeline,
                                defaultType = selectedTimelineType,
                                defaultDepartment = selectedTimelineDept,
                                onTimelineTypeSelected = { type ->
                                    selectedTimelineType = type

                                    timeline = when (type) {
                                        Timeline.Type.ALL -> uiState.personDetails.timeline

                                        else ->
                                            uiState.personDetails.timeline
                                                .mapValues { (_, value) ->
                                                    value.filter { it.type == type }
                                                }
                                                .filter { it.value.isNotEmpty() }
                                    }
                                },
                                onTimelineDeptSelected = { dept ->
                                    selectedTimelineDept = dept

                                    timeline = when (dept) {
                                        Timeline.Department.ALL -> uiState.personDetails.timeline
                                        else ->
                                            uiState.personDetails.timeline
                                                .mapValues { (_, value) ->
                                                    value.filter { it.department == dept }
                                                }
                                                .filter { it.value.isNotEmpty() }
                                    }
                                },
                            )
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
private fun PersonalInfo(personDetails: PersonDetails) {
    Column(
        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.personal_info),
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
private fun SocialButtonRow() {
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
                contentDescription = stringResource(R.string.cont_desc_facebook),
            )
        }
        Spacer(modifier = Modifier.width(IheNkiri.spacing.oneAndHalf))
        IconButton(onClick = {}) {
            Icon(
                modifier = @Suppress("MagicNumber") Modifier.scale(0.5f),
                painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.ic_twitter),
                contentDescription = stringResource(R.string.cont_desc_twitter),
            )
        }
        Spacer(modifier = Modifier.width(IheNkiri.spacing.oneAndHalf))
        IconButton(onClick = {}) {
            Icon(
                modifier = @Suppress("MagicNumber") Modifier.scale(0.5f),
                painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.ic_instagram),
                contentDescription = stringResource(R.string.cont_desc_instagram),
            )
        }
        Spacer(modifier = Modifier.width(IheNkiri.spacing.oneAndHalf))
        IconButton(onClick = {}) {
            Icon(
                modifier = @Suppress("MagicNumber") Modifier.scale(0.5f),
                painter = painterResource(id = me.jerryokafor.core.ui.R.drawable.ic_tiktok),
                contentDescription = stringResource(R.string.cont_desc_tiktok),
            )
        }
    }
}

@Composable
private fun Biography(biography: String) {
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
            text = biography,
            style = IheNkiri.typography.bodyLarge,
            textAlign = TextAlign.Justify,
            overflow = TextOverflow.Ellipsis,
            maxLines = 11,
            color = contentColorFor(IheNkiri.color.inverseOnSurface)
                .copy(alpha = 0.75f),
        )
    }
}

@Composable
private fun KnownForRow(knownFors: List<KnownFor>) {
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
            items(knownFors) {
                val path = ImageUtil.buildImageUrl(
                    path = it.posterPath,
                    size = ImageUtil.Size.Poster.W154,
                )
                @Suppress("MagicNumber")
                Column(
                    modifier = Modifier.width(120.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    MoviePoster(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.7F),
                        posterUrl = path,
                        contentDescription = it.title ?: "",
                        shimmer = shimmer,
                        onClick = { },
                    )
                    OneAndHalfVerticalSpacer()
                    Text(
                        text = it.title ?: "",
                        style = IheNkiri.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        minLines = 3,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@Composable
private fun TimelineView(
    timeline: Map<Int, List<Timeline>>,
    defaultType: Timeline.Type = Timeline.Type.ALL,
    defaultDepartment: Timeline.Department = Timeline.Department.ALL,
    onTimelineTypeSelected: (Timeline.Type) -> Unit,
    onTimelineDeptSelected: (Timeline.Department) -> Unit,
) {
    Column(
        modifier = Modifier.padding(horizontal = IheNkiri.spacing.two),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.acting_timeline),
                style = IheNkiri.typography.titleLarge,
                color = contentColorFor(
                    backgroundColor = IheNkiri.color.inverseOnSurface,
                ),
            )
            FillingSpacer()
            IheNkiriDropDown(
                default = defaultType,
                menus = Timeline.Type.entries,
                onSelectItem = onTimelineTypeSelected,
            )
            OneHorizontalSpacer()
            IheNkiriDropDown(
                default = defaultDepartment,
                menus = Timeline.Department.entries,
                onSelectItem = onTimelineDeptSelected,
            )
        }
        OneAndHalfVerticalSpacer()
        val items = timeline.map { (key, value) -> PersonTimelineView(key, value) }

        Box(modifier = Modifier) {
            if (timeline.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = IheNkiri.spacing.twoAndaHalf),
                        text = "No item matching the given selection!",
                    )
                }
            } else {
                Timeline(modifier = Modifier, items = items)
            }
        }
    }
}
