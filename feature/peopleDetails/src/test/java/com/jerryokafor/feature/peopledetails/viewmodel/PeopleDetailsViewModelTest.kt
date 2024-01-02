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

package com.jerryokafor.feature.peopledetails.viewmodel

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.jerryokafor.feature.peopledetails.navigation.personIdArg
import com.jerryokafor.feature.peopledetails.viewModel.PeopleDetailsViewModel
import com.jerryokafor.feature.peopledetails.viewModel.PersonDetailsUiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.common.outcome.Failure
import me.jerryokafor.core.common.outcome.Success
import me.jerryokafor.core.data.repository.PeopleDetailsRepository
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.core.test.util.PeopleDetailsTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.robolectric.shadows.ShadowLog

const val TEST_PERSON_ID = 0L

class PeopleDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var peopleDetailsViewModel: PeopleDetailsViewModel
    private val savedStateHandle = SavedStateHandle().apply {
        this[personIdArg] = TEST_PERSON_ID
    }

    private val peopleDetailsRepository = mockk<PeopleDetailsRepository>()

    @Before
    fun setUp() {
        ShadowLog.stream = System.out

        peopleDetailsViewModel = PeopleDetailsViewModel(
            savedStateHandle = savedStateHandle,
            peopleDetailsRepository = peopleDetailsRepository,
        )
    }

    @Test
    fun peopleDetailsViewModel_fetchPersonDetails_returnsPersonDetails() = runTest {
        every { peopleDetailsRepository.personDetails(any()) } returns flowOf(
            Success(PeopleDetailsTestData.testPersonDetails()),
        )
        peopleDetailsViewModel.personDetails.test {
            assertThat(awaitItem()).isEqualTo(PersonDetailsUiState.Loading)

            val expected = PeopleDetailsTestData.testPersonDetails()

            awaitItem().let {
                assertThat(it is PersonDetailsUiState.Success).isTrue()
                assertThat((it as PersonDetailsUiState.Success).personDetails).isEqualTo(expected)
            }
        }
    }

    @Test
    fun peopleDetailsViewModel_fetchPersonDetails_returnsError() = runTest {
        every { peopleDetailsRepository.personDetails(any()) } returns flowOf(
            Failure(errorResponse = "Error fetching persons details"),
        )

        peopleDetailsViewModel.personDetails.test {
            assertThat(awaitItem()).isEqualTo(PersonDetailsUiState.Loading)

            awaitItem().let {
                assertThat(it is PersonDetailsUiState.Error).isTrue()
                assertThat((it as PersonDetailsUiState.Error).message)
                    .isEqualTo("Error fetching persons details")
            }
        }
    }
}
