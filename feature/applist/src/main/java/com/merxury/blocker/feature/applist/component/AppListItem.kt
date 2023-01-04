/*
 * Copyright 2022 Blocker
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

package com.merxury.blocker.feature.applist.component

import android.content.pm.PackageInfo
import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.merxury.blocker.core.designsystem.theme.BlockerTheme
import com.merxury.blocker.feature.applist.AppServiceStatus
import com.merxury.blocker.feature.applist.R.string

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppListItem(
    label: String,
    packageName: String,
    versionName: String,
    packageInfo: PackageInfo?,
    appServiceStatus: AppServiceStatus?,
    onClick: (String) -> Unit,
    onClearCacheClick: (String) -> Unit,
    onClearDataClick: (String) -> Unit,
    onForceStopClick: (String) -> Unit,
    onUninstallClick: (String) -> Unit,
    onEnableClick: (String) -> Unit,
    onDisableClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = { onClick(packageName) },
                    onLongClick = { expanded = true },
                )
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            AppIcon(packageInfo, iconModifier.size(48.dp))
            Spacer(modifier = Modifier.width(16.dp))
            AppContent(
                label = label,
                versionName = versionName,
                serviceStatus = appServiceStatus
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            contentAlignment = Alignment.TopEnd
        ) {
            AppListItemMenuList(
                expanded = expanded,
                onClearCacheClick = { onClearCacheClick(packageName) },
                onClearDataClick = { onClearDataClick(packageName) },
                onForceStopClick = { onForceStopClick(packageName) },
                onUninstallClick = { onUninstallClick(packageName) },
                onEnableClick = { onEnableClick(packageName) },
                onDisableClick = { onDisableClick(packageName) },
                onDismissRequest = { expanded = false }
            )
        }
    }
}

@Composable
private fun AppIcon(info: PackageInfo?, modifier: Modifier = Modifier) {
    AsyncImage(
        modifier = modifier,
        model = ImageRequest.Builder(LocalContext.current)
            .data(info)
            .crossfade(true)
            .build(),
        contentDescription = null
    )
}

@Composable
private fun AppContent(
    label: String,
    versionName: String,
    serviceStatus: AppServiceStatus?,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = versionName,
            style = MaterialTheme.typography.bodyMedium
        )
        if (serviceStatus != null) {
            Text(
                text = stringResource(
                    id = string.service_status_template,
                    serviceStatus.running,
                    serviceStatus.blocked,
                    serviceStatus.total
                ),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun AppListItemPreview() {
    val appServiceStatus = AppServiceStatus(
        running = 1,
        blocked = 2,
        total = 10,
        packageName = "com.merxury.blocker"
    )
    BlockerTheme {
        Surface {
            AppListItem(
                label = "Blocker",
                packageName = "com.merxury.blocker",
                versionName = "1.0.12",
                packageInfo = PackageInfo(),
                appServiceStatus = appServiceStatus,
                onClick = {},
                onClearCacheClick = { },
                onClearDataClick = { },
                onForceStopClick = { },
                onUninstallClick = { },
                onEnableClick = { },
                onDisableClick = { },
            )
        }
    }
}

@Composable
@Preview
fun AppListItemWithoutServicePreview() {
    BlockerTheme {
        Surface {
            AppListItem(
                label = "Blocker",
                packageName = "com.merxury.blocker",
                versionName = "1.0.12",
                packageInfo = PackageInfo(),
                appServiceStatus = null,
                onClick = {},
                onClearCacheClick = { },
                onClearDataClick = { },
                onForceStopClick = { },
                onUninstallClick = { },
                onEnableClick = { },
                onDisableClick = { },
            )
        }
    }
}

@Composable
@Preview
fun AppListItemWithLongAppName() {
    BlockerTheme {
        Surface {
            AppListItem(
                label = "AppNameWithVeryLongLongLongLongLongLongName",
                packageName = "com.merxury.blocker",
                versionName = "1.0.12",
                packageInfo = PackageInfo(),
                appServiceStatus = null,
                onClick = {},
                onClearCacheClick = { },
                onClearDataClick = { },
                onForceStopClick = { },
                onUninstallClick = { },
                onEnableClick = { },
                onDisableClick = { },
            )
        }
    }
}