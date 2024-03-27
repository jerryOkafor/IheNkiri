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

import android.util.Log
import android.webkit.JavascriptInterface
import kotlinx.coroutines.flow.update

class YouTubePlayerBridge(
    private val playerState: YoutubePlayerState,
    private val playerController: YoutubePlayerController,
) {
    companion object {
        const val TAG = "YouTubePlayerBridge"
    }

    @JavascriptInterface
    fun onYouTubeIframeAPIReady() {
        Log.d(TAG, "onYouTubeIframeAPIReady()")
        playerState.events.update { YouTubePlayerEvent.OnIframeAPIReady }
        playerController.events.update { YouTubePlayerEvent.OnIframeAPIReady }
    }

    @JavascriptInterface
    fun onPlayerReady() {
        Log.d(TAG, "onPlayerReady()")
        playerState.events.update { YouTubePlayerEvent.OnPlayerReady }
        playerController.events.update { YouTubePlayerEvent.OnPlayerReady }
    }

    @JavascriptInterface
    fun onPlayerStateChange(data: String) {
        Log.d(TAG, "onPlayerStateChange(): $data")
    }

    @JavascriptInterface
    fun onLoadVideoById(videoId: String) {
        Log.d(TAG, "onLoadVideoById(): $videoId")
    }

    @JavascriptInterface
    fun sendPlaybackQualityChange(state: String) {
        Log.d(TAG, "sendPlaybackQualityChange() : $state")
    }

    @JavascriptInterface
    fun sendPlaybackRateChange(rate: String) {
        Log.d(TAG, "sendPlaybackRateChange() : $rate")
    }

    @JavascriptInterface
    fun sendError(error: String) {
        Log.d(TAG, "sendError() : $error")
    }

    @JavascriptInterface
    fun sendApiChange() {
        Log.d(TAG, "sendApiChange()")
    }
}
