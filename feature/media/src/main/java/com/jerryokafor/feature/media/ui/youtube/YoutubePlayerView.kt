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

import android.os.Handler
import android.os.Looper
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.jerryokafor.feature.media.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

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

@Stable
class YoutubePlayerState(coroutineScope: CoroutineScope) {
    var isPlayerReady: Boolean by mutableStateOf(false)

    var isIframeApiReady: Boolean by mutableStateOf(false)

    var events: MutableStateFlow<YouTubePlayerEvent> =
        MutableStateFlow(YouTubePlayerEvent.Default)
        internal set

    internal var webView by mutableStateOf<WebView?>(null)

    init {
        coroutineScope.launch {
            events.collect { event ->
                when (event) {
                    YouTubePlayerEvent.Default -> {}
                    YouTubePlayerEvent.OnIframeAPIReady -> isIframeApiReady = true
                    YouTubePlayerEvent.OnPlayerReady -> isPlayerReady = true
                }
            }
        }
    }
}

@Composable
fun rememberYoutubePlayerState(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): YoutubePlayerState = remember(coroutineScope) { YoutubePlayerState(coroutineScope) }

@Composable
internal fun YoutubePlayerView(
    videoId: String,
    playerController: YoutubePlayerController = rememberYoutubePlayerController(),
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val playerState = rememberYoutubePlayerState()

    @Suppress("MagicNumber")
    AndroidView(
        factory = {
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled = true
                    mediaPlaybackRequiresUserGesture = false
                    cacheMode = WebSettings.LOAD_DEFAULT
                }

                addJavascriptInterface(
                    YouTubePlayerBridge(playerState, playerController),
                    "YouTubePlayerBridge",
                )
            }.also {
                playerController.webView = it
                playerState.webView = it
            }
        },
        modifier = modifier
            .background(Color.LightGray)
            .aspectRatio(1.75f),
        update = { webView ->
            val htmlPage =
                readHTMLFromUTF8File(context.resources.openRawResource(R.raw.youtube_player_embed))
                    .replace(
                        oldValue = "<<VIDEO_ID>>",
                        newValue = videoId,
                    )
            webView.loadDataWithBaseURL(
                "https://www.youtube.com",
                htmlPage,
                "text/html",
                "utf-8",
                null,
            )
        },
        onRelease = {},
    )
}

@VisibleForTesting
@Suppress("TooGenericExceptionThrown")
internal fun readHTMLFromUTF8File(inputStream: InputStream): String {
    inputStream.use {
        try {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
            return bufferedReader.readLines().joinToString("\n")
        } catch (e: Exception) {
            throw RuntimeException("Can't parse HTML file.")
        }
    }
}

fun WebView.invoke(
    function: String,
    vararg args: Any,
) {
    val stringArgs = args.map {
        if (it is String) {
            "'$it'"
        } else {
            it.toString()
        }
    }
    Handler(Looper.getMainLooper()).post {
        loadUrl("javascript:$function(${stringArgs.joinToString(",")})")
    }
}
