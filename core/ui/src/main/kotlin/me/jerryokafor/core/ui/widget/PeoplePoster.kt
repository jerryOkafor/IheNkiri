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

package me.jerryokafor.core.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.OneVerticalSpacer
import me.jerryokafor.core.ui.R

@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun PeoplePosterPreview() {
    IheNkiriTheme {
        Column(
            modifier =
            Modifier
                .size(200.dp)
                .aspectRatio(1F)
                .background(IheNkiri.color.inverseOnSurface),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            PeoplePoster(
                modifier = Modifier,
                size = 120.dp,
                firstName = "Sandra",
                lastName = "Bullock",
                imageUrl = "",
            )
        }
    }
}

@Composable
fun PeoplePoster(
    modifier: Modifier = Modifier,
    size: Dp = 60.dp,
    firstName: String,
    lastName: String,
    imageUrl: String,
    textColor: Color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val isLocalInspection = LocalInspectionMode.current

    val imageLoader = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.FillWidth,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    Column(
        modifier = modifier
            .width(size)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = modifier
                .size(size)
                .aspectRatio(1F),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .border(
                        width = 3.dp,
                        color = IheNkiri.color.tertiaryContainer.copy(alpha = 0.5F),
                        shape = CircleShape,
                    )
                    .clip(CircleShape),
                painter = if (isError.not() && !isLocalInspection) {
                    imageLoader
                } else {
                    painterResource(R.drawable.ic_avatar)
                },
                contentScale = ContentScale.Inside,
                contentDescription = firstName,
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                        .padding(IheNkiri.spacing.twoAndaHalf),
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 1.dp,
                )
            }
        }
        OneVerticalSpacer()
        Text(
            text = "$firstName \n$lastName",
            style = IheNkiri.typography.labelMedium,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}
