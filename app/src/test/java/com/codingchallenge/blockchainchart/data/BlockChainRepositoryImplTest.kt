package com.codingchallenge.blockchainchart.data

import com.codingchallenge.blockchainchart.data.model.ApiResponse
import com.codingchallenge.blockchainchart.domain.LayerConverter
import com.codingchallenge.blockchainchart.rules.RxTrampolineSchedulerRule
import com.google.gson.JsonIOException
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observable
import io.reactivex.exceptions.CompositeException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException
import java.net.UnknownHostException

@RunWith(MockitoJUnitRunner::class)
class BlockChainRepositoryImplTest {
    private lateinit var subject: BlockChainRepositoryImpl

    @Rule
    @JvmField
    var rxTrampolineSchedulerRule = RxTrampolineSchedulerRule()

    @Mock
    private lateinit var service: RepositoryService

    @Mock
    private lateinit var converter: LayerConverter

    @Mock
    private lateinit var mockedApiResponse: ApiResponse

    @Before
    fun setUp() {
        subject = BlockChainRepositoryImpl(service, converter)
    }

    @Test
    fun ifNetworkErrorHandlesCorrectly() {
        `when`(service.loadData(anyString(), anyString(), anyString()))
            .thenReturn(Observable.error(mock(HttpException::class.java)))
        subject.loadData()
            .test()
            .assertError(CompositeException::class.java)
    }

    @Test
    fun ifConnectionErrorHandlesCorrectly() {
        `when`(service.loadData(anyString(), anyString(), anyString()))
            .thenReturn(Observable.error(UnknownHostException()))
        subject.loadData()
            .test()
            .assertError(CompositeException::class.java)
    }

    @Test
    fun ifIOErrorHandlesCorrectly() {
        `when`(service.loadData(anyString(), anyString(), anyString()))
            .thenReturn(Observable.error(IOException()))
        subject.loadData()
            .test()
            .assertError(CompositeException::class.java)
    }

    @Test
    fun ifParsingErrorHandlesCorrectly() {
        `when`(service.loadData(anyString(), anyString(), anyString()))
            .thenReturn(Observable.just(mockedApiResponse))
        `when`(converter.convertFrom(mockedApiResponse)).thenThrow(IllegalArgumentException::class.java)
        subject.loadData()
            .test()
            .assertError(CompositeException::class.java)
    }

    @Test
    fun ifEmptyDataErrorHandlesCorrectly() {
        `when`(service.loadData(anyString(), anyString(), anyString()))
            .thenReturn(Observable.just(mockedApiResponse))
        `when`(converter.convertFrom(mockedApiResponse)).thenThrow(JsonIOException::class.java)
        subject.loadData()
            .test()
            .assertError(CompositeException::class.java)
    }

    @Test
    fun ifUnknownErrorHandlesCorrectly() {
        `when`(service.loadData(anyString(), anyString(), anyString()))
            .thenReturn(Observable.error(Exception()))
        subject.loadData()
            .test()
            .assertError(CompositeException::class.java)
    }
}