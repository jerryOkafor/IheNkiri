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

package com.jerryokafor.feature.media.navigation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.jerryokafor.feature.media.ui.MediaScreen
import kotlinx.coroutines.flow.StateFlow

@Suppress("TopLevelPropertyNaming", "ktlint:standard:property-naming")
internal const val movieIdArg = "movieId"

@Suppress("TopLevelPropertyNaming", "ktlint:standard:property-naming")
internal const val titleArg = "title"

@Suppress("TopLevelPropertyNaming", "ktlint:standard:property-naming")
const val mediaRoutePattern = "watch/{$movieIdArg}?$titleArg={$titleArg}"

internal class MovieDetailsArg(val movieId: StateFlow<Long>) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        movieId = checkNotNull(savedStateHandle.getStateFlow(movieIdArg, 0L)),
    )
}

fun NavController.navigateToMedia(
    movieId: Long,
    title: String,
    navOptions: NavOptions? = null,
) {
    this.navigate(
        route = "watch/$movieId?$titleArg=$title",
        navOptions = navOptions,
    )
}

fun NavGraphBuilder.mediaScreen(onBackClick: () -> Unit) {
    composable(
        route = mediaRoutePattern,
        arguments = listOf(
            navArgument(movieIdArg) {
                type = NavType.LongType
                defaultValue = 0L
            },
            navArgument(titleArg) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
    ) {
        val title = it.arguments?.getString(titleArg) ?: ""
        MediaScreen(onBackClick = onBackClick, title = title)
    }
}
