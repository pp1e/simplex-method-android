package ru.ac.uniyar.simplexmethodtaskssolver.components.artificial

import com.arkivanov.mvikotlin.core.store.Store
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.ArtificialBasisSolver
import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.entities.TaskType

internal interface ArtificialBasisStore : Store<ArtificialBasisStore.Intent, ArtificialBasisStore.State, Nothing> {
    sealed class Intent {
        data class UpdateSimplexTable(val simplexTable: List<List<Double>>) : Intent()
        data class SelectPivot(val selectedPivot: Pair<Int, Int>) : Intent()
        data class ChangeFractionType(val fractionType: FractionType) : Intent()
    }

    data class State(
        val simplexTable: List<List<Double>> = emptyList(),
        val fractionType: FractionType = FractionType.DECIMAL,
        val solver: ArtificialBasisSolver = ArtificialBasisSolver(),
        val selectedPivot: Pair<Int, Int> = Pair(-1, -1),
        val taskType: TaskType = TaskType.MIN
    )
}
