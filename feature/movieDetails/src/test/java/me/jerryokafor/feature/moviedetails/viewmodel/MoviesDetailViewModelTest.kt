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
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.common.outcome.Failure
import me.jerryokafor.core.common.outcome.Outcome
import me.jerryokafor.core.common.outcome.Success
import me.jerryokafor.core.data.repository.MovieDetailsRepository
import me.jerryokafor.core.model.Movie
import me.jerryokafor.core.model.MovieCredit
import me.jerryokafor.core.model.MovieDetails
import me.jerryokafor.core.model.Video
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.core.test.util.MovieDetailsTestData
import me.jerryokafor.ihenkiri.core.test.util.testMovies
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.movieIdArg
import me.jerryokafor.ihenkiri.feature.moviedetails.viewmodel.MovieCreditUiState
import me.jerryokafor.ihenkiri.feature.moviedetails.viewmodel.MovieDetailsUiState
import me.jerryokafor.ihenkiri.feature.moviedetails.viewmodel.MoviesDetailViewModel
import me.jerryokafor.ihenkiri.feature.moviedetails.viewmodel.MoviesVideoUiState
import me.jerryokafor.ihenkiri.feature.moviedetails.viewmodel.SimilarMoviesUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.robolectric.shadows.ShadowLog
import kotlin.test.assertEquals

const val TEST_ID = 0L

