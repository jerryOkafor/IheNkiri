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
import me.jerryokafor.core.model.TVShow
import me.jerryokafor.ihenkiri.core.test.util.TVShowsTestData.testTVShows
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import kotlin.test.assertTrue

class TVShowsListPagingSourceTest {
    private val fetchMovies = mockk<suspend (Int) -> List<TVShow>>()

    @Test
    fun `test fresh load`() {
        coEvery { fetchMovies(any()) } returns testTVShows()

        val pagingSource = TVShowsListPagingSource(fetchMovies)
        val pager = TestPager(
            config = PagingConfig(
                pageSize = 4,
                maxSize = 50,
                enablePlaceholders = true,
            ),
            pagingSource = pagingSource,
        )

        runTest {
            val result = pager.refresh() as PagingSource.LoadResult.Page
            assertThat(pagingSource.getRefreshKey(pager.getPagingState(0))).isEqualTo(1)
            assertThat(result.data).containsExactlyElementsIn(testTVShows()).inOrder()
        }

        coVerify(exactly = 1) { fetchMovies(eq(1)) }
    }

    @Test
    fun `test consecutive load`() {
        coEvery { fetchMovies(any()) } returns testTVShows()

        val pagingSource = TVShowsListPagingSource(fetchMovies)
        val pager = TestPager(
            config = PagingConfig(
                pageSize = 4,
                maxSize = 24,
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
            assertThat(result.data).containsExactlyElementsIn(testTVShows()).inOrder()

            // after loading 4 pages, anchore position = (page count * pages) -1
            val anchorPosition = (4 * 4) - 1
            val pagingState = pager.getPagingState(anchorPosition)
            val refreshKey = pagingSource.getRefreshKey(pagingState)
            assertThat(refreshKey).isEqualTo(4)
        }

        coVerify(exactly = 1) { fetchMovies(eq(1)) }
        coVerify(exactly = 1) { fetchMovies(eq(2)) }
        coVerify(exactly = 1) { fetchMovies(eq(3)) }
    }

    @Test
    fun `test network error on refresh load`() {
        coEvery { fetchMovies(any()) } throws IOException("No internet available")

        val pagingSource = TVShowsListPagingSource(fetchMovies)
        val pager = TestPager(
            config = PagingConfig(
                pageSize = 4,
                maxSize = 12,
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
        coVerify(exactly = 1) { fetchMovies(eq(1)) }
    }

    @Test
    fun `test sever error on refresh load`() {
        coEvery { fetchMovies(any()) } throws HttpException(
            Response.error<Any>(
                409,
                "Error loading tv shows".toResponseBody("plain/text".toMediaTypeOrNull()),
            ),
        )

        val pagingSource = TVShowsListPagingSource(fetchMovies)
        val pager = TestPager(
            config = PagingConfig(
                pageSize = 4,
                maxSize = 12,
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
        coVerify(exactly = 1) { fetchMovies(eq(1)) }
    }
}
