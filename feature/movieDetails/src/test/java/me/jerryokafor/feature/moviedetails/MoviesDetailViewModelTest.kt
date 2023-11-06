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

package me.jerryokafor.feature.moviedetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.jerryokafor.ihenkiri.core.test.test.data.FakeMovieDetailsRepository
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.feature.moviedetails.MoviesDetailViewModel
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.movieIdArg
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

@HiltAndroidTest
class MoviesDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: MoviesDetailViewModel
    private val savedStateHandle = SavedStateHandle().apply {
        this[movieIdArg] = 0L
    }
    private val movieDetailsRepository = FakeMovieDetailsRepository()


    @Before
    fun init() {
        viewModel = MoviesDetailViewModel(
            savedStateHandle = savedStateHandle,
            movieDetailsRepository = movieDetailsRepository
        )
    }

    @Test
    fun test() = runTest {
        assertEquals(savedStateHandle[movieIdArg], 0L)
        viewModel.uiState.test {

            //clean up
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun test2() = runTest {
        val uiStateJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }
        assertEquals(savedStateHandle[movieIdArg], 0L)


        //clean up
        uiStateJob.cancel()
    }

}