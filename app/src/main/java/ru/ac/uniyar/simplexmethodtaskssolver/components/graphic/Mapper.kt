package ru.ac.uniyar.simplexmethodtaskssolver.components.graphic

import android.graphics.PointF
import ru.ac.uniyar.simplexmethodtaskssolver.calculations.SimplexSolver
import kotlin.math.abs
import kotlin.math.round


const val step = 0.2f
const val minValue = -10f
const val maxValue = 10f
val xAxisFull = createXAxis()
val yAxisFull = createXAxis()
val xAxisInteger = xAxisFull.filter { abs(round(it) - it) <= step/2 }.map { round(it) }
val yAxisInteger = yAxisFull.filter { abs(round(it) - it) <= step/2 }.map { round(it) }

fun createXAxis(): List<Float> {
    var xAxis = emptyList<Float>()
    var x = minValue
    while (x <= maxValue) {
        xAxis = xAxis.plus(x)
        x += step
    }
    return xAxis
}


internal val stateToModel: (GraphicStore.State) -> GraphicComponent.Model =
    { state ->
        GraphicComponent.Model(
            restrictions = restrictionToPoints(state.solver.restrict),
            normal = functionToNormal(state.solver.function),
            fractionType = state.fractionType,
            noSolution = state.solver.noSolution,
            answer = answerToPoint(state.solver),
        )
    }

fun restrictionToPoints(restrictions: List<List<Double>>): List<List<PointF>> {
    var pointRestrictions: List<List<PointF>> = emptyList()
    var y: Float

    for (restriction in restrictions) {
        var pointRestriction = emptyList<PointF>()
        for (x in xAxisFull) {
            y = ((restriction[0] - x * restriction[1]) / restriction[2]).toFloat()
            pointRestriction = pointRestriction.plusElement(PointF(x, y))
        }
        pointRestrictions = pointRestrictions.plusElement(pointRestriction)
    }

    return pointRestrictions
}

fun answerToPoint(solver: SimplexSolver): PointF? {
    val answer = solver.answer
    if (answer == null)
        return null
    else {
        return PointF(
            answer.getOrElse(solver.basisList[0]) { 0.0 }.toFloat(),
            answer.getOrElse(solver.basisList[1]) { 0.0 }.toFloat()
        )
    }
}

fun functionToNormal(function: List<Double>): List<PointF> {
    var pointFunc = emptyList<PointF>()
    var y: Float

    pointFunc = pointFunc.plusElement(PointF(0f, 0f))
    pointFunc = pointFunc.plusElement(PointF(0-function[1].toFloat(), 0-function[2].toFloat()))

    return pointFunc
}
