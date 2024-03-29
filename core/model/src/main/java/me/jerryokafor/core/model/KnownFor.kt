/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 IheNkiri Project
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

data class KnownFor(
    val title: String? = null,
    val popularity: Double,
    val posterPath: String?,
)

data class Crew(
    val adult: Boolean,
    val creditId: String,
    val department: String,
    val gender: Int,
    val id: Int,
    val job: String,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val title: String? = null,
    val originalName: String? = null,
    val popularity: Double,
    val profilePath: String?,
    val posterPath: String?,
)

fun Crew.names(): List<String> = name?.split(" ")?.let {
    if (it.size <= 1) it.plus(" ") else it
} ?: emptyList()

data class Cast(
    val adult: Boolean,
    val castId: Int,
    val character: String,
    val creditId: String,
    val gender: Int,
    val id: Int,
    val knownForDepartment: String? = null,
    val name: String? = null,
    val title: String? = null,
    val order: Int,
    val originalName: String? = null,
    val popularity: Double,
    val department: String?,
    val profilePath: String?,
    val posterPath: String?,
)

fun Cast.names(): List<String> = name?.split(" ")?.let {
    if (it.size <= 1) it.plus("") else it
} ?: emptyList()

data class MovieCredit(
    val id: Int,
    val cast: List<Cast> = listOf(),
    val crew: List<Crew> = listOf(),
)
