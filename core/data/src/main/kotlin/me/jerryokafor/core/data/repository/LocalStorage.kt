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
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import me.jerryokafor.core.common.injection.IoDispatcher
import me.jerryokafor.core.data.ThemeConfigProto
import me.jerryokafor.core.data.UserPreferences
import me.jerryokafor.core.model.ThemeConfig
import me.jerryokafor.core.model.UserData
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

interface LocalStorage {
    fun isLoggedIn(): Flow<Boolean>

    fun userData(): Flow<UserData>

    suspend fun saveUserSession(accountId: String, accessToken: String)

    suspend fun saveGuestSession(guestSessionId: String)
    suspend fun setThemeConfig(config: ThemeConfig)
    suspend fun setUseDynamicColor(useDynamicColor: Boolean)
    suspend fun logout()
}

class UserPreferencesSerializer @Inject constructor() : Serializer<UserPreferences> {
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
    @IoDispatcher
    private val dispatcher: CoroutineDispatcher,
    private val userPreferences: DataStore<UserPreferences>,
) : LocalStorage {

    companion object {
        const val DATA_STORE_FILE_NAME = "auth_user.pb"
    }

    override fun isLoggedIn(): Flow<Boolean> = userPreferences.data.map {
        it.accessToken.isNullOrBlank().not() || it.guestSessionId.isNullOrBlank().not()
    }.flowOn(dispatcher)

    override fun userData(): Flow<UserData> = userPreferences.data.map {
        UserData(
            accountId = it.accountId,
            isLoggedIn = it.accessToken.isNullOrBlank().not() ||
                it.guestSessionId.isNullOrBlank().not(),
            themeConfig = when (it.themeConfig) {
                ThemeConfigProto.THEME_CONFIG_FOLLOW_SYSTEM -> ThemeConfig.FOLLOW_SYSTEM
                ThemeConfigProto.THEME_CONFIG_LIGHT -> ThemeConfig.LIGHT
                ThemeConfigProto.THEME_CONFIG_UNSPECIFIED,
                ThemeConfigProto.UNRECOGNIZED,
                ThemeConfigProto.THEME_CONFIG_DARK,
                -> ThemeConfig.DARK
            },
            usDynamicColor = it.useDynamicColor,
            name = null,
            userName = "",
        )
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

    override suspend fun setThemeConfig(config: ThemeConfig) {
        withContext(dispatcher) {
            userPreferences.updateData {
                it.toBuilder()
                    .setThemeConfig(
                        when (config) {
                            ThemeConfig.FOLLOW_SYSTEM -> ThemeConfigProto.THEME_CONFIG_FOLLOW_SYSTEM
                            ThemeConfig.LIGHT -> ThemeConfigProto.THEME_CONFIG_LIGHT
                            ThemeConfig.DARK -> ThemeConfigProto.THEME_CONFIG_DARK
                        },
                    )
                    .build()
            }
        }
    }

    override suspend fun setUseDynamicColor(useDynamicColor: Boolean) {
        withContext(dispatcher) {
            userPreferences.updateData {
                it.toBuilder()
                    .setUseDynamicColor(useDynamicColor)
                    .build()
            }
        }
    }

    override suspend fun logout() {
        withContext(dispatcher) {
            userPreferences.updateData {
                it.toBuilder()
                    .clearAccessToken()
                    .clearAccountId().build()
            }
        }
    }
}
