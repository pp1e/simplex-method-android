package ru.ac.uniyar.simplexmethodtaskssolver.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Environment
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import ru.ac.uniyar.simplexmethodtaskssolver.MainActivity
import ru.ac.uniyar.simplexmethodtaskssolver.components.main.MainComponent
import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.entities.SolutionMethod
import ru.ac.uniyar.simplexmethodtaskssolver.entities.TaskType
import ru.ac.uniyar.simplexmethodtaskssolver.storage.SimplexData
import ru.ac.uniyar.simplexmethodtaskssolver.storage.listTasks
import ru.ac.uniyar.simplexmethodtaskssolver.storage.saveTask
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.UiConstants
import ru.ac.uniyar.simplexmethodtaskssolver.ui.elements.*

@Composable
fun MainScreen(component: MainComponent) {
    val model by component.models.subscribeAsState()
    val stateVertical = rememberScrollState(0)

    Column(modifier = Modifier.verticalScroll(stateVertical)) {
        MainMenu(
            onSaveClicked = component::taskNameWindowToggled,
            onLoadClicked = component::taskChooseWindowToggled
        )
        
        RadioChooser(
            currentSelection = model.fractionType,
            selections = mapOf(
                "Обыкновенные" to FractionType.COMMON,
                "Десятичные" to FractionType.DECIMAL
            ),
            onSelectionChanged = component::onFractionTypeChanged,
            label = "Тип дробей"
        )

        RadioChooser(
            currentSelection = model.solutionMethod,
            selections = mapOf(
                "Симлекс" to SolutionMethod.SIMPLEX,
                "Искусственного базиса" to SolutionMethod.ARTIFICIAL_BASIS,
                "Графический" to SolutionMethod.GRAPHIC
            ),
            onSelectionChanged = component::onSolutionMethodChanged,
            label = "Метод решения"
        )

        RadioChooser(
            currentSelection = model.taskType,
            selections = mapOf(
                "Минимум" to TaskType.MIN,
                "Максимум" to TaskType.MAX
            ),
            onSelectionChanged = component::onTaskTypeChanged,
            label = "Тип задачи"
        )

        Row {
            Spinner(
                value = model.restrictionCount.toString(),
                onValueChanged = component::onRestrictionCountChanged,
                label = "Ограничения",
                maxValue = 16,
                minValue = 1
            )

            Spinner(
                value = model.variablesCount.toString(),
                onValueChanged = component::onVariablesCountChanged,
                label = "Переменные",
                maxValue = 16,
                minValue = 1
            )
        }

        HorizontalSplitter()

        InputTable(
            header = "Целевая функция: ",
            values = listOf(model.function),
            onValueChange = { _, column, value -> component.onFunctionElementChanged(column, value) }
        )

        InputTable(
            header = "Ограничения задачи:",
            values = model.restrictions,
            onValueChange = component::onRestrictionElementChanged
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = component::onContinueClicked,
                modifier = Modifier
                    .padding(UiConstants.Padding),
                enabled = model.isDataCorrect
            ) {
                Text("Продолжить")
            }
        }
    }

    if (model.taskChooseWindowOpened)
        TaskChooseWindow(
            tasks = listTasks(),
            onClosed = component::taskChooseWindowToggled,
            onChose = component::loadSimplexData
        )

    if (model.taskNameWindowOpened)
        TaskNameWindow(
            onClosed = component::taskNameWindowToggled,
            onSaveClicked = component::saveSimplexData
        )
}
