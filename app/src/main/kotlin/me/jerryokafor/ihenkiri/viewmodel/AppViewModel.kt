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

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.jerryokafor.ihenkiri.ui.GraphRoute
import javax.inject.Inject

@HiltViewModel
class AppViewModel
@Inject
constructor() : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _startDestination: MutableState<String> = mutableStateOf(GraphRoute.AUTH)
    val startDestination: MutableState<String> = _startDestination

    private val _uiState = MutableStateFlow(AppUIState())
    val uiState: StateFlow<AppUIState> = _uiState

    init {
        viewModelScope.launch {
            _isLoading.update { false }
        }
    }

    fun updateLoginState(loggedIn: Boolean) {
        _uiState.update {
            it.copy(isLoggedIn = loggedIn)
        }
        _startDestination.value =
            if (loggedIn) {
                GraphRoute.HOME
            } else {
                GraphRoute.AUTH
            }
    }
}

data class AppUIState(
    val isLoggedIn: Boolean = true,
    val isDarkTheme: Boolean = true,
    val isDynamicColor: Boolean = false,
)
