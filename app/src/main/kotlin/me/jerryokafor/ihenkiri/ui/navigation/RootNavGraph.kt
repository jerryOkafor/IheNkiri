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
import androidx.compose.ui.platform.testTag
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import me.jerryokafor.ihenkiri.ui.LANDING_SCREEN_TEST_TAG
import me.jerryokafor.ihenkiri.ui.screens.HomeScreen
import me.jerryokafor.ihenkiri.ui.screens.LandingScreen

@Composable
fun RootNavGraph(
    navHostController: NavHostController,
    startDestination: String,
    onContinueAsGuestClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        route = GraphRoute.ROOT,
    ) {
        composable(
            route = GraphRoute.HOME,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
            content = { HomeScreen() },
        )

        authNavGraph(onContinueAsGuestClick, onSignInClick)
    }
}

fun NavGraphBuilder.authNavGraph(
    onContinueAsGuestClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
    navigation(route = GraphRoute.AUTH, startDestination = AuthScreens.LandingScreen.route) {
        composable(
            route = AuthScreens.LandingScreen.route,
            enterTransition = enterTransition,
            exitTransition = exitTransition,
            popEnterTransition = popEnterTransition,
            popExitTransition = popExitTransition,
        ) {
            LandingScreen(
                modifier = Modifier.testTag(LANDING_SCREEN_TEST_TAG),
                onContinueAsGuestClick = onContinueAsGuestClick,
                onSignInClick = onSignInClick,
            )
        }
    }
}

sealed class AuthScreens(val route: String) {
    data object LandingScreen : AuthScreens(route = "welcome")
}

data object GraphRoute {
    const val ROOT = "root-graph"
    const val HOME = "home-graph"
    const val AUTH = "auth-graph"
}
