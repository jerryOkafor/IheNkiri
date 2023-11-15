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
import me.jerryokafor.ihenkiri.core.network.datasource.DefaultMoviesRemoteDataSource
import me.jerryokafor.ihenkiri.core.network.datasource.MoviesQuery
import me.jerryokafor.ihenkiri.core.network.model.response.equalsMovie
import me.jerryokafor.ihenkiri.core.network.service.MovieListApi
import me.jerryokafor.ihenkiri.core.test.util.testMoviesResponse
import me.jerryokafor.ihenkiri.core.test.util.testNetworkMoviesListResponse
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class DefaultMoviesRemoteDataSourceTest {
    private val moviesApi = mockk<MovieListApi>()
    private lateinit var moviesRemoteDataSource: DefaultMoviesRemoteDataSource
    private val query = MoviesQuery(language = "en-US", page = 1, region = null)

    @Before
    fun setUp() {
        coEvery { moviesApi.nowPlaying(any(), any(), any()) } returns testNetworkMoviesListResponse()
        coEvery { moviesApi.popular(any(), any(), any()) } returns testNetworkMoviesListResponse()
        coEvery { moviesApi.topRated(any(), any(), any()) } returns testNetworkMoviesListResponse()
        coEvery { moviesApi.upcoming(any(), any(), any()) } returns testNetworkMoviesListResponse()

        moviesRemoteDataSource = DefaultMoviesRemoteDataSource(moviesApi = moviesApi)
    }

    @Test
    fun `test nowPlayingMovies, returns list of movies`() =
        runTest {
            val result = moviesRemoteDataSource.nowPlayingMovies(query)

            assertThat(result.size).isEqualTo(7)
            result.zip(testMoviesResponse()) { first, second -> Pair(first, second) }.forEach {
                assertThat(it.second.equalsMovie(it.first)).isTrue()
            }

            coVerify {
                moviesApi.nowPlaying(
                    withArg { assertEquals(query.language, it) },
                    withArg { assertEquals(query.page, it) },
                    withNullableArg { assertEquals(query.region, it) },
                )
            }
        }

    @Test
    fun `test popularMovies, returns list of movies`() =
        runTest {
            val result = moviesRemoteDataSource.popularMovies(query)

            assertThat(result.size).isEqualTo(7)
            result.zip(testMoviesResponse()) { first, second -> Pair(first, second) }.forEach {
                assertThat(it.second.equalsMovie(it.first)).isTrue()
            }

            coVerify {
                moviesApi.popular(
                    withArg { assertEquals(query.language, it) },
                    withArg { assertEquals(query.page, it) },
                    withNullableArg { assertEquals(query.region, it) },
                )
            }
        }

    @Test
    fun `test topRatedMovies, returns list of movies`() =
        runTest {
            val result = moviesRemoteDataSource.topRatedMovies(query)

            assertThat(result.size).isEqualTo(7)
            result.zip(testMoviesResponse()) { first, second -> Pair(first, second) }.forEach {
                assertThat(it.second.equalsMovie(it.first)).isTrue()
            }

            coVerify {
                moviesApi.topRated(
                    withArg { assertEquals(query.language, it) },
                    withArg { assertEquals(query.page, it) },
                    withNullableArg { assertEquals(query.region, it) },
                )
            }
        }

    @Test
    fun `test upcomingMovies, returns list of movies`() =
        runTest {
            val result = moviesRemoteDataSource.upcomingMovies(query)

            assertThat(result.size).isEqualTo(7)
            result.zip(testMoviesResponse()) { first, second -> Pair(first, second) }.forEach {
                assertThat(it.second.equalsMovie(it.first)).isTrue()
            }

            coVerify {
                moviesApi.upcoming(
                    withArg { assertEquals(query.language, it) },
                    withArg { assertEquals(query.page, it) },
                    withNullableArg { assertEquals(query.region, it) },
                )
            }
        }
}
