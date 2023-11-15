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

import me.jerryokafor.ihenkiri.core.network.model.response.NetworkMovie
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkMovieList

fun testMoviesListResponse() = NetworkMovieList(page = 1, results = testMoviesResponse(), totalResults = 7, totalPages = 1)

fun testMoviesResponse() =
    arrayListOf(
        NetworkMovie(
            adult = false,
            backdropPath = "/2vFuG6bWGyQUzYS9d69E5l85nIz.jpg",
            genreIds =
                arrayListOf(
                    28,
                    12,
                    878,
                ),
            id = 66753811,
            originalLanguage = "en",
            originalTitle = "Transformers: Rise of the Beasts",
            overview =
                """
                When a new threat capable of destroying the entire planet emerges, Optimus Prime and 
                the Autobots must team up with a powerful faction known as the Maximals. With the fate 
                of humanity hanging in the balance, humans Noah and Elena will do whatever it takes 
                to help the Transformers as they engage in the ultimate battle to save Earth.
                """.trimIndent(),
            popularity = 2160.316,
            posterPath = "/gPbM0MK8CP8A174rmUwGsADNYKD.jpg",
            title = "Transformers: Rise of the Beasts",
            video = false,
            releaseDate = "2023-06-06",
            voteAverage = 7.5,
            voteCount = 2747,
        ),
        NetworkMovie(
            adult = false,
            backdropPath = "/yF1eOkaYvwiORauRCPWznV9xVvi.jpg",
            genreIds =
                arrayListOf(
                    28,
                    12,
                    878,
                ),
            id = 298618,
            originalLanguage = "en",
            originalTitle = "The Flash",
            overview =
                """
                When his attempt to save his family inadvertently alters the future, 
                Barry Allen becomes trapped in a reality in which General Zod has returned and 
                there are no Super Heroes to turn to. In order to save the world that he is in 
                and return to the future that he knows, Barry's only hope is to race for his life. 
                But will making the ultimate sacrifice be enough to reset the universe?
                """.trimIndent(),
            popularity = 2108.713,
            posterPath = "/rktDFPbfHfUbArZ6OOOKsXcv0Bm.jpg",
            title = "The Flash",
            video = false,
            releaseDate = "2023-06-13",
            voteAverage = 7.0,
            voteCount = 2305,
        ),
        NetworkMovie(
            adult = false,
            backdropPath = "/rRcNmiH55Tz0ugUsDUGmj8Bsa4V.jpg",
            genreIds =
                arrayListOf(
                    35,
                    10749,
                ),
            id = 884605,
            originalLanguage = "en",
            originalTitle = "No Hard Feelings",
            overview =
                """
                On the brink of losing her childhood home, Maddie discovers an intriguing job listing: 
                wealthy helicopter parents looking for someone to “date” their introverted 19-year-old 
                son, Percy, before he leaves for college. To her surprise, Maddie soon discovers the 
                awkward Percy is no sure thing.
                """.trimIndent(),
            popularity = 1978.358,
            posterPath = "/4K7gQjD19CDEPd7A9KZwr2D9Nco.jpg",
            title = "No Hard Feelings",
            video = false,
            releaseDate = "",
            voteAverage = 0.0,
            voteCount = 1,
        ),
        NetworkMovie(
            adult = false,
            backdropPath = "/nHf61UzkfFno5X1ofIhugCPus2R.jpg",
            genreIds =
                arrayListOf(
                    35,
                    12,
                    14,
                ),
            id = 346698,
            originalLanguage = "en",
            originalTitle = "Barbie",
            overview =
                """
                Barbie and Ken are having the time of their lives in the colorful and seemingly perfect 
                world of Barbie Land. However, when they get a chance to go to the real world, they soon 
                discover the joys and perils of living among humans.
                """.trimIndent(),
            popularity = 1976.513,
            posterPath = "/iuFNMS8U5cb6xfzi51Dbkovj7vM.jpg",
            title = "Barbie",
            video = false,
            releaseDate = "2023-07-19",
            voteAverage = 7.5,
            voteCount = 3013,
        ),
        NetworkMovie(
            adult = false,
            backdropPath = "/zN41DPmPhwmgJjHwezALdrdvD0h.jpg",
            genreIds =
                arrayListOf(
                    28,
                    878,
                    27,
                ),
            id = 615656,
            originalLanguage = "en",
            originalTitle = "Meg 2: The Trench",
            overview =
                """
                An exploratory dive into the deepest depths of the ocean of a daring research team 
                spirals into chaos when a malevolent mining operation threatens their mission and 
                forces them into a high-stakes battle for survival.
                """.trimIndent(),
            popularity = 1730.76,
            posterPath = "/4m1Au3YkjqsxF8iwQy0fPYSxE0h.jpg",
            title = "Meg 2: The Trench",
            video = false,
            releaseDate = "",
            voteAverage = 7.0,
            voteCount = 446,
        ),
        NetworkMovie(
            adult = false,
            backdropPath = "/jZIYaISP3GBSrVOPfrp98AMa8Ng.jpg",
            genreIds =
                arrayListOf(
                    16,
                    35,
                    10751,
                    14,
                    10749,
                ),
            id = 976573,
            originalLanguage = "en",
            originalTitle = "Elemental",
            overview =
                """
                In a city where fire, water, land and air residents live together, 
                a fiery young woman and a go-with-the-flow guy will discover something elemental: 
                how much they have in common.
                """.trimIndent(),
            popularity = 1711.569,
            posterPath = "",
            title = "Elemental",
            video = false,
            releaseDate = "2023-06-15",
            voteAverage = 7.7,
            voteCount = 877,
        ),
        NetworkMovie(
            adult = false,
            backdropPath = "/bz66a19bR6BKsbY8gSZCM4etJiK.jpg",
            genreIds =
                arrayListOf(
                    28,
                    27,
                    53,
                ),
            id = 1006462,
            originalLanguage = "en",
            originalTitle = "The Flood",
            overview =
                """
                A horde of giant hungry alligators is unleashed on a group of in-transit prisoners and 
                their guards after a massive hurricane floods Louisiana.
                """.trimIndent(),
            popularity = 1700.342,
            posterPath = "/mvjqqklMpHwOxc40rn7dMhGT0Fc.jpg",
            title = "The Flood",
            video = false,
            releaseDate = "2023-07-14",
            voteAverage = 6.8,
            voteCount = 115,
        ),
    )
