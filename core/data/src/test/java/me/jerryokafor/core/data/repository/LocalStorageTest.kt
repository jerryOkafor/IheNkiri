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
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.data.ThemeConfigProto
import me.jerryokafor.core.data.UserPreferences
import me.jerryokafor.core.data.copy
import me.jerryokafor.core.model.ThemeConfig
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

    private val userPreferences = spyk(tmpFolder.testUserPreferencesDataStore(testScope))
    private lateinit var localStorage: LocalStorage

    @Before
    fun setup() {
        localStorage = DefaultLocalStorage(
            dispatcher = testDispatcher,
            userPreferences = userPreferences,
        )
    }

    @Test
    fun localStorage_initialized_isLoggedInIsFalseByDefault() = testScope.runTest {
        assertThat(localStorage.isLoggedIn().first()).isFalse()
        coVerify { userPreferences.data }
    }

    @Test
    fun localStorage_saveUserSession_isLoggedInIsTrue() = testScope.runTest {
        localStorage.saveUserSession(
            accountId = "4bc889XXXXa3c0z92001001",
            accessToken = """
                eyJhbGciOiJIUzI1NiIsInR5cCIdIkpXVCJ9.XXXXX.sImp0aSI6Ijg4In0.b76Oi
                Es10gdp9oNOoGpBJ94nO9Zi17Y7SvAXJQW8nH2
            """.trimIndent(),
        )

        assertThat(localStorage.isLoggedIn().first()).isTrue()
        coVerify { userPreferences.updateData(any()) }
    }

    @Test
    fun localStorage_saveGuestSession_isLoggedInIsTrue() = testScope.runTest {
        localStorage.saveGuestSession(guestSessionId = "e25304417c5c67f66b7838517257a3e9")

        assertThat(localStorage.isLoggedIn().first()).isTrue()
        coVerify { userPreferences.updateData(any()) }
    }

    @Test
    fun localStorage_setThemeConfig_ThemeConfigSaved() = testScope.runTest {
        localStorage.setThemeConfig(ThemeConfig.DARK)
        assertThat(localStorage.userData().first().themeConfig).isEqualTo(ThemeConfig.DARK)

        localStorage.setThemeConfig(ThemeConfig.LIGHT)
        assertThat(localStorage.userData().first().themeConfig).isEqualTo(ThemeConfig.LIGHT)

        localStorage.setThemeConfig(ThemeConfig.FOLLOW_SYSTEM)
        assertThat(localStorage.userData().first().themeConfig).isEqualTo(ThemeConfig.FOLLOW_SYSTEM)

        coVerify(exactly = 3) { userPreferences.updateData(any()) }
    }

    @Test
    fun localStorage_setUseDynamicColor_useDynamicColorIsSet() = testScope.runTest {
        localStorage.setUseDynamicColor(true)
        assertThat(localStorage.userData().first().usDynamicColor).isTrue()

        localStorage.setUseDynamicColor(false)
        assertThat(localStorage.userData().first().usDynamicColor).isFalse()

        coVerify { userPreferences.updateData(any()) }
    }

    @Test
    fun localStorage_userData_readDefaultUserData() = testScope.runTest {
        localStorage.userData().first()?.let {
            assertThat(it.themeConfig).isEqualTo(ThemeConfig.DARK)
            assertThat(it.usDynamicColor).isFalse()
            assertThat(it.accountId).isEmpty()
            assertThat(it.name).isNull()
            assertThat(it.userName).isEmpty()
        }
    }

    @Test
    fun localStorage_userData_defaultThemeConfigIsFollowSystem() = testScope.runTest {
        coEvery { userPreferences.data } returns flowOf(
            UserPreferences.getDefaultInstance()
                .copy { themeConfig = ThemeConfigProto.THEME_CONFIG_FOLLOW_SYSTEM },
        )
        localStorage.userData().first()?.let {
            assertThat(it.themeConfig).isEqualTo(ThemeConfig.FOLLOW_SYSTEM)
        }
    }

    @Test
    fun localStorage_userData_defaultThemeConfigIsDark() = testScope.runTest {
        coEvery { userPreferences.data } returns flowOf(
            UserPreferences.getDefaultInstance()
                .copy { themeConfig = ThemeConfigProto.THEME_CONFIG_UNSPECIFIED },
        )
        localStorage.userData().first()?.let {
            assertThat(it.themeConfig).isEqualTo(ThemeConfig.DARK)
        }
    }

    @Test
    fun localStorage_userData_themeConfigIsLight() = testScope.runTest {
        coEvery { userPreferences.data } returns flowOf(
            UserPreferences.getDefaultInstance()
                .copy { themeConfig = ThemeConfigProto.THEME_CONFIG_LIGHT },
        )
        localStorage.userData().first()?.let {
            assertThat(it.themeConfig).isEqualTo(ThemeConfig.LIGHT)
        }
    }

    @Test
    fun localStorage_logout_userIsLoggedOut() = testScope.runTest {
        localStorage.saveUserSession(
            accountId = "4bc889XXXXa3c0z92001001",
            accessToken = """
                eyJhbGciOiJIUzI1NiIsInR5cCIdIkpXVCJ9.XXXXX.sImp0aSI6Ijg4In0.b76O
                iEs10gdp9oNOoGpBJ94nO9Zi17Y7SvAXJQW8nH2
            """.trimIndent(),
        )
        assertThat(userPreferences.data.first().accessToken)
            .isNotEmpty()
        assertThat(userPreferences.data.first().accountId)
            .isNotEmpty()

        localStorage.logout()

        assertThat(userPreferences.data.first().accessToken)
            .isEmpty()
        assertThat(userPreferences.data.first().accountId)
            .isEmpty()
    }
}
