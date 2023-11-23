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

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.jerryokafor.ihenkiri.core.test.util.testUserPreferencesDataStore
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class LocalStorageTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private lateinit var localStorage: LocalStorage

    @Before
    fun setup() {
        localStorage = DefaultLocalStorage(
            dispatcher = testDispatcher,
            userPreferences = tmpFolder.testUserPreferencesDataStore(testScope),
        )
    }

    @Test
    fun localStorage_initialized_isLoggedInIsFalseByDefault() = testScope.runTest {
        assertThat(localStorage.isLoggedIn().first()).isFalse()
    }

    @Test
    fun localStorage_saveUserSession_isLoggedInIsTrue() = testScope.runTest {
        localStorage.saveUserSession(
            accountId = "4bc889XXXXa3c0z92001001",
            accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCIdIkpXVCJ9.XXXXX.sImp0aSI6Ijg4In0.b76OiEs10gdp9oNOoGpBJ94nO9Zi17Y7SvAXJQW8nH2",
        )

        assertThat(localStorage.isLoggedIn().first()).isTrue()
    }

    @Test
    fun localStorage_saveGuestSession_isLoggedInIsTrue() = testScope.runTest {
        localStorage.saveGuestSession(guestSessionId = "e25304417c5c67f66b7838517257a3e9")

        assertThat(localStorage.isLoggedIn().first()).isTrue()
    }
}
