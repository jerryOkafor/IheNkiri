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
import me.jerryokafor.ihenkiri.core.network.service.MovieDetailsApi
import me.jerryokafor.ihenkiri.core.test.util.MockWebServerUtil
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class MovieDetailsApiTest : BaseServiceTest() {
    private val testMovieId = 1006462L

    private val movieDetailsApi = MockWebServerUtil
        .createMockedService(mockWebServer, MovieDetailsApi::class.java)

    @Test
    fun `test movieDetails(), returns movie details when status = 200`() {
        mockWebServer.enqueueResponse("movie-details-200.json", 200)

        runTest {
            val networkMovieDetails = movieDetailsApi.movieDetails(testMovieId)
            assertNotNull(networkMovieDetails)
            assertEquals(networkMovieDetails.adult, false)
            assertEquals(networkMovieDetails.id, 1006462)
            assertEquals(networkMovieDetails.imdbId, "tt15670222")
            assertEquals(networkMovieDetails.revenue, 0)
            assertEquals(networkMovieDetails.runtime, 93)
            assertEquals(networkMovieDetails.voteAverage, 6.767)
            assertEquals(networkMovieDetails.voteCount, 217)
            assertEquals(networkMovieDetails.originalLanguage, "en")
            assertEquals(networkMovieDetails.originalTitle, "The Flood")
            assertEquals(networkMovieDetails.backdropPath, "/bz66a19bR6BKsbY8gSZCM4etJiK.jpg")
            assertEquals(networkMovieDetails.posterPath, "/mvjqqklMpHwOxc40rn7dMhGT0Fc.jpg")

            with(networkMovieDetails.spokenLanguages.first()) {
                assertEquals(name, "English")
                assertEquals(iso6391, "en")
                assertEquals(englishName, "English")
            }

            with(networkMovieDetails.genres.first()) {
                assertEquals(name, "Action")
                assertEquals(id, 28)
            }

            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("GET", recordedRequest.method)

            assert(
                recordedRequest.path
                    ?.contains("/${Config.TMDB_API_V3}/movie/$testMovieId") == true,
            )
        }
    }

    @Test
    fun `test movieCredits(), returns movie credit(Cast & Crew) when status = 200`() {
        mockWebServer.enqueueResponse("movie-credits-200.json", 200)

        runTest {
            val networkMovieCredit = movieDetailsApi.movieCredits(testMovieId)
            assertNotNull(networkMovieCredit)
            assertEquals(networkMovieCredit.cast.size, 5)
            assertEquals(networkMovieCredit.crew.size, 7)

            with(networkMovieCredit.cast.first()) {
                assertEquals(adult, false)
                assertEquals(id, 212833)
                assertEquals(gender, 1)
                assertEquals(knownForDepartment, "Acting")
                assertEquals(name, "Nicky Whelan")
                assertEquals(popularity, 30.103)
                assertEquals(profilePath, "/5uleEujk5NgfsSSGOacvQLJmlV9.jpg")
            }

            with(networkMovieCredit.cast.last()) {
                assertEquals(adult, false)
                assertEquals(id, 84841)
                assertEquals(gender, 2)
                assertEquals(knownForDepartment, "Acting")
                assertEquals(name, "Randy Wayne")
                assertEquals(popularity, 6.532)
                assertEquals(profilePath, "/p0SENcEiSxd3uy31ekcFF4xXvJP.jpg")
            }

            with(networkMovieCredit.crew.first()) {
                assertEquals(adult, false)
                assertEquals(id, 2847)
                assertEquals(gender, 0)
                assertEquals(knownForDepartment, "Writing")
                assertEquals(name, "Josh Ridgway")
                assertEquals(popularity, 2.112)
                assertEquals(profilePath, "/5SeQsIid1RLUx3HfH3NWPCYpZbB.jpg")
            }

            with(networkMovieCredit.crew.last()) {
                assertEquals(adult, false)
                assertEquals(id, 1211636)
                assertEquals(gender, 2)
                assertEquals(knownForDepartment, "Writing")
                assertEquals(name, "James Cullen Bressack")
                assertEquals(popularity, 3.308)
                assertEquals(profilePath, "/1s89cGaLByNuIyEyZG7rmFmSv0J.jpg")
            }

            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("GET", recordedRequest.method)
            assert(
                recordedRequest.path
                    ?.contains("/${Config.TMDB_API_V3}/movie/$testMovieId/credits") == true,
            )
        }
    }

    @Test
    fun `test movieVideos(), returns of movie vidoes when status = 200`() {
        mockWebServer.enqueueResponse("movie-videos-200.json", 200)

        runTest {
            val networkVideos = movieDetailsApi.movieVideos(testMovieId)
            assertNotNull(networkVideos)
            assertEquals(networkVideos.id, 1006462)

            with(networkVideos.results.first()) {
                assertEquals(id, "646e931c1130bd01ee7236c3")
                assertEquals(iso31661, "US")
                assertEquals(iso6391, "en")
                assertEquals(name, "Official Trailer")
                assertEquals(key, "xx5sYS0pHJg")
                assertEquals(site, "YouTube")
                assertEquals(size, 1080)
                assertEquals(type, "Trailer")
                assertEquals(official, true)
                assertEquals(publishedAt, "2023-05-24T22:17:01.000Z")
            }

            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("GET", recordedRequest.method)
            assert(
                recordedRequest.path
                    ?.contains("/${Config.TMDB_API_V3}/movie/$testMovieId/videos") == true,
            )
        }
    }

    @Test
    fun `test similar(), returns list of similar movies when status = 200`() {
        mockWebServer.enqueueResponse("movie-similar-200.json", 200)

        runTest {
            val networkMovieList = movieDetailsApi.similar(testMovieId)
            assertNotNull(networkMovieList)
            assertEquals(networkMovieList.page, 1)
            assertEquals(networkMovieList.totalPages, 5835)
            assertEquals(networkMovieList.totalResults, 116696)
            assertEquals(networkMovieList.results.size, 6)

            with(networkMovieList.results.first()) {
                assertEquals(id, 1679)
                assertEquals(adult, false)
                assertEquals(originalTitle, "ゴジラの逆襲")
                assertEquals(title, "Godzilla Raids Again")
                assertEquals(
                    overview,
                    "Two fishing scout pilots make a horrifying discovery when...",
                )
                assertEquals(posterPath, "/bBYdh9tDCyLaArBOzE38QCA7C3y.jpg")
                assertEquals(voteAverage, 5.9)
                assertEquals(voteCount, 222)
            }

            with(networkMovieList.results.last()) {
                assertEquals(id, 8689)
                assertEquals(adult, false)
                assertEquals(originalTitle, "Cannibal Holocaust")
                assertEquals(title, "Cannibal Holocaust")
                assertEquals(
                    overview,
                    "A New York University professor returns from a...",
                )
                assertEquals(posterPath, "/89vXjnQGdTR1DBrAjqfN5oPpmX3.jpg")
                assertEquals(voteAverage, 6.302)
                assertEquals(voteCount, 1484)
            }

            val recordedRequest = mockWebServer.takeRequest()
            assertEquals(mockWebServer.requestCount, 1)
            assertEquals("GET", recordedRequest.method)
            assert(
                recordedRequest.path
                    ?.contains("/${Config.TMDB_API_V3}/movie/$testMovieId/similar") == true,
            )
        }
    }
}
