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

package com.jerryokafor.feature.media.ui

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jerryokafor.feature.media.navigation.movieIdArg
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.common.outcome.Failure
import me.jerryokafor.core.common.outcome.Outcome
import me.jerryokafor.core.common.outcome.Success
import me.jerryokafor.core.data.repository.MovieDetailsRepository
import me.jerryokafor.core.model.Video
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.core.test.util.MovieDetailsTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

private const val TEST_MOVIE_ID = 0L

class MediaScreenViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val savedStateHandle = SavedStateHandle().apply {
        this[movieIdArg] = TEST_MOVIE_ID
    }
    private val moviesDetailsRepository = mockk<MovieDetailsRepository>()
    private lateinit var mediaScreenViewModel: MediaScreenViewModel

    @Before
    fun setUp() {
        mediaScreenViewModel = MediaScreenViewModel(
            savedStateHandle = savedStateHandle,
            movieDetailsRepository = moviesDetailsRepository,
        )
    }

    @Test
    fun mediaScreenViewModel_loadsMovieVideos_returnsVideos() = runTest {
        every { moviesDetailsRepository.movieVideos(TEST_MOVIE_ID) } returns
            flowOf<Outcome<List<Video>>>(
                Success(MovieDetailsTestData.testMovieVideos(TEST_MOVIE_ID)),
            )

        mediaScreenViewModel.mediaUiState.test {
            assertThat(awaitItem()).isEqualTo(MediaUiState.Loading)
            with(awaitItem() as MediaUiState.Success) {
                assertThat(videos).isNotEmpty()
                assertThat(videos)
                    .containsExactlyElementsIn(MovieDetailsTestData.testMovieVideos(TEST_MOVIE_ID))
                    .inOrder()
            }
        }

        assertEquals(savedStateHandle[movieIdArg], TEST_MOVIE_ID)
        verify(exactly = 1) { moviesDetailsRepository.movieVideos(TEST_MOVIE_ID) }
    }

    @Test
    fun mediaScreenViewModel_loadsMovieVideosFailed_returnsError() = runTest {
        every { moviesDetailsRepository.movieVideos(TEST_MOVIE_ID) } returns
            flowOf<Outcome<List<Video>>>(
                Failure("Error loading movie videos"),
            )

        mediaScreenViewModel.mediaUiState.test {
            assertThat(awaitItem()).isEqualTo(MediaUiState.Loading)
            with(awaitItem() as MediaUiState.LoadFailed) {
                assertThat(message).isEqualTo("Error loading movie videos")
            }
        }

        assertEquals(savedStateHandle[movieIdArg], TEST_MOVIE_ID)
        verify(exactly = 1) { moviesDetailsRepository.movieVideos(TEST_MOVIE_ID) }
    }
}
