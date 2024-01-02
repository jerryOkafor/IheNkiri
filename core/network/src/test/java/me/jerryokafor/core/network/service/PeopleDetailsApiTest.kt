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

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.network.util.enqueueResponse
import me.jerryokafor.ihenkiri.core.network.Config
import me.jerryokafor.ihenkiri.core.network.service.PeopleDetailsApi
import me.jerryokafor.ihenkiri.core.test.util.MockWebServerUtil
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

const val TEST_PERSON_ID = 0L

class PeopleDetailsApiTest : BaseServiceTest() {
    private val peopleDetailsApi =
        MockWebServerUtil.createMockedService(mockWebServer, PeopleDetailsApi::class.java)

    @Test
    fun `test nowPlaying(), returns list of movies when status = 200`() {
        mockWebServer.enqueueResponse("people-details-200.json", 200)

        runTest {
            val response = peopleDetailsApi.personDetails(personId = TEST_PERSON_ID)

            assertNotNull(response)

            assertThat(response.id).isEqualTo(16483)
            assertThat(response.adult).isFalse()
            assertThat(response.alsoKnownAs).isNotEmpty()
            assertThat(response.biography).isEqualTo(
                "Sylvester Stallone (born Michael " +
                        "Sylvester Gardenzio Stallone, July 6, 1946) is an American actor and filmmaker"
            )
            assertThat(response.birthday).isEqualTo("1946-07-06")
            assertThat(response.deathday).isNull()
            assertThat(response.gender).isEqualTo(2)
            assertThat(response.homepage).isEqualTo("http://www.sylvesterstallone.com")

            assertThat(response.knownForDepartment).isEqualTo("Acting")
            assertThat(response.name).isEqualTo("Sylvester Stallone")
            assertThat(response.placeOfBirth).isEqualTo("New York City, New York, USA")
            assertThat(response.profilePath).isEqualTo("/mJHmLVLctqZd30CSEUKuVFEPrnt.jpg")
            assertThat(response.credits.cast).isNotEmpty()
            with(response.credits.cast.first()) {
                assertThat(title).isEqualTo("Rocky Balboa")
                assertThat(character).isEqualTo("Robert 'Rocky' Balboa")
                assertThat(releaseDate).isEqualTo("2006-12-20")
            }

            assertThat(response.credits.crew).isNotEmpty()
            with(response.credits.crew.last()) {
                assertThat(job).isEqualTo("Executive Producer")
                assertThat(department).isEqualTo("Production")
                assertThat(posterPath).isEqualTo("/fwTv3RPRAIy0maOMns5eYRRwnDk.jpg")
                assertThat(releaseDate).isEqualTo("2022-11-13")
            }


            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("GET", recordedRequest.method)
            assert(
                recordedRequest.path?.contains("/${Config.TMDB_API_V3}/person/$TEST_PERSON_ID") == true,
            )
        }
    }

}
