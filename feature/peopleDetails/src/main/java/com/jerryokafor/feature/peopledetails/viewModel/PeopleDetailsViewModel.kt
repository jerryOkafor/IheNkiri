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

package com.jerryokafor.feature.peopledetails.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerryokafor.feature.peopledetails.navigation.PeopleDetailsArg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import me.jerryokafor.core.common.outcome.Failure
import me.jerryokafor.core.common.outcome.Success
import me.jerryokafor.core.data.repository.PeopleDetailsRepository
import me.jerryokafor.core.model.PersonDetails
import javax.inject.Inject

@HiltViewModel
class PeopleDetailsViewModel
    @Inject
    constructor(
        private val peopleDetailsRepository: PeopleDetailsRepository,
        savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val personId = PeopleDetailsArg(savedStateHandle).personId

        val personDetails = personId.flatMapLatest { id ->
            peopleDetailsRepository.personDetails(id)
        }.map {
            when (it) {
                is Failure -> {
                    it.throwable?.printStackTrace()
                    PersonDetailsUiState.Error(it.errorResponse)
                }
                is Success -> PersonDetailsUiState.Success(it.data)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = PersonDetailsUiState.Loading,
        )
    }

sealed interface PersonDetailsUiState {
    data object Loading : PersonDetailsUiState

    data class Success(val personDetails: PersonDetails) : PersonDetailsUiState

    data class Error(val message: String) : PersonDetailsUiState
}
