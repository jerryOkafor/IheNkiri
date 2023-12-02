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

package me.jerryokafor.core.network.datasoruce

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.model.equalsPerson
import me.jerryokafor.ihenkiri.core.network.datasource.DefaultPeopleListRemoteDataSource
import me.jerryokafor.ihenkiri.core.network.datasource.PeopleListRemoteDataSource
import me.jerryokafor.ihenkiri.core.network.service.PeopleListsApi
import me.jerryokafor.ihenkiri.core.test.util.PeopleListTestData
import org.junit.Before
import org.junit.Test

class PeopleListRemoteDataSourceTest {
    private val peopleListsApi = mockk<PeopleListsApi>()
    private lateinit var peopleListRemoteDataSource: PeopleListRemoteDataSource

    @Before
    fun setUp() {
        coEvery { peopleListsApi.popularPersons(any()) } returns PeopleListTestData
            .testNetworkPersons()
        peopleListRemoteDataSource =
            DefaultPeopleListRemoteDataSource(peopleListsApi = peopleListsApi)
    }

    @Test
    fun `test upcomingMovies, returns list of movies`() {
        runTest {
            val result = peopleListRemoteDataSource.popularPersons(1)

            assertThat(result.size).isEqualTo(9)
            assertThat(result).containsExactlyElementsIn(PeopleListTestData.testPersons())
                .inOrder()

            result.zip(PeopleListTestData.testPersons()) { first, second -> Pair(first, second) }
                .forEach { pair ->
                    assertThat(pair.second.equalsPerson(pair.first)).isTrue()
                }

            coVerify(exactly = 1) {
                peopleListsApi.popularPersons(eq(1))
            }
        }
    }
}
