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

package me.jerryokafor.ihenkiri.core.network.service

import me.jerryokafor.ihenkiri.core.network.Config
import me.jerryokafor.ihenkiri.core.network.model.response.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesApi {
    /**
     * Get a list of movies that are currently in theatres.
     *
     * @param language the language for the query
     * @param page the current page of the query
     * @param region the region for the query
     * @return list of movies
     */
    @GET("${Config.TMDB_API_V3}/movie/now_playing")
    suspend fun nowPlaying(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String? = null,
    ): MovieListResponse

    /**
     * Get a list of movies ordered by popularity.
     *
     * @param language the language of the query
     * @param page the current page for the query
     * @param region the region for the request
     * @return list of movies
     */
    @GET("${Config.TMDB_API_V3}/movie/popular")
    suspend fun popular(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String? = null,
    ): MovieListResponse

    /**
     * Get a list of movies ordered by rating.
     *
     * @param language the language of the query
     * @param page the current page for the query
     * @param region the region for the request
     * @return list of movies
     */
    @GET("${Config.TMDB_API_V3}/movie/top_rated")
    suspend fun topRated(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String? = null,
    ): MovieListResponse

    /**
     * Get a list of movies that are being released soon.
     *
     * @param language the language of the query
     * @param page the current page for the query
     * @param region the region for the request
     * @return list of movies
     */
    @GET("${Config.TMDB_API_V3}/movie/upcoming")
    suspend fun upcoming(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String? = null,
    ): MovieListResponse
}
