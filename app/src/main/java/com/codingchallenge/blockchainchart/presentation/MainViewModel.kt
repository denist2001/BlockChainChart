package com.codingchallenge.blockchainchart.presentation

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.codingchallenge.blockchainchart.domain.BitCoinRepository
import com.codingchallenge.blockchainchart.domain.LayerConverter
import com.codingchallenge.blockchainchart.domain.RepositoryException
import com.google.gson.JsonIOException
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.lang.IllegalArgumentException
import java.net.UnknownHostException

class MainViewModel @ViewModelInject constructor(
    private val repository: BitCoinRepository,
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
            .map { apiResponse ->
                converter.convertFrom(apiResponse)
            }
            .doOnError { throwable ->
                handleThrowable(throwable)
            }
            .retry(5)
            .subscribe({ presentationData ->
                _stateLiveData.postValue(
                    MainViewModelState.DataLoaded(
                        presentationData
                    )
                )
            }) { throwable ->
                Log.e(this.toString(), "startLoading: " + throwable.message)
            }
        disposables.add(disposable)
    }

    private fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                _stateLiveData.postValue(MainViewModelState.Error("Network error"))
                throw RepositoryException(RepositoryException.Error.NETWORK_ERROR)
            }
            is UnknownHostException -> {
                _stateLiveData.postValue(MainViewModelState.Error("Connection error"))
                throw RepositoryException(RepositoryException.Error.CONNECTION_ERROR)
            }
            is IOException -> {
                _stateLiveData.postValue(MainViewModelState.Error("Parsing error"))
                throw RepositoryException(RepositoryException.Error.PARSING_ERROR)
            }
            is IllegalArgumentException -> {
                _stateLiveData.postValue(MainViewModelState.Error("Invalid data"))
                throw RepositoryException(RepositoryException.Error.EMPTY_DATA)
            }
            is JsonIOException -> {
                _stateLiveData.postValue(MainViewModelState.Error("Invalid data"))
                throw RepositoryException(RepositoryException.Error.EMPTY_DATA)
            }
            else -> throwable.message?.let {
                _stateLiveData.postValue(MainViewModelState.Error(it))
                throw RepositoryException(RepositoryException.Error.UNKNOWN_ERROR)
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