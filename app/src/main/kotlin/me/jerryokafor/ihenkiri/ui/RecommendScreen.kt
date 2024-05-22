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

@file:Suppress("MatchingDeclarationName")

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

package me.jerryokafor.ihenkiri.ui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ui.components.Background
import me.jerryokafor.feature.movies.R
import me.jerryokafor.feature.movies.screen.TITLE_TEST_TAG
import me.jerryokafor.ihenkiri.BuildConfig

@Serializable
data object Recommendation

@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun RecommendationScreenPreview() {
    IheNkiriTheme {
        RecommendationScreen()
    }
}

const val RECOMMENDATION_SCREEN_TEST_TAG = "Recommendation"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Suppress("UnusedPrivateMember")
fun RecommendationScreen(onNavigateUp: () -> Unit = {}) {
    val coroutineScope = rememberCoroutineScope()

    val generativeModel = remember {
        GenerativeModel(
            modelName = "gemini-pro",
            apiKey = BuildConfig.AI_STUDIO_API_KEY,
        )
    }

    val generateApi: () -> Unit = {
        val chat = generativeModel.startChat(
            history = listOf(
                content(role = "user") {
                    text("Hello, I have 2 dogs in my house.")
                },
                content(role = "model") {
                    text("Great to meet you. What would you like to know?")
                    text("Id")
                },
            ),
        )

        coroutineScope.launch(Dispatchers.IO) {
            val response = chat.sendMessage("How many paws are in my house?")

            Log.d("Testing: ", "Response: ${response.text}")
        }
    }

    Background {
        Column(modifier = Modifier.testTag(RECOMMENDATION_SCREEN_TEST_TAG)) {
            CenterAlignedTopAppBar(
                modifier = Modifier
                    .statusBarsPadding()
                    .testTag(TITLE_TEST_TAG),
                title = {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("test_button"),
                        onClick = generateApi,
                    ) {
                        Icon(
                            painter = painterResource(
                                id = me.jerryokafor.core.ui.R.drawable.search,
                            ),
                            contentDescription = stringResource(
                                id = R.string.movies_content_description_search,
                            ),
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
                actions = {},
            )
        }
    }
}
