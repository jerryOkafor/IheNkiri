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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import me.jerryokafor.core.common.injection.IoDispatcher
import me.jerryokafor.core.data.filter.MoviesFilter
import me.jerryokafor.core.data.repository.MovieListRepository
import me.jerryokafor.core.data.repository.MoviesListPagingSource
import me.jerryokafor.core.model.MovieListFilterItem
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel
@Inject
constructor(
    private val movieListRepository: MovieListRepository,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _availableFilters = MutableStateFlow(
        listOf(
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
            MovieListFilterItem(
                label = "Discover",
                isSelected = false,
                type = MovieListFilterItem.FilterType.DISCOVER,
            ),
        ),
    )

    val availableFilters: StateFlow<List<MovieListFilterItem>> = _availableFilters.asStateFlow()

    private var currentFilter = MutableStateFlow(MovieListFilterItem.FilterType.NOW_PLAYING)

    val movies = currentFilter.flatMapLatest { filter ->
        Pager(
            config = PagingConfig(pageSize = 20, maxSize = 200, enablePlaceholders = true),
            initialKey = null,
            pagingSourceFactory = {
                MoviesListPagingSource { page ->
                    val queryFilter = MoviesFilter(
                        language = "en-Us",
                        page = page,
                        region = null,
                    )

                    when (filter) {
                        MovieListFilterItem.FilterType.NOW_PLAYING ->
                            movieListRepository.nowPlayingMovies(queryFilter)

                        MovieListFilterItem.FilterType.POPULAR ->
                            movieListRepository.popularMovies(queryFilter)

                        MovieListFilterItem.FilterType.TOP_RATED ->
                            movieListRepository.topRatedMovies(queryFilter)

                        MovieListFilterItem.FilterType.UPCOMING ->
                            movieListRepository.upcomingMovies(queryFilter)

                        MovieListFilterItem.FilterType.DISCOVER ->
                            movieListRepository.upcomingMovies(queryFilter)
                    }
                }
            },
        ).flow.cachedIn(viewModelScope)
    }.flowOn(dispatcher)

    fun onEvent(event: Event) {
        when (event) {
            is Event.OnFilterSelected -> {
                val updatedFilters = _availableFilters.value.map { filterItem ->
                    if (filterItem.type == event.filter) {
                        filterItem.copy(isSelected = true)
                    } else {
                        filterItem.copy(isSelected = false)
                    }
                }
                currentFilter.update { event.filter }
                _availableFilters.update { updatedFilters }
            }
        }
    }

    sealed interface Event {
        data class OnFilterSelected(val filter: MovieListFilterItem.FilterType) : Event
    }
}
