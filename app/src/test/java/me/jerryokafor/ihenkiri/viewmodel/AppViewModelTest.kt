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

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AppViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AppViewModel

    @Before
    fun setUp() {
        viewModel = AppViewModel()
    }

    @Test
    fun appViewMode_verifyInitialState() = runTest {
        val actualValue = viewModel.uiState.value
        val startDestination = viewModel.startDestination.value
        assertThat(actualValue.isLoggedIn).isTrue()
        assertThat(actualValue.isDarkTheme).isTrue()
        assertThat(actualValue.isDynamicColor).isFalse()
        assertThat(startDestination).isEqualTo("auth-graph")
    }

    @Test
    fun appViewModel_verifyUpdateLogin() = runTest {
        viewModel.updateLoginState(loggedIn = false)
        with(viewModel.uiState.value) {
            assertThat(isLoggedIn).isFalse()
        }
        assertThat(viewModel.startDestination.value).isEqualTo("auth-graph")

        viewModel.updateLoginState(loggedIn = true)
        with(viewModel.uiState.value) {
            assertThat(isLoggedIn).isTrue()
        }

        assertThat(viewModel.startDestination.value).isEqualTo("home-graph")
    }
}
