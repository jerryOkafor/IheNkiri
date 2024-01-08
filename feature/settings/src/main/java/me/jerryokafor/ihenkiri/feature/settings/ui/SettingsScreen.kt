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

@file:Suppress("TooManyFunctions")

package me.jerryokafor.ihenkiri.feature.settings.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import me.jerryokafor.core.common.annotation.ExcludeFromGeneratedCoverageReport
import me.jerryokafor.core.ds.annotation.ThemePreviews
import me.jerryokafor.core.ds.components.IhenkiriButton
import me.jerryokafor.core.ds.theme.FourVerticalSpacer
import me.jerryokafor.core.ds.theme.IheNkiri
import me.jerryokafor.core.ds.theme.IheNkiriTheme
import me.jerryokafor.core.ds.theme.OneAndHalfHorizontalSpacer
import me.jerryokafor.core.ds.theme.OneHorizontalSpacer
import me.jerryokafor.core.ds.theme.OneVerticalSpacer
import me.jerryokafor.core.ds.theme.TwoAndHalfHorizontalSpacer
import me.jerryokafor.core.ds.theme.TwoHorizontalSpacer
import me.jerryokafor.core.ds.theme.TwoVerticalSpacer
import me.jerryokafor.core.ds.theme.supportsDynamicTheming
import me.jerryokafor.core.model.ThemeConfig
import me.jerryokafor.core.model.UserEditableSettings
import me.jerryokafor.core.ui.R
import me.jerryokafor.core.ui.components.Background
import me.jerryokafor.core.ui.widget.MovieRating
import me.jerryokafor.ihenkiri.feature.settings.R.string
import me.jerryokafor.ihenkiri.feature.settings.viewModel.SettingsUiState
import me.jerryokafor.ihenkiri.feature.settings.viewModel.SettingsViewModel

const val MORE_TITLE_TEST_TAG = "more_title"

@Composable
@ThemePreviews
@ExcludeFromGeneratedCoverageReport
fun SettingsScreenPreview() {
    IheNkiriTheme {
        SettingsScreen(
            settingsUiState = SettingsUiState.Success(UserEditableSettings()),
            onChangeDynamicColorPreference = {},
            onChangeDarkThemeConfig = {},
            onLoginClick = {},
            onLogoutClick = {},
        )
    }
}

