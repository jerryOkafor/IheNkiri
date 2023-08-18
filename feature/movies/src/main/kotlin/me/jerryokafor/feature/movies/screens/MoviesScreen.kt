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

package me.jerryokafor.feature.movies.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.TwoAndHalfVerticalSpacer
import me.jerryokafor.feature.movies.R

@ThemePreviews
@Composable
private fun MoviesScreenPreview() {
    IheNkiriTheme {
        MoviesScreen("Movies")
    }
}

@Composable
@Suppress("UnusedPrivateMember")
fun MoviesScreen(currentScreen: String) {
    @Suppress("MagicNumber")
    val colorStops1 = listOf(
        Color(0xFF8000FF),
        Color(0x0),
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(IheNkiri.color.surface),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colorStops1)),
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(IheNkiri.spacing.twoAndaHalf),
            ) {
                SearchBar(modifier = Modifier.fillMaxWidth())
                TwoAndHalfVerticalSpacer()
                ChipGroup() {}
                TwoAndHalfVerticalSpacer()
                LazyRow(horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.two)) {
                    @Suppress("MagicNumber")
                    items(5) {
                        Poster(contentDescription = "") {}
                    }
                }

                TwoAndHalfVerticalSpacer()
                Text(text = "More Movies", style = IheNkiri.typography.titleLarge)
                TwoAndHalfVerticalSpacer()
                LazyRow(horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.two)) {
                    @Suppress("MagicNumber")
                    items(5) {
                        Poster(contentDescription = "") {}
                    }
                }
            }
        }
    }
}

@ThemePreviews
@Composable
fun PosterPreview() {
    IheNkiriTheme {
        Column(modifier = Modifier.padding(IheNkiri.spacing.twoAndaHalf)) {
            Poster("") {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Poster(contentDescription: String, onClick: () -> Unit) {
    Card(onClick = onClick, shape = IheNkiri.shape.medium) {
        Image(
            painter = painterResource(id = R.drawable.sample_movie_poster),
            contentScale = ContentScale.FillBounds,
            contentDescription = contentDescription,
        )
    }
}

@ThemePreviews
@Composable
fun SearchBarPreview() {
    IheNkiriTheme {
        Column(modifier = Modifier.padding(IheNkiri.spacing.twoAndaHalf)) {
            SearchBar()
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, onSearch: (searchQuery: String) -> Unit = {}) {
    val searching by remember { mutableStateOf(false) }
    val trailingIcon =
        if (searching) {
            me.jerryokafor.core.ui.R.drawable.baseline_cancel_24
        } else {
            me.jerryokafor.core.ui.R.drawable.search
        }
    val contentDescription = if (searching) "Close" else "Search"
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = modifier,
        value = searchQuery,
        onValueChange = { searchQuery = it },
        shape = IheNkiri.shape.pill,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(searchQuery) }),
        trailingIcon = {
            Icon(
                painter = painterResource(id = trailingIcon),
                tint = IheNkiri.color.onTertiaryContainer,
                contentDescription = contentDescription,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = IheNkiri.color.tertiaryContainer,
            unfocusedContainerColor = IheNkiri.color.tertiaryContainer,
        ),
    )
}

@ThemePreviews
@Composable
fun ChipGroupPreview() {
    IheNkiriTheme {
        ChipGroup() {}
    }
}

data class Chip(val label: String, val isSelected: Boolean, val type: FilterType) {
    enum class FilterType {
        NOW_PLAYING, POPULAR, TOP_RATED, UPCOMING
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChipGroup(onItemSelected: (Chip.FilterType) -> Unit) {
    val filters = listOf(
        Chip(label = "Now Playing", isSelected = true, type = Chip.FilterType.NOW_PLAYING),
        Chip(label = "Popular", isSelected = false, type = Chip.FilterType.POPULAR),
        Chip(label = "Top Rated", isSelected = false, type = Chip.FilterType.TOP_RATED),
        Chip(label = "Upcoming", isSelected = false, type = Chip.FilterType.UPCOMING),
    )

    LazyRow(horizontalArrangement = Arrangement.spacedBy(IheNkiri.spacing.twoAndaHalf)) {
        items(filters) {
            FilterChip(
                selected = it.isSelected,
                onClick = { onItemSelected(it.type) },
                label = { Text(text = it.label) },
                shape = IheNkiri.shape.pill,
                colors = FilterChipDefaults.filterChipColors(),
            )
        }
    }
}
