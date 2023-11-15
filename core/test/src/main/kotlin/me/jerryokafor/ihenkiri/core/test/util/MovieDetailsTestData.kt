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

package me.jerryokafor.ihenkiri.core.test.util

import me.jerryokafor.core.model.MovieCredit
import me.jerryokafor.core.model.MovieDetails
import me.jerryokafor.core.model.Video
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkCast
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkCrew
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkGenre
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkMovieCredit
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkMovieDetails
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkProductionCompany
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkProductionCountry
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkSpokenLanguage
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkVideo
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkVideos
import me.jerryokafor.ihenkiri.core.network.model.response.asDomainObject

object MovieDetailsTestData {
    fun testNetworkMovieDetails(movieId: Long): NetworkMovieDetails =
        NetworkMovieDetails(
            adult = false,
            backdropPath = "/hZkgoQYus5vegHoetLkCJzb17zJ.jpg",
            belongsToCollection = null,
            budget = 63000000,
            genres =
                listOf(
                    NetworkGenre(id = 18, name = "Drama"),
                    NetworkGenre(id = 53, name = "Thriller"),
                    NetworkGenre(id = 35, name = "Comedy"),
                ),
            homepage = "http://www.foxmovies.com/movies/fight-club",
            id = movieId,
            imdbId = "tt0137523",
            originalLanguage = "en",
            originalTitle = "Fight Club",
            overview =
                """
                A ticking-time-bomb insomniac and a slippery soap salesman channel primal male
                 aggression into a shocking new form of therapy. Their concept catches on, with 
                 underground \"fight clubs\" forming in every town, until an eccentric gets in the 
                 way and ignites an out-of-control spiral toward oblivion.
                """.trimIndent(),
            popularity = 61.416,
            posterPath = "/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg",
            productionCompanies =
                listOf(
                    NetworkProductionCompany(
                        id = 508,
                        logoPath = "/7cxRWzi4LsVm4Utfpr1hfARNurT.png",
                        name = "Regency Enterprises",
                        originCountry = "US",
                    ),
                    NetworkProductionCompany(
                        id = 711,
                        logoPath = "/tEiIH5QesdheJmDAqQwvtN60727.png",
                        name = "Fox 2000 Pictures",
                        originCountry = "US",
                    ),
                ),
            productionCountries =
                listOf(
                    NetworkProductionCountry(
                        iso31661 = "US",
                        "United States of America",
                    ),
                ),
            releaseDate = "1999-10-15",
            revenue = 100853753,
            runtime = 139,
            spokenLanguages =
                listOf(
                    NetworkSpokenLanguage(
                        englishName = "English",
                        iso6391 = "en",
                        name = "English",
                    ),
                ),
            status = "Released",
            tagline = "Mischief. Mayhem. Soap.",
            title = "Fight Club",
            video = false,
            voteAverage = 8.433,
            voteCount = 26280,
        )

    fun testMovieDetails(movieId: Long): MovieDetails = testNetworkMovieDetails(movieId = movieId).asDomainObject()

    @Suppress("UnusedPrivateMember")
    fun testNetworkMovieCredit(movieId: Long): NetworkMovieCredit =
        NetworkMovieCredit(
            id = 7296,
            cast =
                listOf(
                    NetworkCast(
                        adult = false,
                        castId = 4,
                        character = "The Narrator",
                        creditId = "52fe4250c3a36847f80149f3",
                        gender = 2,
                        id = 819,
                        knownForDepartment = "Acting",
                        name = "Edward Norton",
                        order = 0,
                        originalName = "Edward Norton",
                        popularity = 26.99,
                        profilePath = "/huV2cdcolEUwJy37QvH914vup7d.jpg",
                    ),
                    NetworkCast(
                        adult = false,
                        castId = 0,
                        character = "Tyler Durden",
                        creditId = "52fe4250c3a36847f80149f7",
                        gender = 1,
                        id = 1283,
                        knownForDepartment = "Acting",
                        name = "Helena Bonham Carter",
                        order = 2,
                        originalName = "Helena Bonham Carter",
                        popularity = 22.112,
                        profilePath = "/DDeITcCpnBd0CkAIRPhggy9bt5.jpg",
                    ),
                ),
            crew =
                listOf(
                    NetworkCrew(
                        adult = false,
                        creditId = "55731b8192514111610027d7",
                        department = "Production",
                        gender = 2,
                        id = 376,
                        job = "Executive Producer",
                        knownForDepartment = "Production",
                        name = "Arnon Milchan",
                        originalName = "Arnon Milchan",
                        popularity = 2.931,
                        profilePath = "/b2hBExX4NnczNAnLuTBF4kmNhZm.jpg",
                    ),
                    NetworkCrew(
                        adult = false,
                        creditId = "5894c4eac3a3685ec6000218",
                        department = "Costume & Make-Up",
                        gender = 2,
                        id = 605,
                        job = "Costume Design",
                        knownForDepartment = "Costume & Make-Up",
                        name = "Michael Kaplan",
                        originalName = "Michael Kaplan",
                        popularity = 4.294,
                        profilePath = "/bNarnI5K4XYIKaHsX6HAitllyQr.jpg",
                    ),
                ),
        )

    fun testMovieCredit(movieId: Long): MovieCredit = testNetworkMovieCredit(movieId).asDomainObject()

    @Suppress("UnusedPrivateMember")
    fun testNetworkMovieVideos(movieId: Long): NetworkVideos =
        NetworkVideos(
            id = 550,
            results =
                listOf(
                    NetworkVideo(
                        id = "639d5326be6d88007f170f44",
                        iso31661 = "en",
                        iso6391 = "US",
                        key = "O-b2VfmmbyA",
                        name = "Fight Club (1999) Trailer - Starring Brad Pitt, Edward Norton, Helena Bonham Carter",
                        official = false,
                        publishedAt = "2016-03-05T02:03:14.000Z",
                        site = "YouTube",
                        size = 720,
                        type = "Trailer",
                    ),
                    NetworkVideo(
                        id = "5c9294240e0a267cd516835f",
                        iso31661 = "en",
                        iso6391 = "US",
                        key = "BdJKm16Co6M",
                        name = "#TBT Trailer",
                        official = false,
                        publishedAt = "2014-10-02T19:20:22.000Z",
                        site = "YouTube",
                        size = 1080,
                        type = "Trailer",
                    ),
                ),
        )

    fun testMovieVideos(movieId: Long): List<Video> = testNetworkMovieVideos(movieId).results.map { it.asDomainObject() }
}
