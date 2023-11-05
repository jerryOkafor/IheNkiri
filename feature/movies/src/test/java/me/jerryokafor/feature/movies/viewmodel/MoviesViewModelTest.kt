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

package me.jerryokafor.feature.movies.viewmodel

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import me.jerryokafor.core.data.repository.MovieListRepository
import me.jerryokafor.core.model.MovieListFilterItem
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.core.test.util.testMovies
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var moviesViewModel: MoviesViewModel

    private val moviesRepository = mockk<MovieListRepository>(relaxed = true) {
        coEvery { nowPlayingMovies(any()) } returns flow { testMovies() }
        coEvery { popularMovies(any()) } returns flow { testMovies() }
        coEvery { topRatedMovies(any()) } returns flow { testMovies() }
        coEvery { upcomingMovies(any()) } returns flow { testMovies() }
    }

    @Before
    fun setUp() {
        moviesViewModel = MoviesViewModel(moviesRepository)
    }

    @Test
    fun moviesViewModel_init_defaultAvailableFiltersSet() {
        val currentUIState = moviesViewModel.uiState.value
        assertThat(currentUIState.availableFilters).isNotEmpty()
        assertThat(currentUIState.availableFilters.size).isEqualTo(4)
        with(currentUIState.availableFilters[0]) {
            assertThat(label).isEqualTo("Now Playing")
            assertThat(isSelected).isTrue()
            assertThat(type).isEqualTo(MovieListFilterItem.FilterType.NOW_PLAYING)
        }

        with(currentUIState.availableFilters[1]) {
            assertThat(label).isEqualTo("Popular")
            assertThat(isSelected).isFalse()
            assertThat(type).isEqualTo(MovieListFilterItem.FilterType.POPULAR)
        }

        with(currentUIState.availableFilters[2]) {
            assertThat(label).isEqualTo("Top Rated")
            assertThat(isSelected).isFalse()
            assertThat(type).isEqualTo(MovieListFilterItem.FilterType.TOP_RATED)
        }

        with(currentUIState.availableFilters[3]) {
            assertThat(label).isEqualTo("Upcoming")
            assertThat(isSelected).isFalse()
            assertThat(type).isEqualTo(MovieListFilterItem.FilterType.UPCOMING)
        }
    }

    @Test
    fun moviesViewModel_OnEvent_CorrectFilterIsSet() {
        val currentUIState = moviesViewModel.uiState.value
        assertThat(currentUIState.availableFilters).isNotEmpty()
        assertThat(currentUIState.availableFilters.size).isEqualTo(4)

        with(currentUIState.availableFilters[0]) {
            assertThat(label).isEqualTo("Now Playing")
            assertThat(isSelected).isTrue()
            assertThat(type).isEqualTo(MovieListFilterItem.FilterType.NOW_PLAYING)
        }

        // initial call due to default
        coVerify(exactly = 1) { moviesRepository.nowPlayingMovies(any()) }

        moviesViewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(MovieListFilterItem.FilterType.POPULAR))
        with(moviesViewModel.uiState.value) {
            assertThat(availableFilters.first { it.type == MovieListFilterItem.FilterType.POPULAR }.isSelected).isTrue()
        }
        coVerify(exactly = 1) { moviesRepository.popularMovies(any()) }

        moviesViewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(MovieListFilterItem.FilterType.TOP_RATED))
        with(moviesViewModel.uiState.value) {
            assertThat(availableFilters.first { it.type == MovieListFilterItem.FilterType.TOP_RATED }.isSelected).isTrue()
        }
        coVerify(exactly = 1) { moviesRepository.topRatedMovies(any()) }

        moviesViewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(MovieListFilterItem.FilterType.UPCOMING))
        with(moviesViewModel.uiState.value) {
            assertThat(availableFilters.first { it.type == MovieListFilterItem.FilterType.UPCOMING }.isSelected).isTrue()
        }
        coVerify(exactly = 1) { moviesRepository.upcomingMovies(any()) }

        moviesViewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(MovieListFilterItem.FilterType.NOW_PLAYING))
        with(moviesViewModel.uiState.value) {
            assertThat(availableFilters.first { it.type == MovieListFilterItem.FilterType.NOW_PLAYING }.isSelected).isTrue()
        }
        coVerify(exactly = 2) { moviesRepository.nowPlayingMovies(any()) }
    }
}
