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

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import me.jerryokafor.core.ui.extension.setIheAppThemedContent
import me.jerryokafor.ihenkiri.core.network.model.request.CreateRequestTokenRequest
import me.jerryokafor.ihenkiri.core.network.service.AuthService
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "MainActivity"
    }

    @Inject
    lateinit var theMovieDBAPI: AuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { false }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setIheAppThemedContent {
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

            AppContent(onSignInClick = ::onSignInClick)
        }

        val action: String? = intent?.action
        val data: Uri? = intent?.data

        Log.d(TAG, "onCreate() $data")
    }

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
