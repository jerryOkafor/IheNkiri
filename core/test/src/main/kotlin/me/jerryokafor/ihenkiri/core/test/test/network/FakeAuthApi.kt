/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 IheNkiri Project
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

@file:Suppress("TooGenericExceptionThrown")
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

package me.jerryokafor.ihenkiri.core.test.test.network

import me.jerryokafor.ihenkiri.core.network.model.request.CreateAccessTokenRequest
import me.jerryokafor.ihenkiri.core.network.model.request.CreateRequestTokenRequest
import me.jerryokafor.ihenkiri.core.network.model.response.CreateAccessTokenResponse
import me.jerryokafor.ihenkiri.core.network.model.response.CreateGuestTokenResponse
import me.jerryokafor.ihenkiri.core.network.model.response.CreateRequestTokenResponse
import me.jerryokafor.ihenkiri.core.network.service.AuthApi
import me.jerryokafor.ihenkiri.core.test.util.createAccessTokenSuccessResponse
import me.jerryokafor.ihenkiri.core.test.util.createGuestTokenResponse
import me.jerryokafor.ihenkiri.core.test.util.createRequestTokenSuccessResponse
import javax.inject.Inject

class FakeAuthApi
    @Inject
    constructor() : AuthApi {
        override suspend fun createRequestToken(
            requestBody: CreateRequestTokenRequest,
        ): CreateRequestTokenResponse = createRequestTokenSuccessResponse()

        override suspend fun createAccessToken(
            requestBody: CreateAccessTokenRequest,
        ): CreateAccessTokenResponse = createAccessTokenSuccessResponse()

        override suspend fun createGuestSession(): CreateGuestTokenResponse =
            createGuestTokenResponse()

        override suspend fun deleteSession() = Unit
    }

class FakeAuthApiWithException
    @Inject
    constructor() : AuthApi {
        override suspend fun createRequestToken(
            requestBody: CreateRequestTokenRequest,
        ): CreateRequestTokenResponse = throw Exception("Unable to create request token")

        override suspend fun createAccessToken(
            requestBody: CreateAccessTokenRequest,
        ): CreateAccessTokenResponse = throw Exception("Unable to create access token")

        override suspend fun createGuestSession() = throw Exception("Unable to create guest token")

        override suspend fun deleteSession() = Unit
    }
