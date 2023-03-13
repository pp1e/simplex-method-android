package ru.ac.uniyar.simplexmethodtaskssolver.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.colors

private val DarkColorPalette = darkColors(
        primary = Purple200,
        primaryVariant = Purple700,
        secondary = Teal200
)

private val LightColorPalette = lightColors(
        primary = Purple500,
        primaryVariant = Purple700,
        secondary = Teal200

        /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun SimplexMethodTasksSolverTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        colors
    } else {
        colors
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}