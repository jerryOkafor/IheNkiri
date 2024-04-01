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

package me.jerryokafor.ihenkiri.core.test.util

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.test.core.app.ApplicationProvider
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.test.FakeImageLoaderEngine

@OptIn(ExperimentalCoilApi::class)
val engine = FakeImageLoaderEngine.Builder()
    .intercept({ it is String && it.endsWith(".jpg") }, ColorDrawable(Color.RED))
    .intercept({ it is String && it.endsWith(".png") }, ColorDrawable(Color.GRAY))
    .default(ColorDrawable(Color.BLUE))
    .build()

@OptIn(ExperimentalCoilApi::class)
val fakeSuccessImageLoader = ImageLoader.Builder(ApplicationProvider.getApplicationContext())
    .components { add(engine) }
    .build()

@OptIn(ExperimentalCoilApi::class)
@Suppress("TooGenericExceptionThrown")
val errorEngine =
    FakeImageLoaderEngine.Builder().default { chain -> throw Exception("Error loading image") }
        .build()

@OptIn(ExperimentalCoilApi::class)
val fakeErrorImageLoader = ImageLoader.Builder(ApplicationProvider.getApplicationContext())
    .components { add(errorEngine) }
    .build()
