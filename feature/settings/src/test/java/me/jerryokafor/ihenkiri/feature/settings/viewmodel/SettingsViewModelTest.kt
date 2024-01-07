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

package me.jerryokafor.ihenkiri.feature.settings.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.data.repository.LocalStorage
import me.jerryokafor.core.model.ThemeConfig
import me.jerryokafor.core.model.UserData
import me.jerryokafor.core.model.UserEditableSettings
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.feature.settings.viewModel.SettingsUiState
import me.jerryokafor.ihenkiri.feature.settings.viewModel.SettingsViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var settingsViewModel: SettingsViewModel

    private val testUserData = UserData(
        accountId = "4bc889XXXXa3c0z92001001",
        isLoggedIn = false,
        themeConfig = ThemeConfig.DARK,
        usDynamicColor = false,
        name = "Jerry",
        userName = "jerryOkafor",
    )

    private val localStorage = mockk<LocalStorage>(relaxed = true) {
        every { userData() } returns flowOf(testUserData)
    }

    @Before
    fun setUp() {
        settingsViewModel = SettingsViewModel(localStorage = localStorage)
    }

    @Test
    fun settingsViewModel_onLoad_returnsUserData() = runTest {
        settingsViewModel.settingsUiState.test {
            assertThat((awaitItem() as SettingsUiState.Success).preference).isEqualTo(
                UserEditableSettings(
                    isLoggedIn = false,
                    themeConfig = ThemeConfig.DARK,
                    isDynamicColor = false,
                ),
            )
        }
        verify(exactly = 1) { localStorage.userData() }
    }

    @Test
    fun settingsViewModel_onLogOut_useIsLoggedOut() = runTest {
        coEvery { localStorage.logout() } returns Unit
        settingsViewModel.onLogout()
        advanceUntilIdle()

        verify(exactly = 1) { localStorage.userData() }
        coVerify(exactly = 1) { localStorage.logout() }
    }

    @Test
    fun settingsViewModel_onThemeConfig_themeConfigIsShow() = runTest {
        coEvery { localStorage.setThemeConfig(any()) } returns Unit
        settingsViewModel.onThemeConfig(ThemeConfig.DARK)
        advanceUntilIdle()

        verify(exactly = 1) { localStorage.userData() }
        coVerify(exactly = 1) { localStorage.setThemeConfig(eq(ThemeConfig.DARK)) }
    }

    @Test
    fun settingsViewModel_onChangeDynamicColorPreference_correctColorPrefIsSet() = runTest {
        coEvery { localStorage.setUseDynamicColor(any()) } returns Unit
        settingsViewModel.onChangeDynamicColorPreference(true)
        advanceUntilIdle()

        verify(exactly = 1) { localStorage.userData() }
        coVerify(exactly = 1) { localStorage.setUseDynamicColor(eq(true)) }
    }
}
