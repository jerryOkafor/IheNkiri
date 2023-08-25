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

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import me.jerryokafor.core.data.filter.MoviesFilter
import me.jerryokafor.core.data.repository.MoviesRepository
import me.jerryokafor.core.model.Movie
import me.jerryokafor.core.model.MovieListFilterItem
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    private var currentFilter: MovieListFilterItem.FilterType =
        MovieListFilterItem.FilterType.NOW_PLAYING

    private val availableFilters = listOf(
        MovieListFilterItem(
            label = "Now Playing",
            isSelected = true,
            type = MovieListFilterItem.FilterType.NOW_PLAYING,
        ),
        MovieListFilterItem(
            label = "Popular",
            isSelected = false,
            type = MovieListFilterItem.FilterType.POPULAR,
        ),
        MovieListFilterItem(
            label = "Top Rated",
            isSelected = false,
            type = MovieListFilterItem.FilterType.TOP_RATED,
        ),
        MovieListFilterItem(
            label = "Upcoming",
            isSelected = false,
            type = MovieListFilterItem.FilterType.UPCOMING,
        ),
    )

    init {
        _uiState.update {
            it.copy(availableFilters = availableFilters)
        }

        updateMovies()
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnFilterSelected -> {
                val updatedFilters = availableFilters.map {
                    if (it.type == event.filter) it.copy(isSelected = true) else it.copy(isSelected = false)
                }
                _uiState.update {
                    it.copy(availableFilters = updatedFilters)
                }

                currentFilter = event.filter

                // update movies
                updateMovies()
            }
        }
    }

    private fun updateMovies() {
        val filter = MoviesFilter(language = "en-Us", page = 1, region = null)

        when (currentFilter) {
            MovieListFilterItem.FilterType.NOW_PLAYING -> moviesRepository.nowPlayingMovies(filter)
            MovieListFilterItem.FilterType.POPULAR -> moviesRepository.popularMovies(filter)
            MovieListFilterItem.FilterType.TOP_RATED -> moviesRepository.topRatedMovies(filter)
            MovieListFilterItem.FilterType.UPCOMING -> moviesRepository.upcomingMovies(filter)
        }.onStart {
            _uiState.update { it.copy(loading = true) }
        }.onEach { movies ->
            Log.d("Testing: ", "$movies")
            _uiState.update { it.copy(movies = movies) }
        }.onCompletion {
            _uiState.update { it.copy(loading = false) }
        }.catch {
            Log.w("Testing: ", it.localizedMessage)
        }.launchIn(viewModelScope)
    }

    data class UIState(
        val loading: Boolean = false,
        val availableFilters: List<MovieListFilterItem> = emptyList(),
        val movies: List<Movie> = emptyList(),
    )

    sealed interface Event {
        data class OnFilterSelected(val filter: MovieListFilterItem.FilterType) : Event
    }
}
