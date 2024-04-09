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

package me.jerryokafor.ihenkiri.navigation

import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import me.jerryokafor.core.data.injection.LocalStorageBinding
import me.jerryokafor.core.data.repository.LocalStorage
import me.jerryokafor.core.model.ThemeConfig
import me.jerryokafor.core.model.UserData
import me.jerryokafor.feature.movies.screen.MOVIES_GRID_ITEMS_TEST_TAG
import me.jerryokafor.ihenkiri.core.network.injection.NetworkAuthModule
import me.jerryokafor.ihenkiri.core.network.service.AuthApi
import me.jerryokafor.ihenkiri.core.test.test.network.FakeAuthApiWithException
import me.jerryokafor.ihenkiri.feature.people.ui.PEOPLE_LIST_TEST_TAG
import me.jerryokafor.ihenkiri.ui.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLog
import kotlin.properties.ReadOnlyProperty
import me.jerryokafor.core.ui.R as CoreUIR
import me.jerryokafor.feature.movies.R as MoviesR
import me.jerryokafor.ihenkiri.feature.moviedetails.R as MoviesDetailsR
import me.jerryokafor.ihenkiri.feature.people.R as PeopleR
import me.jerryokafor.ihenkiri.feature.settings.R as SettingsR
import me.jerryokafor.ihenkiri.feature.tvshows.R as TVShowsR

@RunWith(AndroidJUnit4::class)
@Config(
    application = HiltTestApplication::class,
    sdk = [Build.VERSION_CODES.O],
    instrumentedPackages = ["androidx.loader.content"],
    qualifiers = "xlarge",
)
@UninstallModules(LocalStorageBinding::class, NetworkAuthModule::class)
@HiltAndroidTest
class NavigationTest {
    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
     */
    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    /**
     * Use the primary activity to initialize the app normally.
     */
    @get:Rule(order = 3)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @BindValue
    @JvmField
    val localStorage = mockk<LocalStorage>(relaxed = true) {
        every { isLoggedIn() } returns flowOf(true)
    }

    @BindValue
    @JvmField
    val authApi: AuthApi = FakeAuthApiWithException()

    private fun AndroidComposeTestRule<*, *>.stringResource(
        @StringRes resId: Int,
    ) = ReadOnlyProperty<Any?, String> { _, _ -> activity.getString(resId) }

    // The strings used for matching in these tests
    private val navigateUp by composeTestRule.stringResource(CoreUIR.string.navigate_up)

    // Movies
    private val movies by composeTestRule.stringResource(MoviesR.string.movies)
    private val moviesNowPlaying by composeTestRule.stringResource(MoviesR.string.now_playing)
    private val moviesPopular by composeTestRule.stringResource(MoviesR.string.popular)
    private val moviesTopRated by composeTestRule.stringResource(MoviesR.string.top_rated)
    private val moviesUpComing by composeTestRule.stringResource(MoviesR.string.upcoming)
//    private val moviesDiscover by composeTestRule.stringResource(MoviesR.string.discover)

    // Tv shoes
    private val tvShows by composeTestRule.stringResource(TVShowsR.string.tv_shows)
    private val tvShowsOnThAir by composeTestRule.stringResource(TVShowsR.string.on_the_air)
    private val tvShowsPopular by composeTestRule.stringResource(TVShowsR.string.popular)
    private val tvShowsTopRated by composeTestRule.stringResource(TVShowsR.string.top_rated)
    private val tvShowsDiscover by composeTestRule.stringResource(TVShowsR.string.top_rated)

    private val people by composeTestRule.stringResource(PeopleR.string.people)
    private val settings by composeTestRule.stringResource(SettingsR.string.settings)

    // Watch  trailer
    private val watchTrailer by composeTestRule.stringResource(CoreUIR.string.title_watch_trailer)
    private val addToWatchList by composeTestRule.stringResource(
        MoviesDetailsR.string.add_to_watch_list,
    )
    private val addToBookmark by composeTestRule.stringResource(
        MoviesDetailsR.string.add_to_bookmark,
    )
    private val addToFavourite by composeTestRule.stringResource(
        MoviesDetailsR.string.add_to_favourite,
    )
    private val rateMovie by composeTestRule.stringResource(MoviesDetailsR.string.rate_it)

