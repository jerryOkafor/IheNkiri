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

package com.jerryokafor.feature.media.ui.youtube

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Stable
@Suppress("TooManyFunctions")
class YoutubePlayerController(
    initialVideoId: String,
    autoPlay: Boolean,
    startSeconds: Float,
    private val coroutineScope: CoroutineScope,
) {
    internal var webView by mutableStateOf<WebView?>(null)

    fun loadVideo(
        videoId: String,
        startSeconds: Float = 0F,
    ) = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("loadVideo", videoId, startSeconds)
    }

    private fun cueVideo(
        videoId: String,
        startSeconds: Float,
    ) = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("cueVideo", videoId, startSeconds)
    }

    var events: MutableStateFlow<YouTubePlayerEvent?> = MutableStateFlow(null)
        internal set

    init {
        coroutineScope.launch(Dispatchers.Main) {
            events.filterNotNull().collect { event ->
                when (event) {
                    YouTubePlayerEvent.OnPlayerReady -> {
                        if (autoPlay) {
                            loadVideo(initialVideoId, startSeconds)
                        } else {
                            cueVideo(initialVideoId, startSeconds)
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun rememberYoutubePlayerController(
    initialVideoId: String = "",
    autoPlay: Boolean = false,
    startSeconds: Float = 0f,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) = remember {
    YoutubePlayerController(
        initialVideoId = initialVideoId,
        autoPlay = autoPlay,
        startSeconds = startSeconds,
        coroutineScope = coroutineScope,
    )
}
