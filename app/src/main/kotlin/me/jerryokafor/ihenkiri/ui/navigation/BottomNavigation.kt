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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.feature.movies.navigation.moviesRoute
import me.jerryokafor.ihenkiri.R
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.movieDetailsNavPattern

const val BOTTOM_NAV_BAR_TEST_TAG = "BottomNavigationBar"

@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun BottomNavigationPreview() {
    IheNkiriTheme {
        Row(modifier = Modifier.padding(IheNkiri.spacing.two)) {
            BottomNavigation(navController = rememberNavController(), show = true)
        }
    }
}

@Composable
fun BottomNavigation(
    navController: NavHostController,
    show: Boolean = true,
) {
    val items =
        listOf(
            BottomNavItem.Movies,
            BottomNavItem.TVShows,
            BottomNavItem.People,
            BottomNavItem.More,
        )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val density = LocalDensity.current

    AnimatedVisibility(
        visible = show,
        enter =
            slideInVertically {
                // Slide in from 40 dp from the top.
                with(density) { -40.dp.roundToPx() }
            } +
                expandVertically(
                    // Expand from the top.
                    expandFrom = Alignment.Top,
                ) +
                fadeIn(
                    initialAlpha = 0.3f,
                ),
        exit = slideOutVertically() + shrinkVertically() + fadeOut(),
        content = {
            NavigationBar(modifier = Modifier.testTag(BOTTOM_NAV_BAR_TEST_TAG)) {
                items.forEach { item ->
                    AddItem(
                        screen = item,
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(
                                item.route,
                                navOptions =
                                    navOptions {
                                        launchSingleTop = true
                                    },
                            )
                        },
                    )
                }
            }
        },
    )
}

sealed class TopLevelDestinations(val route: String) {
    data object MovieDetail : TopLevelDestinations(route = movieDetailsNavPattern)
}

sealed class BottomNavItem(
    val title: String,
    val icon: Int,
    val route: String,
) {
    data object Movies : BottomNavItem(
        title = "Movies",
        icon = R.drawable.baseline_video_library_24,
        route = moviesRoute,
    )

    data object TVShows : BottomNavItem(
        title = "TV Shows",
        icon = R.drawable.baseline_live_tv_24,
        route = "/tv-shows",
    )

    data object People :
        BottomNavItem(title = "People", icon = R.drawable.baseline_people_24, route = "/people")

    data object More :
        BottomNavItem(title = "More", icon = R.drawable.baseline_more_24, route = "/more")
}

@Composable
private fun RowScope.AddItem(
    screen: BottomNavItem,
    selected: Boolean,
    onClick: () -> Unit,
) {
    NavigationBarItem(
        label = { Text(text = screen.title) },
        icon = {
            Icon(
                painterResource(id = screen.icon),
                contentDescription = screen.title,
                tint = IheNkiri.color.secondary,
            )
        },
        selected = selected,
        alwaysShowLabel = true,
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors(),
    )
}
