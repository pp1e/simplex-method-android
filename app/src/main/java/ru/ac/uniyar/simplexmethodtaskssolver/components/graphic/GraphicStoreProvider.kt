package ru.ac.uniyar.simplexmethodtaskssolver.components.graphic

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import com.badoo.reaktive.base.Consumer
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.SimplexSolver
import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.entities.TaskType

class GraphicStoreProvider(
    private val storeFactory: StoreFactory,
    private val output: Consumer<GraphicComponent.Output>,
    private val solver: SimplexSolver,
    private val fractionType: FractionType,
    private val taskType: TaskType
) {
    fun provide(): GraphicStore =
        object :
            GraphicStore,
            Store<GraphicStore.Intent, GraphicStore.State, Nothing> by storeFactory.create(
                name = "GraphicStore",
                initialState = GraphicStore.State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        data class SolverLoaded(val solver: SimplexSolver) : Msg()
        data class FractionTypeChanged(val fractionType: FractionType) : Msg()
        data class TaskTypeChanged(val taskType: TaskType) : Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<GraphicStore.Intent, Unit, GraphicStore.State, Msg, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> GraphicStore.State) {
            var newSolver: SimplexSolver? = null

            while (!solver.isSolved) {
                val pivot: Pair<Int, Int>
                val oldSolver = solver

                val pivots = oldSolver.findPivots()
                pivot = pivots.firstOrNull { it.first > oldSolver.varCount } ?: pivots.first()

                newSolver = oldSolver.generate(pivot)
                newSolver.iterate()
            }

            dispatch(Msg.SolverLoaded(newSolver?: solver))
            dispatch(Msg.TaskTypeChanged(taskType))
            dispatch(Msg.FractionTypeChanged(fractionType))
        }

        override fun executeIntent(intent: GraphicStore.Intent, getState: () -> GraphicStore.State): Unit =
            when (intent) {
                is GraphicStore.Intent.ChangeFractionType -> changeFractionType(intent.fractionType)
            }

        private fun changeFractionType(fractionType: FractionType) {
            dispatch(Msg.FractionTypeChanged(fractionType))
        }
    }

    private object ReducerImpl : Reducer<GraphicStore.State, Msg> {
        override fun GraphicStore.State.reduce(msg: Msg): GraphicStore.State =
            when (msg) {
                is Msg.SolverLoaded -> copy(solver = msg.solver)
                is Msg.FractionTypeChanged -> copy(fractionType = msg.fractionType)
                is Msg.TaskTypeChanged -> copy(taskType = msg.taskType)
            }
    }
}