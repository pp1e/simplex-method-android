package ru.ac.uniyar.simplexmethodtaskssolver.components.graphic

import com.arkivanov.mvikotlin.core.store.Store
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.SimplexSolver

import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.entities.TaskType

interface GraphicStore : Store<GraphicStore.Intent, GraphicStore.State, Nothing> {
    sealed class Intent {
        data class ChangeFractionType(val fractionType: FractionType) : Intent()
    }

    data class State(
        val fractionType: FractionType = FractionType.DECIMAL,
        val solver: SimplexSolver = SimplexSolver(),
        val taskType: TaskType = TaskType.MIN
    )
}