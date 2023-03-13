package ru.ac.uniyar.simplexmethodtaskssolver.ui.elements

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.UiConstants

@Composable
fun HeaderText(text: String) {
    Text(
        text = text,
        fontSize = UiConstants.HeaderTextSize
    )
}
