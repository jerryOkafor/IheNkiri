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

sealed class VimeoPlayerEvent {
    data class OnPlayerReady(val title: String, val duration: Float, val tracks: String) :
        VimeoPlayerEvent()

    data object OnInitFailed : VimeoPlayerEvent()

    data class OnError(val message: String, val method: String, val name: String) :
        VimeoPlayerEvent()

    data class OnTimeUpdate(val seconds: Float) : VimeoPlayerEvent()

    data class OnPlay(val duration: Float) : VimeoPlayerEvent()

    data class OnPause(val duration: Float) : VimeoPlayerEvent()

    data class OnEnded(val duration: Float) : VimeoPlayerEvent()

    data class OnVolumeChange(val volume: Float) : VimeoPlayerEvent()

    data class OnTextTrackChange(val kind: String, val label: String, val language: String) :
        VimeoPlayerEvent()
}
