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

package me.jerryokafor.core.ui.components

import android.os.Build
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.Coil
import com.google.common.truth.Truth.assertThat
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.ihenkiri.core.test.util.fakeSuccessImageLoader
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
class IhenkiriCollapsingToolbarHeaderTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var onNavigateUpClick: Int = 0

    @Before
    @Throws(Exception::class)
    fun setUp() {
        Coil.setImageLoader(fakeSuccessImageLoader)
        ShadowLog.stream = System.out
    }

    @Test
    fun toolbarHeader_scrolled_pictureIsHidden() {
        with(composeTestRule) {
            setContent {
                val scrollState: ScrollState = rememberScrollState(-1)
                val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
                val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }

                Box(modifier = Modifier.fillMaxWidth()) {
                    IhenkiriCollapsingToolbarHeader(
                        scroll = scrollState,
                        headerHeightPx = headerHeightPx,
                        imagePath = "test.png",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(headerHeight),
                    )

                    // Toolbar
                    IhenkiriCollapsingToolbarToolbar(
                        scroll = scrollState,
                        headerHeightPx = headerHeightPx,
                        toolbarHeightPx = toolbarHeightPx,
                        onNavigationIconClick = { onNavigateUpClick++ },
                    )

                    // Toolbar title
                    IhenkiriCollapsingToolbarTitle(
                        scroll = scrollState,
                        name = "Sylvester Stallone",
                        style = IheNkiri.typography.displaySmall,
                        color = Color.White,
                    )
                }
            }

            onNodeWithText("Sylvester Stallone").assertExists()
                .assertIsDisplayed()

            onNodeWithContentDescription("Navigate up").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()

            onNodeWithContentDescription("Person profile picture").assertExists()
                .assertIsNotDisplayed()

            assertThat(onNavigateUpClick).isEqualTo(1)
        }
    }

    @Test
    fun toolbarHeader_scrolled_pictureIsShown() {
        with(composeTestRule) {
            setContent {
                val scrollState: ScrollState = rememberScrollState(0)
                val headerHeightPx = with(LocalDensity.current) { headerHeight.toPx() }
                val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.toPx() }

                Box(modifier = Modifier.fillMaxWidth()) {
                    IhenkiriCollapsingToolbarHeader(
                        scroll = scrollState,
                        headerHeightPx = headerHeightPx,
                        imagePath = "test.png",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(headerHeight),
                    )

                    // Toolbar
                    IhenkiriCollapsingToolbarToolbar(
                        scroll = scrollState,
                        headerHeightPx = headerHeightPx,
                        toolbarHeightPx = toolbarHeightPx,
                        onNavigationIconClick = { onNavigateUpClick++ },
                    )

                    // Toolbar title
                    IhenkiriCollapsingToolbarTitle(
                        scroll = scrollState,
                        name = "Sylvester Stallone",
                        style = IheNkiri.typography.displaySmall,
                        color = Color.White,
                    )
                }
            }

            onNodeWithContentDescription("Person profile picture").assertExists()
                .assertIsDisplayed()
        }
    }
}
