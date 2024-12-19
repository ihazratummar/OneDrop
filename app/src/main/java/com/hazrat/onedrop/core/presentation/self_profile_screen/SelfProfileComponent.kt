package com.hazrat.onedrop.core.presentation.self_profile_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.hazrat.onedrop.R
import com.hazrat.onedrop.core.domain.model.BloodDonorModel
import com.hazrat.onedrop.core.presentation.blood_donor_profile_details_screen.AboutRow
import com.hazrat.onedrop.core.presentation.blood_donor_profile_details_screen.AboutRowData
import com.hazrat.onedrop.ui.theme.dimens

/**
 * @author Hazrat Ummar Shaikh
 * Created on 10-12-2024
 */

@Composable
fun ProfileDetails(
    modifier: Modifier = Modifier,
    bloodDonorModel: BloodDonorModel
) {
    val aboutList = listOf(
        AboutRowData(
            painter = painterResource(
                bloodDonorModel.bloodGroup?.getIconResId() ?: R.drawable.onedrop_logo
            ),
            text = bloodDonorModel.bloodGroup.toString(),
            labelText = "Blood",
            isCustomColor = true
        ),
        AboutRowData(
            painter = painterResource(R.drawable.age),
            text = bloodDonorModel.age,
            labelText = "Age"
        ),
        AboutRowData(
            painter = painterResource(R.drawable.gender),
            text = bloodDonorModel.gender.toString(),
            labelText = "Gender"
        ),
        AboutRowData(
            painter = painterResource(R.drawable.city),
            text = bloodDonorModel.city,
            labelText = "City"
        ),
        AboutRowData(
            painter = painterResource(R.drawable.map),
            text = bloodDonorModel.district,
            labelText = "Distric"
        ),
        AboutRowData(
            painter = painterResource(R.drawable.globe),
            text = bloodDonorModel.state.toString(),
            labelText = "State"
        ),
        AboutRowData(
            painter = painterResource(R.drawable.phone),
            text = bloodDonorModel.contactNumber,
            labelText = "Mobile Number"
        )
    )

    aboutList.forEach {
        AboutRow(
            painter = it.painter,
            text = it.text ?: "Unknown",
            labelText = it.labelText,
            isCustomColor = it.isCustomColor
        )
    }
}


@Composable
fun TabRowComponent(
    modifier: Modifier = Modifier,
    contentScreens: List<@Composable () -> Unit>,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    indicationColor: Color = MaterialTheme.colorScheme.primary,
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimens.size20)
    ) {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = containerColor,
            contentColor = contentColor,
            indicator = { tabPosition ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPosition[selectedTabIndex]),
                    color = indicationColor
                )
            }
        ) {
            ProfileTabs.entries.forEachIndexed { index, tabTitle ->
                Tab(
                    modifier = Modifier.padding(all = dimens.size15),
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index }
                ) {
                    // Text displayed on the tab
                    Text(text = tabTitle.toString())
                }
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                contentScreens.getOrNull(selectedTabIndex)?.invoke()
            }
        }
    }
}

enum class ProfileTabs {
    PROFILE {
        override fun toString() = "Profile"
    },
//    REQUEST_BLOOD {
//        override fun toString() = "Request Blood"
//    }
}

@Composable
fun ProfileTopBar(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onActionClick: () -> Unit,
    actionIcon: Painter
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier,
            onClick = { onBackClick() }
        ) {
            Icon(
                painter = painterResource(R.drawable.arrowleft),
                contentDescription = null
            )
        }
        IconButton(
            modifier = Modifier,
            onClick = { onActionClick() }
        ) {
            Icon(
                painter = actionIcon,
                contentDescription = null
            )
        }

    }
}


@Composable
fun TopBarCard(
    modifier: Modifier = Modifier,
    name: String = "Hazrat Ummar Shaikh"
) {
    Card(
        modifier = modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary, // primary color
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), // lighter shade
                        MaterialTheme.colorScheme.background // darker shade
                    ),
                    startY = 0f,
                    endY = Float.POSITIVE_INFINITY // The gradient will extend vertically
                ),
            )
            .fillMaxWidth()
            .height(dimens.size250),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(width = dimens.size1, color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = dimens.size20),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.age),
                contentDescription = null,
                modifier = Modifier.size(dimens.size100)
            )
            Text(
                text = name.uppercase(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        }
    }
}