package com.codingchallenge.blockchainchart.presentation

import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*

class GraphViewConfig(private val lineChart: LineChart) {
    private val dateFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
    private val boarder = 20f
    private val axisTextColor = Color.rgb(255, 192, 56)
    private val crossPinterColor = Color.rgb(244, 117, 117)

    fun init() {
        lineChartConfig()
        timeAxisConfig(lineChart.xAxis)
        valueAxisConfig(lineChart.axisLeft)
    }

    fun setDataToChart(presentation: PresentationData) {
        val leftAxis: YAxis = lineChart.axisLeft
        val max = presentation.entries.maxOf { entry ->
            entry.y
        }
        val min = presentation.entries.minOf { entry ->
            entry.y
        }
        leftAxis.axisMinimum = min
        leftAxis.axisMaximum = max

        // create a data object with the data sets
        val data = LineData(lineConfig(presentation))
        data.setValueTextColor(Color.BLACK)
        data.setValueTextSize(9f)

        // set data
        lineChart.data = data
    }

    private fun lineConfig(presentation: PresentationData): LineDataSet {
        val lineDataSet = LineDataSet(presentation.entries, presentation.label)
        lineDataSet.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSet.color = ColorTemplate.getHoloBlue()
        lineDataSet.valueTextColor = ColorTemplate.getHoloBlue()
        lineDataSet.lineWidth = 1.5f
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawValues(true)
        lineDataSet.fillAlpha = 65
        lineDataSet.fillColor = ColorTemplate.getHoloBlue()
        lineDataSet.highLightColor = crossPinterColor
        lineDataSet.setDrawCircleHole(false)
        return lineDataSet
    }

    private fun lineChartConfig() {
        lineChart.description.isEnabled = false
        lineChart.setTouchEnabled(true)

        lineChart.dragDecelerationFrictionCoef = 0.9f

        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setDrawGridBackground(false)
        lineChart.isHighlightPerDragEnabled = true

        lineChart.setBackgroundColor(Color.WHITE)
        lineChart.setViewPortOffsets(boarder, boarder, boarder, boarder)
        val l: Legend = lineChart.legend
        l.isEnabled = true
    }

    private fun timeAxisConfig(xAxis: XAxis) {
        xAxis.position = XAxis.XAxisPosition.TOP_INSIDE
        xAxis.textSize = 10f
        xAxis.textColor = Color.BLACK
        xAxis.setDrawAxisLine(false)
        xAxis.setDrawGridLines(true)
        xAxis.textColor = axisTextColor
        xAxis.setCenterAxisLabels(true)
        xAxis.granularity = 1f

        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val millis = value.toLong() * 1000
                return dateFormat.format(Date(millis))
            }
        }
    }

    private fun valueAxisConfig(leftAxis: YAxis) {
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        leftAxis.textColor = ColorTemplate.getHoloBlue()
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true
        leftAxis.yOffset = -9f
        leftAxis.textColor = axisTextColor

        val rightAxis: YAxis = lineChart.axisRight
        rightAxis.isEnabled = false
    }
}