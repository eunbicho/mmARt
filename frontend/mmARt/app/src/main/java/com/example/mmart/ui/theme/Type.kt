package com.example.mmart.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.mmart.R

val mainFont = FontFamily(
    Font(R.font.gmarket_sans_ttf_medium),
    Font(R.font.gmarket_sans_ttf_light, FontWeight.Light),
    Font(R.font.gmarket_sans_ttf_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val mainTypography = Typography(
    defaultFontFamily = mainFont,
//    body1 = TextStyle(
//        fontFamily = mainFont,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp
//    ),
//    button = TextStyle(
//        fontFamily = mainFont,
//        fontWeight = FontWeight.W500,
//        fontSize = 14.sp
//    ),

    /* Other default text styles to override
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)