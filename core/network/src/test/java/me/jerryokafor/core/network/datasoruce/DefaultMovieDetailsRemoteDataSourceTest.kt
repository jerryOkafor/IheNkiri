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

package me.jerryokafor.core.network.datasoruce

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.jerryokafor.ihenkiri.core.network.datasource.DefaultMovieDetailsRemoteDataSource
import me.jerryokafor.ihenkiri.core.network.service.MovieDetailsApi
import me.jerryokafor.ihenkiri.core.test.util.MovieDetailsTestData
import me.jerryokafor.ihenkiri.core.test.util.testNetworkMoviesListResponse
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class DefaultMovieDetailsRemoteDataSourceTest {
    private val movieDetailsApi = mockk<MovieDetailsApi>()
    private lateinit var moviesDetailsRemoteDataSource: DefaultMovieDetailsRemoteDataSource
    private val testMovieId = 0L

    @Before
    fun setUp() {
        coEvery { movieDetailsApi.movieDetails(any()) } returns
            MovieDetailsTestData.testNetworkMovieDetails(0L)
        coEvery { movieDetailsApi.movieCredits(any()) } returns
            MovieDetailsTestData.testNetworkMovieCredit(0L)
        coEvery { movieDetailsApi.movieVideos(any()) } returns
            MovieDetailsTestData.testNetworkMovieVideos(0L)
        coEvery { movieDetailsApi.similar(any()) } returns
            testNetworkMoviesListResponse()

        moviesDetailsRemoteDataSource =
            DefaultMovieDetailsRemoteDataSource(movieDetailsApi = movieDetailsApi)
    }

    @Test
    fun `test movieDetails, returns movies details`() =
        runTest {
            val result = moviesDetailsRemoteDataSource.movieDetails(testMovieId)

            with(result) {
                assertThat(title).isEqualTo("Fight Club")
                assertThat(imdbId).isEqualTo("tt0137523")
                assertThat(budget).isEqualTo(63000000)
                assertThat(adult).isFalse()
                assertThat(video).isFalse()
                assertThat(voteAverage).isEqualTo(8.433)
                assertThat(voteCount).isEqualTo(26280)
            }

            coVerify(exactly = 1) {
                movieDetailsApi.movieDetails(
                    withArg { assertEquals(0L, it) },
                )
            }
        }

    @Test
    fun `test movieCredits, returns list of movie credits (cast & crew)`() =
        runTest {
            val result = moviesDetailsRemoteDataSource.movieCredits(testMovieId)

            assertThat(result.cast.size).isEqualTo(2)
            assertThat(result.crew.size).isEqualTo(2)

            with(result.cast.first()) {
                assertThat(adult).isFalse()
                assertThat(character).isEqualTo("The Narrator")
                assertThat(name).isEqualTo("Edward Norton")
                assertThat(popularity).isEqualTo(26.99)
            }

            with(result.crew.last()) {
                assertThat(adult).isFalse()
                assertThat(department).isEqualTo("Costume & Make-Up")
                assertThat(name).isEqualTo("Michael Kaplan")
                assertThat(popularity).isEqualTo(4.294)
            }

            coVerify(exactly = 1) {
                movieDetailsApi.movieCredits(
                    withArg { assertEquals(0L, it) },
                )
            }
        }

    @Test
    fun `test movieVideos,returns list of movie videos`() =
        runTest {
            val result = moviesDetailsRemoteDataSource.movieVideos(testMovieId)
            assertThat(result.size).isEqualTo(2)

            with(result.last()) {
                assertThat(id).isEqualTo("5c9294240e0a267cd516835f")
                assertThat(iso6391).isEqualTo("US")
                assertThat(name).isEqualTo("#TBT Trailer")
                assertThat(site).isEqualTo("YouTube")
                assertThat(type).isEqualTo("Trailer")
            }

            coVerify(exactly = 1) {
                movieDetailsApi.movieVideos(
                    withArg { assertEquals(0L, it) },
                )
            }
        }

    @Test
    fun `test similar,returns list of similar videos`() =
        runTest {
            val result = moviesDetailsRemoteDataSource.similarVideos(testMovieId)
            assertThat(result.size).isEqualTo(7)

            with(result.last()) {
                assertThat(id).isEqualTo(1006462)
                assertThat(title).isEqualTo("The Flood")
                assertThat(overview).isEqualTo(
                    """
                    A horde of giant hungry alligators is unleashed on a group of in-transit prisoners and 
                    their guards after a massive hurricane floods Louisiana.
                    """.trimIndent(),
                )
                assertThat(posterPath).isEqualTo("/mvjqqklMpHwOxc40rn7dMhGT0Fc.jpg")
                assertThat(voteAverage).isEqualTo(6.8)
            }

            coVerify(exactly = 1) {
                movieDetailsApi.similar(
                    withArg { assertEquals(0L, it) },
                )
            }
        }
}
