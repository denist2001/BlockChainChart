package com.codingchallenge.blockchainchart.domain

import com.codingchallenge.blockchainchart.data.model.ApiResponse
import com.codingchallenge.blockchainchart.presentation.PresentationData
import com.github.mikephil.charting.data.Entry
import javax.inject.Inject

class LayerConverter @Inject constructor() {

    fun convertFrom(domainData: DomainData): PresentationData {
        val entries = ArrayList<Entry>()
        for (value in domainData.entries) {
            val timestamp = value.timestamp.toFloat()
            entries.add(Entry(timestamp, value.value))
        }
        return PresentationData(domainData.label, entries)
    }

    fun convertFrom(apiResponse: ApiResponse): DomainData {
        val entries = ArrayList<DomainEntry>()
        for (value in apiResponse.values) {
            entries.add(DomainEntry(value.x, value.y))
        }
        return DomainData(apiResponse.name, entries)
    }
}