    @Before
    fun setUp() {
        ShadowLog.stream = System.out
        hiltRule.inject()
    }

    @Test
    fun firstScreen_isMoviesScreen() {
        val scenario = launchActivity<MainActivity>()
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onActivity {
            composeTestRule.apply {
                onNodeWithTag(BOTTOM_NAV_BAR_TEST_TAG, true)
                    .assertExists()
                    .assertIsDisplayed()

                onNode(hasText(movies) and isSelectable()).assertIsSelected()
                onNode(hasText(movies) and isSelectable().not()).assertIsDisplayed()

                onNode(hasText(tvShows) and isSelectable()).assertIsNotSelected()
                onNode(hasText(people) and isSelectable()).assertIsNotSelected()
                onNode(hasText(settings) and isSelectable()).assertIsNotSelected()
            }
        }
    }

    @Test
    fun navigationBar_navigateToPreviouslySelectedMovieFilter_restoresContent() {
        val scenario = launchActivity<MainActivity>()
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onActivity {
            composeTestRule.apply {
                onNode(hasText(movies) and isSelectable()).performClick()
                onNode(hasText(moviesNowPlaying) and isSelectable()).performClick()
                onNode(hasText(moviesPopular) and isSelectable()).performClick()
                onNode(hasText(moviesTopRated) and isSelectable()).performClick()
                onNode(hasText(moviesUpComing) and isSelectable()).performClick()

                onNode(hasText(tvShows) and isSelectable()).performClick()
                onNode(hasText(movies) and isSelectable()).performClick()

                onNode(hasText(moviesUpComing) and isSelectable()).assertIsSelected()
            }
        }
    }

    @Test
    fun navigationBar_navigateToPreviouslyTvShowsFilter_restoresContent() {
        val scenario = launchActivity<MainActivity>()
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onActivity {
            composeTestRule.apply {
                onNode(hasText(tvShows) and isSelectable()).performClick()
                onNode(hasText(tvShowsOnThAir) and isSelectable()).performClick()
                onNode(hasText(tvShowsPopular) and isSelectable()).performClick()
                onNode(hasText(tvShowsTopRated) and isSelectable()).performClick()
                onNode(hasText(tvShowsDiscover) and isSelectable()).performClick()

                onNode(hasText(movies) and isSelectable()).performClick()
                onNode(hasText(tvShows) and isSelectable()).performClick()

                onNode(hasText(tvShowsDiscover) and isSelectable()).assertIsSelected()
            }
        }
    }

    @Test
    fun topLevelDestinations_showsBottomNav() {
        val scenario = launchActivity<MainActivity>()
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onActivity {
            composeTestRule.apply {
                onNodeWithTag(BOTTOM_NAV_BAR_TEST_TAG).assertIsDisplayed()

                onNode(hasText(tvShows) and isSelectable()).performClick()
                onNodeWithTag(BOTTOM_NAV_BAR_TEST_TAG).assertIsDisplayed()

                onNode(hasText(people) and isSelectable()).performClick()
                onNodeWithTag(BOTTOM_NAV_BAR_TEST_TAG).assertIsDisplayed()

                onNode(hasText(settings) and isSelectable()).performClick()
                onNodeWithTag(BOTTOM_NAV_BAR_TEST_TAG).assertIsDisplayed()

                onNode(hasText(movies) and isSelectable()).performClick()

                composeTestRule.onNodeWithTag(MOVIES_GRID_ITEMS_TEST_TAG, useUnmergedTree = true)
                    .onChildren()
                    .onFirst()
                    .performClick()
                waitForIdle()
                onNodeWithTag(BOTTOM_NAV_BAR_TEST_TAG).assertDoesNotExist()

                onNodeWithContentDescription(navigateUp).performClick()
                waitForIdle()
                onNodeWithTag(BOTTOM_NAV_BAR_TEST_TAG).assertExists().assertIsDisplayed()

                onNode(hasText(settings) and isSelectable()).performClick()
                onNode(hasText(people) and isSelectable()).performClick()
                composeTestRule.onNodeWithTag(PEOPLE_LIST_TEST_TAG, useUnmergedTree = true)
                    .onChildren()
                    .onFirst()
                    .performClick()
                waitForIdle()
                onNodeWithTag(BOTTOM_NAV_BAR_TEST_TAG).assertDoesNotExist()

                onNodeWithContentDescription(navigateUp).performClick()
                waitForIdle()
                onNodeWithTag(BOTTOM_NAV_BAR_TEST_TAG).assertExists().assertIsDisplayed()
            }
        }
    }

