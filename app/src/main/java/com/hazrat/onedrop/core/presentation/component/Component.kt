package com.hazrat.onedrop.core.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.hazrat.onedrop.R
import com.hazrat.onedrop.auth.presentation.ProfileState
import com.hazrat.onedrop.auth.presentation.common.buttonShimmerEffect
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.domain.model.BloodGroup
import com.hazrat.onedrop.core.navigation.MainRoute
import com.hazrat.onedrop.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 */


@Composable
fun HomePageHeaderCard(
    modifier: Modifier = Modifier,
    profileState: ProfileState
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
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                ProfileAndIcons(
                    modifier = Modifier.align(Alignment.Center),
                    profileState = profileState
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
    profileState: ProfileState
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size20),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = profileState.userData?.fullName ?: "",
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
    onActivityClick: (ActivityAs) -> Unit,
    bloodDonorList: List<BloodDonorModel>
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size20),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val activity = listOf(
            ActivityAs.BloodDonor(bloodDonorList),
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


sealed class ActivityAs(
    val icon: Int,
    val title: String,
    val subText: String,
    val route: MainRoute
) {
    data class BloodDonor(val bloodDonorList: List<BloodDonorModel>) : ActivityAs(
        icon = R.drawable.blood_donor,
        title = "Blood Donor",
        subText = bloodDonorList.size.toString(),
        route = MainRoute.BloodDonorRoute
    )


    data object RequestBlood : ActivityAs(
        icon = R.drawable.request_blood,
        title = "Request Blood",
        subText = "3 steps",
        route = MainRoute.RequestBloodScreenRoute
    )

}


@Composable
fun BasicAppBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    title: String,
    action: @Composable () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimens.size10),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.arrowleft),
            contentDescription = null,
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(dimens.size10)
        )
        Spacer(Modifier.width(dimens.size10))
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.weight(1f))
        action()
    }

}


@Composable
fun BloodDonorsCards(
    isContactPrivate: Boolean = false,
    isAddressPrivate: Boolean = false,
    donorList: BloodDonorModel = BloodDonorModel(),
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable{
                onClick()
            }
            .height(dimens.size150)
            .padding(vertical = dimens.size5, horizontal = dimens.size8),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
    ) {

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.size10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = when (donorList.bloodGroup) {
                    BloodGroup.A_POSITIVE -> painterResource(R.drawable.a_plus)
                    BloodGroup.A_NEGATIVE -> painterResource(R.drawable.a_minus)
                    BloodGroup.B_POSITIVE -> painterResource(R.drawable.b_plus)
                    BloodGroup.B_NEGATIVE -> painterResource(R.drawable.b_minus)
                    BloodGroup.AB_POSITIVE -> painterResource(R.drawable.ab_plus)
                    BloodGroup.AB_NEGATIVE -> painterResource(R.drawable.ab_minus)
                    BloodGroup.O_POSITIVE -> painterResource(R.drawable.o_plus)
                    BloodGroup.O_NEGATIVE -> painterResource(R.drawable.o_minus)
                    null -> painterResource(R.drawable.onedrop_logo)
                },
                contentDescription = null,
                modifier = Modifier
                    .size(dimens.size60),
                tint = Color.Unspecified
            )
            Spacer(Modifier.width(dimens.size20))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    donorList.name,
                    style = MaterialTheme.typography.titleLarge
                )
                if (!isContactPrivate) {
                    Text(
                        donorList.contactNumber,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                if (!isAddressPrivate) {
                    Text(
                        "${donorList.city} , ${donorList.district} , ${donorList.state}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegisterAsDonorCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String = "Register as a blood donor?"
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimens.size10)
            .combinedClickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = { onClick() }
            ),
        colors = CardDefaults.cardColors(
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(dimens.size10)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimens.size10, vertical = dimens.size20),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.width(dimens.size35))
            Icon(
                painter = painterResource(R.drawable.onedrop_logo), null,
                modifier = Modifier.size(dimens.size30),
                tint = Color.Unspecified
            )
            Spacer(Modifier.weight(1f))
        }
    }

}


@Preview
@Composable
fun CardLoadingAnimation() {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimens.size150)
            .padding(vertical = dimens.size5, horizontal = dimens.size8),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimens.size10),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .buttonShimmerEffect(isRounded = true)
                    .size(dimens.size60)
            )

            Spacer(Modifier.width(dimens.size20))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceAround

            ) {
                Box(
                    modifier = Modifier
                        .padding(vertical = dimens.size10)
                        .fillMaxWidth(fraction = 0.8f)
                        .height(dimens.size20)
                        .buttonShimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .padding(vertical = dimens.size10)
                        .fillMaxWidth(fraction = 0.5f)
                        .height(dimens.size15)
                        .buttonShimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .padding(vertical = dimens.size10)
                        .fillMaxWidth()
                        .height(dimens.size10)
                        .buttonShimmerEffect()
                )

            }
        }
    }
}