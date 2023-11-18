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

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme

@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun MovieRatingPreview() {
    IheNkiriTheme {
        Box(
            modifier =
            Modifier
                .size(200.dp)
                .aspectRatio(1F)
                .background(IheNkiri.color.inverseOnSurface),
        ) {
            MovieRating(
                modifier =
                Modifier
                    .size(100.dp)
                    .align(Alignment.Center),
                rating = 0.45F,
            )
        }
    }
}

@Composable
fun MovieRating(
    modifier: Modifier = Modifier,
    rating: Float,
    strokeWidth: Dp = 6.dp,
    textStyle: TextStyle = IheNkiri.typography.titleSmall,
    textColor: Color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
) {
    var progress by remember { mutableStateOf(0f) }
    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec =
        tween(
            durationMillis = 3000,
            delayMillis = 100,
            easing = FastOutSlowInEasing,
        ),
        label = "progressAnimation",
    )

    Box(modifier = modifier) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            trackColor = IheNkiri.color.tertiaryContainer,
            color = IheNkiri.color.secondaryContainer,
            progress = progressAnimation,
            strokeWidth = strokeWidth,
            strokeCap = StrokeCap.Round,
        )
        @Suppress("MagicNumber")
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "${(rating * 100).toInt()}%",
            textAlign = TextAlign.Center,
            style = textStyle,
            fontWeight = FontWeight.W700,
            color = textColor,
        )
    }

    LaunchedEffect(rating) {
        progress = rating
    }
}
