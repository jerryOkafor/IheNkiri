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
import me.jerryokafor.core.common.outcome.Failure
import me.jerryokafor.core.common.outcome.Outcome
import me.jerryokafor.core.common.outcome.Success
import me.jerryokafor.core.model.Movie
import me.jerryokafor.core.model.MovieCredit
import me.jerryokafor.core.model.MovieDetails
import me.jerryokafor.core.model.Video
import me.jerryokafor.ihenkiri.core.network.datasource.MovieDetailsRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

interface MovieDetailsRepository {
    fun movieDetails(movieId: Long): Flow<Outcome<MovieDetails>>

    fun movieCredits(movieId: Long): Flow<Outcome<MovieCredit>>

    fun movieVideos(movieId: Long): Flow<Outcome<List<Video>>>

    fun similarMovies(movieId: Long): Flow<Outcome<List<Movie>>>
}

@Singleton
class DefaultMovieDetailsRepository
    @Inject
    constructor(
        private val remoteDataSource: MovieDetailsRemoteDataSource,
        @IoDispatcher private val dispatcher: CoroutineDispatcher,
    ) : MovieDetailsRepository {
        override fun movieDetails(movieId: Long): Flow<Outcome<MovieDetails>> =
            flow {
                try {
                    val response = remoteDataSource.movieDetails(movieId)
                    emit(Success(response))
                } catch (e: Throwable) {
                    emit(Failure(errorResponse = "Error loading movie details", throwable = e))
                }
            }.flowOn(dispatcher)

        override fun movieCredits(movieId: Long): Flow<Outcome<MovieCredit>> =
            flow {
                try {
                    val response = remoteDataSource.movieCredits(movieId)
                    emit(Success(response))
                } catch (e: Throwable) {
                    e.printStackTrace()
                    emit(Failure(errorResponse = "Error getting movie credits"))
                }
            }.flowOn(dispatcher)

        override fun movieVideos(movieId: Long): Flow<Outcome<List<Video>>> =
            flow {
                try {
                    val response = remoteDataSource.movieVideos(movieId)
                    emit(Success(response))
                } catch (e: Throwable) {
                    emit(Failure(errorResponse = "Error getting movie videos"))
                }
            }.flowOn(dispatcher)

        override fun similarMovies(movieId: Long): Flow<Outcome<List<Movie>>> =
            flow {
                try {
                    val response = remoteDataSource.similarMovies(movieId)
                    emit(Success(response))
                } catch (e: Throwable) {
                    e.printStackTrace()
                    emit(Failure(errorResponse = "Error getting recommended movies"))
                }
            }.flowOn(dispatcher)
    }