@Composable
@Suppress("UnusedPrivateMember")
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onLoginClick: () -> Unit,
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    val onLogoutClick: () -> Unit = { viewModel.onLogout() }
    val onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit = {
        viewModel.onChangeDynamicColorPreference(it)
    }
    val onChangeDarkThemeConfig: (themeConfig: ThemeConfig) -> Unit = {
        viewModel.onThemeConfig(it)
    }
    SettingsScreen(
        settingsUiState = settingsUiState,
        onChangeDynamicColorPreference = onChangeDynamicColorPreference,
        onChangeDarkThemeConfig = onChangeDarkThemeConfig,
        onLoginClick = onLoginClick,
        onLogoutClick = onLogoutClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@ExcludeFromGeneratedCoverageReport
fun SettingsScreen(
    settingsUiState: SettingsUiState,
    supportDynamicColor: Boolean = supportsDynamicTheming(),
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (themeConfig: ThemeConfig) -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    var showThemeSettingsDialog by remember { mutableStateOf(false) }

    Background {
        Column(modifier = Modifier.fillMaxSize()) {
            CenterAlignedTopAppBar(
                modifier = Modifier.testTag(MORE_TITLE_TEST_TAG),
                title = {
                    Text(
                        text = stringResource(string.settings),
                        style = IheNkiri.typography.titleMedium,
                        color = IheNkiri.color.onPrimary,
                    )
                },
                windowInsets = TopAppBarDefaults.windowInsets,
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                ),
            )
            TwoAndHalfHorizontalSpacer()
            Box {
                when (settingsUiState) {
                    is SettingsUiState.Success -> {
                        val preference = settingsUiState.preference
                        val configuration = LocalConfiguration.current

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            item { UserDetails(profilePath = "") }
                            item { MovieAndTVScore() }
                            item { UserAccountStat() }
                            item {
                                UserAccountExtraSettings(
                                    isLoggedIn = preference.isLoggedIn,
                                    onLoginClick = onLoginClick,
                                    onLogoutClick = onLogoutClick,
                                    onChangeTheme = { showThemeSettingsDialog = true },
                                )
                            }
                        }

                        if (showThemeSettingsDialog) {
                            AlertDialog(
                                properties = DialogProperties(usePlatformDefaultWidth = false),
                                modifier = Modifier.widthIn(
                                    max = configuration.screenWidthDp.dp - 80.dp,
                                ),
                                onDismissRequest = { showThemeSettingsDialog = false },
                                title = {
                                    Text(
                                        text = stringResource(string.theme_settings),
                                        style = MaterialTheme.typography.titleLarge,
                                    )
                                },
                                text = {
                                    Divider()
                                    Column(Modifier.verticalScroll(rememberScrollState())) {
                                        SettingsPanel(
                                            themeConfig = preference.themeConfig,
                                            supportDynamicColor = supportDynamicColor,
                                            useDynamicColor = preference.isDynamicColor,
                                            onChangeDynamicColorPreference =
                                            onChangeDynamicColorPreference,
                                            onChangeDarkThemeConfig = onChangeDarkThemeConfig,
                                        )

                                        Divider(Modifier.padding(top = 8.dp))
                                    }
                                },
                                confirmButton = {
                                    Text(
                                        text = stringResource(string.dismiss_dialog_button_text),
                                        style = MaterialTheme.typography.labelLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp)
                                            .clickable { showThemeSettingsDialog = false },
                                    )
                                },
                            )
                        }
                    }

                    SettingsUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(30.dp)
                                .padding(vertical = IheNkiri.spacing.one),
                            strokeWidth = 1.dp,
                            strokeCap = StrokeCap.Round,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.SettingsPanel(
    themeConfig: ThemeConfig,
    useDynamicColor: Boolean,
    supportDynamicColor: Boolean,
    onChangeDynamicColorPreference: (useDynamicColor: Boolean) -> Unit,
    onChangeDarkThemeConfig: (themeConfig: ThemeConfig) -> Unit,
) {
    AnimatedVisibility(visible = supportDynamicColor) {
        Column {
            SettingsDialogSectionTitle(text = stringResource(string.dynamic_color_preference))
            Column(Modifier.selectableGroup()) {
                SettingsDialogThemeChooserRow(
                    text = stringResource(string.dynamic_color_yes),
                    selected = useDynamicColor,
                    onClick = { onChangeDynamicColorPreference(true) },
                )
                SettingsDialogThemeChooserRow(
                    text = stringResource(string.dynamic_color_no),
                    selected = !useDynamicColor,
                    onClick = { onChangeDynamicColorPreference(false) },
                )
            }
        }
    }
    SettingsDialogSectionTitle(text = stringResource(string.theme_preference))
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(string.theme_config_system_default),
            selected = themeConfig == ThemeConfig.FOLLOW_SYSTEM,
            onClick = { onChangeDarkThemeConfig(ThemeConfig.FOLLOW_SYSTEM) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(string.theme_config_light),
            selected = themeConfig == ThemeConfig.LIGHT,
            onClick = { onChangeDarkThemeConfig(ThemeConfig.LIGHT) },
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(string.theme_config_dark),
            selected = themeConfig == ThemeConfig.DARK,
            onClick = { onChangeDarkThemeConfig(ThemeConfig.DARK) },
        )
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = IheNkiri.spacing.two, bottom = IheNkiri.spacing.half),
    )
}

@Composable
private fun ChangeThemeButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier.semantics(true) { },
        onClick = onClick,
        shape = IheNkiri.shape.small,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = IheNkiri.spacing.one, vertical = IheNkiri.spacing.oneAndHalf),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.ColorLens,
                contentDescription = stringResource(string.change_theme),
            )
            OneHorizontalSpacer()
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = stringResource(string.theme_settings),
                    style = IheNkiri.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = stringResource(string.choose_from_light_or_dark_theme),
                    style = IheNkiri.typography.labelMedium,
                )
            }
        }
    }
}

