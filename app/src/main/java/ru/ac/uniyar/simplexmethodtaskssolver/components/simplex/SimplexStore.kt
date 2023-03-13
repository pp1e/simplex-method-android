package ru.ac.uniyar.simplexmethodtaskssolver.components.simplex

import com.arkivanov.mvikotlin.core.store.Store
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.SimplexSolver
import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.entities.TaskType

internal interface SimplexStore : Store<SimplexStore.Intent, SimplexStore.State, Nothing> {
    sealed class Intent {
        data class UpdateSimplexTable(val simplexTable: List<List<Double>>) : Intent()
        data class SelectPivot(val selectedPivot: Pair<Int, Int>) : Intent()
        data class ChangeFractionType(val fractionType: FractionType) : Intent()
    }

    data class State(
        val simplexTable: List<List<Double>> = emptyList(),
        val fractionType: FractionType = FractionType.DECIMAL,
        val solver: SimplexSolver = SimplexSolver(),
        val selectedPivot: Pair<Int, Int> = Pair(-1, -1),
        val taskType: TaskType = TaskType.MIN
    )
}
