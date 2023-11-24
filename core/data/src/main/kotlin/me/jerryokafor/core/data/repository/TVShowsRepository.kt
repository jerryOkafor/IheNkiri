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

import androidx.paging.PagingSource
import androidx.paging.PagingState
import me.jerryokafor.core.data.filter.MoviesFilter
import me.jerryokafor.core.model.TVShow
import me.jerryokafor.ihenkiri.core.network.model.response.asDomainObject
import me.jerryokafor.ihenkiri.core.network.service.TVSeriesListsApi
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

interface TVShowsRepository {
    suspend fun airingToday(filter: MoviesFilter): List<TVShow>
    suspend fun onTheAir(filter: MoviesFilter): List<TVShow>
    suspend fun popular(filter: MoviesFilter): List<TVShow>
    suspend fun topRated(filter: MoviesFilter): List<TVShow>
}

class DefaultTVShowsRepository
@Inject
constructor(private val tvSeriesListsApi: TVSeriesListsApi) : TVShowsRepository {
    override suspend fun airingToday(filter: MoviesFilter): List<TVShow> =
        tvSeriesListsApi.airingToday(
            filter.language,
            filter.page,
            filter.region,
        ).results.map { it.asDomainObject() }

    override suspend fun onTheAir(filter: MoviesFilter): List<TVShow> = tvSeriesListsApi.onTheAir(
        filter.language,
        filter.page,
        filter.region,
    ).results.map { it.asDomainObject() }

    override suspend fun popular(filter: MoviesFilter): List<TVShow> = tvSeriesListsApi.popular(
        filter.language,
        filter.page,
        filter.region,
    ).results.map { it.asDomainObject() }

    override suspend fun topRated(filter: MoviesFilter): List<TVShow> = tvSeriesListsApi.topRated(
        filter.language,
        filter.page,
        filter.region,
    ).results.map { it.asDomainObject() }
}

class TVShowsListPagingSource(private val fetchMovies: suspend (Int) -> List<TVShow>) :
    PagingSource<Int, TVShow>() {
    override fun getRefreshKey(state: PagingState<Int, TVShow>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TVShow> {
        return try {
            val pageNumber = params.key ?: 1
            val response = fetchMovies(pageNumber)
            val prevKey = if (pageNumber > 1) pageNumber - 1 else null
            val nextKey = if (response.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(data = response, prevKey = prevKey, nextKey = nextKey)
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }
}
