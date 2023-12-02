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
import me.jerryokafor.core.model.Genre
import me.jerryokafor.core.model.MovieDetails
import me.jerryokafor.core.model.SpokenLanguage

data class NetworkGenre(
    @SerializedName("id")
    val id: Int = -1,
    @SerializedName("name")
    val name: String = "",
)

data class NetworkSpokenLanguage(
    @SerializedName("english_name")
    val englishName: String = "",
    @SerializedName("iso_639_1")
    val iso6391: String = "",
    @SerializedName("name")
    val name: String = "",
)

data class NetworkMovieDetails(
    val adult: Boolean = false,
    @SerializedName("backdrop_path")
    val backdropPath: String = "",
    val budget: Long = 0,
    val genres: List<NetworkGenre> = arrayListOf(),
    val homepage: String = "",
    val id: Long,
    @SerializedName("imdb_id")
    val imdbId: String = "",
    @SerializedName("original_language")
    val originalLanguage: String = "",
    @SerializedName("original_title")
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    @SerializedName("poster_path")
    val posterPath: String = "",
    val releaseDate: String = "",
    val revenue: Long = 0,
    val runtime: Int = 0,
    @SerializedName("spoken_languages")
    val spokenLanguages: List<NetworkSpokenLanguage> = listOf(),
    val status: String = "",
    val tagline: String = "",
    val title: String = "",
    val video: Boolean = false,
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    val voteCount: Long = 0,
)

fun NetworkMovieDetails.asDomainObject(): MovieDetails = MovieDetails(
    id = this.id,
    adult = this.adult,
    backdropPath = this.backdropPath,
    budget = this.budget,
    genres = this.genres.map { Genre(id = it.id, name = it.name) },
    homepage = this.homepage,
    imdbId = this.imdbId,
    originalLanguage = this.originalLanguage,
    originalTitle = this.originalTitle,
    overview = this.overview,
    popularity = this.popularity,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    revenue = this.revenue,
    runtime = this.runtime,
    spokenLanguages =
        this.spokenLanguages.map {
            SpokenLanguage(
                englishName = it.englishName,
                iso6391 = it.iso6391,
                name = it.name,
            )
        },
    status = status,
    tagline = tagline,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
)
