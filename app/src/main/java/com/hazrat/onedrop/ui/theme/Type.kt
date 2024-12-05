package com.hazrat.onedrop.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hazrat.onedrop.R



val Nunito = FontFamily(
    fonts = listOf(
        Font(R.font.nunito_regular, FontWeight.Normal),
        Font(R.font.nunito_bold, FontWeight.Bold),
        Font(R.font.nunito_semibold, FontWeight.SemiBold),
        Font(R.font.nunito_medium,FontWeight.Medium),
        Font(R.font.nunito_light,FontWeight.Light),
        Font(R.font.nunito_black,FontWeight.Black),
        Font(R.font.nunito_extrabold,FontWeight.ExtraBold),
        Font(R.font.nunito_extralight,FontWeight.ExtraLight),
        Font(R.font.nunito_blackitalic,FontWeight.Black),
        Font(R.font.nunito_bolditalic,FontWeight.Bold),
        Font(R.font.nunito_semibolditalic,FontWeight.SemiBold),
        Font(R.font.nunito_mediumitalic,FontWeight.Medium),
        Font(R.font.nunito_lightitalic,FontWeight.Light),
        Font(R.font.nunito_italic,FontWeight.Normal),
        Font(R.font.nunito_extrabolditalic,FontWeight.ExtraBold),
        Font(R.font.nunito_extralightitalic,FontWeight.ExtraLight),
    )
)

val Poppins = FontFamily(
    fonts = listOf(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_bold, FontWeight.Bold),
        Font(R.font.poppins_semibold, FontWeight.SemiBold)
    )
)

// Set of Material typography styles to start with
val CompactTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 28.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = Nunito,
        lineHeight = 42.sp
    ),
    displayMedium = TextStyle(
        fontSize = 24.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = Nunito,
        lineHeight = 36.sp
    ),
    displaySmall = TextStyle(
        fontSize = 20.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 32.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 20.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 18.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp
    ),
    titleLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp
    ),
    titleSmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 14.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 12.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 19.sp
    ),
    bodySmall = TextStyle(
        fontSize = 10.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    ),
    labelLarge = TextStyle(
        fontSize = 12.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp

    ),
    labelMedium = TextStyle(
        fontSize = 10.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontSize = 8.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 15.sp
    )
)

val CompactMediumTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 32.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 48.sp
    ),
    displayMedium = TextStyle(
        fontSize = 30.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 46.sp
    ),
    displaySmall = TextStyle(
        fontSize = 28.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 42.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 26.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 38.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 24.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 36.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 22.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 34.sp
    ),
    titleLarge = TextStyle(
        fontSize = 20.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 32.sp
    ),
    titleMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    titleSmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 18.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 25.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontSize = 14.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp
    ),
    labelLarge = TextStyle(
        fontSize = 16.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp
    ),
    labelMedium = TextStyle(
        fontSize = 14.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    labelSmall = TextStyle(
        fontSize = 12.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    )
)


val CompactSmallTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 26.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 40.sp
    ),
    displayMedium = TextStyle(
        fontSize = 24.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 38.sp
    ),
    displaySmall = TextStyle(
        fontSize = 22.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 36.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 20.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 34.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 32.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    titleLarge = TextStyle(
        fontSize = 14.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontSize = 12.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 26.sp
    ),
    titleSmall = TextStyle(
        fontSize = 10.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 14.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 21.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 12.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 19.sp
    ),
    bodySmall = TextStyle(
        fontSize = 10.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    ),
    labelLarge = TextStyle(
        fontSize = 12.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontSize = 10.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 18.sp
    ),
    labelSmall = TextStyle(
        fontSize = 8.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 15.sp
    )
)


val MediumTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 34.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 50.sp
    ),
    displayMedium = TextStyle(
        fontSize = 30.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 46.sp
    ),
    displaySmall = TextStyle(
        fontSize = 26.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 42.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 28.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 26.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 38.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 36.sp
    ),
    titleLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 34.sp
    ),
    titleMedium = TextStyle(
        fontSize = 20.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 32.sp
    ),
    titleSmall = TextStyle(
        fontSize = 18.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 20.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 27.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 18.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 25.sp
    ),
    bodySmall = TextStyle(
        fontSize = 16.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    labelLarge = TextStyle(
        fontSize = 18.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    ),
    labelMedium = TextStyle(
        fontSize = 16.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 22.sp
    ),
    labelSmall = TextStyle(
        fontSize = 14.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    )
)


val ExpandedTypography = Typography(
    displayLarge = TextStyle(
        fontSize = 40.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 56.sp
    ),
    displayMedium = TextStyle(
        fontSize = 36.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 52.sp
    ),
    displaySmall = TextStyle(
        fontSize = 32.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 48.sp
    ),
    headlineLarge = TextStyle(
        fontSize = 34.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 50.sp
    ),
    headlineMedium = TextStyle(
        fontSize = 32.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 48.sp
    ),
    headlineSmall = TextStyle(
        fontSize = 30.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 46.sp
    ),
    titleLarge = TextStyle(
        fontSize = 28.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 42.sp
    ),
    titleMedium = TextStyle(
        fontSize = 26.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 40.sp
    ),
    titleSmall = TextStyle(
        fontSize = 24.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 38.sp
    ),
    bodyLarge = TextStyle(
        fontSize = 26.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 34.sp
    ),
    bodyMedium = TextStyle(
        fontSize = 24.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 31.sp
    ),
    bodySmall = TextStyle(
        fontSize = 22.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 30.sp
    ),
    labelLarge = TextStyle(
        fontSize = 22.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp
    ),
    labelMedium = TextStyle(
        fontSize = 20.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 26.sp
    ),
    labelSmall = TextStyle(
        fontSize = 18.sp,
        fontFamily = Nunito,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp
    )
)