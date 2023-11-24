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

package me.jerryokafor.ihenkiri.feature.settings.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.components.IhenkiriButton
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.OneAndHalfHorizontalSpacer
import me.jerryokafor.core.ds.theme.OneVerticalSpacer
import me.jerryokafor.core.ds.theme.TwoAndHalfHorizontalSpacer
import me.jerryokafor.core.ds.theme.TwoHorizontalSpacer
import me.jerryokafor.core.ds.theme.TwoVerticalSpacer
import me.jerryokafor.core.ui.R
import me.jerryokafor.core.ui.components.Background
import me.jerryokafor.core.ui.widget.MovieRating
import me.jerryokafor.ihenkiri.feature.settings.R.string
import me.jerryokafor.ihenkiri.feature.settings.viewModel.SettingsViewModel

const val MORE_TITLE_TEST_TAG = "more_title"

@Composable
@ThemePreviews
fun MoreScreenPreview() {
    IheNkiriTheme {
        MoreScreen()
    }
}

@Composable
@Suppress("UnusedPrivateMember")
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    MoreScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExcludeFromGeneratedCoverageReport
fun MoreScreen() {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val isLocalInspection = LocalInspectionMode.current

    val imageLoader = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data("")
            .crossfade(true)
            .build(),
        contentScale = ContentScale.FillWidth,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    Background {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                modifier = Modifier.testTag(MORE_TITLE_TEST_TAG),
                title = {
                    Text(
                        text = "Settings",
                        style = IheNkiri.typography.titleMedium,
                        color = IheNkiri.color.onPrimary,
                    )
                },
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
            )
            TwoAndHalfHorizontalSpacer()
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .aspectRatio(1F),
                    ) {
                        Image(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                                .border(
                                    width = 3.dp,
                                    color = IheNkiri.color.tertiaryContainer.copy(alpha = 0.5F),
                                    shape = CircleShape,
                                )
                                .clip(CircleShape),
                            painter = if (isError.not() && !isLocalInspection) {
                                imageLoader
                            } else {
                                painterResource(R.drawable.ic_avatar)
                            },
                            contentScale = ContentScale.Inside,
                            contentDescription = null,
                        )

                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.Center)
                                    .padding(IheNkiri.spacing.twoAndaHalf),
                                strokeCap = StrokeCap.Round,
                                strokeWidth = 1.dp,
                            )
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = IheNkiri.spacing.two),
                    ) {
                        OneVerticalSpacer()
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "Jerry Okafor",
                            style = IheNkiri.typography.displayMedium,
                            color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
                            minLines = 1,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "Member since November 2016",
                            style = IheNkiri.typography.labelSmall,
                            color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
                            minLines = 1,
                            maxLines = 1,
                            textAlign = TextAlign.Center,
                        )
                        TwoVerticalSpacer()
                        Text(
                            modifier = Modifier
                                .fillMaxWidth(),
                            text = "A software engineer! Member since November 2016",
                            style = IheNkiri.typography.bodyMedium,
                            color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
                            minLines = 1,
                            maxLines = 1,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
                item {
                    TwoVerticalSpacer()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = IheNkiri.spacing.two),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            modifier = Modifier.weight(1F),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            MovieRating(
                                modifier = Modifier.size(57.dp),
                                rating = 0.45F,
                            )
                            OneAndHalfHorizontalSpacer()
                            Text(
                                text = "Average \nMovie Score",
                                maxLines = 2,
                                minLines = 2,
                            )
                        }
                        TwoHorizontalSpacer()
                        Row(
                            modifier = Modifier.weight(1F),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            MovieRating(
                                modifier = Modifier.size(57.dp),
                                rating = 0.45F,
                            )
                            OneAndHalfHorizontalSpacer()
                            Text(
                                text = "Average \nTV Score",
                                maxLines = 2,
                                minLines = 2,
                            )
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = IheNkiri.spacing.two),
                    ) {
                        SettingsDialogSectionTitle(text = "Stats")
                        Divider()
                        OneVerticalSpacer()
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.weight(1F),
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Text(text = "Total Edits", style = IheNkiri.typography.labelMedium)
                                Text(text = "1", style = IheNkiri.typography.displayLarge)
                            }
                            TwoHorizontalSpacer()
                            Column(
                                modifier = Modifier.weight(1F),
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Text(
                                    text = "Total Ratings",
                                    style = IheNkiri.typography.labelMedium,
                                )
                                Text(text = "1", style = IheNkiri.typography.displayLarge)
                            }
                        }
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = IheNkiri.spacing.two),
                    ) {
                        SettingsDialogSectionTitle(text = "Theme")
                        Divider()
                        OneVerticalSpacer()

                        IhenkiriButton(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                            Text(text = "Login")
                        }

                        IhenkiriButton(modifier = Modifier.fillMaxWidth(), onClick = {}) {
                            Text(text = stringResource(string.logout))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}
