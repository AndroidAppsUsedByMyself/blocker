/*
 * Copyright 2023 Blocker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.merxury.blocker.feature.ruledetail.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.merxury.blocker.core.decoder.StringDecoder
import com.merxury.blocker.core.ui.rule.RuleDetailTabs
import com.merxury.blocker.feature.ruledetail.RuleDetailRoute

@VisibleForTesting
internal const val ruleArg = "rule"

@VisibleForTesting
internal const val tabArg = "tab"

internal class RuleArgs(val rule: String, val tabs: RuleDetailTabs = RuleDetailTabs.Description) {
    constructor(savedStateHandle: SavedStateHandle, stringDecoder: StringDecoder) :
        this(
            stringDecoder.decodeString(checkNotNull(savedStateHandle[ruleArg])),
            RuleDetailTabs.fromName(savedStateHandle[tabArg]),
        )
}

fun NavController.navigateToRuleDetailScreen(rule: String, tab: RuleDetailTabs.Description) {
    val encodedId = android.net.Uri.encode(rule)
    this.navigate("rule_detail_route/$encodedId?screen=${tab.name}") {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
    }
}

fun NavGraphBuilder.ruleDetailScreen(onBackClick: () -> Unit) {
    composable(
        route = "rule_detail_route/{$ruleArg}?screen={$tabArg}",
        arguments = listOf(
            navArgument(ruleArg) { type = NavType.StringType },
            navArgument(tabArg) { type = NavType.StringType },
        ),
    ) {
        RuleDetailRoute(onBackClick)
    }
}
