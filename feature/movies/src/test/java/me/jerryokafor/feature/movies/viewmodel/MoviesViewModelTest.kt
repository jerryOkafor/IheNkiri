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

package me.jerryokafor.feature.movies.viewmodel

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.data.repository.MovieListRepository
import me.jerryokafor.core.model.Movie
import me.jerryokafor.core.model.MovieListFilterItem.FilterType
import me.jerryokafor.feature.movies.viewmodel.MoviesViewModel.Event.OnFilterSelected
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.core.test.util.testMovies
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var moviesViewModel: MoviesViewModel

    private val movieListRepository = mockk<MovieListRepository> {
        coEvery { nowPlayingMovies(any()) } returns testMovies()
        coEvery { popularMovies(any()) } returns testMovies()
        coEvery { topRatedMovies(any()) } returns testMovies()
        coEvery { upcomingMovies(any()) } returns testMovies()
    }

    @Before
    fun setUp() {
        moviesViewModel = MoviesViewModel(
            movieListRepository = movieListRepository,
            dispatcher = mainDispatcherRule.testDispatcher,
        )
    }

    @Test
    fun moviesViewModel_init_defaultAvailableFiltersSet() = runTest {
        moviesViewModel.availableFilters.test {
            val availableFilters = awaitItem()
            assertThat(availableFilters).isNotEmpty()
            assertThat(availableFilters.size).isEqualTo(5)

            with(availableFilters[0]) {
                assertThat(isSelected).isTrue()
                assertThat(type).isEqualTo(FilterType.NOW_PLAYING)
            }

            with(availableFilters[1]) {
                assertThat(isSelected).isFalse()
                assertThat(type).isEqualTo(FilterType.POPULAR)
            }

            with(availableFilters[2]) {
                assertThat(isSelected).isFalse()
                assertThat(type).isEqualTo(FilterType.TOP_RATED)
            }

            with(availableFilters[3]) {
                assertThat(isSelected).isFalse()
                assertThat(type).isEqualTo(FilterType.UPCOMING)
            }

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun moviesViewModel_OnEvent_CorrectFilterIsSet() {
        val availableFilters = moviesViewModel.availableFilters.value
        assertThat(availableFilters).isNotEmpty()
        assertThat(availableFilters.size).isEqualTo(5)

        with(availableFilters[0]) {
            assertThat(isSelected).isTrue()
            assertThat(type).isEqualTo(FilterType.NOW_PLAYING)
        }

        moviesViewModel.onEvent(OnFilterSelected(FilterType.POPULAR))
        assertThat(
            moviesViewModel.availableFilters.value.first {
                it.type == FilterType.POPULAR
            }.isSelected,
        ).isTrue()

        moviesViewModel.onEvent(OnFilterSelected(FilterType.TOP_RATED))
        assertThat(
            moviesViewModel.availableFilters.value.first {
                it.type == FilterType.TOP_RATED
            }.isSelected,
        ).isTrue()

        moviesViewModel.onEvent(OnFilterSelected(FilterType.UPCOMING))
        assertThat(
            moviesViewModel.availableFilters.value.first {
                it.type == FilterType.UPCOMING
            }.isSelected,
        ).isTrue()

        moviesViewModel.onEvent(OnFilterSelected(FilterType.NOW_PLAYING))
        assertThat(
            moviesViewModel.availableFilters.value.first {
                it.type == FilterType.NOW_PLAYING
            }.isSelected,
        ).isTrue()
    }

    @Test
    fun moviesViewModel_NowPlayingFilterSelected_ShowNowPlayingMovies() = runTest {
        moviesViewModel.onEvent(OnFilterSelected(FilterType.NOW_PLAYING))
        val items: Flow<PagingData<Movie>> = moviesViewModel.movies
        val itemsSnapshot: List<Movie> = items.asSnapshot {
            // scroll one page length
            scrollTo(index = 20)
        }

        assertThat(itemsSnapshot).containsAtLeastElementsIn(testMovies())
            .inOrder()

        coVerify { movieListRepository.nowPlayingMovies(any()) }
    }

    @Test
    fun moviesViewModel_PopularFilterSelected_ShowPopularMovies() = runTest {
        moviesViewModel.onEvent(OnFilterSelected(FilterType.POPULAR))
        val items: Flow<PagingData<Movie>> = moviesViewModel.movies
        val itemsSnapshot: List<Movie> = items.asSnapshot {
            // scroll one page length
            scrollTo(index = 20)
        }

        assertThat(itemsSnapshot).containsAtLeastElementsIn(testMovies())
            .inOrder()

        coVerify { movieListRepository.popularMovies(any()) }
    }

    @Test
    fun moviesViewModel_TopRatedFilterSelected_ShowTopRatedMovies() = runTest {
        moviesViewModel.onEvent(OnFilterSelected(FilterType.TOP_RATED))
        val items: Flow<PagingData<Movie>> = moviesViewModel.movies
        val itemsSnapshot: List<Movie> = items.asSnapshot {
            // scroll one page length
            scrollTo(index = 20)
        }

        assertThat(itemsSnapshot).containsAtLeastElementsIn(testMovies())
            .inOrder()

        coVerify { movieListRepository.topRatedMovies(any()) }
    }

    @Test
    fun moviesViewModel_UpcomingFilterSelected_ShowUpcomingMovies() = runTest {
        moviesViewModel.onEvent(OnFilterSelected(FilterType.UPCOMING))
        val items: Flow<PagingData<Movie>> = moviesViewModel.movies
        val itemsSnapshot: List<Movie> = items.asSnapshot {
            // scroll one page length
            scrollTo(index = 20)
        }

        assertThat(itemsSnapshot).containsAtLeastElementsIn(testMovies())
            .inOrder()

        coVerify { movieListRepository.upcomingMovies(any()) }
    }

    @Test
    fun moviesViewModel_DiscoverFilterSelected_ShowDiscoverMovies() = runTest {
        moviesViewModel.onEvent(OnFilterSelected(FilterType.DISCOVER))
        val items: Flow<PagingData<Movie>> = moviesViewModel.movies
        val itemsSnapshot: List<Movie> = items.asSnapshot {
            // scroll one page length
            scrollTo(index = 20)
        }

        assertThat(itemsSnapshot).containsAtLeastElementsIn(testMovies())
            .inOrder()

        coVerify { movieListRepository.upcomingMovies(any()) }
    }
}
