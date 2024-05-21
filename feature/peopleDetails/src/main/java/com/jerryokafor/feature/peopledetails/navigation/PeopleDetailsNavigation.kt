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
@file:Suppress("TopLevelPropertyNaming")

package com.jerryokafor.feature.peopledetails.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.jerryokafor.feature.peopledetails.ui.PeopleDetailsScreen
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable
import me.jerryokafor.core.ui.navigation.enterTransition
import me.jerryokafor.core.ui.navigation.exitTransition
import me.jerryokafor.core.ui.navigation.popEnterTransition
import me.jerryokafor.core.ui.navigation.popExitTransition

@VisibleForTesting
@Suppress("ktlint:standard:property-naming")
internal const val personIdArg = "personId"

internal class PeopleDetailsArg(val personId: StateFlow<Long>) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        personId = checkNotNull(savedStateHandle.getStateFlow(personIdArg, 0L)),
    )
}

@Serializable
data class PeopleDetails(val personId: Long) {
    companion object {
        val ROUTE_PATTERN = "\\D+/\\{(personId)\\}".toRegex()
    }
}

fun NavController.navigateToPersonDetails(
    personId: Long,
    navOptions: NavOptions? = null,
) {
    this.navigate(route = PeopleDetails(personId), navOptions = navOptions)
}

fun NavGraphBuilder.peopleDetailsScreen(onNavigateUp: () -> Unit) {
    composable<PeopleDetails>(
        enterTransition = enterTransition,
        exitTransition = exitTransition,
        popEnterTransition = popEnterTransition,
        popExitTransition = popExitTransition,
    ) {
        PeopleDetailsScreen(onNavigateUp = onNavigateUp)
    }
}
