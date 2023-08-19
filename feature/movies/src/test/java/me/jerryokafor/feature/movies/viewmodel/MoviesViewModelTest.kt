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
import me.jerryokafor.feature.movies.screen.Chip
import org.junit.Before
import org.junit.Test

class MoviesViewModelTest {

    private lateinit var moviesViewModel: MoviesViewModel

    @Before
    fun setUp() {
        moviesViewModel = MoviesViewModel()
    }

    @Test
    fun moviesViewModel_init_defaultAvailableFiltersSet() {
        val currentUIState = moviesViewModel.uiState.value
        assertThat(currentUIState.availableFilters).isNotEmpty()
        assertThat(currentUIState.availableFilters.size).isEqualTo(4)
        with(currentUIState.availableFilters[0]) {
            assertThat(label).isEqualTo("Now Playing")
            assertThat(isSelected).isTrue()
            assertThat(type).isEqualTo(Chip.FilterType.NOW_PLAYING)
        }

        with(currentUIState.availableFilters[1]) {
            assertThat(label).isEqualTo("Popular")
            assertThat(isSelected).isFalse()
            assertThat(type).isEqualTo(Chip.FilterType.POPULAR)
        }

        with(currentUIState.availableFilters[2]) {
            assertThat(label).isEqualTo("Top Rated")
            assertThat(isSelected).isFalse()
            assertThat(type).isEqualTo(Chip.FilterType.TOP_RATED)
        }

        with(currentUIState.availableFilters[3]) {
            assertThat(label).isEqualTo("Upcoming")
            assertThat(isSelected).isFalse()
            assertThat(type).isEqualTo(Chip.FilterType.UPCOMING)
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
            assertThat(type).isEqualTo(Chip.FilterType.NOW_PLAYING)
        }

        moviesViewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(Chip.FilterType.POPULAR))
        with(moviesViewModel.uiState.value) {
            assertThat(availableFilters.first { it.type == Chip.FilterType.POPULAR }.isSelected).isTrue()
        }

        moviesViewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(Chip.FilterType.TOP_RATED))
        with(moviesViewModel.uiState.value) {
            assertThat(availableFilters.first { it.type == Chip.FilterType.TOP_RATED }.isSelected).isTrue()
        }

        moviesViewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(Chip.FilterType.UPCOMING))
        with(moviesViewModel.uiState.value) {
            assertThat(availableFilters.first { it.type == Chip.FilterType.UPCOMING }.isSelected).isTrue()
        }

        moviesViewModel.onEvent(MoviesViewModel.Event.OnFilterSelected(Chip.FilterType.NOW_PLAYING))
        with(moviesViewModel.uiState.value) {
            assertThat(availableFilters.first { it.type == Chip.FilterType.NOW_PLAYING }.isSelected).isTrue()
        }
    }
}
