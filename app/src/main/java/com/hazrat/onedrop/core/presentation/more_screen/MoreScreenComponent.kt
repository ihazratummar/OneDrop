package com.hazrat.onedrop.core.presentation.more_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.hazrat.onedrop.ui.theme.dimens
import com.hazrat.onedrop.R
import com.hazrat.onedrop.core.domain.model.BloodGroup

/**
 * @author Hazrat Ummar Shaikh
 * Created on 21-12-2024
 */

@Composable
fun TopProfileCard(
    profileName: String,
    bloodGroup: BloodGroup?,
    isBloodDonorProfileExists: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.size150),
        onClick = { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isBloodDonorProfileExists) profileName else "Create Donor Profile",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            Text(
                text = if (isBloodDonorProfileExists) "Blood Group $bloodGroup" else return@Card,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun MoreScreenTabCards(
    modifier: Modifier = Modifier,
    icon: Painter = painterResource(R.drawable.onedrop_logo),
    tabText: String = "Settings",
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimens.size15, horizontal = dimens.size10),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { onClick() }
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(0.4f),
                        shape = CircleShape
                    )
                    .size(dimens.size40),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    modifier = Modifier.size(dimens.size20),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(Modifier.width(dimens.size10))
            Text(
                text = tabText,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            )
            Spacer(Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.arrowright),
                contentDescription = null,
                modifier = Modifier.size(dimens.size30),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        Spacer(Modifier.height(dimens.size15))
    }
}