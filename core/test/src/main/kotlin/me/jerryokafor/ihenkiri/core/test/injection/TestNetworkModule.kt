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

package me.jerryokafor.ihenkiri.core.test.injection

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import me.jerryokafor.ihenkiri.core.network.datasource.MoviesRemoteDataSource
import me.jerryokafor.ihenkiri.core.network.injection.NetworkModule
import me.jerryokafor.ihenkiri.core.network.service.AuthApi
import me.jerryokafor.ihenkiri.core.network.service.TVSeriesListsApi
import me.jerryokafor.ihenkiri.core.test.test.network.FakeAuthApi
import me.jerryokafor.ihenkiri.core.test.test.network.FakeMoviesRemoteDataSource
import me.jerryokafor.ihenkiri.core.test.test.network.FakeTVSeriesListsApi

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [NetworkModule::class],
)
abstract class TestNetworkModule {
    @Binds
    abstract fun bindAuthApi(authApi: FakeAuthApi): AuthApi

    @Binds
    abstract fun MoviesRemoteDataSource(
        datasource: FakeMoviesRemoteDataSource,
    ): MoviesRemoteDataSource

    @Binds
    abstract fun provideTVSeriesListsApi(repo: FakeTVSeriesListsApi): TVSeriesListsApi
}
