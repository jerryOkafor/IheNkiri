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

package me.jerryokafor.core.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.common.outcome.Failure
import me.jerryokafor.core.common.outcome.Success
import me.jerryokafor.ihenkiri.core.network.datasource.MovieDetailsRemoteDataSource
import me.jerryokafor.ihenkiri.core.test.util.MovieDetailsTestData
import me.jerryokafor.ihenkiri.core.test.util.testMovies
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class DefaultMovieDetailsRepositoryTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val moviesDetailsRemoteDataSource = mockk<MovieDetailsRemoteDataSource>(relaxed = true)
    private val testMovieId = 0L

    private lateinit var movieDetailsRepository: MovieDetailsRepository

    @Before
    fun setUp() {
        coEvery { moviesDetailsRemoteDataSource.movieDetails(any()) } returns
            MovieDetailsTestData.testMovieDetails(testMovieId)
        coEvery { moviesDetailsRemoteDataSource.movieCredits(any()) } returns
            MovieDetailsTestData.testMovieCredit(testMovieId)
        coEvery { moviesDetailsRemoteDataSource.movieVideos(any()) } returns
            MovieDetailsTestData.testMovieVideos(testMovieId)
        coEvery { moviesDetailsRemoteDataSource.similarMovies(any()) } returns testMovies()

        movieDetailsRepository =
            DefaultMovieDetailsRepository(moviesDetailsRemoteDataSource, testDispatcher)
    }

    @Test
    fun `test movieDetails() returns movie details`() =
        testScope.runTest {
            movieDetailsRepository.movieDetails(testMovieId).test {
                with((awaitItem() as Success).data) {
                    assertThat(title).isEqualTo("Fight Club")
                    assertThat(imdbId).isEqualTo("tt0137523")
                    assertThat(budget).isEqualTo(63000000)
                    assertThat(adult).isFalse()
                    assertThat(video).isFalse()
                    assertThat(voteAverage).isEqualTo(8.433)
                    assertThat(voteCount).isEqualTo(26280)
                }

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) {
                moviesDetailsRemoteDataSource.movieDetails(
                    withArg { assertEquals(it, testMovieId) },
                )
            }
        }

    @Test
    fun `test movieDetails() returns failure`() =
        testScope.runTest {
            coEvery { moviesDetailsRemoteDataSource.movieDetails(any()) } throws Exception()

            movieDetailsRepository.movieDetails(testMovieId).test {
                with((awaitItem() as Failure)) {
                    assertThat(errorResponse).isEqualTo("Error loading movie details")
                }
                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) {
                moviesDetailsRemoteDataSource.movieDetails(
                    withArg { assertEquals(it, testMovieId) },
                )
            }
        }

    @Test
    fun `test movieCredits() returns movie credit (Cases & Crew)`() =
        testScope.runTest {
            movieDetailsRepository.movieCredits(testMovieId).test {
                with((awaitItem() as Success).data) {
                    assertThat(cast.size).isEqualTo(2)
                    assertThat(crew.size).isEqualTo(2)

                    with(cast.first()) {
                        assertThat(adult).isFalse()
                        assertThat(character).isEqualTo("The Narrator")
                        assertThat(name).isEqualTo("Edward Norton")
                        assertThat(popularity).isEqualTo(26.99)
                    }

                    with(crew.last()) {
                        assertThat(adult).isFalse()
                        assertThat(department).isEqualTo("Costume & Make-Up")
                        assertThat(name).isEqualTo("Michael Kaplan")
                        assertThat(popularity).isEqualTo(4.294)
                    }
                }

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) {
                moviesDetailsRemoteDataSource.movieCredits(
                    withArg { assertEquals(it, testMovieId) },
                )
            }
        }

    @Test
    fun `test movieCredits() returns failure`() =
        testScope.runTest {
            coEvery { moviesDetailsRemoteDataSource.movieCredits(any()) } throws Exception()

            movieDetailsRepository.movieCredits(testMovieId).test {
                with((awaitItem() as Failure)) {
                    assertThat(errorResponse).isEqualTo("Error getting movie credits")
                }

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) {
                moviesDetailsRemoteDataSource.movieCredits(
                    withArg { assertEquals(it, testMovieId) },
                )
            }
        }

    @Test
    fun `test movieVideos() returns list of videos for the movie`() =
        testScope.runTest {
            movieDetailsRepository.movieVideos(testMovieId).test {
                with((awaitItem() as Success).data) {
                    assertThat(size).isEqualTo(2)

                    with(last()) {
                        assertThat(id).isEqualTo("5c9294240e0a267cd516835f")
                        assertThat(iso6391).isEqualTo("US")
                        assertThat(name).isEqualTo("#TBT Trailer")
                        assertThat(site).isEqualTo("YouTube")
                        assertThat(type).isEqualTo("Trailer")
                    }
                }

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) {
                moviesDetailsRemoteDataSource.movieVideos(
                    withArg { assertEquals(it, testMovieId) },
                )
            }
        }

    @Test
    fun `test movieVideos() returns  failure`() =
        testScope.runTest {
            coEvery { moviesDetailsRemoteDataSource.movieVideos(any()) } throws Exception()

            movieDetailsRepository.movieVideos(testMovieId).test {
                with((awaitItem() as Failure)) {
                    assertThat(errorResponse).isEqualTo("Error getting movie videos")
                }

                cancelAndIgnoreRemainingEvents()
            }

            coVerify(exactly = 1) {
                moviesDetailsRemoteDataSource.movieVideos(
                    withArg { assertEquals(it, testMovieId) },
                )
            }
        }

    @Test
    fun `test similarMovies() returns list of movies`() =
        testScope.runTest {
            movieDetailsRepository.similarMovies(testMovieId).test {
                with((awaitItem() as Success).data) {
                    assertThat(size).isEqualTo(4)

                    with(last()) {
                        assertThat(id).isEqualTo(346698)
                        assertThat(title).isEqualTo("Barbie")
                        assertThat(overview).isEqualTo(
                            """
                            Barbie and Ken are having the time of their lives in the colorful and seemingly 
                            perfect world of Barbie Land. However, when they get a chance to go to the real world, 
                            they soon discover the joys and perils of living among humans.
                            """.trimIndent(),
                        )
                        assertThat(posterPath).isEqualTo("/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg")
                        assertThat(voteAverage).isEqualTo(7.5)
                    }

                    cancelAndIgnoreRemainingEvents()
                }

                coVerify(exactly = 1) {
                    moviesDetailsRemoteDataSource.similarMovies(
                        withArg { assertEquals(it, testMovieId) },
                    )
                }
            }
        }

    @Test
    fun `test similarMovies() returns failure`() =
        testScope.runTest {
            coEvery { moviesDetailsRemoteDataSource.similarMovies(any()) } throws Exception()

            movieDetailsRepository.similarMovies(testMovieId).test {
                with((awaitItem() as Failure)) {
                    assertThat(errorResponse).isEqualTo("Error getting recommended movies")
                    cancelAndIgnoreRemainingEvents()
                }

                coVerify(exactly = 1) {
                    moviesDetailsRemoteDataSource.similarMovies(
                        withArg { assertEquals(it, testMovieId) },
                    )
                }
            }
        }
}
