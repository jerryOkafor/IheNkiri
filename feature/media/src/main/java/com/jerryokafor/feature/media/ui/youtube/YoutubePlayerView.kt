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

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.jerryokafor.feature.media.R
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

@Composable
internal fun YoutubePlayerView(
    playerController: YoutubePlayerController = rememberYoutubePlayerController(),
    playerState: YoutubePlayerState = rememberYoutubePlayerState(),
    modifier: Modifier = Modifier,
    factory: ((Context) -> WebView)? = null,
) {
    val context = LocalContext.current

    BoxWithConstraints(modifier) {
        // WebView changes it's layout strategy based on
        // it's layoutParams. We convert from Compose Modifier to
        // layout params here.
        val width = if (constraints.hasFixedWidth) {
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        val height = if (constraints.hasFixedHeight) {
            ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            ViewGroup.LayoutParams.WRAP_CONTENT
        }

        val layoutParams = FrameLayout.LayoutParams(
            width,
            height,
        )

        @Suppress("MagicNumber")
        AndroidView(
            factory = { context ->
                (
                    factory?.invoke(context) ?: WebView(context).apply {
                        settings.apply {
                            javaScriptEnabled = true
                            mediaPlaybackRequiresUserGesture = false
                            cacheMode = WebSettings.LOAD_DEFAULT
                        }

                        this.layoutParams = layoutParams

                        addJavascriptInterface(
                            YouTubePlayerBridge(playerState, playerController),
                            "YouTubePlayerBridge",
                        )
                    }.also { playerController.webView = it }
                )
            },
            modifier = Modifier
                .background(Color.LightGray)
                .aspectRatio(1.75f),
            update = { webView ->
                val htmlPage =
                    readHTMLFromUTF8File(
                        context.resources.openRawResource(R.raw.youtube_player_embed),
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
