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

package me.jerryokafor.ihenkiri.feature.people.ui

import android.os.Build
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.test.espresso.action.ViewActions.swipeUp
import kotlinx.coroutines.flow.flowOf
import me.jerryokafor.core.model.Person
import me.jerryokafor.ihenkiri.core.test.util.PeopleListTestData
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
    qualifiers = "xlarge",
)
class PeopleScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun refreshProgress_whenScreenIsLoading_showLoading() {
        val testPagingList = flowOf(
            PagingData.from(
                data = PeopleListTestData.testPersons(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.Loading,
                    append = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                ),
            ),
        )

        with(composeTestRule) {
            setContent {
                PeopleScreen(personLazyPagingItems = testPagingList.collectAsLazyPagingItems())
            }
            // assert
            onNodeWithTag(REFRESH_PROGRESS_INDICATOR).assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun refreshProgress_whenScreenIsLoaded_hideLoading() {
        val testPagingList = flowOf(
            PagingData.from(
                data = PeopleListTestData.testPersons(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                ),
            ),
        )

        with(composeTestRule) {
            setContent {
                PeopleScreen(personLazyPagingItems = testPagingList.collectAsLazyPagingItems())
            }

            // assert
            onNodeWithTag(REFRESH_PROGRESS_INDICATOR).assertDoesNotExist()
        }
    }

    @Test
    fun appendProgress_whenMoreItemIsLoading_showLoading() {
        val testPagingList = flowOf(
            PagingData.from(
                data = listOf<Person>(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    append = LoadState.Loading,
                    prepend = LoadState.NotLoading(false),
                ),
            ),
        )
        with(composeTestRule) {
            setContent {
                PeopleScreen(personLazyPagingItems = testPagingList.collectAsLazyPagingItems())
            }

            onNodeWithTag(APPEND_PROGRESS_INDICATOR).assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun appendProgress_whenMoreItemIsLoaded_hideLoading() {
        val testPagingList = flowOf(
            PagingData.from(
                data = listOf<Person>(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                ),
            ),
        )

        with(composeTestRule) {
            setContent {
                PeopleScreen(personLazyPagingItems = testPagingList.collectAsLazyPagingItems())
            }

            onNodeWithTag(APPEND_PROGRESS_INDICATOR).assertDoesNotExist()
        }
    }

    @Test
    fun peopleList_whenPersonListIsLoaded_showPersons() {
        val testPagingList = flowOf(
            PagingData.from(
                data = PeopleListTestData.testPersons(),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                ),
            ),
        )

        with(composeTestRule) {
            setContent {
                PeopleScreen(personLazyPagingItems = testPagingList.collectAsLazyPagingItems())
            }

            onNodeWithTag(PEOPLE_LIST_TEST_TAG, useUnmergedTree = true).assertExists()
                .assertIsDisplayed()
                .performTouchInput { swipeUp() }
                .onChildren()
                .assertCountEquals(8)
        }
    }
}
