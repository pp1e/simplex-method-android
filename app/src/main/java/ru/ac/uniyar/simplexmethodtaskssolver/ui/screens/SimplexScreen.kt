package ru.ac.uniyar.simplexmethodtaskssolver.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import ru.ac.uniyar.simplexmethodtaskssolver.components.simplex.SimplexComponent
import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.UiConstants
import ru.ac.uniyar.simplexmethodtaskssolver.ui.elements.*

@Composable
fun SimplexScreen(component: SimplexComponent) {
    val model by component.models.subscribeAsState()
    val stateVertical = rememberScrollState(0)

    Column(modifier = Modifier.verticalScroll(stateVertical)) {
        SwitchWithLabel(
            label = "Обыкновенные дроби",
            checked = model.fractionType == FractionType.COMMON,
            onSwitchToggled = {
                if (model.fractionType == FractionType.COMMON) component.onFractionTypeChanged(FractionType.DECIMAL)
                else component.onFractionTypeChanged(FractionType.COMMON)
            }
        )


        SimplexTable(
            onPivotSelected = component::onPivotSelected,
            values = model.simplexTable,
            header = "Симплекс-таблица основной задачи, шаг ${model.iteration}",
            rowNames = model.rowNames,
            columnNames = model.columnNames,
            iteration = model.iteration
        )

        if (model.noSolution) {
            HorizontalSplitter()
            HeaderText("Нет решения.")
        }

        if (model.artificialAnswer.isNotEmpty()) {
            HorizontalSplitter()
            SimplexAnswer(
                answer = model.artificialAnswer,
                header = "Ответ"
            )
        }


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Column {
                Button(
                    onClick = component::onPrevStepClicked,
                    modifier = Modifier
                        .padding(UiConstants.Padding),
                ) {
                    Text("Шаг назад")
                }

                Button(
                    onClick = component::onBackToMainScreenClicked,
                    modifier = Modifier
                        .padding(UiConstants.Padding),
                ) {
                    Text("Главный экран")
                }
            }

            Column {
                Button(
                    onClick = component::onNextStepClicked,
                    modifier = Modifier
                        .padding(UiConstants.Padding),
                    enabled = !model.solved
                ) {
                    Text("Шаг вперёд")
                }

                /*Button(
                    onClick = {},
                    modifier = Modifier
                        .padding(UiConstants.Padding),
                    enabled = !model.solved
                ) {
                    Text("Получить ответ")
                }*/
            }
        }
    }
}
