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

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.metrics.performance.JankStats
import androidx.profileinstaller.ProfileVerifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.common.injection.IoDispatcher
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.model.ThemeConfig
import me.jerryokafor.ihenkiri.viewmodel.AppUiState
import me.jerryokafor.ihenkiri.viewmodel.AppViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    /**
     * Lazily inject [JankStats], which is used to track jank throughout the app.
     * Use Lazy if you have an expensive object that may not get used
     */
    @Inject
    lateinit var lazyStats: dagger.Lazy<JankStats>

    @IoDispatcher
    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher

    private val appViewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var uiState: AppUiState by mutableStateOf(AppUiState.Loading)

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                appViewModel.uiState.onEach { uiState = it }.collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (uiState) {
                AppUiState.Loading -> true
                is AppUiState.Success -> false
            }
        }

        // Turn off the decor fitting system windows, which allows us to handle insets,
        // including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val isDarkTheme = shouldUseDarkTheme(uiState = uiState)
            val isDynamicColor = shouldUseDynamicColor(uiState = uiState)

            IheNkiriTheme(
                isDarkTheme = isDarkTheme,
                isDynamicColor = isDynamicColor,
            ) {
                IhenkiriApp()
            }
        }
    }

//    private fun checkForRedirectionsOnColdBoot(savedInstanceState: Bundle?) {
//        if (savedInstanceState == null) {
//            navigator.onListenForRedirections(intent)
//        }
//    }

    override fun onResume() {
        super.onResume()
        lazyStats.get().isTrackingEnabled = true
        lifecycleScope.launch {
            logCompilationStatus()
        }
    }

    /**
     * Logs the app's Baseline Profile Compilation Status using [ProfileVerifier].
     */
    private suspend fun logCompilationStatus() {
        /*
        When delivering through Google Play, the baseline profile is compiled during installation.
        In this case you will see the correct state logged without any further action necessary.
        To verify baseline profile installation locally, you need to manually trigger baseline
        profile installation.
        For immediate compilation, call:
         `adb shell cmd package compile -f -m speed-profile me.jerryokafor.ihenkiri`
        You can also trigger background optimizations:
         `adb shell pm bg-dexopt-job`
        Both jobs run asynchronously and might take some time complete.
        To see quick turnaround of the ProfileVerifier, we recommend using `speed-profile`.
        If you don't do either of these steps, you might only see the profile status reported as
        "enqueued for compilation" when running the sample locally.
         */
        withContext(ioDispatcher) {
            val status = ProfileVerifier.getCompilationStatusAsync().await()
            Log.d(TAG, "ProfileInstaller status code: ${status.profileInstallResultCode}")
            val compilationStatus = when {
                status.isCompiledWithProfile -> "ProfileInstaller: is compiled with profile"
                status.hasProfileEnqueuedForCompilation() ->
                    "ProfileInstaller: Enqueued for compilation"
                else -> "Profile not compiled or enqueued"
            }
            Log.d(TAG, compilationStatus)
        }
    }

    override fun onPause() {
        super.onPause()
        lazyStats.get().isTrackingEnabled = false
    }
}

/**
 * Returns `true` if dark theme should be used, as a function of the [uiState] and the
 * current system context.
 */
@Composable
@ExcludeFromGeneratedCoverageReport
private fun shouldUseDarkTheme(uiState: AppUiState): Boolean = when (uiState) {
    AppUiState.Loading -> isSystemInDarkTheme()
    is AppUiState.Success -> when (uiState.settings.themeConfig) {
        ThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        ThemeConfig.LIGHT -> false
        ThemeConfig.DARK -> true
    }
}

@Composable
@ExcludeFromGeneratedCoverageReport
private fun shouldUseDynamicColor(uiState: AppUiState): Boolean = when (uiState) {
    AppUiState.Loading -> false
    is AppUiState.Success -> uiState.settings.isDynamicColor
}
