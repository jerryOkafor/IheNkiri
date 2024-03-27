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

package com.jerryokafor.feature.media.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerryokafor.feature.media.navigation.MovieDetailsArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.jerryokafor.core.common.outcome.Failure
import me.jerryokafor.core.common.outcome.Success
import me.jerryokafor.core.data.repository.MovieDetailsRepository
import me.jerryokafor.core.model.Video
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class MediaScreenVIewModel
    @Inject
    constructor(
        savedStateHandle: SavedStateHandle,
        private val movieDetailsRepository: MovieDetailsRepository,
    ) : ViewModel() {
        private val movieId = MovieDetailsArg(savedStateHandle).movieId

        @Suppress("MagicNumber")
        val moviesVideoUiState: StateFlow<MoviesVideoUiState> = movieId.flatMapLatest {
            movieDetailsRepository.movieVideos(it)
        }.map {
            when (it) {
                is Failure -> MoviesVideoUiState.LoadFailed(it.errorResponse)
                is Success -> MoviesVideoUiState.Success(
                    listOf(
                        Video(
                            id = "",
                            iso31661 = "",
                            iso6391 = "",
                            key = "282875052",
                            name = "Fight Club - Theatrical Trailer Remastered in HD",
                            official = false,
                            publishedAt = "2023-12-13T15:43:38.000Z",
                            site = "Vimeo",
                            size = 0,
                            type = "",
                        ),
                    ).plus(
                        it.data,
                    ),
                )
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MoviesVideoUiState.Loading,
        )
    }

sealed interface MoviesVideoUiState {
    data object Loading : MoviesVideoUiState

    data class Success(val videos: List<Video>) : MoviesVideoUiState

    data class LoadFailed(val message: String) : MoviesVideoUiState
}
