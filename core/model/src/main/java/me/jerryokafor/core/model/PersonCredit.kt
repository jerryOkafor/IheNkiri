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
    val posterPath: String?,
    val popularity: Double,
    val character: String,
    val creditId: String,
    val gender: Int,
    val knownForDepartment: String? = null,
    val releaseDate: LocalDate,
)

data class PersonCrew(
    val adult: Boolean,
    val id: Long,
    val title: String? = null,
    val posterPath: String?,
    val popularity: Double,
    val creditId: String,
    val gender: Int,
    val job: String,
    val department: String? = null,
    val releaseDate: LocalDate,
)

data class PersonCredit(
    val id: Int,
    val cast: List<Cast> = listOf(),
    val crew: List<Crew> = listOf(),
)
