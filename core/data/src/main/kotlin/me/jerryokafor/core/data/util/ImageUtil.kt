/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023 IheNkiri Project
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

package me.jerryokafor.core.data.util

import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport

object ImageUtil {
    @ExcludeFromGeneratedCoverageReport
    sealed interface Size {
        val value: String

        @ExcludeFromGeneratedCoverageReport
        enum class BackDrop(override val value: String) : Size {
            W300("w300"),
            W780("w780"),
            W1280("w1280"),
        }

        @ExcludeFromGeneratedCoverageReport
        enum class Logo(override val value: String) : Size {
            W45("w45"),
            W93("w92"),
            W154("w254"),
            W185("w185"),
            W300("w300"),
            W500("w500"),
        }

        @ExcludeFromGeneratedCoverageReport
        enum class Poster(override val value: String) : Size {
            W92("w92"),
            W154("w154"),
            W185("w185"),
            W342("w342"),
            W500("w500"),
            W780("w780"),
        }

        @ExcludeFromGeneratedCoverageReport
        enum class Profile(override val value: String) : Size {
            W45("w45"),
            W185("w185"),
            H632("h632"),
        }

        @ExcludeFromGeneratedCoverageReport
        enum class Still(override val value: String) : Size {
            W92("w92"),
            W185("w185"),
            W300("w300"),
        }

        @ExcludeFromGeneratedCoverageReport
        data object Original : Size {
            override val value: String
                get() = "original"
        }
    }

    @ExcludeFromGeneratedCoverageReport
    fun buildImageUrl(
        path: String?,
        size: Size = Size.Original,
    ): String = "https://image.tmdb.org/t/p/${size.value}/$path"
}
