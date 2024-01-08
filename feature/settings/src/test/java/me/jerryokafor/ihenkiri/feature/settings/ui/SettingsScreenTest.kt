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

package me.jerryokafor.ihenkiri.feature.settings.ui

import android.os.Build
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import coil.Coil
import com.google.common.truth.Truth.assertThat
import me.jerryokafor.core.model.ThemeConfig
import me.jerryokafor.core.model.UserEditableSettings
import me.jerryokafor.ihenkiri.core.test.rule.assertAreDisplayed
import me.jerryokafor.ihenkiri.core.test.util.imageLoader
import me.jerryokafor.ihenkiri.feature.settings.viewModel.SettingsUiState
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog

@RunWith(AndroidJUnit4::class)
@Config(
    sdk = [Build.VERSION_CODES.S],
    instrumentedPackages = ["androidx.loader.content"],
    qualifiers = "xlarge",
)
class SettingsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private var onLoginClick = 0
    private var onLogoutClick = 0
    private var onChangeDynamicColorPreference = 0
    private var onChangeDarkThemeConfig = 0

    @Before
    @Throws(Exception::class)
    fun setUp() {
        Coil.setImageLoader(imageLoader)
        ShadowLog.stream = System.out
    }

    @Test
    fun settingsScreen_settingsUiStateLoading_progressIndicatorIsShow() {
        composeTestRule.apply {
            setContent {
                SettingsScreen(
                    settingsUiState = SettingsUiState.Loading,
                    onChangeDynamicColorPreference = { onChangeDynamicColorPreference++ },
                    onChangeDarkThemeConfig = { onChangeDarkThemeConfig++ },
                    onLoginClick = { onLoginClick++ },
                    onLogoutClick = { onLogoutClick++ },
                )
            }
            onNode(hasProgressBarRangeInfo(ProgressBarRangeInfo.Indeterminate))
                .assertIsDisplayed()
        }
    }

    @Test
    fun settingsScreen_onLoad_settingsScreenIsShownWithLoginButton() {
        composeTestRule.apply {
            setContent {
                SettingsScreen(
                    settingsUiState = SettingsUiState.Success(
                        preference = UserEditableSettings(
                            isLoggedIn = false,
                            themeConfig = ThemeConfig.DARK,
                            isDynamicColor = false,
                        ),
                    ),
                    onChangeDynamicColorPreference = { onChangeDynamicColorPreference++ },
                    onChangeDarkThemeConfig = { onChangeDarkThemeConfig++ },
                    onLoginClick = { onLoginClick++ },
                    onLogoutClick = { onLogoutClick++ },
                )
            }

            onNodeWithText("Jerry Okafor").assertIsDisplayed()
            onNodeWithText("Member since November 2016").assertIsDisplayed()
            onNodeWithText("A software engineer! Member since November 2016").assertIsDisplayed()
            onNodeWithText("Stats").assertIsDisplayed()
            onAllNodesWithText("Settings").assertCountEquals(2).assertAreDisplayed()

            onNodeWithText("Login").assertIsDisplayed().performClick()
        }

        assertThat(onLoginClick).isEqualTo(1)
    }

    @Test
    fun settingsScreen_onLoad_settingsScreenIsShownWithLogoutButton() {
        composeTestRule.apply {
            setContent {
                SettingsScreen(
                    settingsUiState = SettingsUiState.Success(
                        preference = UserEditableSettings(
                            isLoggedIn = true,
                            themeConfig = ThemeConfig.DARK,
                            isDynamicColor = false,
                        ),
                    ),
                    onChangeDynamicColorPreference = { onChangeDynamicColorPreference++ },
                    onChangeDarkThemeConfig = { onChangeDarkThemeConfig++ },
                    onLoginClick = { onLoginClick++ },
                    onLogoutClick = { onLogoutClick++ },
                )
            }
            onNodeWithText("Logout").assertIsDisplayed().performClick()
        }

        assertThat(onLogoutClick).isEqualTo(1)
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun settingsScreen_onChangeThemeButtonClick_showChangeThemeDialog() {
        composeTestRule.apply {
            setContent {
                SettingsScreen(
                    settingsUiState = SettingsUiState.Success(
                        preference = UserEditableSettings(
                            isLoggedIn = false,
                            themeConfig = ThemeConfig.DARK,
                            isDynamicColor = true,
                        ),
                    ),
                    supportDynamicColor = true,
                    onChangeDynamicColorPreference = { onChangeDynamicColorPreference++ },
                    onChangeDarkThemeConfig = { onChangeDarkThemeConfig++ },
                    onLoginClick = { onLoginClick++ },
                    onLogoutClick = { onLogoutClick++ },
                )
            }

            onNodeWithContentDescription("Change theme", true).assertIsDisplayed()
                .performClick()
            waitForIdle()
            onNode(isDialog()).assertExists()
            waitUntilNodeCount(hasText("Use Dynamic Color"), 1)
            onNodeWithText("Use Dynamic Color", useUnmergedTree = true).assertIsDisplayed()
            onNode(hasText("Yes") and isSelectable()).assertIsDisplayed().assertIsSelected()
            onNode(hasText("No") and isSelectable()).assertIsDisplayed().assertIsNotSelected()
                .performClick()

            onNodeWithText("Dark Mode Preference", useUnmergedTree = true).assertIsDisplayed()
            onNode(hasText("Dark") and isSelectable()).assertIsDisplayed().assertIsSelected()
            onNode(hasText("Light") and isSelectable()).assertIsDisplayed().assertIsNotSelected()
            onNode(hasText("System default") and isSelectable()).assertIsDisplayed()
                .assertIsNotSelected().performClick()
            onNodeWithText("OK").assertIsDisplayed().performClick()
            onNode(isDialog()).assertDoesNotExist()
        }

        assertThat(onChangeDynamicColorPreference).isEqualTo(1)
        assertThat(onChangeDarkThemeConfig).isEqualTo(1)
    }
}
