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

import me.jerryokafor.core.model.Person
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkPerson
import me.jerryokafor.ihenkiri.core.network.model.response.NetworkPersonMovie
import me.jerryokafor.ihenkiri.core.network.model.response.PagedNetworkResponse
import me.jerryokafor.ihenkiri.core.network.model.response.toDomainModel

object PeopleListTestData {
    fun testPersons(): List<Person> = testNetworkPersons().results.map { it.toDomainModel() }

    fun testNetworkPersons(): PagedNetworkResponse<NetworkPerson> =
        PagedNetworkResponse(
            page = 1,
            results =
                arrayListOf(
                    NetworkPerson(
                        id = 976,
                        name = "Jason Statham",
                        popularity = 199.055,
                        profilePath = "/whNwkEQYWLFJA8ij0WyOOAD5xhQ.jpg",
                        knownFor =
                            listOf(
                                NetworkPersonMovie(id = 107, title = "Snatch"),
                                NetworkPersonMovie(id = 345940, title = "The Meg"),
                                NetworkPersonMovie(id = 4108, title = "The Transporte"),
                            ),
                    ),
                    NetworkPerson(
                        id = 3194176,
                        name = "Angeli Khang",
                        popularity = 190.975,
                        profilePath = "/7vrTWF8PxQogF6o9ORZprYQoDOr.jpg",
                        knownFor =
                            listOf(
                                NetworkPersonMovie(id = 931599, title = "Silip Sa Apoy"),
                                NetworkPersonMovie(id = 1029446, title = "Selina's Gold"),
                                NetworkPersonMovie(id = 893694, title = "Eva"),
                            ),
                    ),
                    NetworkPerson(
                        id = 974169,
                        name = "Jenna Ortega",
                        popularity = 172.889,
                        profilePath = "/q1NRzyZQlYkxLY07GO9NVPkQnu8.jpg",
                        knownFor =
                            listOf(
                                NetworkPersonMovie(id = 119051, title = "Wednesday"),
                                NetworkPersonMovie(id = 646385, title = "Scream"),
                                NetworkPersonMovie(id = 760104, title = "X"),
                            ),
                    ),
                    NetworkPerson(
                        id = 3234630,
                        name = "Sangeeth Shobhan",
                        popularity = 166.186,
                        profilePath = "/7Vox31bH7XmgPNJzMKGa4uGyjW8.jpg",
                        knownFor =
                            listOf(
                                NetworkPersonMovie(id = 1187075, title = "MAD"),
                                NetworkPersonMovie(id = 138179, title = "Oka Chinna Family Story"),
                                NetworkPersonMovie(id = 1119091, title = "Prema Vimanam"),
                            ),
                    ),
                    NetworkPerson(
                        id = 27972,
                        name = "Josh Hutcherson",
                        popularity = 149.784,
                        profilePath = "/npowygg8rH7uJ4v7rAoDMsHBhNq.jpg",
                        knownFor =
                            listOf(
                                NetworkPersonMovie(id = 70160, title = "The Hunger Games"),
                                NetworkPersonMovie(id = 101299, title = "The Hunger Games: Catching Fire"),
                                NetworkPersonMovie(
                                    id = 131631,
                                    title = "The Hunger Games: Mockingjay - Part 1",
                                ),
                            ),
                    ),
                    NetworkPerson(
                        id = 224513,
                        name = "Florence Pugh",
                        popularity = 115.26,
                        profilePath = "/fhEsn35uAwUZy37RKpLdwWyx2y5.jpg",
                        knownFor =
                            listOf(
                                NetworkPersonMovie(id = 530385, title = "Midsommar"),
                                NetworkPersonMovie(id = 497698, title = "Black Widow"),
                                NetworkPersonMovie(id = 331482, title = "Little Women"),
                            ),
                    ),
                    NetworkPerson(
                        id = 18897,
                        name = "Jackie Chan",
                        popularity = 129.907,
                        profilePath = "/nraZoTzwJQPHspAVsKfgl3RXKKa.jpg",
                        knownFor =
                            listOf(
                                NetworkPersonMovie(id = 2109, title = "Rush Hour"),
                                NetworkPersonMovie(id = 5175, title = "Rush Hour 2"),
                                NetworkPersonMovie(id = 5174, title = "Rush Hour 3"),
                            ),
                    ),
                    NetworkPerson(
                        id = 2359226,
                        name = "Aya Asahina",
                        popularity = 117.261,
                        profilePath = "/dyqW1H1P56oEH2CmqfLvR39jfGA.jpg",
                        knownFor =
                            listOf(
                                NetworkPersonMovie(id = 110316, title = "Alice in Borderland"),
                                NetworkPersonMovie(id = 677602, title = "Grand Blue"),
                                NetworkPersonMovie(id = 91414, title = "Runway 24"),
                            ),
                    ),
                    NetworkPerson(
                        id = 1373737,
                        name = "Florence Pugh",
                        popularity = 115.26,
                        profilePath = "/fhEsn35uAwUZy37RKpLdwWyx2y5.jpg",
                        knownFor =
                            listOf(
                                NetworkPersonMovie(id = 530385, title = "Midsommar"),
                                NetworkPersonMovie(id = 497698, title = "Black Widow"),
                                NetworkPersonMovie(id = 331482, title = "Little Women"),
                            ),
                    ),
                ),
            totalPages = 1,
            totalResults = 9,
        )
}
