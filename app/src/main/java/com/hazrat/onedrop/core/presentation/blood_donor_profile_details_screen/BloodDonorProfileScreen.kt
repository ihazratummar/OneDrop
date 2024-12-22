package com.hazrat.onedrop.core.presentation.blood_donor_profile_details_screen

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.presentation.component.BasicAppBar
import com.hazrat.onedrop.ui.theme.dimens
import com.hazrat.onedrop.R
import com.hazrat.onedrop.auth.presentation.common.shimmerEffect


/**
 * @author Hazrat Ummar Shaikh
 * Created on 09-12-2024
 */

@Composable
fun BloodDonorProfileScreen(
    modifier: Modifier = Modifier,
    bloodDonorModel: BloodDonorModel?,
    onBackClick: () -> Unit,
    profileEvent: (BloodDonorProfileEvent) -> Unit
) {

    val aboutList = listOf(
        AboutRowData(
            painter = painterResource(R.drawable.age),
            text = bloodDonorModel?.age ?: "Unknown",
            labelText = "Age"
        ),
        AboutRowData(
            painter = painterResource(R.drawable.gender),
            text = bloodDonorModel?.gender.toString(),
            labelText = "Gender"
        ),
        AboutRowData(
            painter = painterResource(R.drawable.city),
            text = bloodDonorModel?.city ?: "Unknown",
            labelText = "City"
        ),
        AboutRowData(
            painter = painterResource(R.drawable.map),
            text = bloodDonorModel?.district ?: "Unknown",
            labelText = "District"
        ),
        AboutRowData(
            painter = painterResource(R.drawable.globe),
            text = bloodDonorModel?.state.toString(),
            labelText = "State"
        ),
        AboutRowData(
            painter = painterResource(R.drawable.phone),
            text = bloodDonorModel?.isContactNumberPrivate?.let { if (!it) bloodDonorModel.contactNumber else "Number Private" },
            labelText = "Mobile Number"
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = dimens.size20),
        ) {
            Spacer(Modifier.height(dimens.size40))
            BasicAppBar(
                title = "Profile Details",
                onBackClick = { onBackClick() },
                action = {}
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(Modifier.height(dimens.size50))
                }
                item {
                    if (bloodDonorModel?.bloodGroup == null) {
                        ProfileCardShimmerEffect()
                    } else {
                        TopProfileCard(
                            bloodDonorModel = bloodDonorModel,
                            profileEvent = profileEvent
                        )
                    }
                }
                item {
                    Spacer(Modifier.height(dimens.size30))
                }

                items(aboutList) { aboutList ->
                    if (bloodDonorModel?.state != null) {

                        AboutRow(
                            painter = aboutList.painter,
                            text = aboutList.text ?: "Unknown",
                            labelText = aboutList.labelText
                        )
                        Spacer(Modifier.height(dimens.size10))
                    } else {
                        AboutRowShimmerEffect()
                    }
                }
            }
        }
    }
}

@Composable
fun TopProfileCard(
    modifier: Modifier = Modifier,
    bloodDonorModel: BloodDonorModel?,
    profileEvent: (BloodDonorProfileEvent) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.size200),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = bloodDonorModel?.name ?: "Name",
                style = MaterialTheme.typography.displaySmall
            )
            Text(
                text = "${bloodDonorModel?.bloodGroup.toString()} Blood",
                style = MaterialTheme.typography.labelLarge
            )
            Button(
                modifier = Modifier.fillMaxWidth(0.4f),
                onClick = {
                    profileEvent(BloodDonorProfileEvent.CallNow)
                },
                shape = RoundedCornerShape(dimens.size10),
                enabled = bloodDonorModel?.isContactNumberPrivate == false
            ) {
                Text("Call Now")
            }
        }
    }
}


@Composable
fun AboutRow(
    modifier: Modifier = Modifier,
    painter: Painter,
    text: String?,
    labelText: String,
    isCustomColor: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size8, vertical = dimens.size15),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.secondary,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    ),
                    shape = CircleShape
                )
                .size(dimens.size40),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(dimens.size20),
                tint = if (isCustomColor) Color.Unspecified else MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(Modifier.width(dimens.size10))
        Text(
            text = labelText,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = text ?: "Unknown",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun AboutRowShimmerEffect(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = dimens.size8, vertical = dimens.size15),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dimens.size40)
                .shimmerEffect(isRounded = true)
        )

        Spacer(Modifier.width(dimens.size10))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .height(dimens.size10)
                .shimmerEffect()
        )
        Spacer(Modifier.weight(1f))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(dimens.size10)
                .shimmerEffect()
        )
    }
}

data class AboutRowData(
    val painter: Painter,
    val text: String?,
    val labelText: String,
    val isCustomColor: Boolean = false
)

@Composable
fun ProfileCardShimmerEffect(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(dimens.size200),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(dimens.size20)
                    .shimmerEffect()
                    .fillMaxWidth(0.5f)
            )
            Box(
                modifier = Modifier
                    .height(dimens.size10)
                    .shimmerEffect()
                    .fillMaxWidth(0.3f)
            )
            Box(
                modifier = Modifier
                    .height(dimens.size30)
                    .shimmerEffect()
                    .fillMaxWidth(0.4f)
            )
        }
    }
}