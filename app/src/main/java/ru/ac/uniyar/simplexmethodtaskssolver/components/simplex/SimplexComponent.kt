package ru.ac.uniyar.simplexmethodtaskssolver.components.simplex

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.SimplexSolver
import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.entities.PivotData
import ru.ac.uniyar.simplexmethodtaskssolver.entities.TaskType
import ru.ac.uniyar.simplexmethodtaskssolver.utils.asValue

class SimplexComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<Output>,
    solver: SimplexSolver,
    fractionType: FractionType,
    taskType: TaskType
) : ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            SimplexStoreProvider(
                storeFactory = storeFactory,
                output = output,
                solver = solver,
                fractionType = fractionType,
                taskType = taskType
            ).provide()
        }

    val models: Value<Model> = store.asValue().map(stateToModel)

    fun onSimplexTableUpdated(simplexTable: List<List<Double>>) {
        store.accept(SimplexStore.Intent.UpdateSimplexTable(simplexTable))
    }

    fun onPivotSelected(pivot: Pair<Int, Int>) {
        store.accept(SimplexStore.Intent.SelectPivot(pivot))
    }

    fun onFractionTypeChanged(fractionType: FractionType) {
        store.accept(SimplexStore.Intent.ChangeFractionType(fractionType))
    }

    fun onNextStepClicked() {
        val pivot: Pair<Int, Int>
        val oldSolver = store.state.solver

        if (store.state.selectedPivot.first == -1) {
            val pivots = oldSolver.findPivots()
            pivot = pivots.firstOrNull { it.first > oldSolver.varCount } ?: pivots.first()
        } else
            pivot = store.state.selectedPivot

        val newSolver = oldSolver.generate(pivot)
        newSolver.iterate()

        output(
            Output.NextStepTransit(
                solver = newSolver,
                fractionType = store.state.fractionType,
                taskType = store.state.taskType
            )
        )
    }

    fun onPrevStepClicked() {
        output(Output.PrevStepTransit)
    }

    fun onBackToMainScreenClicked() {
        output(Output.MainTransit)
    }

    data class Model(
        val simplexTable: List<List<Pair<String, PivotData>>>,
        val columnNames: List<String>,
        val rowNames: List<String>,
        val artificialAnswer: List<String>,
        val solved: Boolean,
        val iteration: Int,
        val fractionType: FractionType,
        val noSolution: Boolean
    )

    sealed class Output {
        data class NextStepTransit(
            val solver: SimplexSolver,
            val fractionType: FractionType,
            val taskType: TaskType
        ) : Output()

        object MainTransit : Output()

        object PrevStepTransit : Output()
    }
}
