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

package me.jerryokafor.core.data.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import me.jerryokafor.ihenkiri.core.test.util.PeopleListTestData
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertTrue

class PeopleListPagingSourceTest {
    private val peopleListRepo = mockk<PeopleListRepository>()

    @Test
    fun `test fresh load`() {
        val testPage = 1
        coEvery { peopleListRepo.popularPeople(any()) } returns PeopleListTestData.testPersons()

        val pagingSource = PeopleListPagingSource(peopleListRepo)
        val pager = TestPager(
            config =
                PagingConfig(
                    pageSize = 9,
                    maxSize = 50,
                    enablePlaceholders = true,
                ),
            pagingSource = pagingSource,
        )

        runTest {
            val result = pager.refresh() as PagingSource.LoadResult.Page

            assertThat(pagingSource.getRefreshKey(pager.getPagingState(0))).isEqualTo(1)

            assertThat(result.data)
                .containsExactlyElementsIn(PeopleListTestData.testPersons())
                .inOrder()
        }

        coVerify(exactly = 1) { peopleListRepo.popularPeople(eq(testPage)) }
    }

    @Test
    fun `test consecutive load`() {
        val testPage = 1
        coEvery { peopleListRepo.popularPeople(any()) } returns PeopleListTestData.testPersons()

        val pagingSource = PeopleListPagingSource(peopleListRepo)
        val pager = TestPager(
            config =
                PagingConfig(
                    pageSize = 9,
                    maxSize = 50,
                    enablePlaceholders = true,
                ),
            pagingSource = pagingSource,
        )

        runTest {
            val result = with(pager) {
                pager.refresh()
                append()
                append()
                append()
            } as PagingSource.LoadResult.Page

            assertThat(pager.getPages().size).isEqualTo(4)
            assertThat(result.data)
                .containsExactlyElementsIn(PeopleListTestData.testPersons())
                .inOrder()

            // after loading 4 pages, anchore position = (page count * pages) -1
            val anchorPosition = (9 * 4) - 1
            val pagingState = pager.getPagingState(anchorPosition)
            val refreshKey = pagingSource.getRefreshKey(pagingState)
            assertThat(refreshKey).isEqualTo(4)
        }

        coVerify(exactly = 1) { peopleListRepo.popularPeople(eq(testPage)) }
        coVerify(exactly = 1) { peopleListRepo.popularPeople(eq(testPage + 1)) }
        coVerify(exactly = 1) { peopleListRepo.popularPeople(eq(testPage + 2)) }
    }

    @Test
    fun `test network error on refresh load`() {
        coEvery { peopleListRepo.popularPeople(any()) } throws IOException("No internet available")

        val pagingSource = PeopleListPagingSource(peopleListRepo)
        val pager = TestPager(
            config =
                PagingConfig(
                    pageSize = 9,
                    maxSize = 50,
                    enablePlaceholders = true,
                ),
            pagingSource = pagingSource,
        )

        runTest {
            val result = pager.refresh()
            assertTrue(result is PagingSource.LoadResult.Error)

            val page = pager.getLastLoadedPage()
            assertThat(page).isNull()
        }
        coVerify(exactly = 1) { peopleListRepo.popularPeople(eq(1)) }
    }

    @Test
    fun `test sever error on refresh load`() {
        coEvery { peopleListRepo.popularPeople(any()) } throws
            HttpException(
                Response.error<Any>(
                    409,
                    "Error loading persons".toResponseBody("plain/text".toMediaTypeOrNull()),
                ),
            )

        val pagingSource = PeopleListPagingSource(peopleListRepo)
        val pager = TestPager(
            config =
                PagingConfig(
                    pageSize = 9,
                    maxSize = 50,
                    enablePlaceholders = true,
                ),
            pagingSource = pagingSource,
        )

        runTest {
            val result = pager.refresh()
            assertTrue(result is PagingSource.LoadResult.Error)

            val page = pager.getLastLoadedPage()
            assertThat(page).isNull()
        }
        coVerify(exactly = 1) { peopleListRepo.popularPeople(eq(1)) }
    }
}
