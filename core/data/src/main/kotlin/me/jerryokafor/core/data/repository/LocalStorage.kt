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

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.google.protobuf.InvalidProtocolBufferException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.jerryokafor.core.common.injection.IoDispatcher
import me.jerryokafor.core.data.UserPreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

interface LocalStorage {
    fun isLoggedIn(): Flow<Boolean>

    suspend fun saveUserSession(accountId: String, accessToken: String)

    suspend fun saveGuestSession(guestSessionId: String)
}

object UserPreferencesSerializer : Serializer<UserPreferences> {
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()
    override suspend fun readFrom(input: InputStream): UserPreferences {
        try {
            return UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) = t.writeTo(output)
}

@Singleton
class DefaultLocalStorage @Inject constructor(
    @ApplicationContext
    private val context: Context,
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
) : LocalStorage {

    companion object {
        private const val DATA_STORE_FILE_NAME = "auth_user.pb"
    }

    private val userPreferences: DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer,
            produceFile = { context.dataStoreFile(DATA_STORE_FILE_NAME) },
        )

    override fun isLoggedIn(): Flow<Boolean> = userPreferences.data.map {
        it.accessToken.isNullOrBlank().not() || it.guestSessionId.isNullOrBlank().not()
    }.flowOn(dispatcher)

    override suspend fun saveUserSession(accountId: String, accessToken: String) {
        withContext(dispatcher) {
            userPreferences.updateData {
                it.toBuilder()
                    .setAccountId(accountId)
                    .setAccessToken(accessToken).build()
            }
        }
    }

    override suspend fun saveGuestSession(guestSessionId: String) {
        withContext(dispatcher) {
            userPreferences.updateData {
                it.toBuilder()
                    .setGuestSessionId(guestSessionId)
                    .build()
            }
        }
    }
}
