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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import me.jerryokafor.core.ds.theme.IheNkiri

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
                    indicatorColor = IheNkiri.color.primary,
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

interface TimelineScope {
    val title: String
    val content: @Composable BoxScope.(Modifier) -> Unit
}
