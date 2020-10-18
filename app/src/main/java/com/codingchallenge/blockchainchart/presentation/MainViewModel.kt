package com.codingchallenge.blockchainchart.presentation

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingchallenge.blockchainchart.domain.BlockChainRepository
import com.codingchallenge.blockchainchart.domain.LayerConverter
import com.codingchallenge.blockchainchart.domain.RepositoryException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.schedulers.Schedulers

class MainViewModel @ViewModelInject constructor(
    private val repository: BlockChainRepository,
    private val converter: LayerConverter
) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _stateLiveData = MutableLiveData<MainViewModelState>()
    val stateLiveData: LiveData<MainViewModelState>
        get() = _stateLiveData

    fun onAction(action: MainViewModelAction) {
        when (action) {
            MainViewModelAction.LoadData -> startLoading()
        }
    }

    private fun startLoading() {
        _stateLiveData.postValue(MainViewModelState.Loading)
        val disposable = repository.loadData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .map { domainData ->
                converter.convertFrom(domainData)
            }
            .subscribe({ presentationData ->
                _stateLiveData.postValue(
                    MainViewModelState.DataLoaded(
                        presentationData
                    )
                )
            }) { throwable ->
                val exceptions = (throwable as CompositeException).exceptions
                for (exception in exceptions) {
                    if (exception is RepositoryException) {
                        handleThrowable(exception)
                    }
                }
            }
        disposables.add(disposable)
    }

    private fun handleThrowable(exception: RepositoryException) {
        when (exception.error) {
            RepositoryException.Error.NETWORK_ERROR -> {
                _stateLiveData.postValue(MainViewModelState.Error("Network error"))
            }
            RepositoryException.Error.CONNECTION_ERROR -> {
                _stateLiveData.postValue(MainViewModelState.Error("Connection error"))
            }
            RepositoryException.Error.PARSING_ERROR -> {
                _stateLiveData.postValue(MainViewModelState.Error("Parsing error"))
            }
            RepositoryException.Error.EMPTY_DATA -> {
                _stateLiveData.postValue(MainViewModelState.Error("Invalid data"))
            }
            else -> {
                _stateLiveData.postValue(MainViewModelState.Error("Unknown error"))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) {
            disposables.dispose()
        }
    }
}

sealed class MainViewModelAction {
    object LoadData : MainViewModelAction()
}

sealed class MainViewModelState {
    object Loading : MainViewModelState()
    class DataLoaded(val presentation: PresentationData) : MainViewModelState()
    class Error(val message: String) : MainViewModelState()
}