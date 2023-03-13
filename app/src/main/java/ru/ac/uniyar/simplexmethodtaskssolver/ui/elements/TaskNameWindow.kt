package ru.ac.uniyar.simplexmethodtaskssolver.ui.elements

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.UiConstants

@Composable
fun TaskNameWindow(
    onClosed: () -> Unit,
    onSaveClicked: (String) -> Unit) {
    var name by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(onDismissRequest = onClosed,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                HeaderText(text = "Введите название задания:")
            }
        },
        text = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextField(value = name, onValueChange = { name = it })
            }
        },
        buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = onClosed,
                    modifier = Modifier
                        .padding(UiConstants.Padding),
                ) {
                    Text("Отмена")
                }
                Button(
                    onClick = {
                        onSaveClicked(name.text)
                        onClosed()
                              },
                    modifier = Modifier
                        .padding(UiConstants.Padding),
                    enabled = name.text.isNotBlank()
                ) {
                    Text("Сохранить")
                }
            }
        }
    )
}