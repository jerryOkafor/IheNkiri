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

package me.jerryokafor.ihenkiri.core.test.util

import me.jerryokafor.ihenkiri.core.network.model.request.CreateAccessTokenRequest
import me.jerryokafor.ihenkiri.core.network.model.request.CreateRequestTokenRequest
import me.jerryokafor.ihenkiri.core.network.model.response.CreateAccessTokenResponse
import me.jerryokafor.ihenkiri.core.network.model.response.CreateRequestTokenResponse

fun createRequestToken() = CreateRequestTokenRequest("https://ihenkiri.jerryokafor.me/auth")

fun createRequestTokenSuccessResponse() = CreateRequestTokenResponse(
    requestToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWRpcm.XXXX.dxZddmwFqbiWGn1ycR0YPLNGLtVBWOagzneoVM3pXQ0",
    statusCode = 1,
    statusMessage = "Success.",
    success = true,
)

fun createAccessTokenRequest() = CreateAccessTokenRequest(
    requestToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyZWRpcm.XXXX.dxZddmwFqbiWGn1ycR0YPLNGLtVBWOagzneoVM3pXQ0",
)

fun createAccessTokenSuccessResponse() = CreateAccessTokenResponse(
    accountId = "4bc889XXXXa3c0z92001001",
    accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCIdIkpXVCJ9.XXXXX.sImp0aSI6Ijg4In0.b76OiEs10gdp9oNOoGpBJ94nO9Zi17Y7SvAXJQW8nH2",
    statusMessage = "Success.",
    statusCode = 1,
    success = true,
)
