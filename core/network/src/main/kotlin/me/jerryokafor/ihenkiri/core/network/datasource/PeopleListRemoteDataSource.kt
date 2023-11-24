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

package me.jerryokafor.ihenkiri.core.network.datasource

import me.jerryokafor.core.model.Person
import me.jerryokafor.ihenkiri.core.network.model.response.toDomainModel
import me.jerryokafor.ihenkiri.core.network.service.PeopleListsApi
import javax.inject.Inject

interface PeopleListRemoteDataSource {
    suspend fun popularPersons(page: Int): List<Person>
}

class DefaultPeopleListRemoteDataSource
@Inject
constructor(private val peopleListsApi: PeopleListsApi) : PeopleListRemoteDataSource {
    override suspend fun popularPersons(page: Int): List<Person> =
        peopleListsApi.popularPersons(page).results.map { it.toDomainModel() }
}
