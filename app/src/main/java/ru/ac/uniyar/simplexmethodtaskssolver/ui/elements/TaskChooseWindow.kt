package ru.ac.uniyar.simplexmethodtaskssolver.ui.elements

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ru.ac.uniyar.simplexmethodtaskssolver.storage.SimplexData
import ru.ac.uniyar.simplexmethodtaskssolver.storage.saveTask
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.UiConstants
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.colors
import kotlin.math.round

@Composable
fun TaskChooseWindow(
    tasks: List<String>,
    onClosed: () -> Unit,
    onChose: (String) -> Unit
) {
    val stateVertical = rememberLazyListState(0)
    val stateHorizontal = rememberScrollState(0)
    
    AlertDialog(onDismissRequest = onClosed,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HeaderText(text = "Выберите задание:")
            }
                },
        text = {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .padding(UiConstants.Padding)
                        .horizontalScroll(stateHorizontal),
                    state = stateVertical
                ) {
                    items(tasks) { task ->
                        Box(
                            modifier = Modifier.width(maxWidth)
                        ) {
                            ListField(
                                text = task,
                                onSelected = {
                                    onChose(it)
                                    onClosed()
                                             },
                            )
                        }
                    }

                }
            }
        },
        buttons = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = onClosed,
                    modifier = Modifier
                        .padding(UiConstants.Padding),
                ) {
                    Text("Отмена")
                }
            }
        }
    )
}

@Composable
fun ListField(
    text: String,
    onSelected: (text: String) -> Unit,
) {
    val interaction = remember { MutableInteractionSource() }
    val hovered = interaction.collectIsHoveredAsState()
    val color = if (hovered.value) colors.primaryVariant else colors.primary

    Box(
        modifier = Modifier
            .height(UiConstants.ListFieldHeight)
            .fillMaxWidth()
            .background(color = color)
            .border(
                width = UiConstants.BorderSize,
                shape = RoundedCornerShape(UiConstants.RoundedCornerShapeSize),
                color = colors.secondary
            )
            .hoverable(
                interactionSource = interaction,
                enabled = true
            )
            .clickable { onSelected(text) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center),
            fontSize = UiConstants.DialogTextSize
        )
    }
}