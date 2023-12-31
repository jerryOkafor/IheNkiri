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

package com.jerryokafor.feature.peopledetails.ui

import android.os.Build
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jerryokafor.feature.peopledetails.viewModel.PersonDetailsUiState
import me.jerryokafor.ihenkiri.core.test.util.PeopleDetailsTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
@Config(
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
    qualifiers = "xlarge",
)
class PeopleDetailsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var onNavigateUpClick: Int = 0

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun peopleDetailsScreen_personDetailsLoading_progressIndicatorIsShown() {
        with(composeTestRule) {
            setContent {
                PeopleDetailsScreen(
                    uiState = PersonDetailsUiState.Loading,
                    onNavigateUp = { onNavigateUpClick++ },
                )
            }

            onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun peopleDetailsScreen_personDetailsLoading_movieDetailsIsShown() {
        with(composeTestRule) {
            setContent {
                PeopleDetailsScreen(
                    uiState = PersonDetailsUiState.Success(
                        PeopleDetailsTestData.testPersonDetails(),
                    ),
                    onNavigateUp = { onNavigateUpClick++ },
                )
            }

            onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
                .assertDoesNotExist()
        }
    }
}
