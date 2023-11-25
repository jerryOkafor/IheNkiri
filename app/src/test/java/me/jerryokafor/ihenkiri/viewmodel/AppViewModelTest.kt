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

package me.jerryokafor.ihenkiri.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.data.repository.LocalStorage
import me.jerryokafor.core.model.ThemeConfig
import me.jerryokafor.core.model.UserData
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.shadows.ShadowLog

class AppViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    val localStorage = mockk<LocalStorage> {
        every { userData() } returns flowOf(
            UserData(
                accountId = "",
                isLoggedIn = false,
                themeConfig = ThemeConfig.DARK,
                usDynamicColor = false,
                name = null,
                userName = null,
            ),
        )
    }

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun appViewMode_initialized_isLoggedInIsFalse() = runTest {
        val viewModel = AppViewModel(localStorage = localStorage)
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(AppUiState.Loading)
            with(awaitItem() as AppUiState.Success) {
                assertThat(settings.isLoggedIn).isFalse()
                assertThat(settings.themeConfig).isEqualTo(ThemeConfig.DARK)
                assertThat(settings.isDynamicColor).isFalse()
            }
        }

        verify(exactly = 1) { localStorage.userData() }
    }

    @Test
    fun appViewModel_whenUserIdLoggedIn_isLoggedInIsTrue() = runTest {
        every { localStorage.userData() } returns flowOf(
            UserData(
                accountId = "",
                isLoggedIn = true,
                themeConfig = ThemeConfig.DARK,
                usDynamicColor = false,
                name = null,
                userName = null,
            ),
        )
        val viewModel = AppViewModel(localStorage = localStorage)

        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(AppUiState.Loading)
            with(awaitItem() as AppUiState.Success) {
                assertThat(settings.isLoggedIn).isTrue()
                assertThat(settings.themeConfig).isEqualTo(ThemeConfig.DARK)
                assertThat(settings.isDynamicColor).isFalse()
            }
        }

        verify(exactly = 1) { localStorage.userData() }
    }
}
