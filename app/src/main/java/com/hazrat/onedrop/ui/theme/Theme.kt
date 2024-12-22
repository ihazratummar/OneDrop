package com.hazrat.onedrop.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.hazrat.onedrop.main.MainActivity
import com.hazrat.onedrop.util.CompactDimens
import com.hazrat.onedrop.util.CompactMediumDimens
import com.hazrat.onedrop.util.CompactSmallDimens
import com.hazrat.onedrop.util.Dimens
import com.hazrat.onedrop.util.ExpandedDimens
import com.hazrat.onedrop.util.MediumDimens
import androidx.compose.material3.Typography
import androidx.compose.runtime.SideEffect
import androidx.core.view.WindowCompat
import com.hazrat.onedrop.ui.theme.darkScheme

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    inversePrimary = inversePrimaryLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    inversePrimary = inversePrimaryDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
)

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun OneDropTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    activity: Activity = LocalContext.current as MainActivity,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) darkScheme else lightScheme
        }
        darkTheme -> darkScheme
        else -> lightScheme
    }
    val window = calculateWindowSizeClass(activity = activity)
    val config  = LocalConfiguration.current

    val typography: Typography
    val appDimens: Dimens
    val view = LocalView.current

    if (!view.isInEditMode){
        SideEffect {
            val windowInsetsController = WindowCompat.getInsetsController(activity.window, view)
            windowInsetsController.isAppearanceLightStatusBars = !darkTheme
        }
    }
    when(window.widthSizeClass){
        WindowWidthSizeClass.Compact -> {
            if (config.screenWidthDp <= 360 ){
                appDimens = CompactSmallDimens
                typography = CompactSmallTypography
            }
            else if (config.screenWidthDp < 599 ){
                appDimens = CompactMediumDimens
                typography = CompactMediumTypography
            }else{
                appDimens = CompactDimens
                typography = CompactTypography
            }
        }
        WindowWidthSizeClass.Medium -> {
            appDimens = MediumDimens
            typography = MediumTypography
        }
        else -> {
            appDimens = ExpandedDimens
            typography = ExpandedTypography
        }
    }

    AppUtils(appDimens =appDimens ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}

val dimens
    @Composable
    get() = LocalAppDimens.current