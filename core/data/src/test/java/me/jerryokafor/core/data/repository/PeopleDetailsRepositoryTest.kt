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

package me.jerryokafor.core.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.common.outcome.Failure
import me.jerryokafor.core.common.outcome.Success
import me.jerryokafor.ihenkiri.core.network.service.PeopleDetailsApi
import me.jerryokafor.ihenkiri.core.test.util.PeopleDetailsTestData
import org.junit.Before
import org.junit.Test

const val TEST_PERSON_ID = 1L

class PeopleDetailsRepositoryTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val personDetailsApi = mockk<PeopleDetailsApi>(relaxed = true)
    private lateinit var peopleDetailsRepository: PeopleDetailsRepository

    @Before
    fun setUp() {
        coEvery { personDetailsApi.personDetails(any()) } returns
            PeopleDetailsTestData.testNetworkPersonDetails()

        peopleDetailsRepository = DefaultPeopleDetailsRepository(
            personDetailsApi = personDetailsApi,
            dispatcher = testDispatcher,
        )
    }

    @Test
    fun `test personDetails() returns personDetails`() = testScope.runTest {
        coEvery { personDetailsApi.personDetails(any()) } returns
            PeopleDetailsTestData.testNetworkPersonDetails()

        peopleDetailsRepository.personDetails(TEST_PERSON_ID).test {
            with((awaitItem() as Success)) {
                assertThat(data).isEqualTo(PeopleDetailsTestData.testPersonDetails())
            }

            awaitComplete()
        }

        coVerify(exactly = 1) {
            personDetailsApi.personDetails(eq(TEST_PERSON_ID))
        }
    }

    @Test
    fun `test personDetails() returns error`() = testScope.runTest {
        coEvery { personDetailsApi.personDetails(any()) } throws
            Exception("Error fetching people details")

        peopleDetailsRepository.personDetails(TEST_PERSON_ID).test {
            with((awaitItem() as Failure)) {
                assertThat(errorCode).isNotNull()
                assertThat(errorResponse).isEqualTo("Error fetching people details")
            }

            awaitComplete()
        }

        coVerify(exactly = 1) {
            personDetailsApi.personDetails(personId = eq(TEST_PERSON_ID))
        }
    }
}
