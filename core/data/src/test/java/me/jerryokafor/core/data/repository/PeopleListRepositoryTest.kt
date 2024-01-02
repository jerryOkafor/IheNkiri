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

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.jerryokafor.ihenkiri.core.network.datasource.PeopleListRemoteDataSource
import me.jerryokafor.ihenkiri.core.test.util.PeopleListTestData
import org.junit.Before
import org.junit.Test

class PeopleListRepositoryTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val peopleListRemoteDataSource = mockk<PeopleListRemoteDataSource>(relaxed = true)
    private lateinit var peopleListRepository: PeopleListRepository

    @Before
    fun setUp() {
        coEvery { peopleListRemoteDataSource.popularPersons(any()) } returns
            PeopleListTestData.testPersons()

        peopleListRepository = DefaultPeopleListRepository(
            peopleListRemoteDataSource = peopleListRemoteDataSource,
            dispatcher = testDispatcher,
        )
    }

    @Test
    fun `test popularPersons() returns list of persons`() = testScope.runTest {
        val testPage = 1
        val people = peopleListRepository.popularPeople(testPage)

        assertThat(people.size).isEqualTo(9)
        with(people.first()) {
            assertThat(id).isEqualTo(976)
            assertThat(name).isEqualTo("Jason Statham")
            assertThat(popularity).isEqualTo(199.055)
            assertThat(profilePath).isEqualTo("/whNwkEQYWLFJA8ij0WyOOAD5xhQ.jpg")

            assertThat(knownFor.map { it.title }).containsExactlyElementsIn(
                listOf(
                    "Snatch",
                    "The Meg",
                    "The Transporter",
                ),
            ).inOrder()
        }

        coVerify(exactly = 1) {
            peopleListRemoteDataSource.popularPersons(eq(testPage))
        }
    }
}
