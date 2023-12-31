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

package me.jerryokafor.ihenkiri.core.test.injection

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import me.jerryokafor.core.data.injection.DataModule
import me.jerryokafor.core.data.repository.MovieDetailsRepository
import me.jerryokafor.core.data.repository.MovieListRepository
import me.jerryokafor.core.data.repository.PeopleDetailsRepository
import me.jerryokafor.core.data.repository.PeopleListRepository
import me.jerryokafor.core.data.repository.TVShowsRepository
import me.jerryokafor.ihenkiri.core.test.test.data.FakeMovieDetailsRepository
import me.jerryokafor.ihenkiri.core.test.test.data.FakeMoviesRepository
import me.jerryokafor.ihenkiri.core.test.test.data.FakePeopleDetailsRepository
import me.jerryokafor.ihenkiri.core.test.test.data.FakePeopleListRepository
import me.jerryokafor.ihenkiri.core.test.test.data.FakeTVShowsRepository
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
abstract class TestDataModule {
    @[Binds Singleton]
    abstract fun provideFakeFakeMoviesRepository(repo: FakeMoviesRepository): MovieListRepository

    @[Binds Singleton]
    abstract fun provideMovieDetailsRepository(
        movieDetailsRepository: FakeMovieDetailsRepository,
    ): MovieDetailsRepository

    @[Binds Singleton]
    abstract fun providePeopleListRepository(
        peopleListRepository: FakePeopleListRepository,
    ): PeopleListRepository

    @[Binds Singleton]
    abstract fun provideTVShowsRepository(repo: FakeTVShowsRepository): TVShowsRepository

    @[Binds Singleton]
    abstract fun providePeopleDetailsRepository(
        repo: FakePeopleDetailsRepository,
    ): PeopleDetailsRepository
}
