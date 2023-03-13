package ru.ac.uniyar.simplexmethodtaskssolver.components.artificial

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.ArtificialBasisSolver
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.SimplexSolver
import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.entities.PivotData
import ru.ac.uniyar.simplexmethodtaskssolver.entities.TaskType
import ru.ac.uniyar.simplexmethodtaskssolver.utils.asValue

class ArtificialBasisComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<Output>,
    solver: ArtificialBasisSolver,
    fractionType: FractionType,
    taskType: TaskType
) : ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            ArtificialBasisStoreProvider(
                storeFactory = storeFactory,
                output = output,
                solver = solver,
                fractionType = fractionType,
                taskType = taskType
            ).provide()
        }

    val models: Value<Model> = store.asValue().map(stateToModel)

    fun onSimplexTableUpdated(simplexTable: List<List<Double>>) {
        store.accept(ArtificialBasisStore.Intent.UpdateSimplexTable(simplexTable))
    }

    fun onPivotSelected(pivot: Pair<Int, Int>) {
        store.accept(ArtificialBasisStore.Intent.SelectPivot(pivot))
    }

    fun onFractionTypeChanged(fractionType: FractionType) {
        store.accept(ArtificialBasisStore.Intent.ChangeFractionType(fractionType))
    }

    fun onNextStepClicked() {
        val oldSolver = store.state.solver

        if (oldSolver.isArtificialSolved) {
            val newSolver = SimplexSolver()
            newSolver.init(
                target = oldSolver.target,
                restrict = oldSolver.restrict,
                varCount = oldSolver.varCount,
                restrictCount = oldSolver.restrictCount,
                basisList = oldSolver.basisList
            )

            output(
                Output.SimplexTransit(
                    solver = newSolver,
                    fractionType = store.state.fractionType,
                    taskType = store.state.taskType
                )
            )
        } else {
            val pivot: Pair<Int, Int>

            if (store.state.selectedPivot.first == -1) {
                val pivots = oldSolver.findPivots()
                pivot = pivots.filter { it.first > oldSolver.varCount }.firstOrNull() ?: pivots.first()
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
        val iteration: Int,
        val fractionType: FractionType,
        val noSolution: Boolean
    )

    sealed class Output {
        data class NextStepTransit(
            val solver: ArtificialBasisSolver,
            val fractionType: FractionType,
            val taskType: TaskType
        ) : Output()

        data class SimplexTransit(
            val solver: SimplexSolver,
            val fractionType: FractionType,
            val taskType: TaskType
        ) : Output()

        object MainTransit : Output()

        object PrevStepTransit : Output()
    }
}
