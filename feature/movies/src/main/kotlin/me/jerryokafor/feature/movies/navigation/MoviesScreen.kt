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

package me.jerryokafor.feature.movies.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import me.jerryokafor.core.ui.navigation.enterTransition
import me.jerryokafor.core.ui.navigation.exitTransition
import me.jerryokafor.core.ui.navigation.popEnterTransition
import me.jerryokafor.core.ui.navigation.popExitTransition
import me.jerryokafor.feature.movies.screen.MoviesScreen

@Suppress("TopLevelPropertyNaming", "ktlint:standard:property-naming")
const val moviesRoutePattern = "/movies"

fun NavController.navigateToMoviesScreen() {
    this.navigate(
        route = moviesRoutePattern,
        navOptions = navOptions {
            launchSingleTop = true
            popUpTo(graph.id) { inclusive = true }
        },
    )
}

fun NavGraphBuilder.moviesScreen(onMovieClick: (Long) -> Unit) {
    composable(
        route = moviesRoutePattern,
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
    ) {
        MoviesScreen(onMovieClick = onMovieClick)
    }
}
