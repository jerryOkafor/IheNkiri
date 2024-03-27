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

import android.util.Log
import android.webkit.JavascriptInterface
import kotlinx.coroutines.flow.update

class VimeoPlayerBridge(
    private val playerController: VimeoPlayerController,
    private val playerState: VimeoPlayerState,
) {
    companion object {
        const val TAG = "VimeoPlayerBridge"
    }

    // Events
    @JavascriptInterface
    fun onPlayerReady(
        title: String,
        duration: Float,
        tracks: String,
    ) {
        Log.d(TAG, "onPlayerReady() : $title | $duration | $tracks")
        playerController.events.update { VimeoPlayerEvent.OnPlayerReady(title, duration, tracks) }
        playerState.events.update { VimeoPlayerEvent.OnPlayerReady(title, duration, tracks) }
    }

    @JavascriptInterface
    fun onInitFailed(message: String) {
        Log.d(TAG, "onInitFailed() : $message")
        playerController.events.update { VimeoPlayerEvent.OnInitFailed }
    }

    @JavascriptInterface
    fun onError(
        message: String,
        method: String,
        name: String,
    ) {
        Log.d(TAG, "onError() : $message | $method | $name")
        playerController.events.update { VimeoPlayerEvent.OnError(message, method, name) }
        playerState.events.update { VimeoPlayerEvent.OnError(message, method, name) }
    }

    @JavascriptInterface
    fun onTimeUpdate(seconds: Float) {
        Log.d(TAG, "onTimeUpdate() : $seconds")
        playerController.events.update { VimeoPlayerEvent.OnTimeUpdate(seconds) }
        playerState.events.update { VimeoPlayerEvent.OnTimeUpdate(seconds) }
    }

    @JavascriptInterface
    fun onPlay(duration: Float) {
        Log.d(TAG, "onPlay(): $duration")
        playerController.events.update { VimeoPlayerEvent.OnPlay(duration) }
        playerState.events.update { VimeoPlayerEvent.OnPlay(duration) }
    }

    @JavascriptInterface
    fun onPause(seconds: Float) {
        Log.d(TAG, "onPause() :$seconds")
        playerController.events.update { VimeoPlayerEvent.OnPause(seconds) }
        playerState.events.update { VimeoPlayerEvent.OnPause(seconds) }
    }

    @JavascriptInterface
    fun onEnded(duration: Float) {
        Log.d(TAG, "onEnded() : $duration")
        playerController.events.update { VimeoPlayerEvent.OnEnded(duration) }
        playerState.events.update { VimeoPlayerEvent.OnEnded(duration) }
    }

    @JavascriptInterface
    fun onVolumeChange(volume: Float) {
        Log.d(TAG, "onVolumeChange() : $volume")
        playerController.events.update { VimeoPlayerEvent.OnVolumeChange(volume) }
        playerState.events.update { VimeoPlayerEvent.OnVolumeChange(volume) }
    }

    @JavascriptInterface
    fun onTextTrackChange(
        kind: String,
        label: String,
        language: String,
    ) {
        Log.d(TAG, "onTextTrackChange() :$kind | $label | $language")
        playerController.events.update { VimeoPlayerEvent.OnTextTrackChange(kind, label, language) }
        playerState.events.update { VimeoPlayerEvent.OnTextTrackChange(kind, label, language) }
    }
}
