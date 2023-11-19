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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.ihenkiri.ui.navigation.GraphRoute
import me.jerryokafor.ihenkiri.ui.navigation.RootNavGraph
import me.jerryokafor.ihenkiri.viewmodel.AppViewModel

const val MAIN_CONTENT_TEST_TAG = "mainContent"
const val LANDING_SCREEN_TEST_TAG = "landingScreen"

@Composable
@ExcludeFromGeneratedCoverageReport
internal fun AppContent(
    viewModel: AppViewModel,
    onSignInClick: () -> Unit,
) {
    val navController = rememberNavController()

    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val onContinueAsGuestClick: () -> Unit = {
        viewModel.updateLoginState(true)
    }

    LaunchedEffect(key1 = viewModel.startDestination.value) {
        navController.navigate(
            route = viewModel.startDestination.value,
            navOptions =
            navOptions {
                navController.currentDestination?.route?.let {
                    popUpTo(it) {
                        inclusive = true
                    }
                }
            },
        )
    }

    AppContent(
        navHostController = navController,
        startDestination = GraphRoute.AUTH,
        isDarkTheme = uiState.value.isDarkTheme,
        isDynamicColor = uiState.value.isDynamicColor,
        onContinueAsGuestClick = onContinueAsGuestClick,
        onSignInClick = onSignInClick,
    )
}

@Composable
fun AppContent(
    navHostController: NavHostController,
    startDestination: String,
    isDarkTheme: Boolean,
    isDynamicColor: Boolean,
    onContinueAsGuestClick: () -> Unit,
    onSignInClick: () -> Unit,
) {
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

    IheNkiriTheme(
        isDarkTheme = isDarkTheme,
        isDynamicColor = isDynamicColor,
    ) {
        RootNavGraph(
            navHostController = navHostController,
            startDestination = startDestination,
            onContinueAsGuestClick = onContinueAsGuestClick,
            onSignInClick = onSignInClick,
        )
    }
}
