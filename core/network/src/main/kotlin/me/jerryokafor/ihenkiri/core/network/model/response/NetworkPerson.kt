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

package me.jerryokafor.ihenkiri.core.network.model.response

import com.google.gson.annotations.SerializedName
import me.jerryokafor.core.model.Person
import me.jerryokafor.core.model.PersonMovie

data class NetworkPerson(
    val id: Long,
    val name: String,
    val popularity: Double,
    @SerializedName("profile_path")
    val profilePath: String?,
    val knownFor: List<NetworkPersonMovie>,
)

data class NetworkPersonMovie(
    val id: Long,
    val title: String?,
)

fun NetworkPersonMovie.toDomainModel(): PersonMovie = PersonMovie(
    id = this.id,
    title = this.title!!,
)

fun NetworkPerson.toDomainModel(): Person = Person(
    id = this.id,
    name = this.name,
    popularity = this.popularity,
    profilePath = this.profilePath,
    knownFor = this.knownFor.filter { it.title != null }.map { it.toDomainModel() },
)
