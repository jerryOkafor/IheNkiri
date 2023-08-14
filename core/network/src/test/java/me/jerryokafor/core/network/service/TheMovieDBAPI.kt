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

package me.jerryokafor.core.network.service

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import kotlinx.coroutines.runBlocking
import me.jerryokafor.core.network.util.enqueueResponse
import me.jerryokafor.ihenkiri.core.network.model.request.CreateAccessTokenRequest
import me.jerryokafor.ihenkiri.core.network.model.request.EmptyRequest
import me.jerryokafor.ihenkiri.core.network.model.response.CreateAccessTokenResponse
import me.jerryokafor.ihenkiri.core.network.model.response.CreateRequestTokenResponse
import me.jerryokafor.ihenkiri.core.network.service.TheMovieDBAPI
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TheMovieDBAPI {

    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(TheMovieDBAPI::class.java)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test create request token success if response is 200`() {
        mockWebServer.enqueueResponse("create-request-token-200.json", 200)

        runBlocking {
            val response = api.createRequestToken(EmptyRequest())
            val expected = CreateRequestTokenResponse(
                requestToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWRpcm.XXXX.dxZddmwFqbiWGn1ycR0YPLNGLtVBWOagzneoVM3pXQ0",
                statusCode = 1,
                statusMessage = "Success.",
                success = true,
            )

            assertNotNull(response)

            assertEquals(expected, response)
        }
    }

    @Test
    fun `test create access token success if response is 200`() {
        mockWebServer.enqueueResponse("create-access-token-200.json", 200)

        runBlocking {
            val response = api.createAccessToken(
                CreateAccessTokenRequest(
                    requestToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWRpcm.XXXX.dxZddmwFqbiWGn1ycR0YPLNGLtVBWOagzneoVM3pXQ0",
                ),
            )
            val expected = CreateAccessTokenResponse(
                accountId = "4bc889XXXXa3c0z92001001",
                accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCIdIkpXVCJ9.XXXXX.sImp0aSI6Ijg4In0.b76OiEs10gdp9oNOoGpBJ94nO9Zi17Y7SvAXJQW8nH2",
                statusMessage = "Success.",
                statusCode = 1,
                success = true,
            )

            assertNotNull(response)
            assertEquals(expected, response)
        }
    }
}
