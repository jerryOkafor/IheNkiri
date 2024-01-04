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

package me.jerryokafor.ihenkiri.feature.auth.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.core.util.Consumer
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.common.util.Constants
import me.jerryokafor.core.common.util.getActivity
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.components.IhenkiriButton
import me.jerryokafor.core.ds.components.IhenkiriTextButton
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.ThreeVerticalSpacer
import me.jerryokafor.core.ds.theme.TwoVerticalSpacer
import me.jerryokafor.ihenkiri.feature.auth.R
import me.jerryokafor.ihenkiri.feature.auth.navigation.LANDING_SCREEN_TEST_TAG
import me.jerryokafor.ihenkiri.feature.auth.viewmodel.AuthState
import me.jerryokafor.ihenkiri.feature.auth.viewmodel.AuthViewModel

@ThemePreviews
@Composable
@ExcludeFromGeneratedCoverageReport
fun AuthScreenPreview() {
    IheNkiriTheme {
        AuthScreen {}
    }
}

private const val HALF_WIDTH = 0.5F

@Composable
fun AuthScreen(
    authViewModel: AuthViewModel = hiltViewModel(),
    onCompleteLogin: () -> Unit,
) {
    val authState by authViewModel.authState.collectAsStateWithLifecycle()

    val onSignInClick: () -> Unit = {
        authViewModel.createRequestToken()
    }
    val onContinueAsGuestClick: () -> Unit = {
        authViewModel.createGuestSession()
    }

    AuthScreen(
        authState = authState,
        onSignInClick = onSignInClick,
        onContinueAsGuestClick = onContinueAsGuestClick,
    )

    val context = LocalContext.current
    val activity = (context.getActivity() as ComponentActivity)

    DisposableEffect(Unit) {
        val listener = Consumer<Intent> {
            val data: Uri? = it?.data
            if (data != null && data.path == "/auth") {
                // create session id here
                authViewModel.createSessionId()
            }
        }
        activity.addOnNewIntentListener(listener)
        onDispose { activity.removeOnNewIntentListener(listener) }
    }

    fun launchTMDBAuth(uri: Uri) {
        val intent: CustomTabsIntent = CustomTabsIntent.Builder().build()
        intent.launchUrl(context, uri)
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.RequestTokenCreated -> {
                val token = (authState as AuthState.RequestTokenCreated).requestToken
                val uri = Uri.parse(Constants.TMDB_BASE_AUTH_URL).buildUpon()
                    .appendQueryParameter("request_token", token)
                    .appendQueryParameter("redirect_to", Constants.AUTH_REDIRECT_URL)
                    .build()
                launchTMDBAuth(uri)
            }

            is AuthState.Error -> {
                Toast.makeText(context, (authState as AuthState.Error).message, Toast.LENGTH_SHORT)
                    .show()
            }

            AuthState.CompleteLogin -> {
                onCompleteLogin()
            }

            else -> {}
        }
    }
}

@Composable
fun AuthScreen(
    authState: AuthState,
    onSignInClick: () -> Unit = {},
    onContinueAsGuestClick: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .testTag(LANDING_SCREEN_TEST_TAG)
            .fillMaxSize(),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.ic_movie_landing),
            contentScale = ContentScale.FillBounds,
            contentDescription = "auth_banner",
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

            val isLoading = authState == AuthState.LoadingSession
            val isGuestSessionLoading = authState == AuthState.LoadingGuestSession

            IhenkiriButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = IheNkiri.spacing.two),
                onClick = onSignInClick,
                isLoading = isLoading,
                enabled = !isLoading && !isGuestSessionLoading,
            ) {
                Text(text = stringResource(R.string.app_sign_in))
            }

            TwoVerticalSpacer()
            IhenkiriTextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = IheNkiri.spacing.two),
                isLoading = isGuestSessionLoading,
                enabled = !isLoading && !isGuestSessionLoading,
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
