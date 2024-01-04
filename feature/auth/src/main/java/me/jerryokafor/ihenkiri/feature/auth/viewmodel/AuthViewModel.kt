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

package me.jerryokafor.ihenkiri.feature.auth.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.jerryokafor.core.common.util.Constants
import me.jerryokafor.core.data.repository.LocalStorage
import me.jerryokafor.ihenkiri.core.network.model.request.CreateAccessTokenRequest
import me.jerryokafor.ihenkiri.core.network.model.request.CreateRequestTokenRequest
import me.jerryokafor.ihenkiri.core.network.service.AuthApi
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val localStorage: LocalStorage,
        private val authApi: AuthApi,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        companion object {
            const val KEY_REQUEST_TOKEN = "request_token"
        }

        private val _authState = MutableStateFlow<AuthState>(AuthState.Default)
        val authState: StateFlow<AuthState> = _authState.asStateFlow()

        fun createRequestToken() {
            viewModelScope.launch {
                try {
                    _authState.update { AuthState.LoadingSession }

                    val request = authApi.createRequestToken(
                        CreateRequestTokenRequest(redirectTo = Constants.AUTH_REDIRECT_URL),
                    )

                    val requestToken = request.requestToken
                    savedStateHandle[KEY_REQUEST_TOKEN] = requestToken
                    _authState.update { AuthState.RequestTokenCreated(requestToken) }
                } catch (e: Exception) {
                    val error = "Error : ${e.message}, please try again"
                    _authState.update { AuthState.Error(error) }
                    _authState.update { AuthState.Default }
                }
            }
        }

        fun createSessionId() {
            val requestToken = savedStateHandle.get<String>(KEY_REQUEST_TOKEN)
            viewModelScope.launch {
                requestToken?.let { token ->
                    try {
                        _authState.update { AuthState.LoadingSession }
                        val accessTokenResponse = authApi.createAccessToken(
                            requestBody = CreateAccessTokenRequest(requestToken = token),
                        )

                        localStorage.saveUserSession(
                            accountId = accessTokenResponse.accountId,
                            accessToken = accessTokenResponse.accessToken,
                        )
                        _authState.update { AuthState.CompleteLogin }
                    } catch (e: Exception) {
                        val error = "Error: ${e.message}, please try again"
                        _authState.update { AuthState.Error(error) }
                        _authState.update { AuthState.Default }
                    }
                }
            }
        }

        fun createGuestSession() {
            viewModelScope.launch {
                try {
                    _authState.update { AuthState.LoadingGuestSession }
                    val response = authApi.createGuestSession()

                    localStorage.saveGuestSession(guestSessionId = response.guestSessionId)
                    _authState.update { AuthState.CompleteLogin }
                } catch (e: Exception) {
                    val error = "Error: ${e.message}, please try again"
                    _authState.update { AuthState.Error(error) }
                    _authState.update { AuthState.Default }
                }
            }
        }
    }

sealed interface AuthState {
    data object Default : AuthState

    data object LoadingSession : AuthState

    data object LoadingGuestSession : AuthState

    data class RequestTokenCreated(val requestToken: String?) : AuthState

    data class Error(val message: String) : AuthState

    data object CompleteLogin : AuthState
}
