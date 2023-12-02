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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.jerryokafor.feature.peopledetails.viewModel.PeopleDetailsViewModel
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.theme.IheNkiriTheme

@Preview
@Composable
@ExcludeFromGeneratedCoverageReport
fun PeopleDetailsScreenPreview() {
    IheNkiriTheme {
        PeopleDetailsScreen()
    }
}

@Composable
@Suppress("UnusedPrivateMember")
fun PeopleDetailsScreen(
    viewModel: PeopleDetailsViewModel = hiltViewModel(),
    onPersonClick: (Long) -> Unit,
    onNavigateUp: () -> Unit,
) {
    PeopleDetailsScreen(onPersonClick = onPersonClick, onNavigateUp = onNavigateUp)
}

@Composable
@Suppress("UnusedPrivateMember")
fun PeopleDetailsScreen(
    onPersonClick: (Long) -> Unit = {},
    onNavigateUp: () -> Unit = {},
) {
    LazyColumn {
        @Suppress("MagicNumber")
        repeat(5) {
            item {
                TimelineNode {
                    Text(modifier = it, text = "This is a timeline")
                }
            }
        }
    }
}

@Composable
fun TimelineNode(content: @Composable BoxScope.(modifier: Modifier) -> Unit) {
    Box(modifier = Modifier.wrapContentSize()) {
        content(Modifier)
    }
}
