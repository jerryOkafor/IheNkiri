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
import me.jerryokafor.ihenkiri.core.network.model.request.CreateAccessTokenRequest
import me.jerryokafor.ihenkiri.core.network.model.request.CreateRequestTokenRequest
import me.jerryokafor.ihenkiri.core.network.model.response.CreateAccessTokenResponse
import me.jerryokafor.ihenkiri.core.network.model.response.CreateGuestTokenResponse
import me.jerryokafor.ihenkiri.core.network.model.response.CreateRequestTokenResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {
    /**
     * This method generates a new request token that you can ask a user to approve.
     * This is the first step in getting permission from a user to read and write data on
     * their behalf.
     *
     * @param requestBody the request body for this request
     * @return request token response
     */
    @POST("${Config.TMDB_API_V4}/auth/request_token")
    suspend fun createRequestToken(
        @Body requestBody: CreateRequestTokenRequest,
    ): CreateRequestTokenResponse

    /**
     * This method will finish the user authentication flow and issue an official user access token.
     * The request token in this request is sent along as part of the POST body. You should still
     * use your standard API read access token for authenticating this request.
     *
     * @param requestBody the request body for the request
     * @return access token
     */
    @POST("${Config.TMDB_API_V4}/auth/access_token")
    suspend fun createAccessToken(
        @Body requestBody: CreateAccessTokenRequest,
    ): CreateAccessTokenResponse

    /**
     * Log out of a session.
     *
     * @return
     */
    @DELETE("${Config.TMDB_API_V4}/auth/access_token")
    suspend fun deleteSession()

    /**
     * Creates a session for a guest user
     * Guest sessions are a special kind of session that give you some of the functionality
     * of an account, but not all. For example, some of the things you can do with a
     * guest session are; maintain a rated list, a watchlist and a favourite list.
     *
     * Guest sessions will automatically be deleted if they are not used
     * within 60 minutes of it being issued.
     *
     * @return
     */
    @GET("${Config.TMDB_API_V3}/authentication/guest_session/new")
    suspend fun createGuestSession(): CreateGuestTokenResponse
}
