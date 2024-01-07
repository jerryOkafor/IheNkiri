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
import me.jerryokafor.ihenkiri.core.network.service.MovieListApi
import me.jerryokafor.ihenkiri.core.test.util.MockWebServerUtil
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MovieListApiTest : BaseServiceTest() {
    private val movieListApi =
        MockWebServerUtil.createMockedService(mockWebServer, MovieListApi::class.java)

    @Test
    fun `test nowPlaying(), returns list of movies when status = 200`() {
        mockWebServer.enqueueResponse("movie-list-200.json", 200)

        runTest {
            val response = movieListApi.nowPlaying(language = "en-US", page = 1)
            assertNotNull(response)
            assertEquals(response.page, 1)
            assertEquals(response.totalPages, 79)
            assertEquals(response.totalResults, 1570)
            assertTrue(response.results.isNotEmpty())
            assertEquals(response.results.size, 7)
            assertEquals(response.results.first().id, 667538)
            assertEquals(response.results.last().id, 1006462)

            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("GET", recordedRequest.method)
            assert(
                recordedRequest.path
                    ?.contains("/${Config.TMDB_API_V3}/movie/now_playing") == true,
            )
        }
    }

    @Test
    fun `test popular(), returns list of movies when status = 200`() {
        mockWebServer.enqueueResponse("movie-list-200.json", 200)

        runTest {
            val response = movieListApi.popular(language = "en-US", page = 1)
            assertNotNull(response)
            assertEquals(response.page, 1)
            assertEquals(response.totalPages, 79)
            assertEquals(response.totalResults, 1570)
            assertTrue(response.results.isNotEmpty())
            assertEquals(response.results.size, 7)
            assertEquals(response.results.first().id, 667538)
            assertEquals(response.results.last().id, 1006462)

            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("GET", recordedRequest.method)
            assert(recordedRequest.path?.contains("/${Config.TMDB_API_V3}/movie/popular") == true)
        }
    }

    @Test
    fun `test topRated(), returns list of movies when status = 200`() {
        mockWebServer.enqueueResponse("movie-list-200.json", 200)

        runTest {
            val response = movieListApi.topRated(language = "en-US", page = 1)
            assertNotNull(response)
            assertEquals(response.page, 1)
            assertEquals(response.totalPages, 79)
            assertEquals(response.totalResults, 1570)
            assertTrue(response.results.isNotEmpty())
            assertEquals(response.results.size, 7)
            assertEquals(response.results.first().id, 667538)
            assertEquals(response.results.last().id, 1006462)

            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("GET", recordedRequest.method)
            assert(recordedRequest.path?.contains("/${Config.TMDB_API_V3}/movie/top_rated") == true)
        }
    }

    @Test
    fun `test upcoming(), returns list of movies when status = 200`() {
        mockWebServer.enqueueResponse("movie-list-200.json", 200)

        runTest {
            val response = movieListApi.upcoming(language = "en-US", page = 1)
            assertNotNull(response)
            assertEquals(response.page, 1)
            assertEquals(response.totalPages, 79)
            assertEquals(response.totalResults, 1570)
            assertEquals(response.results.size, 7)
            assertTrue(response.results.isNotEmpty())
            assertEquals(response.results.first().id, 667538)
            assertEquals(response.results.last().id, 1006462)

            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("GET", recordedRequest.method)
            assert(recordedRequest.path?.contains("/${Config.TMDB_API_V3}/movie/upcoming") == true)
        }
    }
}
