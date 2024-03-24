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

import androidx.activity.ComponentActivity
import androidx.compose.material3.LocalTextStyle
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import dagger.hilt.android.testing.HiltTestApplication
import me.jerryokafor.core.ui.R
import me.jerryokafor.ihenkiri.core.test.util.captureMultiTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(application = HiltTestApplication::class, sdk = [33], qualifiers = "480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
class MovieDetailsTextScreenshotTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun genreChip_multipleThemes() {
        composeTestRule.captureMultiTheme("MovieDetailsText") { desc: String ->
            val text =
                "Terrible Script, dialogue, directing, hammy editing. Music is meh. Poor acting" +
                    " (but they clearly got no support). This crap show's the contempt " +
                    "producers like this have for the audience that gives them money. " +
                    "The writer, director, producers and editor must have been so full of"
            MovieDetailsText(
                text = text, textAlign = TextAlign.Justify,
                modifier = Modifier,
                dialogTitle = stringResource(id = R.string.core_ds_read_more),
                color = Color.Unspecified,
                fontSize = TextUnit.Unspecified,
                fontStyle = null,
                fontWeight = null,
                fontFamily = null,
                letterSpacing = TextUnit.Unspecified,
                textDecoration = null,
                lineHeight = TextUnit.Unspecified,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                maxLines = Int.MAX_VALUE,
                minLines = 1,
                maxCharacters = 200,
                onTextLayout = { _ -> },
                style = LocalTextStyle.current,
            )
        }
    }
}
