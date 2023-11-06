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
import me.jerryokafor.feature.movies.screen.MoviesScreen
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.movieDetailsScreen
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.navigateToMovieDetails
import me.jerryokafor.ihenkiri.ui.MoreScree
import me.jerryokafor.ihenkiri.ui.PeopleScreen
import me.jerryokafor.ihenkiri.ui.TvShowScreen
import me.jerryokafor.ihenkiri.ui.screens.SearchView

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
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = BottomNavItem.Movies.route,
    ) {
        composable(
            route = BottomNavItem.Movies.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        ) {
            MoviesScreen(
                onMovieClick = {
                    navController.navigateToMovieDetails(movieId = it, navOptions = navOptions {
                        launchSingleTop = true
                    })
                },
            )
        }

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
            route = BottomNavItem.People.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        ) {
            PeopleScreen()
        }

        composable(
            route = BottomNavItem.More.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        ) {
            MoreScree()
        }

        movieDetailsScreen(onNavigateUp = onNavigateUp)

    }
}
