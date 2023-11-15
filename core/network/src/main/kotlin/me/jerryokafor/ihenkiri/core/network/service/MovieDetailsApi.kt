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

import me.jerryokafor.ihenkiri.core.network.model.response.NetworkMovieCredit
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkMovieDetails
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkMovieList
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkVideos
import retrofit2.http.GET
import retrofit2.http.Path

private const val V3 = "3"

interface MovieDetailsApi {
    /**
     * Get the top level details of a movie by ID.
     *
     * @param movieId is the ID of the movie
     * @return
     */
    @GET("$V3/movie/{movieId}")
    suspend fun movieDetails(
        @Path("movieId") movieId: Long,
    ): NetworkMovieDetails

    /**
     * Get the list of casts and crews of a movie by ID.
     *
     * @param movieId is the ID of the movie
     * @return
     */
    @GET("$V3/movie/{movieId}/credits")
    suspend fun movieCredits(
        @Path("movieId") movieId: Long,
    ): NetworkMovieCredit

    /**
     * Get videos of a movie by ID.
     *
     * @param movieId is the ID of the movie
     * @return
     */
    @GET("$V3/movie/{movieId}/videos")
    suspend fun movieVideos(
        @Path("movieId") movieId: Long,
    ): NetworkVideos

    /**
     * Get the similar movies based on genres and keywords.
     *
     * @param movieId is the ID of the movie
     * @return
     */
    @GET("$V3/movie/{movieId}/similar")
    suspend fun similar(
        @Path("movieId") movieId: Long,
    ): NetworkMovieList
}
