package ru.ac.uniyar.simplexmethodtaskssolver.ui.elements

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.UiConstants

@Composable
fun InputTable(
    values: List<List<Pair<Boolean, String>>>,
    header: String,
    onValueChange: (Int, Int, String) -> Unit
) {
    val stateHorizontal = rememberScrollState(0)

    Column(
        modifier = Modifier
        .padding(UiConstants.Padding)
    ) {
        HeaderText(header)
        HorizontalSplitter()

        Column(
            modifier = Modifier.horizontalScroll(stateHorizontal)
        ) {
            Row {
                for (index in 1..values.first().size)
                    TableHeader(if (index == values.first().size) "C" else "X${index}")
            }

            for (rowIndex in values.indices)
                Row {
                    for (columnIndex in values[rowIndex].indices) {
                        InputTableItem(
                            value = values[rowIndex][columnIndex].second,
                            onValueChange = { onValueChange(rowIndex, columnIndex, it) },
                            isError = values[rowIndex][columnIndex].first
                        )
                    }
                }
        }
    }

}
