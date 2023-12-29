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

@file:Suppress("MagicNumber", "MagicNumber", "TopLevelPropertyNaming")
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

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ui.R

val Black900 = Color(0x88000000)

val headerHeight = 380.dp
val toolbarHeight = 56.dp

private val paddingMedium = 16.dp

private val titlePaddingStart = 16.dp
private val titlePaddingEnd = 72.dp

@Suppress("ktlint:standard:property-naming")
private const val titleFontScaleStart = 1f

@Suppress("ktlint:standard:property-naming")
private const val titleFontScaleEnd = 0.66f

@Composable
fun IhenkiriCollapsingToolbarHeader(
    scroll: ScrollState,
    headerHeightPx: Float,
    imagePath: String,
    modifier: Modifier = Modifier,
) {
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val isLocalInspection = LocalInspectionMode.current

    val posterImageLoader = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imagePath)
            .placeholder(R.drawable.sample_poster)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.FillBounds,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    Box(
        modifier = modifier
            .graphicsLayer {
                translationY = -scroll.value.toFloat() / 2f // Parallax effect
                alpha = (-1f / headerHeightPx) * scroll.value + 1
            },
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = if (isError.not() && !isLocalInspection) {
                posterImageLoader
            } else {
                painterResource(R.drawable.sample_poster)
            },
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Black900),
                        startY = 3 * headerHeightPx / 4,
                    ),
                ),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IhenkiriCollapsingToolbarToolbar(
    scroll: ScrollState,
    headerHeightPx: Float,
    toolbarHeightPx: Float,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit,
) {
    val toolbarBottom by remember {
        mutableStateOf(headerHeightPx - toolbarHeightPx)
    }

    val showToolbar by remember {
        derivedStateOf { scroll.value >= toolbarBottom }
    }

    val toolbarBackgroundColor by animateColorAsState(
        targetValue = if (showToolbar) Color.LightGray.copy(alpha = 0.4F) else Color.Transparent,
        label = "toolbarbackgroundcolor",
    )

    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            Surface(
                modifier = Modifier.padding(IheNkiri.spacing.one),
                color = Color.LightGray.copy(alpha = 0.3F),
                shape = CircleShape,
            ) {
                IconButton(onClick = onNavigationIconClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                    )
                }
            }
        },
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = toolbarBackgroundColor,
        ),
    )
}

@Composable
fun IhenkiriCollapsingToolbarTitle(
    name: String,
    scroll: ScrollState,
    style: TextStyle,
    color: Color = Color.White,
    modifier: Modifier = Modifier,
) {
    var titleHeightPx by remember { mutableStateOf(0f) }
    var titleWidthPx by remember { mutableStateOf(0f) }

    Text(
        text = name,
        style = style,
        color = color,
        modifier = modifier
            .statusBarsPadding()
            .graphicsLayer {
                val collapseRange: Float = (headerHeight.toPx() - toolbarHeight.toPx())
                val collapseFraction: Float = (scroll.value / collapseRange).coerceIn(0f, 1f)

                val scaleXY = lerp(
                    start = titleFontScaleStart.dp,
                    stop = titleFontScaleEnd.dp,
                    fraction = collapseFraction,
                )

                val titleExtraStartPadding = titleWidthPx.toDp() * (1 - scaleXY.value) / 2f

                val titleYFirstInterpolatedPoint = lerp(
                    start = headerHeight - titleHeightPx.toDp() - (paddingMedium.times(4)),
                    stop = headerHeight / 2,
                    fraction = collapseFraction,
                )

                val titleXFirstInterpolatedPoint = lerp(
                    start = titlePaddingStart,
                    stop = (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                    fraction = collapseFraction,
                )

                val titleYSecondInterpolatedPoint = lerp(
                    start = headerHeight / 2,
                    stop = toolbarHeight / 2 - titleHeightPx.toDp() / 2,
                    fraction = collapseFraction,
                )

                val titleXSecondInterpolatedPoint = lerp(
                    start = (titlePaddingEnd - titleExtraStartPadding) * 5 / 4,
                    stop = titlePaddingEnd - titleExtraStartPadding,
                    fraction = collapseFraction,
                )

                val titleY = lerp(
                    start = titleYFirstInterpolatedPoint,
                    stop = titleYSecondInterpolatedPoint,
                    fraction = collapseFraction,
                )

                val titleX = lerp(
                    start = titleXFirstInterpolatedPoint,
                    stop = titleXSecondInterpolatedPoint,
                    fraction = collapseFraction,
                )

                translationY = titleY.toPx()
                translationX = titleX.toPx()
                scaleX = scaleXY.value
                scaleY = scaleXY.value
            }
            .onGloballyPositioned {
                titleHeightPx = it.size.height.toFloat()
                titleWidthPx = it.size.width.toFloat()
            },
    )
}
