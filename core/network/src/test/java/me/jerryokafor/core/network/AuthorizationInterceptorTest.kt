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

package me.jerryokafor.core.network

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import me.jerryokafor.core.network.util.enqueueResponse
import me.jerryokafor.ihenkiri.core.network.AuthorizationInterceptor
import me.jerryokafor.ihenkiri.core.network.AuthorizationInterceptor.Companion.AUTH_HEADER
import me.jerryokafor.ihenkiri.core.network.service.AuthApi
import me.jerryokafor.ihenkiri.core.test.test.network.createRequestToken
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class AuthorizationInterceptorTest {
    private val mockWebServer = MockWebServer()
    private val authToken = "eyJhbGciOiJIUzI1NiJ9"
    private val authInterceptor = spyk(AuthorizationInterceptor(authToken))

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .build()
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()
    private val authApi = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        .create(AuthApi::class.java)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test authToken set when a request is sent`() {
        mockWebServer.enqueueResponse("create-request-token-200.json", 200)

        runBlocking {
            authApi.createRequestToken(createRequestToken())
        }

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals(recordedRequest.getHeader(AUTH_HEADER), "Bearer $authToken")

        verify(exactly = 1) { authInterceptor.intercept(any()) }
    }
}
