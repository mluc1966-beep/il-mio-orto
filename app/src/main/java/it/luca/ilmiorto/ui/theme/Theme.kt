package it.luca.ilmiorto.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Color(0xFF2F6B3B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB4F1B4),
    onPrimaryContainer = Color(0xFF002109),
    secondary = Color(0xFF52634F),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFD5E8CF),
    onSecondaryContainer = Color(0xFF101F10),
    tertiary = Color(0xFF39656B),
    tertiaryContainer = Color(0xFFBDEBF2),
    background = Color(0xFFF7FBF2),
    surface = Color(0xFFF7FBF2),
    surfaceVariant = Color(0xFFDEE5DA),
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF98D59A),
    onPrimary = Color(0xFF003914),
    primaryContainer = Color(0xFF175224),
    onPrimaryContainer = Color(0xFFB4F1B4),
    secondary = Color(0xFFB9CCB3),
    tertiary = Color(0xFFA1CED6),
)

@Composable
fun IlMioOrtoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = GardenTypography,
        content = content,
    )
}
