package com.hazrat.onedrop.core.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.hazrat.onedrop.R
import com.hazrat.onedrop.auth.presentation.AuthState
import com.hazrat.onedrop.core.navigation.Route
import com.hazrat.onedrop.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */


@Composable
fun HomePageHeaderCard(
    modifier: Modifier = Modifier,
    authState: AuthState
) {
    Box(
        modifier = modifier
            .height(dimens.size300)
            .padding(bottom = dimens.size10)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimens.size250),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                ProfileAndIcons(
                    modifier = Modifier.align(Alignment.Center),
                    authState = authState
                )
            }
        }
        OutlinedTextField(
            value = "",
            onValueChange = {},
            shape = RoundedCornerShape(dimens.size10),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.size20, vertical = dimens.size10)
                .align(Alignment.BottomCenter),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.outline_search),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = dimens.size20)
                        .size(dimens.size30)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            placeholder = { Text("Search Blood") },
            singleLine = true
        )

    }
}

@Composable
fun ProfileAndIcons(
    modifier: Modifier = Modifier,
    authState: AuthState
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size20),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = authState.firebaseUser?.displayName ?: authState.firebaseUserData?.fullName?:"",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle()) {
                        append("Donate Blood: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        append("Off")
                    }
                }
            )
        }
        Spacer(Modifier.weight(1f))
        Icon(painter = painterResource(R.drawable.notificationonn), null)
    }
}


@Composable
fun HomeActivityGrid(
    onActivityClick: (ActivityAs) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size20),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val activity = listOf(
            ActivityAs.BloodDonor,
            ActivityAs.RequestBlood,
        )
        activity.forEach {
            ActivityCards(
                modifier = Modifier,
                icon = it.icon,
                title = it.title,
                subText = it.subText,
            ) {
                onActivityClick(it)
            }
        }
    }
}

@Composable
fun BloodGroupCard(
    modifier: Modifier = Modifier,
    icon: Int,
) {
    Card(
        modifier = Modifier
            .padding(vertical = dimens.size10, horizontal = dimens.size8),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(width = dimens.size1, color = Color.Black.copy(0.5f))
    ) {
        Icon(
            painter = painterResource(icon), contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(dimens.size80)
                .align(Alignment.CenterHorizontally)
                .padding(vertical = dimens.size10)
        )
    }
}

@Preview
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActivityCards(
    modifier: Modifier = Modifier,
    icon: Int = 1,
    title: String = "Blood Donor",
    subText: String = "3 Steps",
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .combinedClickable(
                onClick = { onClick.invoke() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = dimens.size10),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(width = dimens.size1, color = Color.Black.copy(0.5f))
    ) {
        Row(
            modifier = Modifier
                .padding(dimens.size10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(dimens.size35),
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(dimens.size10))
            Column {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(Modifier.height(dimens.size5))
                Text(
                    text = subText,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Composable
fun ContributionCard(
    modifier: Modifier = Modifier,
    cardColor: Color,
    number: String,
    text: String,
    textColor: Color
) {
    Card(
        modifier = modifier.padding(horizontal = dimens.size5, vertical = dimens.size10),
        colors = CardDefaults.cardColors(containerColor = cardColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.size10),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = number,
                style = MaterialTheme.typography.displaySmall,
                color = textColor
            )
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        }
    }
}


@Composable
fun HomePageSectionHeading(
    text: String
) {
    Text(
        text = text,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size20)
    )
}


sealed class BloodGroup(val name: String, val icon: Int) {
    data object APositive : BloodGroup(name = "A+", icon = R.drawable.a_plus)
    data object ANegative : BloodGroup(name = "A-", icon = R.drawable.a_minus)
    data object BPositive : BloodGroup(name = "B+", icon = R.drawable.b_plus)
    data object BNegative : BloodGroup(name = "B-", icon = R.drawable.b_minus)
    data object ABPositive : BloodGroup(name = "AB+", icon = R.drawable.ab_plus)
    data object ABNegative : BloodGroup(name = "AB-", icon = R.drawable.ab_minus)
    data object OPositive : BloodGroup(name = "O+", icon = R.drawable.o_plus)
    data object ONegative : BloodGroup(name = "O-", icon = R.drawable.o_minus)
}

sealed class ActivityAs(
    val icon: Int,
    val title: String,
    val subText: String,
    val route: Route
) {
    data object BloodDonor : ActivityAs(
        icon = R.drawable.blood_donor,
        title = "Blood Donor",
        subText = "120 Posts",
        route = Route.BloodDonorRoute
    )


    data object RequestBlood : ActivityAs(
        icon = R.drawable.create_post,
        title = "Request Blood",
        subText = "3 steps",
        route = Route.RequestBloodRoute
    )

}
