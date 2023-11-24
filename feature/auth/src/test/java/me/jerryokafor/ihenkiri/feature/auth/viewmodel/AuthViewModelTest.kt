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

package me.jerryokafor.ihenkiri.feature.auth.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.data.repository.LocalStorage
import me.jerryokafor.ihenkiri.core.network.service.AuthApi
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.core.test.util.createAccessTokenRequest
import me.jerryokafor.ihenkiri.core.test.util.createAccessTokenSuccessResponse
import me.jerryokafor.ihenkiri.core.test.util.createGuestTokenResponse
import me.jerryokafor.ihenkiri.core.test.util.createRequestToken
import me.jerryokafor.ihenkiri.core.test.util.createRequestTokenSuccessResponse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class AuthViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var authViewModel: AuthViewModel
    private val localStorage = mockk<LocalStorage>()
    private val authApi = mockk<AuthApi>()

    private val savedStateHandle = SavedStateHandle().apply {
        this[AuthViewModel.KEY_REQUEST_TOKEN] =
            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWRpcm.XXXX.dxZddmwFqbiWGn1ycR0YPLNGLtVBWOagzneoVM3pXQ0"
    }

    @Before
    fun setup() {
        coEvery { localStorage.saveUserSession(any(), any()) } returns Unit
        coEvery { localStorage.saveGuestSession(any()) } returns Unit

        coEvery { authApi.createRequestToken(any()) } returns createRequestTokenSuccessResponse()
        coEvery { authApi.createAccessToken(any()) } returns createAccessTokenSuccessResponse()
        coEvery { authApi.createGuestSession() } returns createGuestTokenResponse()
        coEvery { authApi.deleteSession() } returns Unit

        authViewModel = AuthViewModel(
            localStorage = localStorage,
            authApi = authApi,
            savedStateHandle = savedStateHandle,
        )
    }

    @Test
    fun authViewModel_createRequestToken_requestTokenIsCreated() = runTest {
        authViewModel.authState.test {
            assertThat(awaitItem()).isEqualTo(AuthState.Default)

            authViewModel.createRequestToken()
            assertThat(awaitItem()).isEqualTo(AuthState.LoadingSession)
            (awaitItem() as AuthState.RequestTokenCreated).let {
                assertThat(it.requestToken).isEqualTo(createRequestTokenSuccessResponse().requestToken)
            }
        }

        coVerify(exactly = 1) { authApi.createRequestToken(createRequestToken()) }
    }

    @Test
    fun authViewModel_createRequestToken_returnsError() = runTest {
        val errorMessage = "network error"
        coEvery { authApi.createRequestToken(any()) } throws Exception(errorMessage)

        authViewModel.authState.test {
            assertThat(awaitItem()).isEqualTo(AuthState.Default)

            authViewModel.createRequestToken()
            assertThat(awaitItem()).isEqualTo(AuthState.LoadingSession)
            (awaitItem() as AuthState.Error).let {
                assertThat(it.message).isEqualTo("Error : $errorMessage, please try again")
            }

            assertThat(awaitItem()).isEqualTo(AuthState.Default)
        }

        coVerify(exactly = 1) { authApi.createRequestToken(createRequestToken()) }
    }

    @Test
    fun authViewModel_createSessionId_sessionIdCreated() = runTest {
        authViewModel.authState.test {
            assertThat(awaitItem()).isEqualTo(AuthState.Default)

            authViewModel.createSessionId()

            assertThat(awaitItem()).isEqualTo(AuthState.LoadingSession)
            assertThat(awaitItem()).isEqualTo(AuthState.Default)
        }

        val expectedResponse = createAccessTokenSuccessResponse()
        coVerify(exactly = 1) { authApi.createAccessToken(any()) }
        coVerify(exactly = 1) {
            localStorage.saveUserSession(
                expectedResponse.accountId,
                expectedResponse.accessToken,
            )
        }
    }

    @Test
    fun authViewModel_createSessionId_returnsError() = runTest {
        val errorMessage = "Error creating session Id"
        coEvery { authApi.createAccessToken(any()) } throws Exception(errorMessage)

        authViewModel.authState.test {
            assertThat(awaitItem()).isEqualTo(AuthState.Default)

            authViewModel.createSessionId()

            assertThat(awaitItem()).isEqualTo(AuthState.LoadingSession)
            (awaitItem() as AuthState.Error).let {
                assertThat(it.message).isEqualTo("Error: $errorMessage, please try again")
            }

            assertThat(awaitItem()).isEqualTo(AuthState.Default)
        }

        coVerify(exactly = 1) { authApi.createAccessToken(createAccessTokenRequest()) }
    }

    @Test
    fun authViewModel_createGuestSession_guestSessionIdCreated() = runTest {
        authViewModel.authState.test {
            assertThat(awaitItem()).isEqualTo(AuthState.Default)
            authViewModel.createGuestSession()

            assertThat(awaitItem()).isEqualTo(AuthState.LoadingGuestSession)
            assertThat(awaitItem()).isEqualTo(AuthState.Default)
        }

        val expectedResponse = createGuestTokenResponse()
        coVerify(exactly = 1) { authApi.createGuestSession() }
        coVerify(exactly = 1) {
            localStorage.saveGuestSession(expectedResponse.guestSessionId)
        }
    }

    @Test
    fun authViewModel_createGuestSession_returnsError() = runTest {
        val errorMessage = "Error creating session Id"
        coEvery { authApi.createGuestSession() } throws Exception(errorMessage)

        authViewModel.authState.test {
            assertThat(awaitItem()).isEqualTo(AuthState.Default)
            authViewModel.createGuestSession()

            assertThat(awaitItem()).isEqualTo(AuthState.LoadingGuestSession)
            (awaitItem() as AuthState.Error).let {
                assertThat(it.message).isEqualTo("Error: $errorMessage, please try again")
            }
            assertThat(awaitItem()).isEqualTo(AuthState.Default)
        }

        coVerify(exactly = 1) { authApi.createGuestSession() }
    }
}
