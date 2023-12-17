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

package me.jerryokafor.ihenkiri.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.jerryokafor.feature.peopledetails.navigation.navigateToPersonDetails
import com.jerryokafor.feature.peopledetails.navigation.peopleDetailsRoutePattern
import com.jerryokafor.feature.peopledetails.navigation.peopleDetailsScreen
import me.jerryokafor.feature.movies.navigation.moviesRoutePattern
import me.jerryokafor.feature.movies.navigation.moviesScreen
import me.jerryokafor.ihenkiri.feature.auth.navigation.authNavGraph
import me.jerryokafor.ihenkiri.feature.auth.navigation.loginRoutePattern
import me.jerryokafor.ihenkiri.feature.auth.navigation.navigateToAuth
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.movieDetailsRoutePattern
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.movieDetailsScreen
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.navigateToMovieDetails
import me.jerryokafor.ihenkiri.feature.people.navigation.peopleScreen
import me.jerryokafor.ihenkiri.feature.settings.navigation.settingsScreen
import me.jerryokafor.ihenkiri.feature.tvshows.navigation.tvShowsScreen

const val MAIN_CONTENT_TEST_TAG = "mainContent"

@Composable
fun IhenkiriApp(navController: NavHostController = rememberNavController()) {
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val systemUiController = rememberSystemUiController()
    val useDarkIcons = !isSystemInDarkTheme()

    DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = Color.Transparent,
            darkIcons = useDarkIcons,
        )
        onDispose {}
    }

    val onNavigateUp: () -> Unit = {
        navController.navigateUp()
    }

    val onMovieClick: (Long) -> Unit = { movieId ->
        navController.navigateToMovieDetails(
            movieId = movieId,
            navOptions = navOptions {},
        )
    }

    val onPersonClick: (Long) -> Unit = {
        navController.navigateToPersonDetails(personId = it)
    }

    val onLogin: () -> Unit = {
        navController.navigateToAuth()
    }

    Scaffold(
        modifier = Modifier.testTag(MAIN_CONTENT_TEST_TAG),
        bottomBar = { BottomNavigation(navController, bottomBarState.value) },
        content = { innerPadding ->
            Box(
                modifier = Modifier.padding(
                    bottom = max(
                        a = 0.dp,
                        b = innerPadding.calculateBottomPadding() - 40.dp,
                    ),
                ),
            ) {
                NavHost(
                    modifier = Modifier,
                    navController = navController,
                    startDestination = moviesRoutePattern,
                ) {
                    authNavGraph()
                    moviesScreen(onMovieClick = onMovieClick)
                    tvShowsScreen(onTVShowClick = {})
                    movieDetailsScreen(onMovieItemClick = onMovieClick, onNavigateUp = onNavigateUp)
                    peopleScreen(onPersonClick = onPersonClick)
                    peopleDetailsScreen(onNavigateUp = onNavigateUp)
                    settingsScreen(onLogin = onLogin)
                }
            }
        },
    )

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        when (navBackStackEntry?.destination?.route) {
            movieDetailsRoutePattern, peopleDetailsRoutePattern, loginRoutePattern ->
                bottomBarState.value = false

            else -> bottomBarState.value = true
        }
    }
}
