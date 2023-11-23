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

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ImageUtilTest {
    private val testPath = "/gsUmIBN7b03aCTanD3BX3QCRfii.jpg"

    @Test
    fun `test buildImageUrl()`() {
        // Backdrop
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.BackDrop.W300),
        ).isEqualTo("https://image.tmdb.org/t/p/w300/$testPath")

        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.BackDrop.W780),
        ).isEqualTo("https://image.tmdb.org/t/p/w780/$testPath")

        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.BackDrop.W1280),
        ).isEqualTo("https://image.tmdb.org/t/p/w1280/$testPath")

        // Logo
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Logo.W45),
        ).isEqualTo("https://image.tmdb.org/t/p/w45/$testPath")
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Logo.W92),
        ).isEqualTo("https://image.tmdb.org/t/p/w92/$testPath")
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Logo.W154),
        ).isEqualTo("https://image.tmdb.org/t/p/w154/$testPath")
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Logo.W185),
        ).isEqualTo("https://image.tmdb.org/t/p/w185/$testPath")
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Logo.W300),
        ).isEqualTo("https://image.tmdb.org/t/p/w300/$testPath")
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Logo.W500),
        ).isEqualTo("https://image.tmdb.org/t/p/w500/$testPath")

        // Poster
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Poster.W92),
        ).isEqualTo("https://image.tmdb.org/t/p/w92/$testPath")
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Poster.W154),
        ).isEqualTo("https://image.tmdb.org/t/p/w154/$testPath")
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Poster.W185),
        ).isEqualTo("https://image.tmdb.org/t/p/w185/$testPath")
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Poster.W342),
        ).isEqualTo("https://image.tmdb.org/t/p/w342/$testPath")
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Poster.W500),
        ).isEqualTo("https://image.tmdb.org/t/p/w500/$testPath")
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Poster.W780),
        ).isEqualTo("https://image.tmdb.org/t/p/w780/$testPath")

        // Profile
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Profile.W45),
        ).isEqualTo("https://image.tmdb.org/t/p/w45/$testPath")

        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Profile.W185),
        ).isEqualTo("https://image.tmdb.org/t/p/w185/$testPath")

        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Profile.H632),
        ).isEqualTo("https://image.tmdb.org/t/p/h632/$testPath")

        // Still
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Still.W92),
        ).isEqualTo("https://image.tmdb.org/t/p/w92/$testPath")

        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Still.W185),
        ).isEqualTo("https://image.tmdb.org/t/p/w185/$testPath")

        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Still.W300),
        ).isEqualTo("https://image.tmdb.org/t/p/w300/$testPath")

        // Original
        assertThat(
            ImageUtil.buildImageUrl(path = testPath, size = ImageUtil.Size.Original),
        ).isEqualTo("https://image.tmdb.org/t/p/original/$testPath")
    }
}
