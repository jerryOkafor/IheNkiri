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

@file:Suppress("MagicNumber")
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
import me.jerryokafor.core.model.KnownFor
import me.jerryokafor.core.model.PersonDetails
import me.jerryokafor.core.model.toTimeline

data class NetworkPersonDetails(
    val id: Long,
    val name: String,
    val adult: Boolean,
    @SerializedName("also_known_as")
    val alsoKnownAs: List<String> = listOf(),
    val biography: String = "",
    val birthday: String? = null,
    val deathday: String? = null,
    val gender: Int = 0,
    val homepage: String? = null,
    @SerializedName("imdb_id")
    val imdbId: String,
    @SerializedName("known_for_department")
    val knownForDepartment: String? = null,
    @SerializedName("place_of_birth")
    val placeOfBirth: String? = null,
    val popularity: Double = 0.0,
    @SerializedName("profile_path")
    val profilePath: String? = null,
    @SerializedName("combined_credits")
    val credits: NetworkPersonCredit,
)

fun NetworkPersonDetails.toDomainModel(): PersonDetails = PersonDetails(
    id = this.id,
    name = this.name,
    adult = this.adult,
    alsoKnownAs = this.alsoKnownAs,
    biography = this.biography,
    birthday = this.birthday,
    deathday = this.deathday,
    gender = when (this.gender) {
        1 -> "Female"
        2 -> "Male"
        3 -> "Non-binary"
        else -> "Not specified"
    },
    homepage = this.homepage,
    imdbId = this.imdbId,
    knownForDepartment = this.knownForDepartment,
    placeOfBirth = this.placeOfBirth,
    popularity = this.popularity,
    profilePath = this.profilePath,
    credits = this.credits.asDomainObject(),
    totalCredits = this.credits.cast.count() + this.credits.crew.count(),
    knownFor = (
        credits.cast.map {
            KnownFor(
                title = it.title,
                popularity = it.popularity,
                posterPath = it.posterPath,
            )
        }.sortedBy { it.popularity }.take(5) +
            credits.crew.map {
                KnownFor(
                    title = it.title,
                    popularity = it.popularity,
                    posterPath = it.posterPath,
                )
            }.sortedBy { it.popularity }.take(5)
    ).sortedBy { it.popularity },
    timeline = credits.asDomainObject().toTimeline(),
)
