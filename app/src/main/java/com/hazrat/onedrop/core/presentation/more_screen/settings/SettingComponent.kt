package com.hazrat.onedrop.core.presentation.more_screen.settings

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.hazrat.onedrop.R
import com.hazrat.onedrop.ui.theme.dimens
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

/**
 * @author Hazrat Ummar Shaikh
 * Created on 21-12-2024
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SettingTabTabCards(
    modifier: Modifier = Modifier,
    tabText: String = "Theme Mode",
    onClick: () -> Unit = {},
    isChecked: Boolean
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
                .fillMaxWidth(),
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
                    painter = painterResource(R.drawable.theme_light_dark),
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
            CustomSwitcher(
                isChecked = isChecked,
                onClick = { onClick() }
            )
        }
        Spacer(Modifier.height(dimens.size15))
    }
}


@Composable
fun CustomSwitcher(
    isChecked: Boolean,
    size: Dp = dimens.size40,
    iconSize: Dp = size / 3,
    padding: Dp = dimens.size10,
    borderWidth: Dp = dimens.size1,
    parentShape: Shape = CircleShape,
    toggleShape: Shape = CircleShape,
    offIcon: Painter = painterResource(R.drawable.darkmode),
    onIcon: Painter = painterResource(R.drawable.lightmode),
    animationSpec: AnimationSpec<Dp> = tween(durationMillis = 300),
    onClick: () -> Unit
) {
    val offset by animateDpAsState(
        targetValue = if (isChecked) 0.dp else size,
        animationSpec = animationSpec, label = ""
    )

    Box(modifier = Modifier
        .width(size * 2)
        .height(size)
        .clip(shape = parentShape)
        .clickable { onClick() }
        .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .offset(x = offset)
                .padding(all = padding)
                .clip(shape = toggleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {}
        Row(
            modifier = Modifier
                .border(
                    border = BorderStroke(
                        width = borderWidth,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    shape = parentShape
                )
        ) {
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = offIcon,
                    contentDescription = "Theme Icon",
                    tint = if (isChecked) MaterialTheme.colorScheme.secondaryContainer
                    else MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier.size(size),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(iconSize),
                    painter = onIcon,
                    contentDescription = "Theme Icon",
                    tint = if (isChecked) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}