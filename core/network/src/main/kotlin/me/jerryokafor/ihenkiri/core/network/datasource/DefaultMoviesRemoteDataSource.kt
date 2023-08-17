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

package me.jerryokafor.ihenkiri.core.network.datasource

import com.google.gson.Gson
import me.jerryokafor.core.common.injection.qualifier.AuthOkHttpClient
import me.jerryokafor.core.model.Movie
import me.jerryokafor.core.network.BuildConfig
import me.jerryokafor.ihenkiri.core.network.model.response.toMovie
import me.jerryokafor.ihenkiri.core.network.service.MovieApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultMoviesRemoteDataSource @Inject constructor(
    gson: Gson,
    @AuthOkHttpClient val okHttpClient: OkHttpClient,
) : MoviesRemoteDataSource {

    private val moviesApi = Retrofit.Builder()
        .baseUrl(BuildConfig.TMDB_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(MovieApi::class.java)

    override suspend fun nowPlayingMovies(query: MoviesQuery): List<Movie> = moviesApi.nowPlaying(
        language = query.language,
        page = query.page,
        region = query.region,
    ).results.map { it.toMovie() }

    override suspend fun popularMovies(query: MoviesQuery): List<Movie> = moviesApi.popular(
        language = query.language,
        page = query.page,
        region = query.region,
    ).results.map { it.toMovie() }

    override suspend fun topRatedMovies(query: MoviesQuery): List<Movie> = moviesApi.topRated(
        language = query.language,
        page = query.page,
        region = query.region,
    ).results.map { it.toMovie() }

    override suspend fun upcomingMovies(query: MoviesQuery): List<Movie> = moviesApi.upcoming(
        language = query.language,
        page = query.page,
        region = query.region,
    ).results.map { it.toMovie() }
}
