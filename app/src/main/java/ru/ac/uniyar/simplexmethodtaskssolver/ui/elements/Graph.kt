package ru.ac.uniyar.simplexmethodtaskssolver.ui.elements

import android.graphics.Paint
import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ac.uniyar.simplexmethodtaskssolver.components.graphic.*
import kotlin.math.abs

val graphColors = listOf(Color.Red, Color.Green, Color.Gray, Color.Yellow)
var graphColorsIterator = graphColors.iterator()

fun nextColor(): Color =
    if (graphColorsIterator.hasNext())
        graphColorsIterator.next()
    else {
        graphColorsIterator = graphColors.iterator()
        graphColorsIterator.next()
    }

@Composable
fun Graph(
    modifier : Modifier,
    pointsSets: List<List<PointF>>,
    paddingSpace: Dp,
    normal: List<PointF>,
    answer: PointF?
) {
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    Box(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            val xAxisSpace = (size.width - paddingSpace.toPx()) / xAxisInteger.size
            val yAxisSpace = size.height / yAxisInteger.size
            val xCenter = size.width/2
            val yCenter = (size.height - yAxisSpace)/2

            drawXAxis(
                xAxisSpace = xAxisSpace,
                yAxisSpace = yAxisSpace,
                yCenter = yCenter,
                textPaint = textPaint
            )
            drawYAxis(
                xAxisSpace = xAxisSpace,
                yAxisSpace = yAxisSpace,
                yCenter = yCenter,
                textPaint = textPaint
            )

//            /** placing x axis points */
//            for (i in xAxis.indices) {
//                drawCircle(
//                    color = Color.Red,
//                    radius = 8f,
//                    center = Offset(xAxisSpace * (i + 1), yCenter)
//                )
//                drawContext.canvas.nativeCanvas.drawText(
//                    xAxis[i].toString(),
//                    xAxisSpace * (i + 1),
//                    yCenter + 35,
//                    textPaint
//                )
//            }

//            val strokeX = Path().apply {
//                reset()
//                moveTo(xAxisSpace, yCenter)
//                lineTo(
//                    size.width - xAxisSpace,
//                    yCenter
//                )
//            }
//
//
//            drawPath(
//                strokeX,
//                color = Color.Black,
//                style = Stroke(
//                    width = 5f,
//                    cap = StrokeCap.Round
//                )
//            )
//
//            /** placing y axis points */
//            for (i in yAxis.indices) {
//                drawCircle(
//                    color = Color.Red,
//                    radius = 8f,
//                    center = Offset(xCenter, size.height - yAxisSpace * (i + 1))
//                )
//                drawContext.canvas.nativeCanvas.drawText(
//                    yAxis[i].toString(),
//                    xCenter + 20,
//                    size.height - yAxisSpace * (i + 1),
//                    textPaint
//                )
//            }
//
//            val strokeY = Path().apply {
//                reset()
//                moveTo(xCenter, 0f)
//                lineTo(
//                    xCenter,
//                    size.height - yAxisSpace
//                )
//            }
//
//
//            drawPath(
//                strokeY,
//                color = Color.Black,
//                style = Stroke(
//                    width = 5f,
//                    cap = StrokeCap.Round
//                )
//            )
//
            for (points in pointsSets) {
                drawPath(
                    createStroke(points, xAxisSpace, yAxisSpace, yCenter),
                    color = nextColor(),
                    style = Stroke(
                        width = 5f,
                        cap = StrokeCap.Round
                    )
                )
            }

            drawPath(
                createStroke(normal, xAxisSpace, yAxisSpace, yCenter),
                color = Color.DarkGray,
                style = Stroke(
                    width = 5f,
                    cap = StrokeCap.Round
                )
            )

            var points = emptyList<PointF>()
            for(x in xAxisFull.filter { it >= 0  }) {
                val maxY = minY(pointsSets, x)
                if ((maxY >= 0) and (maxY != Float.MAX_VALUE))
                   points = points.plusElement(PointF(x, maxY))
            }

            val path = createStroke(points, xAxisSpace, yAxisSpace, yCenter)

            drawPath(
                path,
                color = Color.Blue,
                style = Stroke(
                    width = 5f,
                    cap = StrokeCap.Round
                )
            )

            val fillPath = android.graphics.Path(
                path.asAndroidPath()
            )
            .asComposePath()
            .apply {
                lineTo(xCenter, yCenter)
                lineTo(xCenter, yCenter)
                close()
            }


            drawPath(
                fillPath,
                color = Color.Blue,
                alpha = 0.4f
            )

            if (answer != null)
                drawAnswer(answer, xAxisSpace, yAxisSpace, yCenter)
        }
    }
}

