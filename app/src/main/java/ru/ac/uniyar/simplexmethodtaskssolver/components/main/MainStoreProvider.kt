package ru.ac.uniyar.simplexmethodtaskssolver.components.main

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.reaktive.ReaktiveExecutor
import ru.ac.uniyar.simplexmethodtaskssolver.components.main.MainStore.Intent
import ru.ac.uniyar.simplexmethodtaskssolver.components.main.MainStore.State
import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.entities.SolutionMethod
import ru.ac.uniyar.simplexmethodtaskssolver.entities.TaskType
import ru.ac.uniyar.simplexmethodtaskssolver.utils.insert

internal class MainStoreProvider(
    private val storeFactory: StoreFactory,
    // private val database: Database,
) {

    fun provide(): MainStore =
        object :
            MainStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = "MainStore",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Msg {
        data class RestrictionCountChanged(val count: Int) : Msg()
        data class VariablesCountChanged(val count: Int) : Msg()
        data class FractionTypeChanged(val fractionType: FractionType) : Msg()
        data class SolutionMethodChanged(val solutionMethod: SolutionMethod) : Msg()
        data class TaskTypeChanged(val taskType: TaskType) : Msg()
        data class RestrictionsElementChanged(val row: Int, val column: Int, val value: Pair<Boolean, String>) : Msg()
        data class FunctionElementChanged(val column: Int, val value: Pair<Boolean, String>) : Msg()
        data class FunctionLoaded(val function: List<Pair<Boolean, String>>) : Msg()
        data class RestrictionsLoaded(val restrictions: List<List<Pair<Boolean, String>>>) : Msg()
        object TaskChooseWindowToggled : Msg()
        object TaskNameWindowToggled : Msg()
    }

    private inner class ExecutorImpl : ReaktiveExecutor<Intent, Unit, State, Msg, Nothing>() {
        override fun executeAction(action: Unit, getState: () -> State) {
        }

        override fun executeIntent(intent: Intent, getState: () -> State): Unit =
            when (intent) {
                is Intent.ChangeVariablesCount -> changeVariablesCount(intent.count, getState)
                is Intent.ChangeRestrictionCount -> changeRestrictionCount(intent.count, getState)
                is Intent.ChangeFractionType -> changeFractionType(intent.fractionType)
                is Intent.ChangeTaskType -> changeTaskType(intent.taskType)
                is Intent.ChangeSolutionMethod -> changeSolutionMethod(intent.solutionMethod, getState)
                is Intent.ChangeRestrictionsElement -> changeRestrictionsElement(intent.row, intent.column, intent.value)
                is Intent.ChangeFunctionElement -> changeFunctionElement(intent.column, intent.value)
                is Intent.LoadFunction -> loadFunction(intent.function)
                is Intent.LoadRestrictions -> loadRestrictions(intent.restrictions)
                is Intent.ToggleTaskChooseWindow -> toggleFileChooseWindow()
                is Intent.ToggleTaskNameWindow -> toggleNameChooseWindow()
            }

        private fun toggleFileChooseWindow() {
            dispatch(Msg.TaskChooseWindowToggled)
        }

        private fun toggleNameChooseWindow() {
            dispatch(Msg.TaskNameWindowToggled)
        }

        private fun loadRestrictions(restrictions: List<List<Pair<Boolean, String>>>) {
            dispatch(Msg.RestrictionsLoaded(restrictions))
        }

        private fun loadFunction(function: List<Pair<Boolean, String>>) {
            dispatch(Msg.FunctionLoaded(function))
        }

        private fun changeRestrictionsElement(row: Int, column: Int, value: Pair<Boolean, String>) {
            dispatch(Msg.RestrictionsElementChanged(row, column, value))
        }

        private fun changeFunctionElement(column: Int, value: Pair<Boolean, String>) {
            dispatch(Msg.FunctionElementChanged(column, value))
        }

        private fun changeVariablesCount(count: Int, getState: () -> State) {
            if (getState().solutionMethod == SolutionMethod.GRAPHIC)
                dispatch(Msg.VariablesCountChanged(2))
            else
                dispatch(Msg.VariablesCountChanged(count))
        }

        private fun changeRestrictionCount(count: Int, getState: () -> State) {
            dispatch(Msg.RestrictionCountChanged(count))
            if (getState().function.size - 1 < count)
                dispatch(Msg.VariablesCountChanged(count))

        }

        private fun changeFractionType(fractionType: FractionType) {
            dispatch(Msg.FractionTypeChanged(fractionType))
        }

        private fun changeTaskType(taskType: TaskType) {
            dispatch(Msg.TaskTypeChanged(taskType))
        }

        private fun changeSolutionMethod(solutionMethod: SolutionMethod, getState: () -> State) {
            dispatch(Msg.SolutionMethodChanged(solutionMethod))
            if (solutionMethod == SolutionMethod.GRAPHIC)
                dispatch(Msg.VariablesCountChanged(2))
            else
                changeRestrictionCount(getState().restrictions.size, getState)
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.RestrictionCountChanged -> copy(restrictions = formatRestrictionListRows(restrictions, msg.count))
                is Msg.VariablesCountChanged -> copy(
                    function = formatFunction(function, msg.count + 1),
                    restrictions = formatRestrictionListVariables(restrictions, msg.count + 1)
                )
                is Msg.FractionTypeChanged -> copy(fractionType = msg.fractionType)
                is Msg.TaskTypeChanged -> copy(taskType = msg.taskType)
                is Msg.SolutionMethodChanged -> copy(solutionMethod = msg.solutionMethod)
                is Msg.FunctionElementChanged -> copy(function = function.insert(msg.column, msg.value))
                is Msg.RestrictionsElementChanged -> copy(restrictions = restrictions.insert(msg.row, msg.column, msg.value))
                is Msg.TaskChooseWindowToggled -> copy(taskChooseWindowOpened = !taskChooseWindowOpened)
                is Msg.TaskNameWindowToggled -> copy(taskNameWindowOpened = !taskNameWindowOpened)
                is Msg.FunctionLoaded -> copy(function = msg.function)
                is Msg.RestrictionsLoaded -> copy(restrictions = msg.restrictions)
            }

        fun formatRestrictionListRows(restrictions: List<List<Pair<Boolean, String>>>, restrictionCount: Int): List<List<Pair<Boolean, String>>> {
            var res = restrictions
            if (restrictions.size < restrictionCount) {
                for (i in 0 until (restrictionCount - restrictions.size)) {
                    var row = emptyList<Pair<Boolean, String>>()
                    for (j in 0 until restrictions.first().size)
                        row = row.plus(Pair(false, "0.0"))
                    res = res.plusElement(row)
                }
            } else
                res = res.subList(0, restrictions.size - (restrictions.size - restrictionCount))
            return res
        }

        fun formatRestrictionListVariables(restrictions: List<List<Pair<Boolean, String>>>, variablesCount: Int): List<List<Pair<Boolean, String>>> {
            val res = restrictions.toMutableList()
            if (restrictions.first().size < variablesCount) {
                for (row in restrictions) {
                    var newRow = row
                    for (i in 0 until (variablesCount - restrictions.first().size))
                        newRow = newRow.plusElement(Pair(false, "0.0"))
                    res[res.indexOf(row)] = newRow
                }
            } else {
                for (row in restrictions) {
                    val newRow = row.subList(0, restrictions.size - (restrictions.size - variablesCount))
                    res.set(res.indexOf(row), newRow)
                }
            }
            return res.toList()
        }

        fun formatFunction(function: List<Pair<Boolean, String>>, variablesCount: Int): List<Pair<Boolean, String>> {
            var res = function
            if (function.size < variablesCount) {
                for (i in 0 until (variablesCount - function.size))
                    res = res.plusElement(Pair(false, "0.0"))
            } else
                res = res.subList(0, function.size - (function.size - variablesCount))
            return res
        }
    }
}
