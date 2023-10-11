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
import me.jerryokafor.core.data.filter.MoviesFilter
import me.jerryokafor.ihenkiri.core.network.datasource.MoviesRemoteDataSource
import me.jerryokafor.ihenkiri.core.test.util.testMovies
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class MoviesRepositoryTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val moviesRemoteDataSource = mockk<MoviesRemoteDataSource>(relaxed = true)
    private val testFilter = MoviesFilter(language = "en-US", page = 1, region = null)

    private lateinit var moviesRepository: MoviesRepository

    @Before
    fun setUp() {
        coEvery { moviesRemoteDataSource.nowPlayingMovies(any()) } returns testMovies()
        coEvery { moviesRemoteDataSource.popularMovies(any()) } returns testMovies()
        coEvery { moviesRemoteDataSource.topRatedMovies(any()) } returns testMovies()
        coEvery { moviesRemoteDataSource.upcomingMovies(any()) } returns testMovies()

        moviesRepository = DefaultMoviesRepository(moviesRemoteDataSource, testDispatcher)
    }

    @Test
    fun `test nowPlayingMovies() returns list of movies`() = testScope.runTest {
        moviesRepository.nowPlayingMovies(testFilter).test {
            val items = awaitItem()
            assertThat(items).isNotEmpty()
            assertThat(items.size).isEqualTo(4)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            moviesRemoteDataSource.nowPlayingMovies(
                withArg {
                    assertEquals(it.language, testFilter.language)
                    assertEquals(it.page, testFilter.page)
                    assertEquals(it.region, testFilter.region)
                },
            )
        }
    }

    @Test
    fun `test popularMovies() returns list of movies`() = testScope.runTest {
        moviesRepository.popularMovies(testFilter).test {
            val items = awaitItem()
            assertThat(items).isNotEmpty()
            assertThat(items.size).isEqualTo(4)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            moviesRemoteDataSource.popularMovies(
                withArg {
                    assertEquals(it.language, testFilter.language)
                    assertEquals(it.page, testFilter.page)
                    assertEquals(it.region, testFilter.region)
                },
            )
        }
    }

    @Test
    fun `test topRatedMovies() returns list of movies`() = testScope.runTest {
        moviesRepository.topRatedMovies(testFilter).test {
            val items = awaitItem()
            assertThat(items).isNotEmpty()
            assertThat(items.size).isEqualTo(4)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            moviesRemoteDataSource.topRatedMovies(
                withArg {
                    assertEquals(it.language, testFilter.language)
                    assertEquals(it.page, testFilter.page)
                    assertEquals(it.region, testFilter.region)
                },
            )
        }
    }

    @Test
    fun `test upcomingMovies() returns list of movies`() = testScope.runTest {
        moviesRepository.upcomingMovies(testFilter).test {
            val items = awaitItem()
            assertThat(items).isNotEmpty()
            assertThat(items.size).isEqualTo(4)

            awaitComplete()
        }

        coVerify(exactly = 1) {
            moviesRemoteDataSource.upcomingMovies(
                withArg {
                    assertEquals(it.language, testFilter.language)
                    assertEquals(it.page, testFilter.page)
                    assertEquals(it.region, testFilter.region)
                },
            )
        }
    }
}
