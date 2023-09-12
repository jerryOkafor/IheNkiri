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

package me.jerryokafor.ihenkiri.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import me.jerryokafor.ihenkiri.ui.MAIN_CONTENT_TEST_TAG
import me.jerryokafor.ihenkiri.ui.navigation.BottomNavigation
import me.jerryokafor.ihenkiri.ui.navigation.HomeNavGraph
import me.jerryokafor.ihenkiri.ui.navigation.TopLevelDestinations

private const val BOTTOM_NAV_BAR_DISPLAY_DELAY = 700L

@Composable
fun HomeScreen(navController: NavHostController = rememberNavController()) {
    val bottomBarState = rememberSaveable { (mutableStateOf(false)) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(navBackStackEntry?.destination?.route) {
        when (navBackStackEntry?.destination?.route) {
            TopLevelDestinations.SearchView.route, TopLevelDestinations.MovieDetail.route ->
                bottomBarState.value = false

            else -> {
                delay(BOTTOM_NAV_BAR_DISPLAY_DELAY)
                bottomBarState.value = true
            }
        }
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
                HomeNavGraph(navController = navController)
            }
        },
    )
}
