package com.codingchallenge.blockchainchart.presentation

import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.codingchallenge.blockchainchart.R
import com.github.mikephil.charting.charts.LineChart
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var lineChart: LineChart
    private lateinit var progressBar: ProgressBar
    private lateinit var errorMessage: TextView
    private lateinit var graphViewConfig: GraphViewConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lineChart = findViewById(R.id.line_chart_view)
        progressBar = findViewById(R.id.progress_pb)
        errorMessage = findViewById(R.id.error_tv)
        viewModel.stateLiveData.observe(this) { state ->
            renderState(state)
        }
        graphViewConfig = GraphViewConfig(lineChart)
        graphViewConfig.init()
        if (savedInstanceState == null) {
            viewModel.onAction(MainViewModelAction.LoadData)
        }
    }

    private fun renderState(state: MainViewModelState) {
        when (state) {
            MainViewModelState.Loading -> renderLoading()
            is MainViewModelState.DataLoaded -> renderData(state.presentation)
            is MainViewModelState.Error -> renderError(state.message)
        }
    }

    private fun renderError(message: String) {
        errorMessage.text = message
        progressBar.visibility = INVISIBLE
        lineChart.visibility = INVISIBLE
        errorMessage.visibility = VISIBLE
    }

    private fun renderData(presentation: PresentationData) {
        graphViewConfig.setDataToChart(presentation)
        progressBar.visibility = INVISIBLE
        lineChart.visibility = VISIBLE
        errorMessage.visibility = INVISIBLE
    }

    private fun renderLoading() {
        progressBar.visibility = VISIBLE
        lineChart.visibility = INVISIBLE
        errorMessage.visibility = INVISIBLE
    }
}