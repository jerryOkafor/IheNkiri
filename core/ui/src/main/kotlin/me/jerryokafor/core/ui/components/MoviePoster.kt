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

package me.jerryokafor.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme

const val MOVIE_POSTER_TEST_TAG = "movie_poster"
private const val SIZE_ASPECT_RATIO = 0.8F

@ExcludeFromGeneratedCoverageReport
@ThemePreviews
@Composable
fun PosterPreview() {
    IheNkiriTheme {
        Column(
            modifier =
                Modifier
                    .padding(IheNkiri.spacing.twoAndaHalf)
                    .background(IheNkiri.color.inverseOnSurface),
        ) {
            MoviePoster(
                modifier =
                    Modifier
                        .width(100.dp)
                        .aspectRatio(SIZE_ASPECT_RATIO),
                path = "https://example.com/image.jpg",
                contentDescription = "Image",
                shimmer =
                    rememberShimmer(
                        shimmerBounds = ShimmerBounds.Window,
                    ),
            ) {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviePoster(
    modifier: Modifier = Modifier,
    path: String?,
    contentDescription: String,
    shimmer: Shimmer,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier.testTag(MOVIE_POSTER_TEST_TAG),
        onClick = onClick,
        shape = IheNkiri.shape.medium,
    ) {
        SubcomposeAsyncImage(
            model =
                ImageRequest.Builder(LocalContext.current)
                    .data(path)
                    .crossfade(true).build(),
            contentScale = ContentScale.FillBounds,
            contentDescription = contentDescription,
        ) {
            val state = painter.state
            val showShimmer = state is AsyncImagePainter.State.Loading

            ShimmerBox(
                modifier = Modifier.fillMaxWidth(),
                shimmer = shimmer,
                showShimmer = showShimmer,
            ) {
                if (state !is AsyncImagePainter.State.Loading) {
                    SubcomposeAsyncImageContent()
                } else {
                    // Show empty place holder here
                }
            }
        }
    }
}