fun minY(pointsSets: List<List<PointF>>, x: Float): Float {
    var min = Float.MAX_VALUE
    for(set in pointsSets) {
        val point = set.findByX(x)
        if (point != null)
            if (point.y < min)
                min = point.y
    }
    return min
}

fun DrawScope.drawXAxis(xAxisSpace: Float, yAxisSpace: Float, yCenter: Float,  textPaint: Paint) {
    val coordinates = mutableListOf<PointF>()
    val points = xAxisInteger.map { PointF(it, 0f) }

    for (point in points)
        if ((point.y <= yAxisInteger.max()) and (point.y >= yAxisInteger.min())) {
            val x1 = xAxisSpace * (point.x + yAxisInteger.count { it <= 0 })
            val y1 = yCenter - (yAxisSpace * point.y)

            coordinates.add(PointF(x1, y1))
            /** drawing circles to indicate all the points */
            drawCircle(
                color = Color.Black,
                radius = 8f,
                center = Offset(x1, y1)
            )
            drawContext.canvas.nativeCanvas.drawText(
                point.x.toInt().toString(),
                x1,
                y1 + 35f,
                textPaint
            )
        }

    val stroke = Path().apply {
        reset()
        moveTo(coordinates.first().x, coordinates.first().y)
        for (i in 0 until coordinates.size - 1) {
            lineTo(
                coordinates[i + 1].x,
                coordinates[i + 1].y
            )
        }
    }

    drawPath(
        stroke,
        color = Color.Black,
        style = Stroke(
            width = 5f,
            cap = StrokeCap.Round
        )
    )
}

fun DrawScope.drawYAxis(xAxisSpace: Float, yAxisSpace: Float, yCenter: Float,  textPaint: Paint) {
    val coordinates = mutableListOf<PointF>()
    val points = yAxisInteger.map { PointF(0f, it) }

    for (point in points)
        if ((point.y <= yAxisInteger.max()) and (point.y >= yAxisInteger.min())) {
            val x1 = xAxisSpace * (point.x + yAxisInteger.count { it <= 0 })
            val y1 = yCenter - (yAxisSpace * point.y)

            coordinates.add(PointF(x1, y1))
            /** drawing circles to indicate all the points */
            drawCircle(
                color = Color.Black,
                radius = 8f,
                center = Offset(x1, y1)
            )
            drawContext.canvas.nativeCanvas.drawText(
                point.y.toInt().toString(),
                x1 + 20f,
                y1,
                textPaint
            )
        }

    val stroke = Path().apply {
        reset()
        moveTo(coordinates.first().x, coordinates.first().y)
        for (i in 0 until coordinates.size - 1) {
            lineTo(
                coordinates[i + 1].x,
                coordinates[i + 1].y
            )
        }
    }

    drawPath(
        stroke,
        color = Color.Black,
        style = Stroke(
            width = 5f,
            cap = StrokeCap.Round
        )
    )
}


fun DrawScope.createStroke(points: List<PointF>, xAxisSpace: Float, yAxisSpace: Float, yCenter: Float): Path {
    val coordinates = mutableListOf<PointF>()

    for (point in points)
        if ((point.y <= yAxisInteger.max()) and (point.y >= yAxisInteger.min())) {
            val x1 = xAxisSpace * (point.x + yAxisInteger.count { it <= 0 })
            val y1 = yCenter - (yAxisSpace * point.y)

            coordinates.add(PointF(x1, y1))
        }

    val stroke = Path().apply {
        reset()
        moveTo(coordinates.first().x, coordinates.first().y)
        for (i in 0 until coordinates.size - 1) {
            lineTo(
                coordinates[i + 1].x,
                coordinates[i + 1].y
            )
        }
    }

    return stroke
}

fun List<PointF>.findByX(x: Float): PointF? {
    for (point in this)
        if (abs(x - point.x) <= step/2)
            return point
    return null
}

fun DrawScope.drawAnswer(point: PointF, xAxisSpace: Float, yAxisSpace: Float, yCenter: Float) {
    val x1 = xAxisSpace * (point.x + yAxisInteger.count { it <= 0 })
    val y1 = yCenter - (yAxisSpace * point.y)

    drawCircle(
        color = Color.Red,
        radius = 10f,
        center = Offset(x1, y1)
    )
}