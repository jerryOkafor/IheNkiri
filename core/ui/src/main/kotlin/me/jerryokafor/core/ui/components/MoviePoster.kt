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

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ui.R

const val MOVIE_POSTER_TEST_TAG = "movie_poster"
private const val SIZE_ASPECT_RATIO = 0.8F

@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun PosterPreview() {
    IheNkiriTheme {
        Column(
            modifier = Modifier.padding(IheNkiri.spacing.twoAndaHalf),
        ) {
            MoviePoster(
                modifier =
                Modifier
                    .width(100.dp)
                    .aspectRatio(SIZE_ASPECT_RATIO),
                posterUrl = "https://example.com/image.jpg",
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
    posterUrl: String?,
    contentDescription: String,
    shimmer: Shimmer,
    onClick: () -> Unit,
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val isLocalInspection = LocalInspectionMode.current

    val imageLoader = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(posterUrl)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.FillBounds,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    Card(
        modifier = modifier.testTag(MOVIE_POSTER_TEST_TAG),
        onClick = onClick,
        shape = IheNkiri.shape.small,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        ShimmerBox(
            modifier = Modifier.fillMaxSize(),
            shimmer = shimmer,
            isLoading = isLoading,
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = if (isError.not() && !isLocalInspection) {
                    imageLoader
                } else {
                    painterResource(R.drawable.sample_poster)
                },
                contentScale = ContentScale.FillBounds,
                contentDescription = contentDescription,
            )
        }
    }
}
