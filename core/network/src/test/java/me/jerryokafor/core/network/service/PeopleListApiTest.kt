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
import me.jerryokafor.ihenkiri.core.network.service.PeopleListApi
import me.jerryokafor.ihenkiri.core.test.util.MockWebServerUtil
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PeopleListApiTest : BaseServiceTest() {
    private val peopleListsApi = MockWebServerUtil
        .createMockedService(mockWebServer, PeopleListApi::class.java)

    @Test
    fun `test popularPersons(), returns list of persons when response is 200`() = runTest {
        mockWebServer.enqueueResponse("people-list-200.json", 200)
        val persons = peopleListsApi.popularPersons(page = 1)
        assertNotNull(persons)
        assertEquals(persons.results.size, 9)

        with(persons.results.first()) {
            assertEquals(id, 976)
            assertEquals(name, "Jason Statham")
            assertEquals(popularity, 199.055)
            assertEquals(profilePath, "/whNwkEQYWLFJA8ij0WyOOAD5xhQ.jpg")
            assertEquals(knownFor.size, 3)

            with(knownFor.first()) {
                assertEquals(id, 107)
                assertEquals(title, "Snatch")
            }
        }

        with(persons.results.last()) {
            assertEquals(id, 1373737)
            assertEquals(name, "Florence Pugh")
            assertEquals(popularity, 115.26)
            assertEquals(profilePath, "/fhEsn35uAwUZy37RKpLdwWyx2y5.jpg")
            assertEquals(knownFor.size, 3)

            with(knownFor.first()) {
                assertEquals(id, 530385)
                assertEquals(title, "Midsommar")
            }
        }

        val recordedRequest = mockWebServer.takeRequest()
        assertEquals(mockWebServer.requestCount, 1)
        assertEquals("GET", recordedRequest.method)

        assertEquals(
            expected = "/${Config.TMDB_API_V3}/person/popular?page=1",
            actual = recordedRequest.path,
        )
    }
}
