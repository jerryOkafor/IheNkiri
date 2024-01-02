/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 IheNkiri Project
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

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import me.jerryokafor.core.ds.theme.IheNkiri

@Composable
fun <T> IheNkiriDropDown(
    modifier: Modifier = Modifier,
    default: T,
    menus: List<T>,
    onSelectItem: (T) -> Unit,
) {
    var isTimelineTypeExpanded by remember { mutableStateOf(false) }
    var selected by remember(default) { mutableStateOf(default) }

    Surface(
        modifier = modifier.semantics(mergeDescendants = true) {
            role = Role.DropdownList
            contentDescription = "Selection for $default"
        },
        onClick = { isTimelineTypeExpanded = true },
        shape = IheNkiri.shape.pill,
        border = BorderStroke(1.dp, color = IheNkiri.color.primary),
    ) {
        Row(
            modifier = Modifier.padding(
                top = IheNkiri.spacing.half,
                bottom = IheNkiri.spacing.half,
                start = IheNkiri.spacing.oneAndHalf,
                end = IheNkiri.spacing.one,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.animateContentSize(),
                text = selected.toString(),
                style = IheNkiri.typography.bodyMedium,
            )
            Icon(
                modifier = Modifier,
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = "Timeline movie filter",
            )
        }

        DropdownMenu(
            offset = DpOffset(x = 0.dp, y = 10.dp),
            expanded = isTimelineTypeExpanded,
            onDismissRequest = {
                isTimelineTypeExpanded = false
            },
        ) {
            menus.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.toString()) },
                    onClick = {
                        isTimelineTypeExpanded = false
                        selected = it
                        onSelectItem(it)
                    },
                )
            }
        }
    }
}
