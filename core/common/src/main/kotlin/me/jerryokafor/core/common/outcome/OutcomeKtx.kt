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

package me.jerryokafor.core.common.outcome

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * Filters and maps a [Flow<Outcome<T>>] to T value, ignoring any failures.
 */
fun <T> Flow<Outcome<T>>.success(): Flow<T> = this
    .filter { it is Success }
    .map { (it as Success).invoke() }

/**
 * Maps a [Flow<Outcome<T>>] to a [Flow<OutCome<R>>]. Allows any successful Outcome to be mutated.
 */
inline fun <T, reified R> Flow<Outcome<T>>.mapSuccess(
    crossinline onSuccess: (T) -> R,
): Flow<Outcome<R>> = this
    .map {
        when (it) {
            is Success -> Success(onSuccess(it()))
            is Failure -> it
        }
    }

/**
 * Maps a [Flow<Outcome<T>>]  to a [Flow<Outcome<R>>]. Successful response can be mapped to any [Outcome]
 */
inline fun <T, R> Flow<Outcome<T>>.mapOutcome(
    crossinline onSuccess: (Success<T>) -> Outcome<R>,
): Flow<Outcome<R>> = this
    .map {
        when (it) {
            is Success -> onSuccess(it)
            is Failure -> it
        }
    }

inline fun <T> Flow<Outcome<T>>.onSuccess(
    crossinline action: suspend (data: T) -> Unit,
): Flow<Outcome<T>> = this.onEach { if (it is Success) action(it.data) }

inline fun <T> Flow<Outcome<T>>.onFailure(
    crossinline action: suspend (
        errorResponse: String,
        errorCode: Int,
        throwable: Throwable?,
    ) -> Unit,
): Flow<Outcome<T>> =
    this.onEach { if (it is Failure) action(it.errorResponse, it.errorCode, it.throwable) }
