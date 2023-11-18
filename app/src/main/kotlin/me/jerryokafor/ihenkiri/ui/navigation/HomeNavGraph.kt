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

package me.jerryokafor.ihenkiri.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ui.navigation.enterTransition
import me.jerryokafor.core.ui.navigation.exitTransition
import me.jerryokafor.core.ui.navigation.popEnterTransition
import me.jerryokafor.core.ui.navigation.popExitTransition
import me.jerryokafor.feature.movies.navigation.moviesScreen
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.movieDetailsScreen
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.navigateToMovieDetails
import me.jerryokafor.ihenkiri.feature.people.navigation.peopleScreen
import me.jerryokafor.ihenkiri.ui.MoreScreen
import me.jerryokafor.ihenkiri.ui.TvShowScreen

@Composable
@ExcludeFromGeneratedCoverageReport
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
// https://proandroiddev.com/screen-transition-animations-with-jetpack-navigation-17afdc714d0e
// https://medium.com/androiddevelopers/animations-in-navigation-compose-36d48870776b

    val onNavigateUp: () -> Unit = {
        navController.navigateUp()
    }

    val onMovieClick: (Long) -> Unit = {
        navController.navigateToMovieDetails(
            movieId = it,
            navOptions =
            navOptions {
                launchSingleTop = true
            },
        )
    }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BottomNavItem.Movies.route,
    ) {
        moviesScreen(onMovieClick = onMovieClick)
        movieDetailsScreen(onNavigateUp = onNavigateUp)
        peopleScreen()

        composable(
            route = BottomNavItem.TVShows.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        ) {
            TvShowScreen()
        }

        composable(
            route = BottomNavItem.More.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        ) {
            MoreScreen()
        }
    }
}
