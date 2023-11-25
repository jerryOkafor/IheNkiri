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

import androidx.datastore.core.CorruptionException
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import me.jerryokafor.core.data.userPreferences
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertFailsWith

class UserPreferencesSerializerTest {
    private val userPreferencesSerializer = UserPreferencesSerializer()

    @Test
    fun userPreferencesSerializer_initialized_defaultValueIsEmpty() {
        assertThat(userPreferences { }).isEqualTo(userPreferencesSerializer.defaultValue)
    }

    @Test
    fun userPreferencesSerializer_writeAndRead_outputCorrectValues() = runTest {
        val expectedUserPreferences = userPreferences {
            guestSessionId = "e25304417c5c67f66b7838517257a3e9"
            accountId = "4bc889XXXXa3c0z92001001"
            accessToken =
                "eyJhbGciOiJIUzI1NiIsInR5cCIdIkpXVCJ9.XXXXX.sImp0aSI6Ijg4In0.b76OiEs10gdp9oNOoGpBJ94nO9Zi17Y7SvAXJQW8nH2"
        }

        val outputStream = ByteArrayOutputStream()
        expectedUserPreferences.writeTo(outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())
        val actualUserPreferences = userPreferencesSerializer.readFrom(inputStream)

        assertThat(expectedUserPreferences).isEqualTo(actualUserPreferences)
    }

    @Test
    fun userPreferencesSerializer_readingInvalidValue_throwsCorruptionException() = runTest {
        assertFailsWith<CorruptionException>(message = "Cannot read proto.") {
            userPreferencesSerializer.readFrom(ByteArrayInputStream(byteArrayOf(0)))
        }
    }
}
