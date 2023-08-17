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

package me.jerryokafor.ihenkiri

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import me.jerryokafor.ihenkiri.screens.LandingScreen

@Composable
internal fun AppContent(onSignInClick: () -> Unit) {
    val navController = rememberNavController()
    val isLoggedIn = remember { mutableStateOf(false) }

    val onContinueAsGuestClick: () -> Unit = {
        isLoggedIn.value = true
    }
    Crossfade(
        targetState = isLoggedIn.value,
        label = "loginState",
        animationSpec = tween(durationMillis = 3000),
    ) {
        if (it) {
            Scaffold(
                bottomBar = { BottomNavigation(navController) },
            ) {
                NavigationGraph(navController)
            }
        } else {
            LandingScreen(
                onContinueAsGuestClick = onContinueAsGuestClick,
                onSignInClick = onSignInClick,
            )
        }
    }
}
