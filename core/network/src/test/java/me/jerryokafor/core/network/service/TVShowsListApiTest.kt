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
import me.jerryokafor.ihenkiri.core.network.service.TVShowsListApi
import me.jerryokafor.ihenkiri.core.test.util.MockWebServerUtil
import org.junit.Test

class TVShowsListApiTest : BaseServiceTest() {
    private val tvShowsListsApi =
        MockWebServerUtil.createMockedService(mockWebServer, TVShowsListApi::class.java)

    @Test
    fun `tvSeriesListsApi airingToday(), returns list of tvShows when response is 200`() = runTest {
        mockWebServer.enqueueResponse("tv_shows-list-200.json", 200)

        val tvShows = tvShowsListsApi
            .airingToday(language = "en-US", page = 1)
        assertThat(tvShows).isNotNull()
        assertThat(tvShows.results.size).isEqualTo(20)

        with(tvShows.results.first()) {
            assertThat(id).isEqualTo(94722)
            assertThat(name).isEqualTo("Tagesschau")
            assertThat(originalName).isEqualTo("Tagesschau")
            assertThat(firstAirDate).isEqualTo("1952-12-26")
            assertThat(posterPath).isEqualTo("/7dFZJ2ZJJdcmkp05B9NWlqTJ5tq.jpg")
            assertThat(overview)
                .isEqualTo(
                    "German daily news program, the oldest still existing " +
                        "program on German television.",
                )
            assertThat(originalLanguage).isEqualTo("de")
            assertThat(backdropPath).isEqualTo("/jWXrQstj7p3Wl5MfYWY6IHqRpDb.jpg")
            assertThat(genreIds.first()).isEqualTo(10763)
        }

        with(tvShows.results.last()) {
            assertThat(id).isEqualTo(216578)
            assertThat(name).isEqualTo("Can't Buy Me Love")
            assertThat(originalName).isEqualTo("Can't Buy Me Love")
            assertThat(firstAirDate).isEqualTo("2023-10-16")
            assertThat(posterPath).isEqualTo("/jCGaLxSOANF44kAQqpA380Md1fq.jpg")
            assertThat(overview).isEqualTo(
                "When a young man gets caught up in a deadly plot against a " +
                    "rich woman, he pays a devastating cost to free her - " +
                    "creating a debt that binds them together.",
            )
            assertThat(originalLanguage).isEqualTo("tl")
            assertThat(backdropPath).isEqualTo("/sFRlpUJEN0y6yV7QK7DQJochbin.jpg")
            assertThat(genreIds.first()).isEqualTo(18)
        }

        val recordedRequest = mockWebServer.takeRequest()
        assertThat(mockWebServer.requestCount).isEqualTo(1)
        assertThat(recordedRequest.method).isEqualTo("GET")
        assertThat(recordedRequest.path).contains("/${Config.TMDB_API_V3}/tv/airing_today")
    }

    @Test
    fun `tvSeriesListsApi onTheAir(), returns list of tvShows when response is 200`() = runTest {
        mockWebServer.enqueueResponse("tv_shows-list-200.json", 200)
        val tvShows = tvShowsListsApi.onTheAir(language = "en-US", page = 1)
        assertThat(tvShows).isNotNull()
        assertThat(tvShows.results.size).isEqualTo(20)

        with(tvShows.results.first()) {
            assertThat(id).isEqualTo(94722)
            assertThat(name).isEqualTo("Tagesschau")
            assertThat(originalName).isEqualTo("Tagesschau")
            assertThat(firstAirDate).isEqualTo("1952-12-26")
            assertThat(posterPath).isEqualTo("/7dFZJ2ZJJdcmkp05B9NWlqTJ5tq.jpg")
            assertThat(overview)
                .isEqualTo(
                    "German daily news program, the oldest still existing " +
                        "program on German television.",
                )
            assertThat(originalLanguage).isEqualTo("de")
            assertThat(originCountry.first()).isEqualTo("DE")
            assertThat(backdropPath).isEqualTo("/jWXrQstj7p3Wl5MfYWY6IHqRpDb.jpg")
            assertThat(genreIds.first()).isEqualTo(10763)
            assertThat(voteAverage).isEqualTo(7.131)
            assertThat(voteCount).isEqualTo(168)
        }

        with(tvShows.results.last()) {
            assertThat(id).isEqualTo(216578)
            assertThat(name).isEqualTo("Can't Buy Me Love")
            assertThat(originalName).isEqualTo("Can't Buy Me Love")
            assertThat(firstAirDate).isEqualTo("2023-10-16")
            assertThat(posterPath).isEqualTo("/jCGaLxSOANF44kAQqpA380Md1fq.jpg")
            assertThat(overview)
                .isEqualTo(
                    "When a young man gets caught up in a deadly plot against a rich " +
                        "woman, he pays a devastating cost to free her - creating " +
                        "a debt that binds them together.",
                )
            assertThat(originalLanguage).isEqualTo("tl")
            assertThat(originCountry.first()).isEqualTo("PH")
            assertThat(backdropPath).isEqualTo("/sFRlpUJEN0y6yV7QK7DQJochbin.jpg")
            assertThat(genreIds.first()).isEqualTo(18)
            assertThat(voteAverage).isEqualTo(4)
            assertThat(voteCount).isEqualTo(1)
        }

        val recordedRequest = mockWebServer.takeRequest()
        assertThat(mockWebServer.requestCount).isEqualTo(1)
        assertThat(recordedRequest.method).isEqualTo("GET")
        assertThat(recordedRequest.path).contains("/${Config.TMDB_API_V3}/tv/on_the_air")
    }

