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

package me.jerryokafor.ihenkiri.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.jerryokafor.feature.media.navigation.mediaScreen
import com.jerryokafor.feature.media.navigation.navigateToMedia
import com.jerryokafor.feature.peopledetails.navigation.navigateToPersonDetails
import com.jerryokafor.feature.peopledetails.navigation.peopleDetailsScreen
import me.jerryokafor.feature.movies.navigation.moviesRoutePattern
import me.jerryokafor.feature.movies.navigation.moviesScreen
import me.jerryokafor.ihenkiri.feature.auth.navigation.authNavGraph
import me.jerryokafor.ihenkiri.feature.auth.navigation.navigateToAuth
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.movieDetailsScreen
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.navigateToMovieDetails
import me.jerryokafor.ihenkiri.feature.people.navigation.peopleScreen
import me.jerryokafor.ihenkiri.feature.settings.navigation.settingsScreen
import me.jerryokafor.ihenkiri.feature.tvshows.navigation.tvShowsScreen
import me.jerryokafor.ihenkiri.ui.Recommendation
import me.jerryokafor.ihenkiri.ui.RecommendationScreen

@Composable
fun IhenkiriNavHost(
    navController: NavHostController,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    val onNavigateUp: () -> Unit = {
        navController.navigateUp()
    }

    val onMovieClick: (Long) -> Unit = { movieId ->
        navController.navigateToMovieDetails(
            movieId = movieId,
            navOptions = navOptions {},
        )
    }

    val onTVShowClick: (Long) -> Unit = { movieId ->
        navController.navigateToMovieDetails(
            movieId = movieId,
            navOptions = navOptions {},
        )
    }

    val onPersonClick: (Long) -> Unit = {
        navController.navigateToPersonDetails(personId = it)
    }

    val onLoginClick: () -> Unit = {
        navController.navigateToAuth()
    }

    val onWatchTrailerClick: (Long, String) -> Unit = { id, title ->
        navController.navigateToMedia(movieId = id, title = title)
    }

    val onRecommendationClick: () -> Unit = {
        navController.navigate(route = Recommendation)
    }

    NavHost(
        modifier = Modifier,
        navController = navController,
        startDestination = moviesRoutePattern,
    ) {
        authNavGraph(
            onCompleteLogin = onNavigateUp,
            onShowSnackbar = onShowSnackbar,
        )
        moviesScreen(
            onRecommendationClick = onRecommendationClick,
            onMovieClick = onMovieClick,
        )
        tvShowsScreen(
            onRecommendationClick = onRecommendationClick,
            onTVShowClick = onTVShowClick,
        )
        movieDetailsScreen(
            onMovieItemClick = onMovieClick,
            onNavigateUp = onNavigateUp,
            onWatchTrailerClick = onWatchTrailerClick,
        )
        peopleScreen(onPersonClick = onPersonClick)
        peopleDetailsScreen(onNavigateUp = onNavigateUp)
        settingsScreen(onLoginClick = onLoginClick)
        mediaScreen(onBackClick = onNavigateUp)

        composable<Recommendation> {
            RecommendationScreen(onNavigateUp = onNavigateUp)
        }
    }
}
