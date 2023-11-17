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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
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
fun PersonItemViewPreview() {
    IheNkiriTheme {
        Column(
            modifier =
                Modifier
                    .wrapContentSize()
                    .background(IheNkiri.color.inverseOnSurface),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OneVerticalSpacer()
            PersonItemView(
                modifier =
                    Modifier
                        .width(250.dp)
                        .padding(IheNkiri.spacing.twoAndaHalf),
                name = "Sandra Bullock",
                knownFor = "Rush Hour, Rush Hour 2, and Rush Hour 3",
                imageUrl = "",
            )
            OneVerticalSpacer()
        }
    }
}

@Composable
fun PersonItemView(
    modifier: Modifier = Modifier,
    name: String,
    knownFor: String,
    imageUrl: String,
    textColor: Color = contentColorFor(backgroundColor = IheNkiri.color.surfaceVariant),
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(5.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .aspectRatio(1F),
            ) {
                val contentDescription = "$name's profile"
                SubcomposeAsyncImage(
                    modifier =
                        Modifier
                            .matchParentSize()
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(5.dp)),
                    contentScale = ContentScale.FillWidth,
                    model =
                        ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .placeholder(R.drawable.ic_avatar)
                            .fallback(R.drawable.ic_avatar)
                            .build(),
                    contentDescription = contentDescription,
                ) {
                    when (painter.state) {
                        AsyncImagePainter.State.Empty, is AsyncImagePainter.State.Error -> {
                            Image(
                                modifier =
                                    Modifier
                                        .matchParentSize()
                                        .clip(RoundedCornerShape(5.dp)),
                                painter = painterResource(id = R.drawable.ic_avatar),
                                contentDescription = contentDescription,
                                contentScale = ContentScale.FillBounds,
                            )
                        }

                        is AsyncImagePainter.State.Loading ->
                            CircularProgressIndicator(
                                modifier =
                                    Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                        .padding(IheNkiri.spacing.twoAndaHalf),
                                strokeCap = StrokeCap.Round,
                                strokeWidth = 1.dp,
                            )

                        is AsyncImagePainter.State.Success -> SubcomposeAsyncImageContent()
                    }
                }
            }
            OneVerticalSpacer()
            Column(modifier = Modifier.padding(horizontal = IheNkiri.spacing.one)) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = name,
                    style = IheNkiri.typography.titleMedium,
                    maxLines = 1,
                    color = textColor,
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = knownFor,
                    style = IheNkiri.typography.bodySmall,
                    maxLines = 2,
                    minLines = 2,
                    color = textColor.copy(alpha = 0.7f),
                )
                OneVerticalSpacer()
            }
        }
    }
}