    @Test
    fun authScreen_hidesBottomNav() {
        every { localStorage.userData() } returns flowOf(
            UserData(
                accountId = "",
                isLoggedIn = false,
                themeConfig = ThemeConfig.DARK,
                usDynamicColor = false,
                name = "Jerry",
                userName = "jerryOkafor",
            ),
        )

        val scenario = launchActivity<MainActivity>()
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onActivity {
            composeTestRule.apply {
                onNode(hasText(settings) and isSelectable()).performClick()
                onNodeWithText("Login")
                    .assertExists()
                    .assertIsDisplayed()
                    .performClick()

                waitForIdle()
                onNodeWithText("Sign In").assertExists().assertIsDisplayed()
                onNodeWithText("Continue as Guest").assertExists().assertIsDisplayed()
                onNodeWithTag(BOTTOM_NAV_BAR_TEST_TAG).assertDoesNotExist()
            }
        }
    }

    @Test
    fun authScreen_guestSessionLoadedWithError_showsSnackbar() {
        every { localStorage.userData() } returns flowOf(
            UserData(
                accountId = "",
                isLoggedIn = false,
                themeConfig = ThemeConfig.DARK,
                usDynamicColor = false,
                name = "Jerry",
                userName = "jerryOkafor",
            ),
        )

        composeTestRule.apply {
            onNode(hasText(settings) and isSelectable()).performClick()
            onNodeWithText("Login")
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            waitForIdle()
            onNodeWithText("Continue as Guest").performClick()
            onNodeWithText("Error creating guest session, please try again")
                .assertIsDisplayed()
        }
    }

    @Test
    fun authScreen_authSessionLoadedWithError_showsSnackbar() {
        every { localStorage.userData() } returns flowOf(
            UserData(
                accountId = "",
                isLoggedIn = false,
                themeConfig = ThemeConfig.DARK,
                usDynamicColor = false,
                name = "Jerry",
                userName = "jerryOkafor",
            ),
        )

        composeTestRule.apply {
            onNode(hasText(settings) and isSelectable()).performClick()
            onNodeWithText("Login")
                .assertExists()
                .assertIsDisplayed()
                .performClick()

            waitForIdle()
            onNodeWithText("Sign In").performClick()
            onNodeWithText("Error creating request token, please try again")
                .assertIsDisplayed()
        }
    }

    @Test
    fun movieDetailsScreen_watchTrailer() {
        val scenario = launchActivity<MainActivity>()
        scenario.moveToState(Lifecycle.State.CREATED)
        scenario.onActivity {
            composeTestRule.apply {
                onNode(hasText(movies) and isSelectable())
                    .performClick()

                waitForIdle()
                onNodeWithTag(MOVIES_GRID_ITEMS_TEST_TAG)
                    .assertIsDisplayed()
                    .onChildren()
                    .onFirst()
                    .performClick()

                waitForIdle()
                onNode(
                    matcher = hasContentDescription(addToWatchList) and hasClickAction(),
                    useUnmergedTree = false,
                ).assertIsDisplayed()
                    .performClick()

                onNode(
                    matcher = hasContentDescription(addToBookmark) and hasClickAction(),
                    useUnmergedTree = false,
                ).assertIsDisplayed()
                    .performClick()

                onNode(
                    matcher = hasContentDescription(addToFavourite) and hasClickAction(),
                    useUnmergedTree = false,
                ).assertIsDisplayed()
                    .performClick()

                onNode(
                    matcher = hasContentDescription(rateMovie) and hasClickAction(),
                    useUnmergedTree = false,
                ).assertIsDisplayed()
                    .performClick()
                onNode(hasText(watchTrailer) and hasClickAction())
                    .assertIsDisplayed()
                    .performClick()

                waitForIdle()
                onNodeWithText("Fight Club")
                    .assertIsDisplayed()
            }
        }
    }
}
