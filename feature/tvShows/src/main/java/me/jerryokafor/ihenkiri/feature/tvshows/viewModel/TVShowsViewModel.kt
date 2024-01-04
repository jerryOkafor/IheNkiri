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

package me.jerryokafor.ihenkiri.feature.tvshows.viewModel

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
import me.jerryokafor.core.data.repository.TVShowsListPagingSource
import me.jerryokafor.core.data.repository.TVShowsRepository
import me.jerryokafor.core.model.TVShowsFilterItem
import javax.inject.Inject

@HiltViewModel
class TVShowsViewModel
    @Inject
    constructor(
        private val tvShowsRepository: TVShowsRepository,
        @IoDispatcher
        private val dispatcher: CoroutineDispatcher,
    ) : ViewModel() {
        @Suppress("ktlint:standard:property-naming")
        private val _tvShowFilters = MutableStateFlow(
            listOf(
                TVShowsFilterItem(
                    isSelected = true,
                    type = TVShowsFilterItem.FilterType.AIRING_TODAY,
                ),
                TVShowsFilterItem(
                    isSelected = false,
                    type = TVShowsFilterItem.FilterType.ON_THE_AIR,
                ),
                TVShowsFilterItem(
                    isSelected = false,
                    type = TVShowsFilterItem.FilterType.POPULAR,
                ),
                TVShowsFilterItem(
                    isSelected = false,
                    type = TVShowsFilterItem.FilterType.TOP_RATED,
                ),
                TVShowsFilterItem(
                    isSelected = false,
                    type = TVShowsFilterItem.FilterType.DISCOVER,
                ),
            ),
        )

        val tvShowsFilters: StateFlow<List<TVShowsFilterItem>> = _tvShowFilters.asStateFlow()

        private var currentFilter = MutableStateFlow(TVShowsFilterItem.FilterType.AIRING_TODAY)

        val tvShows = currentFilter.flatMapLatest { filter ->
            Pager(
                config = PagingConfig(pageSize = 20, maxSize = 200, enablePlaceholders = true),
                initialKey = null,
                pagingSourceFactory = {
                    TVShowsListPagingSource { page ->
                        val queryFilter = MoviesFilter(
                            language = "en-Us",
                            page = page,
                            region = null,
                        )

                        when (filter) {
                            TVShowsFilterItem.FilterType.AIRING_TODAY ->
                                tvShowsRepository.airingToday(queryFilter)

                            TVShowsFilterItem.FilterType.ON_THE_AIR ->
                                tvShowsRepository.onTheAir(queryFilter)

                            TVShowsFilterItem.FilterType.POPULAR ->
                                tvShowsRepository.popular(queryFilter)

                            TVShowsFilterItem.FilterType.TOP_RATED ->
                                tvShowsRepository.topRated(queryFilter)

                            TVShowsFilterItem.FilterType.DISCOVER ->
                                tvShowsRepository.airingToday(queryFilter)
                        }
                    }
                },
            ).flow.cachedIn(viewModelScope)
        }.flowOn(dispatcher)

        fun onFilterChange(filter: TVShowsFilterItem.FilterType) {
            val updatedFilters = _tvShowFilters.value.map { filterItem ->
                if (filterItem.type == filter) {
                    filterItem.copy(isSelected = true)
                } else {
                    filterItem.copy(isSelected = false)
                }
            }
            currentFilter.update { filter }
            _tvShowFilters.update { updatedFilters }
        }
    }
