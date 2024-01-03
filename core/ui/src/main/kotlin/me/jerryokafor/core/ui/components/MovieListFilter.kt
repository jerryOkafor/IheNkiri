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

package me.jerryokafor.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.TwoAndHalfHorizontalSpacer
import me.jerryokafor.core.model.MovieListFilterItem

@ExcludeFromGeneratedCoverageReport
@Composable
private fun testFilters() = listOf(
    MovieListFilterItem(
        isSelected = true,
        type = MovieListFilterItem.FilterType.NOW_PLAYING,
    ),
    MovieListFilterItem(
        isSelected = false,
        type = MovieListFilterItem.FilterType.POPULAR,
    ),
    MovieListFilterItem(
        isSelected = false,
        type = MovieListFilterItem.FilterType.TOP_RATED,
    ),
    MovieListFilterItem(
        isSelected = false,
        type = MovieListFilterItem.FilterType.UPCOMING,
    ),
)

@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun MovieListFilterPreview() {
    IheNkiriTheme {
        MovieListFilter(
            items = testFilters().map { Pair("Upcoming", it.isSelected) },
        ) {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListFilter(
    modifier: Modifier = Modifier,
    items: List<Pair<String, Boolean>>,
    onItemSelected: (Int) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
    ) {
        item { TwoAndHalfHorizontalSpacer() }
        itemsIndexed(items) { index, item ->
            FilterChip(
                selected = item.second,
                onClick = { onItemSelected(index) },
                label = { Text(text = item.first) },
                shape = IheNkiri.shape.pill,
                colors =
                    FilterChipDefaults.filterChipColors(
                        containerColor = IheNkiri.color.primary,
                        labelColor = IheNkiri.color.onPrimary.copy(alpha = 0.7f),
                    ),
                border =
                    FilterChipDefaults.filterChipBorder(
                        borderColor = IheNkiri.color.onPrimary.copy(alpha = 0.7f),
                    ),
            )
        }
        item { TwoAndHalfHorizontalSpacer() }
    }
}
