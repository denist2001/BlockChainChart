package com.codingchallenge.blockchainchart.domain

import com.codingchallenge.blockchainchart.data.model.ApiResponse
import com.codingchallenge.blockchainchart.presentation.model.PresentationData
import com.codingchallenge.blockchainchart.presentation.model.PresentationEntry
import javax.inject.Inject

class LayerConverter @Inject constructor() {

    fun convertFrom(domainData: DomainData): PresentationData {
        val entries = mutableListOf<PresentationEntry>()
        for (value in domainData.entries) {
            val timestamp = value.timestamp
            entries.add(PresentationEntry(timestamp, value.value))
        }
        return PresentationData(domainData.label, entries)
    }

    fun convertFrom(apiResponse: ApiResponse): DomainData {
        val entries = mutableListOf<DomainEntry>()
        for (value in apiResponse.values) {
            entries.add(DomainEntry(value.x, value.y))
        }
        return DomainData(apiResponse.name, entries)
    }
}
