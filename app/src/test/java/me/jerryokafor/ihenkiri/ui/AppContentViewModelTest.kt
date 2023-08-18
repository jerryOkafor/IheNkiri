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

import kotlinx.coroutines.test.runTest
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class AppContentViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: AppContentViewModel

    @Before
    fun setUp() {
        viewModel = AppContentViewModel()
    }

    @Test
    fun `Given UI initial state, when viewModel is observed, then isLoggedIn == false`() = runTest {
        val expected = AppUIState()
        val actualValue = viewModel.uiState.value
        assertEquals(expected.isLoggedIn, actualValue.isLoggedIn)
        assertEquals(expected.isDarkTheme, actualValue.isDarkTheme)
        assertEquals(expected.isDynamicColor, actualValue.isDynamicColor)
    }

    @Test
    fun `Given isLoggedIn == false, when isLoggedIn == true and viewModel is observed, then isLoggedIn = true`() =
        runTest {
            val actualInitialValue = viewModel.uiState.value
            val expected = AppUIState()
            assertEquals(AppUIState(), actualInitialValue)
            assertEquals(expected.isDarkTheme, actualInitialValue.isDarkTheme)
            assertEquals(expected.isDynamicColor, actualInitialValue.isDynamicColor)

            viewModel.updateLoginState(loggedIn = true)
            val appUIState = AppUIState(isLoggedIn = true)
            val otherValue = viewModel.uiState.value
            assertEquals(appUIState.isLoggedIn, otherValue.isLoggedIn)
            assertEquals(appUIState.isDarkTheme, otherValue.isDarkTheme)
            assertEquals(appUIState.isDynamicColor, otherValue.isDynamicColor)
        }
}
