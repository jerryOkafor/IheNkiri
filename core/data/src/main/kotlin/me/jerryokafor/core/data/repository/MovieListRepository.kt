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
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.common.injection.IoDispatcher
import me.jerryokafor.core.data.filter.MoviesFilter
import me.jerryokafor.core.data.filter.toQuery
import me.jerryokafor.core.model.Movie
import me.jerryokafor.ihenkiri.core.network.datasource.MoviesRemoteDataSource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface MovieListRepository {
    suspend fun nowPlayingMovies(filter: MoviesFilter): List<Movie>

    suspend fun popularMovies(filter: MoviesFilter): List<Movie>

    suspend fun topRatedMovies(filter: MoviesFilter): List<Movie>

    suspend fun upcomingMovies(filter: MoviesFilter): List<Movie>
}

@Singleton
class DefaultMovieListRepository
    @Inject
    constructor(
        private val moviesRemoteDataSource: MoviesRemoteDataSource,
        @IoDispatcher private val defaultDispatcher: CoroutineDispatcher,
    ) : MovieListRepository {
        override suspend fun nowPlayingMovies(filter: MoviesFilter): List<Movie> =
            withContext(defaultDispatcher) {
                moviesRemoteDataSource.nowPlayingMovies(filter.toQuery())
            }

        override suspend fun popularMovies(filter: MoviesFilter): List<Movie> =
            withContext(defaultDispatcher) {
                moviesRemoteDataSource.popularMovies(filter.toQuery())
            }

        override suspend fun topRatedMovies(filter: MoviesFilter): List<Movie> =
            withContext(defaultDispatcher) {
                moviesRemoteDataSource.topRatedMovies(filter.toQuery())
            }

        override suspend fun upcomingMovies(filter: MoviesFilter): List<Movie> =
            withContext(defaultDispatcher) {
                moviesRemoteDataSource.upcomingMovies(filter.toQuery())
            }
    }

@ExcludeFromGeneratedCoverageReport
class MoviesListPagingSource(private val fetchMovies: suspend (Int) -> List<Movie>) :
    PagingSource<Int, Movie>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val pageNumber = params.key ?: 1
            val response = fetchMovies(pageNumber)
            val prevKey = if (pageNumber > 1) pageNumber - 1 else null
            val nextKey = if (response.isNotEmpty()) pageNumber + 1 else null

            LoadResult.Page(
                data = response,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}
