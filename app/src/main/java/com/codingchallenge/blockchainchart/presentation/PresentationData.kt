package com.codingchallenge.blockchainchart.presentation

import com.github.mikephil.charting.data.Entry

data class PresentationData(
    val label: String,
    val entries: List<Entry>
)