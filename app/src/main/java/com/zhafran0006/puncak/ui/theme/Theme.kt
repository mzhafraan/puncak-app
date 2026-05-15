package com.zhafran0006.puncak.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val PurpleDark = darkColorScheme(primary = Purple80, secondary = PurpleGrey80, tertiary = Pink80)
private val PurpleLight = lightColorScheme(primary = Purple40, secondary = PurpleGrey40, tertiary = Pink40)

private val BlueDark = darkColorScheme(primary = Color(0xFFADC6FF), secondary = Color(0xFFBBC6E4), tertiary = Color(0xFFD9BBDD))
private val BlueLight = lightColorScheme(primary = Color(0xFF335CA8), secondary = Color(0xFF545D78), tertiary = Color(0xFF705573))

private val GreenDark = darkColorScheme(primary = Color(0xFF81D6A5), secondary = Color(0xFFB3CCBE), tertiary = Color(0xFFBDC7DE))
private val GreenLight = lightColorScheme(primary = Color(0xFF006D44), secondary = Color(0xFF4E6355), tertiary = Color(0xFF3C6472))

@Composable
fun PuncakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    themeIndex: Int = 0,
    dynamicColor: Boolean = false, // Disabled to prioritize custom theme selection
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        else -> {
            when (themeIndex) {
                1 -> if (darkTheme) BlueDark else BlueLight
                2 -> if (darkTheme) GreenDark else GreenLight
                else -> if (darkTheme) PurpleDark else PurpleLight
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