    @Test
    fun `tvSeriesListsApi popular(), returns list of tvShows when response is 200`() = runTest {
        mockWebServer.enqueueResponse("tv_shows-list-200.json", 200)

        val tvShows = tvShowsListsApi.popular(language = "en-US", page = 1)
        assertThat(tvShows).isNotNull()
        assertThat(tvShows.results.size).isEqualTo(20)

        with(tvShows.results.first()) {
            assertThat(id).isEqualTo(94722)
            assertThat(name).isEqualTo("Tagesschau")
            assertThat(originalName).isEqualTo("Tagesschau")
            assertThat(firstAirDate).isEqualTo("1952-12-26")
            assertThat(posterPath).isEqualTo("/7dFZJ2ZJJdcmkp05B9NWlqTJ5tq.jpg")
            assertThat(overview)
                .isEqualTo(
                    "German daily news program, the oldest still existing " +
                        "program on German television.",
                )
            assertThat(originalLanguage).isEqualTo("de")
            assertThat(originCountry.first()).isEqualTo("DE")
            assertThat(backdropPath).isEqualTo("/jWXrQstj7p3Wl5MfYWY6IHqRpDb.jpg")
            assertThat(genreIds.first()).isEqualTo(10763)
            assertThat(voteAverage).isEqualTo(7.131)
            assertThat(voteCount).isEqualTo(168)
        }

        with(tvShows.results.last()) {
            assertThat(id).isEqualTo(216578)
            assertThat(name).isEqualTo("Can't Buy Me Love")
            assertThat(originalName).isEqualTo("Can't Buy Me Love")
            assertThat(firstAirDate).isEqualTo("2023-10-16")
            assertThat(posterPath).isEqualTo("/jCGaLxSOANF44kAQqpA380Md1fq.jpg")
            assertThat(overview)
                .isEqualTo(
                    "When a young man gets caught up in a deadly plot against a rich " +
                        "woman, he pays a devastating cost to free her - creating a debt " +
                        "that binds them together.",
                )
            assertThat(originalLanguage).isEqualTo("tl")
            assertThat(originCountry.first()).isEqualTo("PH")
            assertThat(backdropPath).isEqualTo("/sFRlpUJEN0y6yV7QK7DQJochbin.jpg")
            assertThat(genreIds.first()).isEqualTo(18)
            assertThat(voteAverage).isEqualTo(4)
            assertThat(voteCount).isEqualTo(1)
        }

        val recordedRequest = mockWebServer.takeRequest()
        assertThat(mockWebServer.requestCount).isEqualTo(1)
        assertThat(recordedRequest.method).isEqualTo("GET")
        assertThat(recordedRequest.path).contains("/${Config.TMDB_API_V3}/tv/popular")
    }

    @Test
    fun `tvSeriesListsApi topRated(), returns list of tvShows when response is 200`() = runTest {
        mockWebServer.enqueueResponse("tv_shows-list-200.json", 200)

        val tvShows = tvShowsListsApi.topRated(language = "en-US", page = 1)
        assertThat(tvShows).isNotNull()
        assertThat(tvShows.results.size).isEqualTo(20)

        with(tvShows.results.first()) {
            assertThat(id).isEqualTo(94722)
            assertThat(name).isEqualTo("Tagesschau")
            assertThat(originalName).isEqualTo("Tagesschau")
            assertThat(firstAirDate).isEqualTo("1952-12-26")
            assertThat(posterPath).isEqualTo("/7dFZJ2ZJJdcmkp05B9NWlqTJ5tq.jpg")
            assertThat(overview)
                .isEqualTo(
                    "German daily news program, the oldest still existing program " +
                        "on German television.",
                )
            assertThat(originalLanguage).isEqualTo("de")
            assertThat(originCountry.first()).isEqualTo("DE")
            assertThat(backdropPath).isEqualTo("/jWXrQstj7p3Wl5MfYWY6IHqRpDb.jpg")
            assertThat(genreIds.first()).isEqualTo(10763)
            assertThat(voteAverage).isEqualTo(7.131)
            assertThat(voteCount).isEqualTo(168)
        }

        with(tvShows.results.last()) {
            assertThat(id).isEqualTo(216578)
            assertThat(name).isEqualTo("Can't Buy Me Love")
            assertThat(originalName).isEqualTo("Can't Buy Me Love")
            assertThat(firstAirDate).isEqualTo("2023-10-16")
            assertThat(posterPath).isEqualTo("/jCGaLxSOANF44kAQqpA380Md1fq.jpg")
            assertThat(overview)
                .isEqualTo(
                    "When a young man gets caught up in a deadly plot against a rich " +
                        "woman, he pays a devastating cost to free her - creating a debt " +
                        "that binds them together.",
                )
            assertThat(originalLanguage).isEqualTo("tl")
            assertThat(originCountry.first()).isEqualTo("PH")
            assertThat(backdropPath).isEqualTo("/sFRlpUJEN0y6yV7QK7DQJochbin.jpg")
            assertThat(genreIds.first()).isEqualTo(18)
            assertThat(voteAverage).isEqualTo(4)
            assertThat(voteCount).isEqualTo(1)
        }

        val recordedRequest = mockWebServer.takeRequest()
        assertThat(mockWebServer.requestCount).isEqualTo(1)
        assertThat(recordedRequest.method).isEqualTo("GET")
        assertThat(recordedRequest.path).contains("/${Config.TMDB_API_V3}/tv/top_rated")
    }
}
