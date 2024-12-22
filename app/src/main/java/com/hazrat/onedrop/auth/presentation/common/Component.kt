package com.hazrat.onedrop.auth.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.hazrat.onedrop.R
import com.hazrat.onedrop.ui.theme.Nunito
import com.hazrat.onedrop.ui.theme.dimens
import kotlinx.coroutines.delay

/**
 * @author Hazrat Ummar Shaikh
 * Created on 30-11-2024
 */

@Composable
fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = true,
    textFieldTopLabel: String,
    keyboardType: KeyboardType,
    keyBoardActions: KeyboardActions,
    label: String,
    placeholder: String,
    imeAction: ImeAction = ImeAction.Next,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    errorMessage: String?,
    onTrailingIconCliCk: () -> Unit = {},
    isPasswordVisible: Boolean = false,
    isTrailingForPassword: Boolean = false,
    isEnabled: Boolean = true
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = textFieldTopLabel,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = Nunito
            )
            Spacer(Modifier.weight(1f))
            if (isError) {
                Icon(
                    modifier = Modifier.size(dimens.size10),
                    painter = painterResource(R.drawable.alert),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.width(dimens.size5))
                errorMessage?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = Nunito,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

        }
        OutlinedTextField(
            enabled = isEnabled,
            modifier = modifier
                .fillMaxWidth(),
            value = value,
            onValueChange = { onValueChange(it) },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            singleLine = true,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorTextColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                errorTrailingIconColor = MaterialTheme.colorScheme.error,
                errorLeadingIconColor = MaterialTheme.colorScheme.error,
            ),
            keyboardActions = keyBoardActions,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            visualTransformation = visualTransformation,
            trailingIcon = {
                if (isTrailingForPassword) {
                    Text(
                        text = if (isPasswordVisible) "Show" else "Hide",
                        modifier = Modifier.clickable {
                            onTrailingIconCliCk()
                        }
                    )
                } else {
                    Text(
                        text = "Clear",
                        modifier = Modifier.clickable {
                            onTrailingIconCliCk()
                        }
                    )
                }
            }

        )
    }
}


@Composable
fun AuthenticationButton(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    buttonText: String,
    isButtonEnabled: Boolean,
    isLoadings: Boolean
) {


    Button(
        modifier = modifier.fillMaxWidth().let {
            if (isLoadings) it.shimmerEffect() else it
        },
        onClick = {
            onButtonClick()
        },
        shape = RoundedCornerShape(dimens.size10),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (!isLoadings) MaterialTheme.colorScheme.primary else Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = if (!isLoadings) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        enabled = isButtonEnabled
    ) {
        Text(
            modifier = Modifier.padding(vertical = dimens.size5),
            text = buttonText,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}


@Composable
fun SocialLoginButton(
    modifier: Modifier = Modifier,
    onButtonClick: () -> Unit,
    buttonText: String,
    icon: Painter,
    textColor: Color,
) {
    Button(
        modifier = modifier,
        onClick = { onButtonClick() },
        shape = RoundedCornerShape(dimens.size10),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Icon(
            modifier = Modifier.size(dimens.size30),
            painter = icon,
            contentDescription = null,
            tint = Color.Unspecified
        )
        Spacer(Modifier.width(dimens.size10))
        Text(
            buttonText,
            color = textColor
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)

@Composable
fun BottomText(
    modifier: Modifier = Modifier,
    messageText: String = "Don't have an account?",
    buttonText: String = "Sign Up",
    onButtonClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimens.size30),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "$messageText "
        )
        Text(
            text = buttonText,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.combinedClickable(
                onClick = { onButtonClick() },
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
        )
    }
}


fun Modifier.shimmerEffect(
    isRounded: Boolean = false,
): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "")

    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(2000)), label = ""
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.secondaryContainer,
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondaryContainer,
            ),
            start = Offset(startOffsetX, 0F),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        ),
        shape = if (!isRounded) RoundedCornerShape(dimens.size10) else CircleShape
    ).onGloballyPositioned {
        size = it.size
    }

}




@Composable
fun NetworkStatusBar(
    modifier: Modifier = Modifier,
    isConnectionAvailable: Boolean
) {

    var showMessageBar by rememberSaveable { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var backgroundColor by remember { mutableStateOf(Color.Red) }

    LaunchedEffect(isConnectionAvailable) {
        if (isConnectionAvailable){
            message = "Back Online!"
            backgroundColor = Color.Green
            delay(4000)
            showMessageBar = false
        }else{
            showMessageBar = true
            message = "No Internet Connection"
            backgroundColor = Color.Red
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = showMessageBar,
        enter = slideInVertically(animationSpec = tween(durationMillis = 600)) { h -> h },
        exit = slideOutVertically(animationSpec = tween(durationMillis = 600)) { h -> h }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}