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
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun play() = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("playVideo")
    }

    fun pause() = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("pauseVideo")
    }

    fun stop() = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("stopVideo")
    }

    fun loadVideo(
        videoId: String,
        startSeconds: Float = 0F,
    ) = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("loadVideo", videoId, startSeconds)
    }

    fun cueVideo(
        videoId: String,
        startSeconds: Float,
    ) = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("cueVideo", videoId, startSeconds)
    }

    fun nextVideo() = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("nextVideo")
    }

    fun previousVideo() = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("previousVideo")
    }

    fun playVideoAt(index: Int) = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("playVideoAt", index)
    }

    fun setLoop(loop: Boolean) = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("setLoop", loop)
    }

    fun setShuffle(shuffle: Boolean) = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("setShuffle", shuffle)
    }

    fun mute() = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("mute")
    }

    fun unMute() = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("unMute")
    }

    @Suppress("MagicNumber")
    fun setVolume(volumePercent: Int) = coroutineScope.launch(Dispatchers.Main) {
        require(volumePercent in 0..100) { "Volume must be between 0 and 100" }
        webView?.invoke("setVolume", volumePercent)
    }

    fun seekTo(time: Float) = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("seekTo", time)
    }

    fun setPlaybackRate(playbackRate: PlayerConstants.PlaybackRate) =
        coroutineScope.launch(Dispatchers.Main) {
            webView?.invoke("setPlaybackRate", playbackRate.toFloat())
        }

    fun toggleFullscreen() = coroutineScope.launch(Dispatchers.Main) {
        webView?.invoke("toggleFullscreen")
    }

    var events: MutableStateFlow<YouTubePlayerEvent> =
        MutableStateFlow(YouTubePlayerEvent.Default)
        internal set

    init {
        coroutineScope.launch(Dispatchers.Main) {
            events.collect { event ->
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