@RunWith(JUnit4::class)
class MoviesDetailViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var moviesDetailViewModel: MoviesDetailViewModel
    private val savedStateHandle = SavedStateHandle().apply {
        this[movieIdArg] = TEST_ID
    }
    private val movieDetailsRepository = mockk<MovieDetailsRepository>()

    @Before
    fun setUp() {
        ShadowLog.stream = System.out

        moviesDetailViewModel = MoviesDetailViewModel(
            savedStateHandle = savedStateHandle,
            movieDetailsRepository = movieDetailsRepository,
        )
    }

    @Test
    fun moviesDetailViewModel_loadMovieDetailsSuccessful_sendMovieDetails() = runTest {
        every { movieDetailsRepository.movieDetails(any()) } returns flowOf<Outcome<MovieDetails>>(
            Success(MovieDetailsTestData.testMovieDetails(TEST_ID)),
        )

        moviesDetailViewModel.movieDetailsUiState.test {
            assertThat(awaitItem()).isEqualTo(MovieDetailsUiState.Loading)
            with((awaitItem() as MovieDetailsUiState.Success)) {
                assertThat(movieDetails).isEqualTo(MovieDetailsTestData.testMovieDetails(TEST_ID))
            }
        }

        assertEquals(savedStateHandle[movieIdArg], TEST_ID)
        verify(exactly = 1) { movieDetailsRepository.movieDetails(TEST_ID) }
    }

    @Test
    fun moviesDetailViewModel_loadMovieDetailsFailed_sendError() = runTest {
        every { movieDetailsRepository.movieDetails(any()) } returns flowOf<Outcome<MovieDetails>>(
            Failure("Error loading movies"),
        )

        moviesDetailViewModel.movieDetailsUiState.test {
            assertThat(awaitItem()).isEqualTo(MovieDetailsUiState.Loading)
            with((awaitItem() as MovieDetailsUiState.LoadFailed)) {
                assertThat(message).isEqualTo("Error loading movies")
            }
        }

        assertEquals(savedStateHandle[movieIdArg], TEST_ID)
        verify(exactly = 1) { movieDetailsRepository.movieDetails(TEST_ID) }
    }

    @Test
    fun moviesDetailViewModel_loadMovieCreditSuccess_sendMovieCredit() = runTest {
        every { movieDetailsRepository.movieCredits(any()) } returns flowOf<Outcome<MovieCredit>>(
            Success(MovieDetailsTestData.testMovieCredit(TEST_ID)),
        )

        moviesDetailViewModel.movieCreditUiState.test {
            assertThat(awaitItem()).isEqualTo(MovieCreditUiState.Loading)
            with((awaitItem() as MovieCreditUiState.Success)) {
                assertThat(movieCredit).isEqualTo(MovieDetailsTestData.testMovieCredit(TEST_ID))
            }
        }

        assertEquals(savedStateHandle[movieIdArg], TEST_ID)
        verify(exactly = 1) { movieDetailsRepository.movieCredits(TEST_ID) }
    }

    @Test
    fun moviesDetailViewModel_loadMovieCreditFailed_sendError() = runTest {
        every { movieDetailsRepository.movieCredits(any()) } returns flowOf<Outcome<MovieCredit>>(
            Failure("Error loading movie credits"),
        )

        moviesDetailViewModel.movieCreditUiState.test {
            assertThat(awaitItem()).isEqualTo(MovieCreditUiState.Loading)
            with((awaitItem() as MovieCreditUiState.LoadFailed)) {
                assertThat(message).isEqualTo("Error loading movie credits")
            }
        }

        assertEquals(savedStateHandle[movieIdArg], TEST_ID)
        verify(exactly = 1) { movieDetailsRepository.movieCredits(TEST_ID) }
    }

    @Test
    fun moviesDetailViewModel_loadSimilarMovieSuccess_sendSimilarMovies() = runTest {
        every { movieDetailsRepository.similarMovies(any()) } returns flowOf<Outcome<List<Movie>>>(
            Success(testMovies()),
        )

        moviesDetailViewModel.similarMoviesUiState.test {
            assertThat(awaitItem()).isEqualTo(SimilarMoviesUiState.Loading)
            with((awaitItem() as SimilarMoviesUiState.Success)) {
                assertThat(movies).containsExactlyElementsIn(testMovies())
                    .inOrder()
            }
        }

        assertEquals(savedStateHandle[movieIdArg], TEST_ID)
        verify(exactly = 1) { movieDetailsRepository.similarMovies(TEST_ID) }
    }

    @Test
    fun moviesDetailViewModel_loadSimilarMovieFailed_sendError() = runTest {
        every { movieDetailsRepository.similarMovies(any()) } returns flowOf<Outcome<List<Movie>>>(
            Failure("Error loading similar movies"),
        )

        moviesDetailViewModel.similarMoviesUiState.test {
            assertThat(awaitItem()).isEqualTo(SimilarMoviesUiState.Loading)
            with((awaitItem() as SimilarMoviesUiState.LoadFailed)) {
                assertThat(message).isEqualTo("Error loading similar movies")
            }
        }

        assertEquals(savedStateHandle[movieIdArg], TEST_ID)
        verify(exactly = 1) { movieDetailsRepository.similarMovies(TEST_ID) }
    }

    @Test
    fun moviesDetailViewModel_loadMovieVideoSuccess_sendMoviesVideos() = runTest {
        every { movieDetailsRepository.movieVideos(any()) } returns flowOf<Outcome<List<Video>>>(
            Success(MovieDetailsTestData.testMovieVideos(TEST_ID)),
        )

        moviesDetailViewModel.moviesVideoUiState.test {
            assertThat(awaitItem()).isEqualTo(MoviesVideoUiState.Loading)
            with((awaitItem() as MoviesVideoUiState.Success)) {
                assertThat(videos)
                    .containsExactlyElementsIn(MovieDetailsTestData.testMovieVideos(TEST_ID))
                    .inOrder()
            }
        }

        assertEquals(savedStateHandle[movieIdArg], TEST_ID)
        verify(exactly = 1) { movieDetailsRepository.movieVideos(TEST_ID) }
    }

    @Test
    fun moviesDetailViewModel_loadMovieVideoFailed_sendError() = runTest {
        every { movieDetailsRepository.movieVideos(any()) } returns flowOf<Outcome<List<Video>>>(
            Failure("Error loading movie videos"),
        )

        moviesDetailViewModel.moviesVideoUiState.test {
            assertThat(awaitItem()).isEqualTo(MoviesVideoUiState.Loading)
            with((awaitItem() as MoviesVideoUiState.LoadFailed)) {
                assertThat(message).isEqualTo("Error loading movie videos")
            }

            assertEquals(savedStateHandle[movieIdArg], TEST_ID)
            verify(exactly = 1) { movieDetailsRepository.movieVideos(TEST_ID) }
        }
    }
}
