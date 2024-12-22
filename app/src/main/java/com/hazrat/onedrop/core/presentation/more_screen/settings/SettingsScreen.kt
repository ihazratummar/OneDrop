package com.hazrat.onedrop.core.presentation.more_screen.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hazrat.onedrop.R
import com.hazrat.onedrop.core.presentation.more_screen.MoreScreenTabCards
import com.hazrat.onedrop.core.presentation.more_screen.TabInfo
import com.hazrat.onedrop.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 21-12-2024
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    settingScreenState: SettingScreenState,
    settingsEvent: (SettingsEvent) -> Unit
) {

    val settingsTabs = listOf(
        TabInfo(
            icon = painterResource(R.drawable.notificationonn),
            tabText = "Notification",
            onClick = {}
        ),
        TabInfo(
            icon = painterResource(R.drawable.privacy),
            tabText = "Privacy Settings",
            onClick = {}
        ),
    )

    val deleteAccountTabs = listOf(
        TabInfo(
            icon = painterResource(R.drawable.deletedonor),
            tabText = "Delete Donor Profile",
            onClick = {}
        ),
        TabInfo(
            icon = painterResource(R.drawable.delete),
            tabText = "Delete Account",
            onClick = {}
        ),
    )

    Scaffold(
        contentWindowInsets = WindowInsets(top = 0.dp, bottom = 0.dp),
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(
                        onClick = { onBackClick() }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.arrowleft),
                            contentDescription = null
                        )
                    }
                },
                windowInsets = WindowInsets(top = dimens.size20)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding()
                )
                .padding(horizontal = dimens.size20)
                .fillMaxSize()
        ) {
            item {
                SettingTabTabCards(
                    modifier = Modifier
                        .fillMaxWidth(),
                    tabText = "Theme Mode",
                    onClick = {
                        settingsEvent(
                            SettingsEvent.ToggleThemeDropDown
                        )
                    },
                    isChecked = settingScreenState.selectedTheme
                )
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    )
                ) {
                    settingsTabs.forEach{
                        MoreScreenTabCards(
                            icon = it.icon,
                            tabText = it.tabText,
                            onClick = { it.onClick }
                        )
                    }
                }
                Spacer(Modifier.height(dimens.size10))
            }
            if (settingScreenState.isDonorProfileExists){
                item{
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer.copy(0.3f)
                        )
                    ) {
                        deleteAccountTabs.forEach{
                            MoreScreenTabCards(
                                icon = it.icon,
                                tabText = it.tabText,
                                onClick = { it.onClick }
                            )
                        }
                    }
                }
            }
        }
    }
}