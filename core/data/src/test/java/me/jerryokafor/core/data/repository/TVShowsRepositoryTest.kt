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

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.data.filter.MoviesFilter
import me.jerryokafor.ihenkiri.core.network.service.TVSeriesListsApi
import me.jerryokafor.ihenkiri.core.test.util.TVShowsTestData.testNetworkTVShows
import me.jerryokafor.ihenkiri.core.test.util.TVShowsTestData.testTVShows
import org.junit.Before
import org.junit.Test

class TVShowsRepositoryTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val tvSeriesListsApi = mockk<TVSeriesListsApi>(relaxed = true)
    private val testFilter = MoviesFilter(language = "en-US", page = 1, region = null)

    private lateinit var tvShowsRepository: TVShowsRepository

    @Before
    fun setUp() {
        coEvery {
            tvSeriesListsApi.airingToday(
                any(),
                any(),
                any(),
            )
        } returns testNetworkTVShows()
        coEvery {
            tvSeriesListsApi.onTheAir(
                any(),
                any(),
                any(),
            )
        } returns testNetworkTVShows()
        coEvery {
            tvSeriesListsApi.popular(
                any(),
                any(),
                any(),
            )
        } returns testNetworkTVShows()
        coEvery {
            tvSeriesListsApi.topRated(
                any(),
                any(),
                any(),
            )
        } returns testNetworkTVShows()

        tvShowsRepository = DefaultTVShowsRepository(
            tvSeriesListsApi = tvSeriesListsApi,
            defaultDispatcher = testDispatcher,
        )
    }

    @Test
    fun tvShowsRepository_airingToday_returnsListOfTVShows() = testScope.runTest {
        val tvShows = tvShowsRepository.airingToday(testFilter)
        assertThat(tvShows).isNotEmpty()
        assertThat(tvShows.size).isEqualTo(4)
        assertThat(tvShows).containsExactlyElementsIn(testTVShows())
            .inOrder()

        coVerify(exactly = 1) {
            tvSeriesListsApi.airingToday(
                testFilter.language,
                testFilter.page,
                testFilter.region,
            )
        }
    }

    @Test
    fun tvShowsRepository_onTheAir_returnsListOfTVShows() = testScope.runTest {
        val tvShows = tvShowsRepository.onTheAir(testFilter)
        assertThat(tvShows).isNotEmpty()
        assertThat(tvShows.size).isEqualTo(4)
        assertThat(tvShows).containsExactlyElementsIn(testTVShows())
            .inOrder()

        coVerify(exactly = 1) {
            tvSeriesListsApi.onTheAir(
                testFilter.language,
                testFilter.page,
                testFilter.region,
            )
        }
    }

    @Test
    fun tvShowsRepository_popular_returnsListOfTVShows() = testScope.runTest {
        val tvShows = tvShowsRepository.popular(testFilter)
        assertThat(tvShows).isNotEmpty()
        assertThat(tvShows.size).isEqualTo(4)
        assertThat(tvShows).containsExactlyElementsIn(testTVShows())
            .inOrder()

        coVerify(exactly = 1) {
            tvSeriesListsApi.popular(
                testFilter.language,
                testFilter.page,
                testFilter.region,
            )
        }
    }

    @Test
    fun tvShowsRepository_topRated_returnsListOfTVShows() = testScope.runTest {
        val tvShows = tvShowsRepository.topRated(testFilter)
        assertThat(tvShows).isNotEmpty()
        assertThat(tvShows.size).isEqualTo(4)
        assertThat(tvShows).containsExactlyElementsIn(testTVShows())
            .inOrder()

        coVerify(exactly = 1) {
            tvSeriesListsApi.topRated(
                testFilter.language,
                testFilter.page,
                testFilter.region,
            )
        }
    }
}
