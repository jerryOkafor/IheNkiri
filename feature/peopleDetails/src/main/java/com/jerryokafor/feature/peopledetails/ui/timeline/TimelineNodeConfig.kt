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

package com.jerryokafor.feature.peopledetails.ui.timeline

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
