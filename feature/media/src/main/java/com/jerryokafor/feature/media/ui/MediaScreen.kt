/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 IheNkiri Project
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

package com.jerryokafor.feature.media.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jerryokafor.feature.media.R
import com.jerryokafor.feature.media.ui.vimeo.VimeoPlayerView
import com.jerryokafor.feature.media.ui.vimeo.rememberVimeoPlayerController
import com.jerryokafor.feature.media.ui.vimeo.rememberVimeoPlayerState
import com.jerryokafor.feature.media.ui.youtube.YoutubePlayerView
import com.jerryokafor.feature.media.ui.youtube.rememberYoutubePlayerController
import com.jerryokafor.feature.media.util.VideoProvider
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.theme.HalfVerticalSpacer
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.OneVerticalSpacer
import me.jerryokafor.core.ds.theme.TwoHorizontalSpacer
import me.jerryokafor.core.ui.components.IheNkiriCircularProgressIndicator
import java.time.format.DateTimeFormatter

internal const val VIMEO_PLAYER = "VIMEO_PLAYER"
internal const val YOUTUBE_PLAYER = "YOUTUBE_PLAYER"
internal const val VIDEO_LIST = "VIDEO_LIST"

@Preview
@Composable
@ExcludeFromGeneratedCoverageReport
private fun MediaScreenPreview() {
    IheNkiriTheme {
        MediaScreen(
            uiState = MediaUiState.Loading,
            title = "Testing",
        ) {}
    }
}

@Composable
fun MediaScreen(
    viewModel: MediaScreenViewModel = hiltViewModel(),
    title: String,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.mediaUiState.collectAsStateWithLifecycle()
    MediaScreen(uiState = uiState, title = title, onBackClick = onBackClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaScreen(
    uiState: MediaUiState,
    title: String,
    onBackClick: () -> Unit = {},
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CenterAlignedTopAppBar(
            title = { Text(text = title) },
            navigationIcon = {
                IconButton(
                    onClick = onBackClick,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            },
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState) {
                is MediaUiState.LoadFailed -> {
                    Text(modifier = Modifier.align(Alignment.Center), text = uiState.message)
                }

                MediaUiState.Loading -> {
                    IheNkiriCircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is MediaUiState.Success -> {
                    val coroutineScope = rememberCoroutineScope()
                    var currentVideo by remember { mutableStateOf(uiState.videos.first()) }
                    val provider by remember {
                        derivedStateOf { VideoProvider.fromString(currentVideo.site) }
                    }

                    val youtubePlayerController = rememberYoutubePlayerController(
                        initialVideoId = currentVideo.key,
                        autoPlay = false,
                        coroutineScope = coroutineScope,
                    )

                    Column {
                        when (provider) {
                            VideoProvider.YOUTUBE -> {
                                YoutubePlayerView(
                                    playerController = youtubePlayerController,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag(YOUTUBE_PLAYER),
                                )

                                youtubePlayerController.loadVideo(currentVideo.key)
                            }

                            VideoProvider.VIMEO -> {
                                val vimeoPlayerController = rememberVimeoPlayerController(
                                    initialVideoId = currentVideo.key,
                                    autoPlay = true,
                                )
                                VimeoPlayerView(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag(VIMEO_PLAYER),
                                    playerController = vimeoPlayerController,
                                    playerState = rememberVimeoPlayerState(),
                                )
                                vimeoPlayerController.loadVideo(currentVideo.key)
                            }

                            else -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                ) {
                                    Text(
                                        modifier = Modifier.align(Alignment.Center),
                                        text = "Provider: ${currentVideo.site} not supported",
                                    )
                                }
                            }
                        }
                        OneVerticalSpacer()

                        LazyColumn(
                            modifier = Modifier
                                .padding(bottom = IheNkiri.spacing.two)
                                .testTag(VIDEO_LIST),
                        ) {
                            items(uiState.videos) { video ->
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            horizontal = IheNkiri.spacing.oneAndHalf,
                                            vertical = IheNkiri.spacing.half,
                                        )
                                        .semantics(mergeDescendants = true) {
                                            contentDescription = video.site
                                        },
                                    shape = IheNkiri.shape.small,
                                    color = IheNkiri.color.surfaceBright,
                                    onClick = { currentVideo = video },
                                ) {
                                    Row(
                                        modifier = Modifier.padding(
                                            horizontal = IheNkiri.spacing.two,
                                            vertical = IheNkiri.spacing.one,
                                        ),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        val image = when (VideoProvider.fromString(video.site)) {
                                            VideoProvider.YOUTUBE -> R.drawable.youtube
                                            VideoProvider.VIMEO -> R.drawable.vimeo
                                            else -> null
                                        }
                                        image?.let {
                                            Image(
                                                modifier = Modifier.size(60.dp),
                                                painter = painterResource(id = it),
                                                contentDescription = video.site,
                                            )
                                            TwoHorizontalSpacer()
                                        }
                                        Column {
                                            val date = video.publishedAt.formatDate()
                                            Text(
                                                text = video.name,
                                                style = IheNkiri.typography.bodyLarge,
                                            )
                                            HalfVerticalSpacer()
                                            Text(
                                                text = "Publish Date: $date",
                                                style = IheNkiri.typography.bodySmall,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

internal fun String.formatDate(): String {
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    return DateTimeFormatter.ofPattern("MMM yyyy").format(formatter.parse(this))
}
