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

package com.jerryokafor.feature.peopledetails.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.jerryokafor.core.ds.theme.IheNkiri

data class TimelineNodeConfig(
    val indicatorColor: Color,
    val circleRadius: Dp,
    val circleStrokeWidth: Dp,
    val lineStrokeWidth: Dp,
    val titleTextStyle: TextStyle,
)

object TimelineNodeDefaults {
    private val indicatorColor: Color = Color.White
    private val circleRadius: Dp = 8.dp
    private val circleStrokeWidth: Dp = 3.0.dp
    private val lineStrokeWidth: Dp = 1.0.dp
    private val titleTextStyle: TextStyle = TextStyle(fontSize = 14.sp)

    @Composable
    fun timelineConfig(
        indicatorColor: Color = TimelineNodeDefaults.indicatorColor,
        circleRadius: Dp = TimelineNodeDefaults.circleRadius,
        circleStrokeWidth: Dp = TimelineNodeDefaults.circleStrokeWidth,
        lineStrokeWidth: Dp = TimelineNodeDefaults.lineStrokeWidth,
        titleTextStyle: TextStyle = TimelineNodeDefaults.titleTextStyle,
    ): TimelineNodeConfig = TimelineNodeConfig(
        indicatorColor = indicatorColor,
        circleRadius = circleRadius,
        circleStrokeWidth = circleStrokeWidth,
        lineStrokeWidth = lineStrokeWidth,
        titleTextStyle = titleTextStyle,
    )
}

interface TimelineScope {
    val title: String
    val content: @Composable BoxScope.(Modifier) -> Unit
}

@Composable
fun <T : TimelineScope> Timeline(
    modifier: Modifier = Modifier,
    items: List<T>,
) {
    val lineAnim = remember { Animatable(0f) }
    val circleAnim = remember { Animatable(0f) }

    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        items.forEach { item ->
            TimelineNode(
                title = item.title,
                lineAnim = lineAnim.value,
                circleAnim = circleAnim.value,
                config = TimelineNodeDefaults.timelineConfig(
                    titleTextStyle = IheNkiri.typography.titleMedium,
                ),
            ) {
                item.content(this, it)
            }
        }
    }

    LaunchedEffect(Unit, block = {
        launch {
            lineAnim.animateTo(
                targetValue = 1f,
                animationSpec = @Suppress("MagicNumber")
                (tween(2000, easing = LinearEasing)),
            )
        }
        launch {
            circleAnim.animateTo(
                targetValue = 1f,
                animationSpec = @Suppress("MagicNumber")
                (tween(2000, easing = LinearEasing)),
            )
        }
    })
}

@Composable
fun TimelineNode(
    modifier: Modifier = Modifier,
    title: String,
    lineAnim: Float,
    circleAnim: Float,
    config: TimelineNodeConfig = TimelineNodeDefaults.timelineConfig(),
    content: @Composable BoxScope.(Modifier) -> Unit,
) {
    val textMeasurer = rememberTextMeasurer()
    val linePadding = 8.dp

    Box(
        modifier = modifier
            .wrapContentSize()
            .padding(start = 8.dp, top = 8.dp)
            .drawWithCache {
                val textLayoutResult = textMeasurer.measure(
                    text = AnnotatedString(title),
                    constraints = Constraints.fixedWidth(size.width.toInt()),
                    style = config.titleTextStyle,
                )

                onDrawBehind {
                    val circleRadiusPx = config.circleRadius.toPx()
                    val linePaddingPx = linePadding.toPx()
                    val circleStrokeSizePx = config.circleStrokeWidth.toPx()
                    val lineStrokeSizePx = config.lineStrokeWidth.toPx()
                    val timelineColors = listOf(Color.Cyan, Color.LightGray)

                    drawArc(
                        color = config.indicatorColor,
                        startAngle = 270f,
                        sweepAngle =
                            @Suppress("MagicNumber")
                            360f
                                *
                                circleAnim,
                        useCenter = false,
                        topLeft = Offset(x = 0f, y = 0f),
                        size = Size(width = circleRadiusPx * 2, height = circleRadiusPx * 2),
                        style = Stroke(width = circleStrokeSizePx),
                    )

                    drawText(
                        topLeft = Offset(circleRadiusPx * 2 + linePaddingPx, 0F),
                        textLayoutResult = textLayoutResult,
                        color = Color.White,
                    )

                    clipRect(bottom = size.height * lineAnim) {
                        drawLine(
                            brush = Brush.verticalGradient(timelineColors),
                            start = Offset(
                                x = circleRadiusPx,
                                y = circleRadiusPx * 2 + linePaddingPx,
                            ),
                            end = Offset(
                                x = circleRadiusPx,
                                y = size.height,
                            ),
                            cap = StrokeCap.Round,
                            strokeWidth = lineStrokeSizePx,
                        )
                    }
                }
            },
        content = { content(Modifier) },
    )
}
