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

package me.jerryokafor.core.model

import java.time.LocalDate

data class PersonCast(
    val adult: Boolean,
    val id: Long,
    val title: String? = null,
    val name: String? = null,
    val posterPath: String?,
    val backdropPath: String?,
    val popularity: Double,
    val character: String,
    val creditId: String,
    val releaseDate: LocalDate,
    val mediaType: String,
)

data class PersonCrew(
    val adult: Boolean,
    val id: Long,
    val title: String? = null,
    val name: String? = null,
    val posterPath: String?,
    val backdropPath: String?,
    val popularity: Double,
    val creditId: String,
    val job: String,
    val department: String,
    val releaseDate: LocalDate,
    val mediaType: String,
)

data class PersonCredit(
    val id: Int,
    val cast: List<PersonCast> = listOf(),
    val crew: List<PersonCrew> = listOf(),
)

data class Timeline(
    val title: String,
    val description: String,
    val date: LocalDate,
    val type: Type,
    val department: Department,
) {
    enum class Type(val type: String) {
        ALL(type = "All"),
        MOVIE(type = "Movie"),
        TV_SHOW(type = "TV Show"),
        ;

        override fun toString(): String = this.type
    }

    enum class Department(val department: String) {
        ALL(department = "All"),
        ACTING(department = "Acting"),
        WRITING(department = "Writing"),
        PRODUCTION(department = "Production"),
        DIRECTING("Directing"),
        CREATOR(department = "Creator"),
        CREW(department = "Crew"),
        ;

        override fun toString(): String = this.department
    }
}

@Suppress("CyclomaticComplexMethod")
fun PersonCredit.toTimeline(): Map<Int, List<Timeline>> {
    val castTimeline = cast
        .filterNot { it.title.isNullOrBlank() && it.name.isNullOrBlank() && it.character.isBlank() }
        .map {
            val type = when (it.mediaType) {
                "tv" -> Timeline.Type.TV_SHOW
                "movie" -> Timeline.Type.MOVIE
                else -> throw IllegalArgumentException("Unknown media type: ${it.mediaType}")
            }
            Timeline(
                title = (if (type == Timeline.Type.MOVIE) it.title else it.name) ?: "Unknown",
                description = "... as ${it.character}",
                date = it.releaseDate,
                type = type,
                department = Timeline.Department.ACTING,
            )
        }
    val crewTimeline = crew
        .filterNot { it.title.isNullOrBlank() && it.name.isNullOrBlank() }
        .map {
            val type = when (it.mediaType) {
                "tv" -> Timeline.Type.TV_SHOW
                "movie" -> Timeline.Type.MOVIE
                else -> throw IllegalArgumentException("Unknown media type: ${it.mediaType}")
            }
            Timeline(
                title = (if (type == Timeline.Type.MOVIE) it.title else it.name) ?: "Unknown",
                description = "... ${it.job}",
                date = it.releaseDate,
                type = type,
                department = when (it.department.trim()) {
                    "Directing" -> Timeline.Department.DIRECTING
                    "Acting" -> Timeline.Department.ACTING
                    "Writing" -> Timeline.Department.WRITING
                    "Production" -> Timeline.Department.PRODUCTION
                    "Creator" -> Timeline.Department.CREATOR
                    "Crew" -> Timeline.Department.CREW
                    else -> Timeline.Department.ACTING
                },
            )
        }
    return castTimeline.plus(crewTimeline).sortedByDescending { it.date }
        .groupBy { it.date.year }
}
