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
import me.jerryokafor.core.model.PersonCast
import me.jerryokafor.core.model.PersonCredit
import me.jerryokafor.core.model.PersonCrew
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class NetworkPersonCast(
    val adult: Boolean,
    val id: Long,
    @SerializedName("title", alternate = ["original_title"])
    val title: String? = null,
    @SerializedName("name", alternate = ["original_name"])
    val name: String? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    val popularity: Double = 0.0,
    val character: String,
    @SerializedName("credit_id")
    val creditId: String,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("media_type")
    val mediaType: String,
)

data class NetworkPersonCrew(
    val adult: Boolean,
    val id: Long,
    @SerializedName("title", alternate = ["original_title"])
    val title: String? = null,
    @SerializedName("name", alternate = ["original_name"])
    val name: String? = null,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    @SerializedName("backdrop_path")
    val backdropPath: String? = null,
    val popularity: Double = 0.0,
    @SerializedName("credit_id")
    val creditId: String,
    val department: String,
    val job: String,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("media_type")
    val mediaType: String,
)

fun NetworkPersonCast.asDomainObject(): PersonCast = PersonCast(
    adult = adult,
    character = character,
    creditId = creditId,
    id = id,
    title = title,
    name = name,
    popularity = popularity,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = runCatching {
        releaseDate?.let {
            LocalDate.parse(
                it,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            )
        }
    }.getOrNull() ?: LocalDate.now(),
    mediaType = this.mediaType,
)

fun NetworkPersonCrew.asDomainObject(): PersonCrew = PersonCrew(
    adult = adult,
    creditId = creditId,
    department = department,
    id = id,
    job = job,
    title = title,
    name = name,
    popularity = popularity,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = runCatching {
        releaseDate?.let {
            LocalDate.parse(
                it,
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            )
        }
    }.getOrNull() ?: LocalDate.now(),
    mediaType = this.mediaType,
)

data class NetworkPersonCredit(
    val id: Int,
    val cast: List<NetworkPersonCast> = listOf(),
    val crew: List<NetworkPersonCrew> = listOf(),
)

fun NetworkPersonCredit.asDomainObject(): PersonCredit = PersonCredit(
    id = id,
    cast = cast.map { it.asDomainObject() },
    crew = crew.map { it.asDomainObject() },
)
