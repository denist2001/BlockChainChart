package com.codingchallenge.blockchainchart.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.codingchallenge.blockchainchart.domain.BlockChainRepository
import com.codingchallenge.blockchainchart.domain.DomainData
import com.codingchallenge.blockchainchart.domain.LayerConverter
import com.codingchallenge.blockchainchart.domain.RepositoryException
import com.codingchallenge.blockchainchart.rules.RxTrampolineSchedulerRule
import io.reactivex.Observable
import io.reactivex.exceptions.CompositeException
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    private lateinit var subject: MainViewModel

    @Rule
    @JvmField
    var rxTrampolineSchedulerRule = RxTrampolineSchedulerRule()

    @get:Rule
    val testInstantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: BlockChainRepository

    @Mock
    private lateinit var converter: LayerConverter

    @Mock
    private lateinit var mockedDomainData: DomainData

    @Mock
    private lateinit var stateObserver: Observer<MainViewModelState>

    @Mock
    private lateinit var mockedCompositeException: CompositeException

    @Captor
    private lateinit var stateCaptor: ArgumentCaptor<MainViewModelState>

    @Before
    fun setUp() {
        subject = MainViewModel(repository, converter)
    }

    @Test
    fun ifNetworkErrorHandlesCorrectly() {
        `when`(mockedCompositeException.exceptions).thenReturn(
            listOf(RepositoryException(RepositoryException.Error.NETWORK_ERROR))
        )
        `when`(repository.loadData()).thenReturn(Observable.error(mockedCompositeException))
        subject.stateLiveData.observeForever(stateObserver)
        subject.onAction(MainViewModelAction.LoadData)
        stateCaptor.run {
            verify(stateObserver, times(2)).onChanged(capture())
            assertEquals(MainViewModelState.Loading, allValues[0])
            val state = allValues[1] as MainViewModelState.Error
            assertEquals("Network error", state.message)
        }
    }

    @Test
    fun ifConnectionErrorHandlesCorrectly() {
        `when`(mockedCompositeException.exceptions).thenReturn(
            listOf(RepositoryException(RepositoryException.Error.CONNECTION_ERROR))
        )
        `when`(repository.loadData()).thenReturn(Observable.error(mockedCompositeException))
        subject.stateLiveData.observeForever(stateObserver)
        subject.onAction(MainViewModelAction.LoadData)
        stateCaptor.run {
            verify(stateObserver, times(2)).onChanged(capture())
            assertEquals(MainViewModelState.Loading, allValues[0])
            val state = allValues[1] as MainViewModelState.Error
            assertEquals("Connection error", state.message)
        }
    }

    @Test
    fun ifParsingErrorHandlesCorrectly() {
        `when`(mockedCompositeException.exceptions).thenReturn(
            listOf(RepositoryException(RepositoryException.Error.PARSING_ERROR))
        )
        `when`(repository.loadData()).thenReturn(Observable.just(mockedDomainData))
        `when`(converter.convertFrom(mockedDomainData))
            .thenThrow(mockedCompositeException)
        subject.stateLiveData.observeForever(stateObserver)
        subject.onAction(MainViewModelAction.LoadData)
        stateCaptor.run {
            verify(stateObserver, times(2)).onChanged(capture())
            assertEquals(MainViewModelState.Loading, allValues[0])
            val state = allValues[1] as MainViewModelState.Error
            assertEquals("Parsing error", state.message)
        }
    }

    @Test
    fun ifEmptyDataErrorHandlesCorrectly() {
        `when`(mockedCompositeException.exceptions).thenReturn(
            listOf(RepositoryException(RepositoryException.Error.EMPTY_DATA))
        )
        `when`(repository.loadData()).thenReturn(Observable.just(mockedDomainData))
        `when`(converter.convertFrom(mockedDomainData))
            .thenThrow(mockedCompositeException)
        subject.stateLiveData.observeForever(stateObserver)
        subject.onAction(MainViewModelAction.LoadData)
        stateCaptor.run {
            verify(stateObserver, times(2)).onChanged(capture())
            assertEquals(MainViewModelState.Loading, allValues[0])
            val state = allValues[1] as MainViewModelState.Error
            assertEquals("Invalid data", state.message)
        }
    }

    @Test
    fun ifUnknownErrorHandlesCorrectly() {
        `when`(mockedCompositeException.exceptions).thenReturn(
            listOf(RepositoryException(RepositoryException.Error.UNKNOWN_ERROR))
        )
        `when`(repository.loadData()).thenReturn(Observable.error(mockedCompositeException))
        subject.stateLiveData.observeForever(stateObserver)
        subject.onAction(MainViewModelAction.LoadData)
        stateCaptor.run {
            verify(stateObserver, times(2)).onChanged(capture())
            assertEquals(MainViewModelState.Loading, allValues[0])
            val state = allValues[1] as MainViewModelState.Error
            assertEquals("Unknown error", state.message)
        }
    }
}