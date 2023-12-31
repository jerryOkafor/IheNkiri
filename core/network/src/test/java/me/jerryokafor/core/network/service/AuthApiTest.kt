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

package me.jerryokafor.core.network.service

import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.network.util.enqueueResponse
import me.jerryokafor.ihenkiri.core.network.Config
import me.jerryokafor.ihenkiri.core.network.service.AuthApi
import me.jerryokafor.ihenkiri.core.test.util.MockWebServerUtil
import me.jerryokafor.ihenkiri.core.test.util.createAccessTokenRequest
import me.jerryokafor.ihenkiri.core.test.util.createAccessTokenSuccessResponse
import me.jerryokafor.ihenkiri.core.test.util.createGuestTokenResponse
import me.jerryokafor.ihenkiri.core.test.util.createRequestTokenRequest
import me.jerryokafor.ihenkiri.core.test.util.createRequestTokenSuccessResponse
import okhttp3.mockwebserver.MockResponse
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AuthApiTest : BaseServiceTest() {
    private val authApi = MockWebServerUtil
        .createMockedService(mockWebServer, AuthApi::class.java)

    @Test
    fun `test createRequestToken(), returns request token if response is 200`() = runTest {
        mockWebServer.enqueueResponse("create-request-token-200.json", 200)

        val requestBody = createRequestTokenRequest()
        val response = authApi.createRequestToken(requestBody)
        val expected = createRequestTokenSuccessResponse()

        assertNotNull(response)
        assertEquals(expected, response)

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals(mockWebServer.requestCount, 1)
        assertEquals("POST", recordedRequest.method)
        assertEquals("/${Config.TMDB_API_V4}/auth/request_token", recordedRequest.path)
    }

    @Test
    fun `test createAccessToken(), return access token if response is 200`() = runTest {
        mockWebServer.enqueueResponse("create-access-token-200.json", 200)

        val response = authApi.createAccessToken(createAccessTokenRequest())
        val expected = createAccessTokenSuccessResponse()

        assertNotNull(response)
        assertEquals(expected, response)

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals(mockWebServer.requestCount, 1)
        assertEquals("POST", recordedRequest.method)
        assertEquals("/${Config.TMDB_API_V4}/auth/access_token", recordedRequest.path)
    }

    @Test
    fun `test createGuestSession(), return access token if response is 200`() = runTest {
        mockWebServer.enqueueResponse("create-guest-token-200.json", 200)

        val response = authApi.createGuestSession()
        val expected = createGuestTokenResponse()

        assertNotNull(response)
        assertEquals(expected, response)

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals(mockWebServer.requestCount, 1)
        assertEquals("GET", recordedRequest.method)
        assertEquals(
            "/${Config.TMDB_API_V3}/authentication/guest_session/new",
            recordedRequest.path,
        )
    }

    @Test
    fun `test deleteSession(), return success token if response is 200`() = runTest {
        mockWebServer.enqueue(MockResponse().setResponseCode(200))

        authApi.deleteSession()

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals(mockWebServer.requestCount, 1)
        assertEquals("DELETE", recordedRequest.method)
        assertEquals(
            "/${Config.TMDB_API_V4}/auth/access_token",
            recordedRequest.path,
        )
    }
}
