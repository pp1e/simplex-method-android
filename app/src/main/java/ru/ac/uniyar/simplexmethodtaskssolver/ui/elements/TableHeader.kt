package ru.ac.uniyar.simplexmethodtaskssolver.ui.elements

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.UiConstants
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.colors

@Composable
fun TableHeader(text: String) {
    Box(
        modifier = Modifier
            .padding(UiConstants.GridPadding)
            .width(UiConstants.InputTableItemWidth)
            .height(UiConstants.InputTableItemHeight)
            .border(
                width = 1.dp,
                color = colors.primary,
                shape = RoundedCornerShape(4.dp)
            )
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.Center),
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = UiConstants.TableHeaderFontSize
        )
    }
}