@Composable
private fun UserDetails(profilePath: String) {
    val isLocalInspection = LocalInspectionMode.current
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(profilePath)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.FillWidth,
        onState = { state ->
            isLoading = state is AsyncImagePainter.State.Loading
            isError = state is AsyncImagePainter.State.Error
        },
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = IheNkiri.spacing.two),
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .aspectRatio(1F)
                .align(Alignment.CenterHorizontally),
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        color = IheNkiri.color.tertiaryContainer.copy(alpha = 0.5F),
                        shape = CircleShape,
                    ),
                painter = if (isError.not() && !isLocalInspection) {
                    imageLoader
                } else {
                    painterResource(R.drawable.ic_avatar)
                },
                contentScale = ContentScale.Inside,
                contentDescription = null,
            )

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.Center)
                        .padding(IheNkiri.spacing.twoAndaHalf),
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 1.dp,
                )
            }
        }
        OneVerticalSpacer()
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Jerry Okafor",
            style = IheNkiri.typography.displaySmall,
            color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
            minLines = 1,
            maxLines = 1,
            textAlign = TextAlign.Center,
        )
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Member since November 2016",
            style = IheNkiri.typography.labelSmall,
            color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
            minLines = 1,
            maxLines = 1,
            textAlign = TextAlign.Center,
        )
        TwoVerticalSpacer()
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "A software engineer! Member since November 2016",
            style = IheNkiri.typography.bodyMedium,
            color = contentColorFor(backgroundColor = IheNkiri.color.inverseOnSurface),
            minLines = 1,
            maxLines = 1,
            textAlign = TextAlign.Start,
        )
    }
}

@Composable
private fun UserAccountExtraSettings(
    isLoggedIn: Boolean,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onChangeTheme: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = IheNkiri.spacing.two),
    ) {
        SettingsDialogSectionTitle(text = stringResource(string.settings))
        Divider(color = IheNkiri.color.onPrimary.copy(alpha = 0.5F))

        TwoVerticalSpacer()
        ChangeThemeButton(onClick = onChangeTheme)

        FourVerticalSpacer()
        Crossfade(
            targetState = isLoggedIn,
            label = "preference_isLoggedIn",
        ) {
            when (it) {
                true -> IhenkiriButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onLogoutClick,
                ) {
                    Text(text = stringResource(string.logout))
                }

                false -> IhenkiriButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onLoginClick,
                ) {
                    Text(text = stringResource(string.login))
                }
            }
        }
    }
}

@Composable
private fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp)
            .clip(IheNkiri.shape.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
private fun MovieAndTVScore() {
    TwoVerticalSpacer()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = IheNkiri.spacing.two),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1F),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            MovieRating(
                modifier = Modifier.size(57.dp),
                rating = 0.45F,
            )
            OneAndHalfHorizontalSpacer()
            Text(
                text = "Average \nMovie Score",
                maxLines = 2,
                minLines = 2,
            )
        }
        TwoHorizontalSpacer()
        Row(
            modifier = Modifier.weight(1F),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            MovieRating(
                modifier = Modifier.size(57.dp),
                rating = 0.45F,
            )
            OneAndHalfHorizontalSpacer()
            Text(
                text = "Average \nTV Score",
                maxLines = 2,
                minLines = 2,
            )
        }
    }
}

@Composable
private fun UserAccountStat() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = IheNkiri.spacing.two),
    ) {
        OneVerticalSpacer()
        SettingsDialogSectionTitle(text = "Stats")
        Divider(color = IheNkiri.color.onPrimary.copy(alpha = 0.2F))
        TwoVerticalSpacer()
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(string.total_edits),
                    style = IheNkiri.typography.labelMedium,
                )
                Text(
                    text = "0",
                    style = IheNkiri.typography.displayLarge,
                    color = IheNkiri.color.secondaryContainer,
                )
            }
            TwoHorizontalSpacer()
            Column(
                modifier = Modifier.weight(1F),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = stringResource(string.total_ratings),
                    style = IheNkiri.typography.labelMedium,
                )
                Text(
                    text = "0",
                    style = IheNkiri.typography.displayLarge,
                    color = IheNkiri.color.secondaryContainer,
                )
            }
        }
    }
}
