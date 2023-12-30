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

import me.jerryokafor.core.model.PersonDetails
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkPersonCast
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkPersonCredit
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkPersonCrew
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkPersonDetails
import me.jerryokafor.ihenkiri.core.network.model.response.toDomainModel

object PeopleDetailsTestData {
    fun testNetworkPersonDetails(): NetworkPersonDetails = NetworkPersonDetails(
        id = 16483L,
        name = "Sylvester Stallone",
        adult = false,
        alsoKnownAs = listOf(
            "Sly Stallone",
            "Italian Stallion",
            "Michael Sylvester Gardenzio Stallone",
            "Sylvester Enzio Stallone",
        ),
        biography = "Sylvester Stallone (born Michael Sylvester Gardenzio Stallone, " +
            "July 6, 1946) is an American actor and filmmaker.",
        birthday = "1946-07-06",
        deathday = null,
        gender = 2,
        homepage = "http://www.sylvesterstallone.com",
        imdbId = "nm0000230",
        knownForDepartment = "Acting",
        placeOfBirth = "New York City, New York, USA",
        popularity = 167.141,
        profilePath = "/mJHmLVLctqZd30CSEUKuVFEPrnt.jpg",
        credits = NetworkPersonCredit(
            id = 0,
            cast = listOf(
                NetworkPersonCast(
                    adult = false,
                    id = 1366,
                    title = "Rocky",
                    name = null,
                    posterPath = "/cqxg1CihGR5ge0i1wYXr4Rdeppu.jpg",
                    backdropPath = "/kK9v1wclQxug6ZUJucD4DTaHgVF.jpg",
                    popularity = 0.0,
                    character = "Robert 'Rocky' Balboa",
                    creditId = "52fe42efc3a36847f802e019",
                    releaseDate = "1976-11-21",
                    mediaType = "movie",
                ),
                NetworkPersonCast(
                    adult = false,
                    id = 2221L,
                    title = null,
                    name = "The View",
                    posterPath = "/zn5ZtKXYo8XOoXUgtQxw7q2CjVt.jpg",
                    backdropPath = "/6LFqVT02WaNq3q3FlU7dAmt6X5A.jpg",
                    popularity = 0.0,
                    character = "Self",
                    creditId = "52572878760ee3776a296bbf",
                    releaseDate = "1997-08-11",
                    mediaType = "tv",
                ),
            ),
            crew = listOf(
                NetworkPersonCrew(
                    adult = false,
                    id = 0L,
                    title = null,
                    name = "Tulsa King",
                    posterPath = "/fwTv3RPRAIy0maOMns5eYRRwnDk.jpg",
                    backdropPath = "/mNHRGO1gFpR2CYZdANe72kcKq7G.jpg",
                    popularity = 0.0,
                    creditId = "61b4770a7d5f4b0091225baf",
                    job = "Executive Producer",
                    department = "Production",
                    releaseDate = "2022-11-13",
                    mediaType = "",
                ),
                NetworkPersonCrew(
                    adult = false,
                    id = 1366,
                    title = "Rocky",
                    name = null,
                    posterPath = "/cqxg1CihGR5ge0i1wYXr4Rdeppu.jpg",
                    backdropPath = "/kK9v1wclQxug6ZUJucD4DTaHgVF.jpg",
                    popularity = 62.657,
                    creditId = "57994b84c3a3687e7d004926",
                    job = "Choreographer",
                    department = "Crew",
                    releaseDate = "1976-11-21",
                    mediaType = "movie",
                ),
            ),
        ),
    )

    fun testPersonDetails(): PersonDetails = testNetworkPersonDetails().toDomainModel()
}
