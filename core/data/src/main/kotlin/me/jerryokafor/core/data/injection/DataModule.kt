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

package me.jerryokafor.core.data.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.jerryokafor.core.data.UserPreferences
import me.jerryokafor.core.data.repository.DefaultLocalStorage
import me.jerryokafor.core.data.repository.DefaultMovieDetailsRepository
import me.jerryokafor.core.data.repository.DefaultMovieListRepository
import me.jerryokafor.core.data.repository.DefaultPeopleListRepository
import me.jerryokafor.core.data.repository.DefaultTVShowsRepository
import me.jerryokafor.core.data.repository.LocalStorage
import me.jerryokafor.core.data.repository.MovieDetailsRepository
import me.jerryokafor.core.data.repository.MovieListRepository
import me.jerryokafor.core.data.repository.PeopleListRepository
import me.jerryokafor.core.data.repository.TVShowsRepository
import me.jerryokafor.core.data.repository.UserPreferencesSerializer
import me.jerryokafor.ihenkiri.core.network.datasource.DefaultMoviesRemoteDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun provideMoviesRepository(repo: DefaultMovieListRepository): MovieListRepository

    @Binds
    fun provideMovieDetailsRepository(repo: DefaultMovieDetailsRepository): MovieDetailsRepository

    @Binds
    fun providePeopleListRepository(repo: DefaultPeopleListRepository): PeopleListRepository

    @Binds
    fun provideTVShowsRepository(repo: DefaultTVShowsRepository): TVShowsRepository
}

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {
    @[Singleton Provides]
    fun provideUserPreferencesDatastore(@ApplicationContext context: Context): DataStore<UserPreferences> =
        DataStoreFactory.create(
            serializer = UserPreferencesSerializer(),
            produceFile = { context.dataStoreFile(DefaultLocalStorage.DATA_STORE_FILE_NAME) },
        )
}

@Module
@InstallIn(SingletonComponent::class)
interface LocalStorageBinding {
    @Binds
    fun provideLocalStorage(storage: DefaultLocalStorage): LocalStorage
}
