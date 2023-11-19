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

package me.jerryokafor.ihenkiri.feature.people.viewmodel

import androidx.paging.testing.asSnapshot
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.data.repository.PeopleListRepository
import me.jerryokafor.core.model.Person
import me.jerryokafor.ihenkiri.core.test.util.MainDispatcherRule
import me.jerryokafor.ihenkiri.core.test.util.PeopleListTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PeopleViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var peopleViewModel: PeopleViewModel

    private val peopleListRepository = mockk<PeopleListRepository>()

    @Before
    fun setUp() {
        coEvery { peopleListRepository.popularPeople(any()) } returns PeopleListTestData.testPersons()
        peopleViewModel = PeopleViewModel(peopleListRepository = peopleListRepository)
    }

    @Test
    fun `peopleViewModel whenInitialized list of persons is shown`() = runTest {
        val itemsSnapshot: List<Person> = peopleViewModel.persons.asSnapshot {
            refresh()
            appendScrollWhile { it.name == "Florence Pugh" }
        }

        assertThat(itemsSnapshot.distinct())
            .containsExactlyElementsIn(PeopleListTestData.testPersons())
            .inOrder()
    }
}
