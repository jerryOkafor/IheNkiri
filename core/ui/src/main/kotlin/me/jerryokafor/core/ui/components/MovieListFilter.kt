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

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.TwoAndHalfHorizontalSpacer
import me.jerryokafor.core.model.MovieListFilterItem

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme", showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme", showBackground = true)
@Composable
@ExcludeFromGeneratedCoverageReport
private fun MovieListFilterPreview() {
    IheNkiriTheme {
        MovieListFilter(
            filters = listOf(
                MovieListFilterItem(
                    label = "Now Playing",
                    isSelected = true,
                    type = MovieListFilterItem.FilterType.NOW_PLAYING,
                ),
                MovieListFilterItem(
                    label = "Popular",
                    isSelected = false,
                    type = MovieListFilterItem.FilterType.POPULAR,
                ),
                MovieListFilterItem(
                    label = "Top Rated",
                    isSelected = false,
                    type = MovieListFilterItem.FilterType.TOP_RATED,
                ),
                MovieListFilterItem(
                    label = "Upcoming",
                    isSelected = false,
                    type = MovieListFilterItem.FilterType.UPCOMING,
                ),
            ),
        ) {}
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListFilter(
    modifier: Modifier = Modifier,
    filters: List<MovieListFilterItem>,
    onItemSelected: (MovieListFilterItem.FilterType) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.one),
    ) {
        item { TwoAndHalfHorizontalSpacer() }
        items(filters) {
            FilterChip(
                selected = it.isSelected,
                onClick = { onItemSelected(it.type) },
                label = { Text(text = it.label) },
                shape = IheNkiri.shape.pill,
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = IheNkiri.color.primary,
                    labelColor = IheNkiri.color.onPrimary.copy(alpha = 0.7f),
                ),
                border = FilterChipDefaults.filterChipBorder(
                    borderColor = IheNkiri.color.onPrimary.copy(alpha = 0.7f),
                ),
            )
        }
        item { TwoAndHalfHorizontalSpacer() }
    }
}
