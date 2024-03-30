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

package com.jerryokafor.feature.media.ui.vimeo

import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.jerryokafor.feature.media.ui.youtube.invoke
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@Stable
class VimeoPlayerController(
    val initialVideoId: String?,
    val autoPlay: Boolean,
    coroutineScope: CoroutineScope,
) {
    internal var webView by mutableStateOf<WebView?>(null)

//    fun play() = coroutineScope.launch(Dispatchers.Main) {
//        webView?.invoke("playVideo")
//    }
//
//    fun pause() = coroutineScope.launch(Dispatchers.Main) {
//        webView?.invoke("pauseVideo")
//    }
//
//    fun stop() = coroutineScope.launch(Dispatchers.Main) {
//        webView?.invoke("stopVideo")
//    }

    fun loadVideo(videoId: String) {
        webView?.invoke("loadVideo", videoId)
        if (autoPlay) {
            webView?.invoke("playVideo")
        }
    }

    var events: MutableStateFlow<VimeoPlayerEvent?> = MutableStateFlow(null)
        internal set

    init {
        coroutineScope.launch {
            events.collect {
                when (it) {
                    is VimeoPlayerEvent.OnPlayerReady -> {
                        if (autoPlay) {
                            webView?.invoke("playVideo")
                        }
                    }

                    else -> {}
                }
            }
        }
    }
}

@Composable
fun rememberVimeoPlayerController(
    initialVideoId: String? = "",
    autoPlay: Boolean = false,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) = remember {
    VimeoPlayerController(
        initialVideoId = initialVideoId,
        autoPlay = autoPlay,
        coroutineScope = coroutineScope,
    )
}
