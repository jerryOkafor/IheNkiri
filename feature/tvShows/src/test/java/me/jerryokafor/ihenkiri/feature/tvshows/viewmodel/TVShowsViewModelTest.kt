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

package me.jerryokafor.ihenkiri.feature.tvshows.viewmodel

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.data.repository.TVShowsRepository
import me.jerryokafor.core.model.TVShow
import me.jerryokafor.core.model.TVShowsFilterItem
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.core.test.util.TVShowsTestData
import me.jerryokafor.ihenkiri.feature.tvshows.viewModel.TVShowsViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TVShowsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var tvShowsViewModel: TVShowsViewModel

    private val tvShowsRepository = mockk<TVShowsRepository> {
        coEvery { airingToday(any()) } returns TVShowsTestData.testTVShows()
        coEvery { onTheAir(any()) } returns TVShowsTestData.testTVShows()
        coEvery { popular(any()) } returns TVShowsTestData.testTVShows()
        coEvery { topRated(any()) } returns TVShowsTestData.testTVShows()
    }

    @Before
    fun setUp() {
        tvShowsViewModel = TVShowsViewModel(
            tvShowsRepository = tvShowsRepository,
            dispatcher = mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun tvShowsViewModelTest_onLoad_defaultTVShowsFiltersAreSet() = runTest {
        tvShowsViewModel.tvShowsFilters.test {
            val availableFilters = awaitItem()
            assertThat(availableFilters.size).isEqualTo(5)

            with(availableFilters[0]) {
                assertThat(isSelected).isTrue()
                assertThat(type).isEqualTo(TVShowsFilterItem.FilterType.AIRING_TODAY)
            }
            with(availableFilters[1]) {
                assertThat(isSelected).isFalse()
                assertThat(type).isEqualTo(TVShowsFilterItem.FilterType.ON_THE_AIR)
            }
            with(availableFilters[2]) {
                assertThat(isSelected).isFalse()
                assertThat(type).isEqualTo(TVShowsFilterItem.FilterType.POPULAR)
            }
            with(availableFilters[3]) {
                assertThat(isSelected).isFalse()
                assertThat(type).isEqualTo(TVShowsFilterItem.FilterType.TOP_RATED)
            }
            with(availableFilters[4]) {
                assertThat(isSelected).isFalse()
                assertThat(type).isEqualTo(TVShowsFilterItem.FilterType.DISCOVER)
            }
        }
    }

    @Test
    fun tvShowsViewModel_onFilterChange_correctFilterIsSet() = runTest {
        tvShowsViewModel.tvShowsFilters.test {
            tvShowsViewModel.onFilterChange(TVShowsFilterItem.FilterType.AIRING_TODAY)
            with(awaitItem().first { it.type == TVShowsFilterItem.FilterType.AIRING_TODAY }) {
                assertThat(isSelected).isTrue()
                assertThat(type).isEqualTo(TVShowsFilterItem.FilterType.AIRING_TODAY)
            }

            tvShowsViewModel.onFilterChange(TVShowsFilterItem.FilterType.ON_THE_AIR)
            with(awaitItem().first { it.type == TVShowsFilterItem.FilterType.ON_THE_AIR }) {
                assertThat(isSelected).isTrue()
                assertThat(type).isEqualTo(TVShowsFilterItem.FilterType.ON_THE_AIR)
            }

            tvShowsViewModel.onFilterChange(TVShowsFilterItem.FilterType.POPULAR)
            with(awaitItem().first { it.type == TVShowsFilterItem.FilterType.POPULAR }) {
                assertThat(isSelected).isTrue()
                assertThat(type).isEqualTo(TVShowsFilterItem.FilterType.POPULAR)
            }

            tvShowsViewModel.onFilterChange(TVShowsFilterItem.FilterType.TOP_RATED)
            with(awaitItem().first { it.type == TVShowsFilterItem.FilterType.TOP_RATED }) {
                assertThat(isSelected).isTrue()
                assertThat(type).isEqualTo(TVShowsFilterItem.FilterType.TOP_RATED)
            }

            tvShowsViewModel.onFilterChange(TVShowsFilterItem.FilterType.DISCOVER)
            with(awaitItem().first { it.type == TVShowsFilterItem.FilterType.DISCOVER }) {
                assertThat(isSelected).isTrue()
                assertThat(type).isEqualTo(TVShowsFilterItem.FilterType.DISCOVER)
            }
        }
    }

    @Test
    fun tvShowsViewModel_onAiringTodayFilterSelected_showAiringTodayTVShows() = runTest {
        tvShowsViewModel.onFilterChange(TVShowsFilterItem.FilterType.AIRING_TODAY)
        val items: Flow<PagingData<TVShow>> = tvShowsViewModel.tvShows

        val itemSnapshot: List<TVShow> = items.asSnapshot {
            scrollTo(20)
        }

        assertThat(itemSnapshot).containsAtLeastElementsIn(TVShowsTestData.testTVShows())
            .inOrder()
    }

    @Test
    fun tvShowsViewModel_onTheAirFilterSelected_showAiringTodayTVShows() = runTest {
        tvShowsViewModel.onFilterChange(TVShowsFilterItem.FilterType.ON_THE_AIR)
        val items: Flow<PagingData<TVShow>> = tvShowsViewModel.tvShows

        val itemSnapshot: List<TVShow> = items.asSnapshot {
            scrollTo(20)
        }

        assertThat(itemSnapshot).containsAtLeastElementsIn(TVShowsTestData.testTVShows())
            .inOrder()
    }

    @Test
    fun tvShowsViewModel_onPopularFilterSelected_showAiringTodayTVShows() = runTest {
        tvShowsViewModel.onFilterChange(TVShowsFilterItem.FilterType.POPULAR)
        val items: Flow<PagingData<TVShow>> = tvShowsViewModel.tvShows

        val itemSnapshot: List<TVShow> = items.asSnapshot {
            scrollTo(20)
        }

        assertThat(itemSnapshot).containsAtLeastElementsIn(TVShowsTestData.testTVShows())
            .inOrder()
    }

    @Test
    fun tvShowsViewModel_onTopRatedFilterSelected_showAiringTodayTVShows() = runTest {
        tvShowsViewModel.onFilterChange(TVShowsFilterItem.FilterType.TOP_RATED)
        val items: Flow<PagingData<TVShow>> = tvShowsViewModel.tvShows

        val itemSnapshot: List<TVShow> = items.asSnapshot {
            scrollTo(20)
        }

        assertThat(itemSnapshot).containsAtLeastElementsIn(TVShowsTestData.testTVShows())
            .inOrder()
    }

    @Test
    fun tvShowsViewModel_onDiscoverFilterSelected_showAiringTodayTVShows() = runTest {
        tvShowsViewModel.onFilterChange(TVShowsFilterItem.FilterType.DISCOVER)
        val items: Flow<PagingData<TVShow>> = tvShowsViewModel.tvShows

        val itemSnapshot: List<TVShow> = items.asSnapshot {
            scrollTo(20)
        }

        assertThat(itemSnapshot).containsAtLeastElementsIn(TVShowsTestData.testTVShows())
            .inOrder()
    }
}
