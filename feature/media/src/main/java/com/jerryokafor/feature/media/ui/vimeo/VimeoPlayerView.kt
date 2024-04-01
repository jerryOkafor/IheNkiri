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

import android.content.Context
import android.view.ViewGroup.LayoutParams
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.jerryokafor.feature.media.R
import com.jerryokafor.feature.media.ui.youtube.invoke
import com.jerryokafor.feature.media.ui.youtube.readHTMLFromUTF8File
import me.jerryokafor.core.ds.theme.IheNkiriTheme

@Preview
@Composable
private fun VimeoPlayerViewPreview() {
    IheNkiriTheme {
        VimeoPlayerView(modifier = Modifier.fillMaxWidth())
    }
}

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun VimeoPlayerView(
    modifier: Modifier = Modifier,
    playerController: VimeoPlayerController = rememberVimeoPlayerController(autoPlay = true),
    playerState: VimeoPlayerState = rememberVimeoPlayerState(),
    factory: ((Context) -> WebView)? = null,
) {
    val context = LocalContext.current

    BoxWithConstraints(modifier) {
        // WebView changes it's layout strategy based on
        // it's layoutParams. We convert from Compose Modifier to
        // layout params here.
        val width = if (constraints.hasFixedWidth) {
            LayoutParams.MATCH_PARENT
        } else {
            LayoutParams.WRAP_CONTENT
        }

        val height = if (constraints.hasFixedHeight) {
            LayoutParams.MATCH_PARENT
        } else {
            LayoutParams.WRAP_CONTENT
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

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(
                                view: WebView?,
                                url: String?,
                            ) {
                                super.onPageFinished(view, url)
                                view?.invoke("initVimeoPlayer")
                            }
                        }

                        addJavascriptInterface(
                            VimeoPlayerBridge(playerController, playerState),
                            "VimeoPlayerBridge",
                        )
                    }.also {
                        playerController.webView = it
                    }
                )
            },
            modifier = modifier
                .background(Color.LightGray)
                .aspectRatio(1.75f),
            update = { webView ->
                val options = VimeoOptions()
                val hashKey: String? = ""
                val videoId = playerController.initialVideoId

                val videoUrl =
                    if (hashKey.isNullOrBlank()) {
                        "https://vimeo.com/$videoId"
                    } else {
                        "https://vimeo.com/$videoId/h=$hashKey"
                    }
                val htmlPage =
                    readHTMLFromUTF8File(context.resources.openRawResource(R.raw.vimeo_player))
                        .replace("<VIDEO_URL>", videoUrl)
                        .replace(
                            oldValue = "<BACKGROUND_COLOR>",
                            newValue = options.backgroundColor.toHexString(),
                        )
                        .replace(oldValue = "<AUTOPLAY>", newValue = options.autoPlay.toString())
                        .replace("<PLAYSINLINE>", "1")
                        .replace("<COLOR>", options.color.toHexString())
                        .replace("<MUTED>", options.muted.toString())
                        .replace("<LOOP>", options.loop.toString())
                        .replace("<TITLE>", options.title.toString())
                        .replace("<QUALITY>", options.quality)
                webView.loadDataWithBaseURL(
                    "https://vimeo.com",
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
