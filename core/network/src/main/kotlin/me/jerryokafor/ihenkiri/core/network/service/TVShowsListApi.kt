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

package me.jerryokafor.ihenkiri.core.network.service

import me.jerryokafor.ihenkiri.core.network.Config
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkTvShow
import me.jerryokafor.ihenkiri.core.network.model.response.PagedNetworkResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TVShowsListApi {
    /**
     * Get a list of TV shows airing today.
     *
     * @param language
     * @param page
     * @param region
     * @return
     */
    @GET("${Config.TMDB_API_V3}/tv/airing_today")
    suspend fun airingToday(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String? = null,
    ): PagedNetworkResponse<NetworkTvShow>

    /**
     * Get a list of TV shows that air in the next 7 days.
     *
     * @param language
     * @param page
     * @param region
     * @return
     */
    @GET("${Config.TMDB_API_V3}/tv/on_the_air")
    suspend fun onTheAir(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String? = null,
    ): PagedNetworkResponse<NetworkTvShow>

    /**
     * Get a list of TV shows ordered by popularity.
     *
     * @param language
     * @param page
     * @param region
     * @return
     */
    @GET("${Config.TMDB_API_V3}/tv/popular")
    suspend fun popular(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String? = null,
    ): PagedNetworkResponse<NetworkTvShow>

    /**
     * Get a list of TV shows ordered by rating.
     *
     * @param language
     * @param page
     * @param region
     * @return
     */
    @GET("${Config.TMDB_API_V3}/tv/top_rated")
    suspend fun topRated(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String? = null,
    ): PagedNetworkResponse<NetworkTvShow>
}
