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

import kotlinx.coroutines.runBlocking
import me.jerryokafor.core.network.util.enqueueResponse
import me.jerryokafor.ihenkiri.core.network.Config
import me.jerryokafor.ihenkiri.core.network.service.AuthService
import me.jerryokafor.ihenkiri.core.test.test.network.createAccessTokenRequest
import me.jerryokafor.ihenkiri.core.test.test.network.createAccessTokenSuccessResponse
import me.jerryokafor.ihenkiri.core.test.test.network.createRequestToken
import me.jerryokafor.ihenkiri.core.test.test.network.createRequestTokenSuccessResponse
import me.jerryokafor.ihenkiri.core.test.util.MockWebServerUtil.createMockedService
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class AuthServiceTest : BaseServiceTest() {

    private val authService =
        createMockedService(mockWebServer, AuthService::class.java)

    @Test
    fun `test createRequestToken(), returns request token if response is 200`() {
        mockWebServer.enqueueResponse("create-request-token-200.json", 200)

        runBlocking {
            val requestBody = createRequestToken()
            val response = authService.createRequestToken(requestBody)
            val expected = createRequestTokenSuccessResponse()

            assertNotNull(response)
            assertEquals(expected, response)

            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("POST", recordedRequest.method)
            assertEquals("/${Config.TMDB_API_V4}/auth/request_token", recordedRequest.path)
        }
    }

    @Test
    fun `test createAccessToken(), return access token if response is 200`() {
        mockWebServer.enqueueResponse("create-access-token-200.json", 200)

        runBlocking {
            val response = authService.createAccessToken(createAccessTokenRequest())
            val expected = createAccessTokenSuccessResponse()

            assertNotNull(response)
            assertEquals(expected, response)

            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("POST", recordedRequest.method)
            assertEquals("/${Config.TMDB_API_V4}/auth/access_token", recordedRequest.path)
        }
    }
}
