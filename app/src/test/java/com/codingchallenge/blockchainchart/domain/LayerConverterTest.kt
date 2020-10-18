package com.codingchallenge.blockchainchart.domain

import com.codingchallenge.blockchainchart.data.model.ApiResponse
import com.codingchallenge.blockchainchart.data.model.Value
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LayerConverterTest {
    lateinit var subject: LayerConverter

    @Before
    fun setUp() {
        subject = LayerConverter()
    }

    @Test
    fun convertDomainDataToPresentationData() {
        val domainEntry1 = DomainEntry(123456789, 1.1F)
        val domainEntry2 = DomainEntry(987654321, 2.2F)
        val domainData = DomainData("Domain Data", listOf(domainEntry1, domainEntry2))
        val presentationData = subject.convertFrom(domainData)
        assertEquals("Domain Data", presentationData.label)
        assertTrue(1.23456792E8F == presentationData.entries[0].x)
        assertEquals(1.1F, presentationData.entries[0].y)
        assertTrue(9.8765434E8F == presentationData.entries[1].x)
        assertEquals(2.2F, presentationData.entries[1].y)
    }

    @Test
    fun convertApiResponseToDomainData() {
        val value1 = Value(1599955200, 10446.44F)
        val value2 = Value(1600041600, 10330.77F)
        val apiResponse = ApiResponse(
            "OK",
            "Market Price (USD)",
            "USD",
            "day",
            "Average USD market price across major bitcoin exchanges.",
            listOf(value1, value2)
        )
        val domainData = subject.convertFrom(apiResponse)
        assertEquals("Market Price (USD)", domainData.label)
        assertTrue(1599955200L == domainData.entries[0].timestamp)
        assertEquals(10446.44F, domainData.entries[0].value)
        assertTrue(1600041600L == domainData.entries[1].timestamp)
        assertEquals(10330.77F, domainData.entries[1].value)
    }
}