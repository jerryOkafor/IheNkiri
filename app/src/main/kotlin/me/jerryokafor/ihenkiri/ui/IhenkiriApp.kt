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

package me.jerryokafor.ihenkiri.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jerryokafor.feature.media.navigation.WatchMedia
import com.jerryokafor.feature.peopledetails.navigation.PeopleDetails
import me.jerryokafor.core.ui.extension.TrackDisposableJank
import me.jerryokafor.ihenkiri.feature.auth.navigation.Welcome
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.MovieDetail
import me.jerryokafor.ihenkiri.navigation.BottomNavigation
import me.jerryokafor.ihenkiri.navigation.IhenkiriNavHost

const val MAIN_CONTENT_TEST_TAG = "mainContent"

@Composable
fun IhenkiriApp(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onShowSnackbar: suspend (String, String?) -> Boolean = { msg, act ->
        snackbarHostState.showSnackbar(
            message = msg,
            actionLabel = act,
            duration = SnackbarDuration.Short,
        ) == SnackbarResult.ActionPerformed
    },
) {
    NavigationTrackingSideEffect(navController)
    val showBottomBarState = rememberSaveable { mutableStateOf(false) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        modifier = Modifier.testTag(MAIN_CONTENT_TEST_TAG),
        bottomBar = { BottomNavigation(navController, showBottomBarState.value) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(
                bottom = max(
                    a = 0.dp,
                    b = innerPadding.calculateBottomPadding() - 40.dp,
                ),
            ),
            content = {
                IhenkiriNavHost(
                    navController = navController,
                    onShowSnackbar = onShowSnackbar,
                )
            },
        )
    }

    Log.d("Testing: ", "Dest: ${navBackStackEntry?.destination?.route}")
    val route = navBackStackEntry?.destination?.route
    when {
        route.equals(Welcome::class.qualifiedName) ||
            route.equals(WatchMedia::class.qualifiedName) ||
            route.equals(Recommendation::class.qualifiedName) ||
            route?.matches(PeopleDetails.ROUTE_PATTERN) == true ||
            route?.matches(MovieDetail.ROUTE_PATTERN) == true ||
            route?.matches(WatchMedia.ROUTE_PATTERN) == true
        -> showBottomBarState.value = false

        else -> showBottomBarState.value = true
    }
}

/**
 * Stores information about navigation events to be used with JankStats
 */
@Composable
private fun NavigationTrackingSideEffect(navController: NavHostController) {
    TrackDisposableJank(navController) { metricsHolder ->
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            metricsHolder.state?.putState("Navigation", destination.route.toString())
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
