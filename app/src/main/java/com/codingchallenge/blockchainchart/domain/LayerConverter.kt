package com.codingchallenge.blockchainchart.domain

import com.codingchallenge.blockchainchart.data.model.ApiResponse
import com.codingchallenge.blockchainchart.presentation.PresentationData
import com.github.mikephil.charting.data.Entry
import javax.inject.Inject

class LayerConverter @Inject constructor() {
    fun convertFrom(apiResponse: ApiResponse): PresentationData {
        val entries = ArrayList<Entry>()
        for (value in apiResponse.values) {
            val xValue = value.x.toFloat()
            entries.add(Entry(xValue, value.y))
        }
        return PresentationData(apiResponse.name, entries)
    }
}
