package com.hazrat.onedrop.core.presentation.more_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.hazrat.onedrop.R
import com.hazrat.onedrop.auth.presentation.AuthEvent
import com.hazrat.onedrop.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 06-12-2024
 */

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    authEvent: (AuthEvent) -> Unit,
    clearAllState: () -> Unit,
    moreScreenState: MoreScreenState,
    onProfileClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {}
) {

    val aboutTab = listOf(
        TabInfo(
            icon = painterResource(R.drawable.info),
            tabText = "About Us",
            onClick = {}
        ),
        TabInfo(
            icon = painterResource(R.drawable.faq),
            tabText = "Faq",
            onClick = {}
        ),
        TabInfo(
            icon = painterResource(R.drawable.message),
            tabText = "Contact Us",
            onClick = {}
        )
    )

    val socialTab = listOf(
        TabInfo(
            icon = painterResource(R.drawable.share),
            tabText = "Invite a Friend",
            onClick = {}
        ),
        TabInfo(
            icon = painterResource(R.drawable.star),
            tabText = "Rate Us",
            onClick = {}
        )
    )

    val reportTab = listOf(
        TabInfo(
            icon = painterResource(R.drawable.feedback),
            tabText = "Submit Feedback",
            onClick = {}
        ),
        TabInfo(
            icon = painterResource(R.drawable.report),
            tabText = "Report",
            onClick = {}
        )
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = dimens.size20)
        ) {
            item {
                TopProfileCard(
                    profileName = if (moreScreenState.isBloodDonorProfileExists) moreScreenState.profileName?:"" else "Create Donor Profile",
                    bloodGroup = moreScreenState.bloodGroup,
                    onClick = { onProfileClick() },
                    isBloodDonorProfileExists = moreScreenState.isBloodDonorProfileExists
                )
                Spacer(Modifier.height(dimens.size20))
            }
            item {
                MoreScreenTabCards(
                    icon = painterResource(R.drawable.setting),
                    tabText = "Settings",
                    onClick = { onSettingsClick() }
                )
                Spacer(Modifier.height(dimens.size10))
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    )
                ) {
                    aboutTab.forEach {
                        MoreScreenTabCards(
                            icon = it.icon,
                            tabText = it.tabText,
                            onClick = { it.onClick }
                        )
                    }
                }
                Spacer(Modifier.height(dimens.size10))
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                    )
                ) {
                    socialTab.forEach {
                        MoreScreenTabCards(
                            icon = it.icon,
                            tabText = it.tabText,
                            onClick = { it.onClick }
                        )
                    }
                }
                Spacer(Modifier.height(dimens.size10))
            }
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer.copy(0.4f)
                    )
                ) {
                    reportTab.forEach {
                        MoreScreenTabCards(
                            icon = it.icon,
                            tabText = it.tabText,
                            onClick = { it.onClick }
                        )
                    }
                }
                Spacer(Modifier.height(dimens.size10))
            }
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimens.size20),
                    onClick = {
                        authEvent(AuthEvent.SignOut)
                        clearAllState()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(dimens.size10)
                ) {
                    Text("Sign Out")
                }
            }
        }
    }


}


data class TabInfo(
    val icon: Painter,
    val tabText: String,
    val onClick: () -> Unit
)