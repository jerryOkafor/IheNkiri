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

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AppContentViewModelTest {
    private lateinit var viewModel: AppContentViewModel

    @Before
    fun setUp() {
        viewModel = AppContentViewModel()
    }

    @Test
    fun `Given UI initial state, when viewModel is observed, then isLoggedIn == false`() = runTest {
        viewModel.uiState.test {
            val uiState = awaitItem()
            assertThat(uiState.isLoggedIn).isFalse()
            assertThat(uiState.isDarkTheme).isFalse()
            assertThat(uiState.isDynamicColor).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given isLoggedIn == false, when isLoggedIn == true and viewModel is observed, then isLoggedIn = true`() =
        runTest {
            viewModel.uiState.test {
                assertThat(awaitItem().isLoggedIn).isFalse()
                viewModel.updateLoginState(loggedIn = true)
                with(awaitItem()) {
                    assertThat(isLoggedIn).isTrue()
                    assertThat(isDarkTheme).isFalse()
                    assertThat(isDynamicColor).isTrue()
                }

                cancelAndIgnoreRemainingEvents()
            }
        }
}
