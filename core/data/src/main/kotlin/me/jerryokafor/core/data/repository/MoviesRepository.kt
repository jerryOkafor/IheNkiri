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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import me.jerryokafor.core.common.injection.IoDispatcher
import me.jerryokafor.core.data.filter.MoviesFilter
import me.jerryokafor.core.data.filter.toQuery
import me.jerryokafor.core.model.Movie
import me.jerryokafor.ihenkiri.core.network.datasource.MoviesRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

interface MoviesRepository {
    fun nowPlayingMovies(filter: MoviesFilter): Flow<List<Movie>>
    fun popularMovies(filter: MoviesFilter): Flow<List<Movie>>
    fun topRatedMovies(filter: MoviesFilter): Flow<List<Movie>>
    fun upcomingMovies(filter: MoviesFilter): Flow<List<Movie>>
}

@Singleton
class DefaultMoviesRepository @Inject constructor(
    private val moviesRemoteDataSource: MoviesRemoteDataSource,
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : MoviesRepository {
    override fun nowPlayingMovies(filter: MoviesFilter): Flow<List<Movie>> = flow {
        emit(moviesRemoteDataSource.nowPlayingMovies(filter.toQuery()))
    }.flowOn(defaultDispatcher)

    override fun popularMovies(filter: MoviesFilter): Flow<List<Movie>> = flow {
        emit(moviesRemoteDataSource.popularMovies(filter.toQuery()))
    }.flowOn(defaultDispatcher)

    override fun topRatedMovies(filter: MoviesFilter): Flow<List<Movie>> = flow {
        emit(moviesRemoteDataSource.topRatedMovies(filter.toQuery()))
    }.flowOn(defaultDispatcher)

    override fun upcomingMovies(filter: MoviesFilter): Flow<List<Movie>> = flow {
        emit(moviesRemoteDataSource.upcomingMovies(filter.toQuery()))
    }.flowOn(defaultDispatcher)
}
