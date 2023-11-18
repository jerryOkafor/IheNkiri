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

package me.jerryokafor.ihenkiri.core.network.injection

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.jerryokafor.core.common.injection.qualifier.AuthOkHttpClient
import me.jerryokafor.core.common.injection.qualifier.NoAuthOkHttpClient
import me.jerryokafor.core.network.BuildConfig
import me.jerryokafor.ihenkiri.core.network.AuthorizationInterceptor
import me.jerryokafor.ihenkiri.core.network.datasource.DefaultMovieDetailsRemoteDataSource
import me.jerryokafor.ihenkiri.core.network.datasource.DefaultMoviesRemoteDataSource
import me.jerryokafor.ihenkiri.core.network.datasource.MovieDetailsRemoteDataSource
import me.jerryokafor.ihenkiri.core.network.datasource.MoviesRemoteDataSource
import me.jerryokafor.ihenkiri.core.network.service.AuthApi
import me.jerryokafor.ihenkiri.core.network.service.MovieDetailsApi
import me.jerryokafor.ihenkiri.core.network.service.MovieListApi
import me.jerryokafor.ihenkiri.core.network.service.PeopleListsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@Suppress("TooManyFunctions")
object NetworkModule {
    @[Provides Singleton]
    fun provideChuckerInterceptor(
        @ApplicationContext context: Context,
    ): ChuckerInterceptor {
        // Create the Collector
        val chuckerCollector =
            ChuckerCollector(
                context = context,
                showNotification = true,
                retentionPeriod = RetentionManager.Period.ONE_HOUR,
            )

        // Create the Interceptor
        @Suppress("MagicNumber")
        return ChuckerInterceptor.Builder(context).apply {
            collector(chuckerCollector)
            maxContentLength(250_000L)
            redactHeaders("Auth-Token", "Bearer")
            alwaysReadResponseBody(true)
            createShortcut(true)
        }.build()
    }

    @[Provides Singleton AuthOkHttpClient]
    fun provideAuthOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
        val authToken = BuildConfig.TMDB_API_KEY
        val builder = OkHttpClient.Builder().addInterceptor(chuckerInterceptor).addInterceptor(AuthorizationInterceptor(authToken))

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @[Provides Singleton NoAuthOkHttpClient]
    fun provideNoAuthOkHttpClient(chuckerInterceptor: ChuckerInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder().addInterceptor(chuckerInterceptor)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        return builder.build()
    }

    @[Provides Singleton]
    fun provideGson(): Gson = GsonBuilder().apply {
        setPrettyPrinting()
        setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
    }.create()

    @[Provides Singleton]
    fun provideMoviesRemoteDataSource(moviesApi: MovieListApi): MoviesRemoteDataSource =
        DefaultMoviesRemoteDataSource(moviesApi = moviesApi)

    @[Provides Singleton]
    fun provideMovieDetailsRemoteDataSource(movieDetailsApi: MovieDetailsApi): MovieDetailsRemoteDataSource =
        DefaultMovieDetailsRemoteDataSource(movieDetailsApi = movieDetailsApi)

    @[Provides Singleton]
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @[Provides Singleton]
    fun provideMoviesApi(retrofit: Retrofit): MovieListApi = retrofit.create(MovieListApi::class.java)

    @[Provides Singleton]
    fun provideMovieDetailsApi(retrofit: Retrofit): MovieDetailsApi = retrofit.create(MovieDetailsApi::class.java)

    @[Provides Singleton]
    fun providePeopleListsApi(retrofit: Retrofit): PeopleListsApi = retrofit.create(PeopleListsApi::class.java)

    @[Provides Singleton]
    fun provideRetrofit(
        @AuthOkHttpClient okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit =
        Retrofit.Builder().baseUrl(BuildConfig.TMDB_BASE_URL).client(okHttpClient).addConverterFactory(GsonConverterFactory.create(gson))
            .build()
}
