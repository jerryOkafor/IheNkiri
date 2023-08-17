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

package me.jerryokafor.ihenkiri.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.ThreeVerticalSpacer
import me.jerryokafor.core.ds.theme.TwoVerticalSpacer
import me.jerryokafor.ihenkiri.R

@ThemePreviews
@Composable
fun LandingScreenPreview() {
    IheNkiriTheme {
        LandingScreen()
    }
}

private const val HALF_WIDTH = 0.5F

@Composable
fun LandingScreen(onSignInClick: () -> Unit = {}, onContinueAsGuestClick: () -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.ic_movie_landing),
            contentScale = ContentScale.FillBounds,
            contentDescription = "",
        )

        @Suppress("MagicNumber")
        val colorStops = arrayOf(
            0.0f to Color(0x000000000),
            0.85f to Color(0xFF000000),
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(colorStops = colorStops)),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(IheNkiri.spacing.twoAndaHalf),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                Image(
                    modifier = Modifier.fillMaxWidth(HALF_WIDTH),
                    painter = painterResource(id = R.drawable.ic_tmdb_logo_long),
                    contentDescription = stringResource(R.string.app_cont_txt_tmdb_logo),
                    colorFilter = ColorFilter.tint(Color.White),
                )
            }
            TwoVerticalSpacer()
            Text(
                text = buildAnnotatedString {
                    append(stringResource(R.string.app_landing_text_everything_about))
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(stringResource(R.string.app_landing_text_extra))
                    }
                },
                color = Color.White,
                style = IheNkiri.typography.headlineLarge,
            )
            ThreeVerticalSpacer()
            Text(
                text = stringResource(R.string.app_landing_sub_text),
                color = Color.White,
                style = IheNkiri.typography.bodyMedium,
            )
            TwoVerticalSpacer()
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = IheNkiri.spacing.two),
                onClick = onSignInClick,
            ) {
                Text(text = stringResource(R.string.app_sign_in))
            }
            TwoVerticalSpacer()

            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = IheNkiri.spacing.two),
                onClick = onContinueAsGuestClick,
            ) {
                Text(
                    text = stringResource(R.string.app_continue_as_guest),
                    color = Color.White,
                )
            }
            TwoVerticalSpacer()
        }
    }
}