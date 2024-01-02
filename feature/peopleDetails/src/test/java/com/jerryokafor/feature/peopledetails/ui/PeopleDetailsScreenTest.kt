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

package com.jerryokafor.feature.peopledetails.ui

import android.os.Build
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasParent
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isPopup
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeRight
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.Coil
import com.google.common.truth.Truth.assertThat
import com.jerryokafor.feature.peopledetails.viewModel.PersonDetailsUiState
import me.jerryokafor.ihenkiri.core.test.rule.assertAreDisplayed
import me.jerryokafor.ihenkiri.core.test.util.PeopleDetailsTestData
import me.jerryokafor.ihenkiri.core.test.util.imageLoader
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
        Coil.setImageLoader(imageLoader)
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
    fun peopleDetailsScreen_personDetailsLoaded_movieDetailsIsShown() {
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

            // title is displayed
            onNodeWithText("Sylvester Stallone").assertExists()
                .assertIsDisplayed()

            // back button is displayed
            onNodeWithContentDescription("Back")
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
            assertThat(onNavigateUpClick).isEqualTo(1)

            onNodeWithTag(PEOPLE_DETAILS_MAIN_BODY).assertExists()
                .assertIsDisplayed()

            // assert social buttons
            onNodeWithContentDescription("Facebook")
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithContentDescription("Twitter")
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithContentDescription("Instagram")
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithContentDescription("Tiktok")
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .assertHasClickAction()

            // assert person biography
            onNodeWithText("Biography").assertExists()
                .performScrollTo()
                .assertIsDisplayed()
            onNodeWithText(
                "Sylvester Stallone (born Michael Sylvester Gardenzio Stallone, " +
                    "July 6, 1946) is an American actor and filmmaker.",
            ).assertExists()
                .assertIsDisplayed()

            // assert know for
            onNodeWithTag(PEOPLE_DETAILS_KNOWN_FOR_TITLE)
                .assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .assert(hasText("Known For"))

            onNodeWithTag(PEOPLE_DETAILS_KNOWN_FOR).assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .onChildren()
                .assertCountEquals(4)

            // assert timeline
            onNodeWithText("Timeline").assertExists()
                .performScrollTo()
                .assertIsDisplayed()
            onNodeWithTag(TIMELINE_COLUMN_TAG).assertExists()
                .performScrollTo()
                .assertIsDisplayed()
                .onChildren()
                .assertCountEquals(3)

            onNodeWithText("... as Robert 'Rocky' Balboa").assertExists()
                .performScrollTo()
                .assertIsDisplayed()

            onNodeWithText("... as Self").assertExists()
                .performScrollTo()
                .assertIsDisplayed()

            onNodeWithText("... Executive Producer").assertExists()
                .performScrollTo()
                .assertIsDisplayed()

            onNodeWithText("... Choreographer").assertExists()
                .performScrollTo()
                .assertIsDisplayed()

            // assert dropdowns
            val dropDownNodeMatcher = SemanticsMatcher.expectValue(
                key = SemanticsProperties.Role,
                expectedValue = Role.DropdownList,
            )

            onAllNodes(dropDownNodeMatcher)
                .assertAreDisplayed()
                .assertCountEquals(2)

            onNodeWithTag(PEOPLE_DETAILS_MEDIA_TYPE_OPTIONS).assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()

            waitForIdle()
            onNode(isPopup()).assertExists()
                .assertIsDisplayed()
            onAllNodes(hasText("All") and hasParent(dropDownNodeMatcher), true)
                .assertCountEquals(2)
                .assertAreDisplayed()
            onNodeWithText("Movie").assertExists().assertIsDisplayed()
            onNodeWithText("TV Show").assertExists().assertIsDisplayed()

            // close the popup
            onNode(isPopup()).assertExists()
                .assertIsDisplayed()
                .performTouchInput { swipeRight() }

            onNodeWithTag(PEOPLE_DETAILS_DEPARTMENT_OPTIONS).assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()

            waitForIdle()
            onNode(isPopup()).assertExists()
                .assertIsDisplayed()

            onAllNodes(
                matcher = hasText("All") and hasParent(dropDownNodeMatcher),
                useUnmergedTree = true,
            ).assertCountEquals(1)
                .assertAreDisplayed()

            onNode(hasText("Acting") and hasAnySibling(hasText("Writing")))
                .assertExists().assertIsDisplayed()
            onNodeWithText("Writing").assertExists().assertIsDisplayed()
            onNodeWithText("Production").assertExists().assertIsDisplayed()
            onNodeWithText("Directing").assertExists().assertIsDisplayed()
            onNodeWithText("Creator").assertExists().assertIsDisplayed()
            onNodeWithText("Crew").assertExists().assertIsDisplayed()
        }
    }

    @Test
    fun peopleDetailsScreen_personDetailsLoadError_movieErrorIsShown() {
        with(composeTestRule) {
            setContent {
                PeopleDetailsScreen(
                    uiState = PersonDetailsUiState.Error(message = "Error fetching person details"),
                    onNavigateUp = { onNavigateUpClick++ },
                )
            }

            onNodeWithText("Error fetching person details")
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText("Close")
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()

            assertThat(onNavigateUpClick).isEqualTo(1)
        }
    }
}
