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

package me.jerryokafor.ihenkiri.feature.settings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.jerryokafor.core.data.repository.LocalStorage
import me.jerryokafor.core.model.ThemeConfig
import me.jerryokafor.core.model.UserEditableSettings
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
    @Inject
    constructor(private val localStorage: LocalStorage) : ViewModel() {
        val settingsUiState: StateFlow<SettingsUiState> = localStorage.userData()
            .map {
                SettingsUiState.Success(
                    preference = UserEditableSettings(
                        isLoggedIn = it.isLoggedIn,
                        themeConfig = it.themeConfig,
                        isDynamicColor = it.usDynamicColor,
                    ),
                )
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = SettingsUiState.Loading,
            )

        fun onLogout() {
            viewModelScope.launch { localStorage.logout() }
        }

        fun onThemeConfig(config: ThemeConfig) {
            viewModelScope.launch { localStorage.setThemeConfig(config) }
        }

        fun onChangeDynamicColorPreference(useDynamicColor: Boolean) {
            viewModelScope.launch { localStorage.setUseDynamicColor(useDynamicColor) }
        }
    }

sealed interface SettingsUiState {
    data object Loading : SettingsUiState

    data class Success(val preference: UserEditableSettings) : SettingsUiState
}
