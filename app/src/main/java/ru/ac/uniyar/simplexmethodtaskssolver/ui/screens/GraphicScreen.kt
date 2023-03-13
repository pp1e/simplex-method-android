package ru.ac.uniyar.simplexmethodtaskssolver.ui.screens

import ru.ac.uniyar.simplexmethodtaskssolver.ui.elements.Graph
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import ru.ac.uniyar.simplexmethodtaskssolver.components.graphic.GraphicComponent
import ru.ac.uniyar.simplexmethodtaskssolver.ui.constants.UiConstants

@Composable
fun GraphicScreen(component: GraphicComponent) {
    val model by component.models.subscribeAsState()
    val stateVertical = rememberScrollState(0)

    Column(modifier = Modifier.verticalScroll(stateVertical)) {

        Graph(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp),
            pointsSets = model.restrictions,
            paddingSpace = 16.dp,
            normal = model.normal,
            answer = model.answer
        )

        Button(
            onClick = component::onBackToMainScreenClicked,
            modifier = Modifier
                .padding(UiConstants.Padding),
        ) {
            Text("Главный экран")
        }
    }
}
