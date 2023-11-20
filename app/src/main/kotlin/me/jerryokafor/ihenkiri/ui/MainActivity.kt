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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.ihenkiri.core.network.model.request.CreateRequestTokenRequest
import me.jerryokafor.ihenkiri.core.network.service.AuthApi
import me.jerryokafor.ihenkiri.viewmodel.AppViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var theMovieDBAPI: AuthApi

    private val appViewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { appViewModel.isLoading.value }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val uiState by appViewModel.uiState.collectAsStateWithLifecycle()
            val startDestination by appViewModel.startDestination

            val onContinueAsGuestClick: () -> Unit = {
                appViewModel.updateLoginState(true)
            }

            IheNkiriTheme(
                isDarkTheme = uiState.isDarkTheme,
                isDynamicColor = uiState.isDynamicColor,
            ) {
                IhenkiriApp(
                    onContinueAsGuestClick = onContinueAsGuestClick,
                    startDestination = startDestination,
                    onSignInClick = ::onSignInClick,
                )
            }
        }

        val action: String? = intent?.action
        val data: Uri? = intent?.data

        Log.d(TAG, "onCreate() $data")
    }

    @VisibleForTesting
    private fun onSignInClick() {
        lifecycleScope.launch {
            try {
                val redirectTo = "https://ihenkiri.jerryokafor.me/auth"

                val request =
                    theMovieDBAPI.createRequestToken(CreateRequestTokenRequest(redirectTo = redirectTo))
                Log.d("MainActivity", "Result: $request")
                val requestToken = request.requestToken

                val url =
                    "https://www.themoviedb.org/auth/access?request_token=$requestToken&redirect_to=$redirectTo"
                val intent: CustomTabsIntent = CustomTabsIntent.Builder().build()
                intent.launchUrl(this@MainActivity, Uri.parse(url))

                // after approval, you can then use the request token to
                // get an access token and save for future use
            } catch (e: Exception) {
                Log.e("MainActivity", "Error: ${e.localizedMessage}")
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val action: String? = intent?.action
        val data: Uri? = intent?.data

        Log.d(TAG, "onNewIntent() $data")
    }
}
