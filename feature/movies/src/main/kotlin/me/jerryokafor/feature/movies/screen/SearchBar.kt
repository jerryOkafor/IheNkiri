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

package me.jerryokafor.feature.movies.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import me.jerryokafor.core.common.annotation.IgnoreCoverageAsGenerated
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme

@ThemePreviews
@Composable
@IgnoreCoverageAsGenerated
private fun SearchBarPreview() {
    IheNkiriTheme {
        Column(modifier = Modifier.padding(IheNkiri.spacing.twoAndaHalf)) {
            SearchBar()
        }
    }
}

@Composable
internal fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (searchQuery: String) -> Unit = {},
) {
    val searching by remember { mutableStateOf(false) }
    val trailingIcon = if (searching) {
        me.jerryokafor.core.ui.R.drawable.baseline_cancel_24
    } else {
        me.jerryokafor.core.ui.R.drawable.search
    }
    val contentDescription = if (searching) "Close" else "Search"
    var searchQuery by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = modifier,
        value = searchQuery,
        placeholder = { Text(text = "the end game") },
        onValueChange = { searchQuery = it },
        shape = IheNkiri.shape.pill,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(searchQuery) }),
        trailingIcon = {
            Icon(
                painter = painterResource(id = trailingIcon),
                tint = IheNkiri.color.tertiaryContainer,
                contentDescription = contentDescription,
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedContainerColor = IheNkiri.color.onPrimaryContainer.copy(alpha = 0.7f),
            unfocusedContainerColor = IheNkiri.color.onPrimaryContainer.copy(alpha = 0.7f),
            focusedPlaceholderColor = IheNkiri.color.onPrimary.copy(alpha = 0.5F),
            unfocusedPlaceholderColor = IheNkiri.color.onPrimary.copy(alpha = 0.8f),
            focusedTextColor = IheNkiri.color.onPrimary,
            unfocusedTextColor = IheNkiri.color.onPrimary.copy(alpha = 0.4F),
        ),
    )
}
