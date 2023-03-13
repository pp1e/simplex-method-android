package ru.ac.uniyar.simplexmethodtaskssolver.components.graphic

import android.graphics.PointF
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import com.badoo.reaktive.base.invoke
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.SimplexSolver
import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.entities.TaskType
import ru.ac.uniyar.simplexmethodtaskssolver.utils.asValue

class GraphicComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val output: Consumer<Output>,
    solver: SimplexSolver,
    fractionType: FractionType,
    taskType: TaskType
) : ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore {
            GraphicStoreProvider(
                storeFactory = storeFactory,
                output = output,
                solver = solver,
                fractionType = fractionType,
                taskType = taskType
            ).provide()
        }

    val models: Value<Model> = store.asValue().map(stateToModel)

    fun onFractionTypeChanged(fractionType: FractionType) {
        store.accept(GraphicStore.Intent.ChangeFractionType(fractionType))
    }

    fun onBackToMainScreenClicked() {
        output(Output.MainTransit)
    }

    data class Model(
        val restrictions: List<List<PointF>>,
        val normal: List<PointF>,
        val fractionType: FractionType,
        val noSolution: Boolean,
        val answer: PointF?
    )

    sealed class Output {
        object MainTransit : Output()
    }
}