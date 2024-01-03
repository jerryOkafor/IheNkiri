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
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
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
import me.jerryokafor.feature.movies.screen.MOVIES_GRID_ITEMS_TEST_TAG
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
@UninstallModules(LocalStorageBinding::class)
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
    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @BindValue
    @JvmField
    val localStorage = mockk<LocalStorage>(relaxed = true) {
        every { isLoggedIn() } returns flowOf(true)
    }

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
                onNode(hasText(movies) and isSelectable()).assertIsSelected()
                onNode(hasText(movies) and isSelectable().not()).assertIsDisplayed()
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
    fun navigationBar_navigateToPreviouslyTvsShowFilter_restoresContent() {
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
    fun topLevelDestinations_showBottomNav() {
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
}
