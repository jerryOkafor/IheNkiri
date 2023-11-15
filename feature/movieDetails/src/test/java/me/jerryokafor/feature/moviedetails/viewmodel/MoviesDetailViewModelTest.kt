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

package me.jerryokafor.feature.moviedetails.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.jerryokafor.ihenkiri.core.test.test.data.FakeMovieDetailsRepository
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.core.test.util.MovieDetailsTestData
import me.jerryokafor.ihenkiri.feature.moviedetails.MoviesDetailViewModel
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.movieIdArg
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

const val TEST_ID = 0L

@HiltAndroidTest
class MoviesDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var moviesDetailViewModel: MoviesDetailViewModel
    private val savedStateHandle =
        SavedStateHandle().apply {
            this[movieIdArg] = TEST_ID
        }
    private val testMovie = MovieDetailsTestData.testMovieDetails(TEST_ID)
    private val testMovieCredit = MovieDetailsTestData.testMovieCredit(TEST_ID)
    private val movieDetailsRepository = FakeMovieDetailsRepository()

    @Before
    fun init() {
        moviesDetailViewModel =
            MoviesDetailViewModel(
                savedStateHandle = savedStateHandle,
                movieDetailsRepository = movieDetailsRepository,
            )
    }

    @Test
    fun moviesDetailViewModel_init_loadsMovieDetails_using_turbine() =
        runTest {
            assertEquals(savedStateHandle[movieIdArg], TEST_ID)
            moviesDetailViewModel.uiState.test {
                awaitItem().apply {
                    assertEquals(expected = testMovieCredit.crew, actual = crew)
                    assertEquals(expected = testMovieCredit.cast, actual = cast)
                    assertEquals(expected = testMovie.title, actual = title)
                    assertEquals(expected = testMovie.overview, actual = overview)
                }

                // clean up
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun moviesDetailViewModel_init_loadsMovieDetails() =
        runTest {
            val uiStateJob =
                launch(UnconfinedTestDispatcher()) { moviesDetailViewModel.uiState.collect() }
            assertEquals(savedStateHandle[movieIdArg], TEST_ID)

            val actualUiState = moviesDetailViewModel.uiState.value
            assertEquals(expected = testMovieCredit.crew, actual = actualUiState.crew)
            assertEquals(expected = testMovieCredit.cast, actual = actualUiState.cast)
            assertEquals(expected = testMovie.title, actual = actualUiState.title)
            assertEquals(expected = testMovie.overview, actual = actualUiState.overview)

            // clean up
            uiStateJob.cancel()
        }
}
