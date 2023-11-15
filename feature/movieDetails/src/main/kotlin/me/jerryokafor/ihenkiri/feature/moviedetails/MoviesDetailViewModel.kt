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

package me.jerryokafor.ihenkiri.feature.moviedetails

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.jerryokafor.core.common.outcome.onFailure
import me.jerryokafor.core.common.outcome.onSuccess
import me.jerryokafor.core.data.repository.MovieDetailsRepository
import me.jerryokafor.core.model.Cast
import me.jerryokafor.core.model.Crew
import me.jerryokafor.core.model.Movie
import me.jerryokafor.core.model.Video
import me.jerryokafor.ihenkiri.feature.moviedetails.navigation.MovieDetailsArg
import javax.inject.Inject

private const val ONE_HOUR_IN_MINUTES = 60
private const val MAX_MOVIE_RATING = 10.0

@HiltViewModel
class MoviesDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val movieDetailsRepository: MovieDetailsRepository,
) : ViewModel() {
    private val movieId = MovieDetailsArg(savedStateHandle).movieId

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        loadDetails(movieId)
    }

    private fun loadDetails(movieId: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy() }

            val detailsJob = async {
                movieDetailsRepository.movieDetails(movieId)
                    .onSuccess { movieDetails ->
                        val hours = movieDetails.runtime / ONE_HOUR_IN_MINUTES
                        val minutes = movieDetails.runtime.rem(ONE_HOUR_IN_MINUTES)
                        val formattedRuntime =
                            "${hours}${if (hours > 1) "hr(s)" else "hr"} ${minutes}m"
                        _uiState.update {
                            it.copy(
                                title = movieDetails.title,
                                overview = movieDetails.overview,
                                categories = movieDetails.genres.map { genre -> genre.name },
                                postPath = movieDetails.posterPath,
                                releaseDate = movieDetails.releaseDate.formatDate(),
                                runtime = formattedRuntime,
                                rating = (movieDetails.voteAverage / MAX_MOVIE_RATING).toFloat(),
                            )
                        }
                    }.onFailure { errorResponse, errorCode, throwable ->
                        Log.w("TestIng: ", "$throwable")
                    }.collect()
            }

            val creditDetailsJob = async {
                movieDetailsRepository.movieCredits(movieId)
                    .onSuccess { movieCredit ->
                        _uiState.update { state ->
                            state.copy(
                                cast = movieCredit.cast.distinctBy { it.name },
                                crew = movieCredit.crew.distinctBy { it.name },
                            )
                        }
                    }
                    .onFailure { _, _, e ->
                        Log.w("TestIng: ", "$e")
                    }.collect()
            }

            val similarMovies = async {
                movieDetailsRepository.similarMovies(movieId)
                    .onSuccess { movies ->
                        Log.d("TestIng: ", "Recommendations: $movies")
                        _uiState.update { it.copy(recommendations = movies) }
                    }.onFailure { errorResponse, errorCode, throwable ->
                        throwable?.printStackTrace()
                        Log.w("TestIng: ", "$errorResponse")
                    }.collect()
            }

            val videosJob = async {
                movieDetailsRepository.movieVideos(movieId)
                    .onSuccess { videos ->
                        _uiState.update { it.copy(videos = videos) }
                    }.onFailure { errorResponse, errorCode, throwable ->
                        Log.w("TestIng: ", "$throwable")
                    }.collect()
            }

            detailsJob.await()
            creditDetailsJob.join()
            similarMovies.join()
            videosJob.join()
        }
    }

    data class UIState(
        val loading: Boolean = false,
        val title: String = "",
        val overview: String = "",
        val postPath: String = "",
        val releaseDate: String = "",
        val runtime: String = "",
        val rating: Float = 0F,
        val cast: List<Cast> = listOf(),
        val crew: List<Crew> = listOf(),
        val categories: List<String> = listOf(),
        val recommendations: List<Movie> = listOf(),
        val videos: List<Video> = listOf(),
    )
}

private fun String.formatDate(): String {
    return this.replace("-", "/")
}
