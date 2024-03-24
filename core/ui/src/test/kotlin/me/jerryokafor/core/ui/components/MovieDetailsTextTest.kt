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
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.text.style.TextAlign
import androidx.test.ext.junit.runners.AndroidJUnit4
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
class MovieDetailsTextTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    @Throws(Exception::class)
    fun setUp() {
        ShadowLog.stream = System.out
    }

    @Test
    fun movieDetailsText_textTooLarge_showReadMoreButton() {
        with(composeTestRule) {
            setContent {
                val text = "Terrible Script, dialogue, directing, hammy editing. Music is meh. " +
                    "Poor acting (but they clearly got no support). This crap show's the " +
                    "contempt producers like this have for the audience that gives them " +
                    "money. The writer, director, producers and editor must have been so " +
                    "full of ego that I bet continues today, despite the poor reviews. " +
                    "During the boring minutes (90) I drifted off imagining them all " +
                    "working on these"
                MovieDetailsText(
                    text = text,
                    dialogTitle = "Kungfu Panda",
                    textAlign = TextAlign.Justify,
                )
            }

            onNodeWithText("Read more")
                .assertExists()
                .assertHasClickAction()
                .assertIsDisplayed()
                .performClick()
            onNode(isDialog()).assertIsDisplayed()
            onNodeWithText("Ok").assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .performClick()
        }
    }

    @Test
    fun movieDetailsText_SmallText_showReadMoreButton() {
        with(composeTestRule) {
            setContent {
                val text = "Terrible Script, dialogue, directing, hammy editing. Music is meh. " +
                    "Poor acting (but they clearly got no support). This crap show's the " +
                    "contempt "
                MovieDetailsText(
                    text = text,
                    dialogTitle = "Kungfu Panda",
                    textAlign = TextAlign.Justify,
                )
            }

            onNodeWithText("Read more")
                .assertDoesNotExist()
        }
    }
}
