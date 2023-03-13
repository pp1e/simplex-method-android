package ru.ac.uniyar.simplexmethodtaskssolver.ui.navigation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.pop
import com.arkivanov.decompose.router.popWhile
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.badoo.reaktive.base.Consumer
import kotlinx.parcelize.RawValue
import ru.ac.uniyar.simplexmethodtaskssolver.utils.Consumer
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.ArtificialBasisSolver
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.SimplexSolver
import ru.ac.uniyar.simplexmethodtaskssolver.components.artificial.ArtificialBasisComponent
import ru.ac.uniyar.simplexmethodtaskssolver.components.main.MainComponent
import ru.ac.uniyar.simplexmethodtaskssolver.components.simplex.SimplexComponent
import ru.ac.uniyar.simplexmethodtaskssolver.entities.FractionType
import ru.ac.uniyar.simplexmethodtaskssolver.entities.TaskType
import com.arkivanov.essenty.backpressed.*
import ru.ac.uniyar.simplexmethodtaskssolver.components.graphic.GraphicComponent

class MainRouter constructor(
    private val componentContext: ComponentContext,
    private val mainComponent: (ComponentContext, Consumer<MainComponent.Output>) -> MainComponent,
    private val artificialBasisComponent: (
        ComponentContext,
        Consumer<ArtificialBasisComponent.Output>,
        solver: ArtificialBasisSolver,
        fractionType: FractionType,
        taskType: TaskType
    ) -> ArtificialBasisComponent,
    private val simplexComponent: (
        ComponentContext,
        Consumer<SimplexComponent.Output>,
        solver: SimplexSolver,
        fractionType: FractionType,
        taskType: TaskType
    ) -> SimplexComponent,
    private val graphicComponent: (
        ComponentContext,
        Consumer<GraphicComponent.Output>,
        solver: SimplexSolver,
        fractionType: FractionType,
        taskType: TaskType
    ) -> GraphicComponent,
) : ComponentContext by componentContext {

    private val router = router<ScreenConfig, Child>(
        initialConfiguration = ScreenConfig.Main,
        childFactory = ::createChild,
        handleBackButton = true,
    )

    val routerState: Value<RouterState<*, Child>> = router.state

    constructor(
        componentContext: ComponentContext,
        storeFactory: StoreFactory,
    ) : this(
        componentContext = componentContext,
        mainComponent = { component, output ->
            MainComponent(
                componentContext = component,
                storeFactory = storeFactory,
                output = output,
            )
        },
        artificialBasisComponent = { component, output, solver, fracType, taskType ->
            ArtificialBasisComponent(
                componentContext = component,
                storeFactory = storeFactory,
                output = output,
                solver = solver,
                fractionType = fracType,
                taskType = taskType
            )
        },
        simplexComponent = { component, output, solver, fracType, taskType ->
            SimplexComponent(
                componentContext = component,
                storeFactory = storeFactory,
                output = output,
                solver = solver,
                fractionType = fracType,
                taskType = taskType
            )
        },
        graphicComponent = { component, output, solver, fracType, taskType ->
            GraphicComponent(
                componentContext = component,
                storeFactory = storeFactory,
                output = output,
                solver = solver,
                fractionType = fracType,
                taskType = taskType
            )

        }
    )

    private fun createChild(
        screenConfig: ScreenConfig,
        componentContext: ComponentContext
    ): Child =
        when (screenConfig) {
            is ScreenConfig.Main -> Child.Main(
                mainComponent(
                    componentContext,
                    Consumer(::onMainOutput)
                )
            )

            is ScreenConfig.Simplex -> Child.Simplex(
                simplexComponent(
                    componentContext,
                    Consumer(::onSimplexOutput),
                    screenConfig.solver,
                    screenConfig.fractionType,
                    screenConfig.taskType
                )
            )

            is ScreenConfig.ArtificialBasis -> Child.ArtificialBasis(
                artificialBasisComponent(
                    componentContext,
                    Consumer(::onArtificialBasisOutput),
                    screenConfig.solver,
                    screenConfig.fractionType,
                    screenConfig.taskType
                )

            )

            is ScreenConfig.Graphic -> Child.Graphic(
                graphicComponent(
                    componentContext,
                    Consumer(::onGraphicOutput),
                    screenConfig.solver,
                    screenConfig.fractionType,
                    screenConfig.taskType
                )
            )
        }

    private fun onMainOutput(output: MainComponent.Output): Unit =
        when (output) {
            is MainComponent.Output.SimplexTransit -> router.push(ScreenConfig.Simplex(output.solver, output.fractionType, output.taskType))
            is MainComponent.Output.GraphicTransit -> router.push(ScreenConfig.Graphic(output.solver, output.fractionType, output.taskType))
            is MainComponent.Output.ArtificialBasisTransit -> router.push(ScreenConfig.ArtificialBasis(output.solver, output.fractionType, output.taskType))
        }

    private fun onArtificialBasisOutput(output: ArtificialBasisComponent.Output): Unit =
        when (output) {
            is ArtificialBasisComponent.Output.NextStepTransit -> router.push(ScreenConfig.ArtificialBasis(output.solver, output.fractionType, output.taskType))
            is ArtificialBasisComponent.Output.MainTransit -> router.popWhile { it != ScreenConfig.Main }
            is ArtificialBasisComponent.Output.PrevStepTransit -> router.pop()
            is ArtificialBasisComponent.Output.SimplexTransit -> router.push(ScreenConfig.Simplex(output.solver, output.fractionType, output.taskType))
        }

    private fun onSimplexOutput(output: SimplexComponent.Output): Unit =
        when (output) {
            is SimplexComponent.Output.NextStepTransit -> router.push(ScreenConfig.Simplex(output.solver, output.fractionType, output.taskType))
            is SimplexComponent.Output.MainTransit -> router.popWhile { it != ScreenConfig.Main }
            is SimplexComponent.Output.PrevStepTransit -> router.pop()
        }

    private fun onGraphicOutput(output: GraphicComponent.Output): Unit =
        when (output) {
            is GraphicComponent.Output.MainTransit -> router.pop()
        }

    sealed class Child {
        data class Main(val component: MainComponent) : Child()
        data class Simplex(val component: SimplexComponent) : Child()
        data class ArtificialBasis(val component: ArtificialBasisComponent) : Child()
        data class Graphic(val component: GraphicComponent) : Child()
    }

    private sealed class ScreenConfig: Parcelable  {
        @Parcelize
        object Main : ScreenConfig()

        @Parcelize
        data class Simplex(val solver: @RawValue SimplexSolver, val fractionType: FractionType, val taskType: TaskType) : ScreenConfig()

        @Parcelize
        data class ArtificialBasis(val solver: @RawValue ArtificialBasisSolver, val fractionType: FractionType, val taskType: TaskType) : ScreenConfig()

        @Parcelize
        data class Graphic(val solver: @RawValue SimplexSolver, val fractionType: FractionType, val taskType: TaskType) : ScreenConfig()
    }
}
