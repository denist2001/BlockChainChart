package com.codingchallenge.blockchainchart.data

import com.codingchallenge.blockchainchart.domain.BitCoinRepository
import com.codingchallenge.blockchainchart.domain.DomainData
import com.codingchallenge.blockchainchart.domain.LayerConverter
import com.codingchallenge.blockchainchart.domain.RepositoryException
import com.google.gson.JsonIOException
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class BitCoinRepositoryImpl @Inject constructor(
    private val service: RepositoryService,
    private val converter: LayerConverter
) : BitCoinRepository {

    override fun loadData(): Observable<DomainData> {
        return service.loadData()
            .observeOn(Schedulers.io())
            .subscribeOn(Schedulers.io())
            .map { apiResponse ->
                converter.convertFrom(apiResponse)
            }
            .doOnError { throwable ->
                handleThrowable(throwable)
            }
            .retry(5)
    }

    private fun handleThrowable(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                throw RepositoryException(RepositoryException.Error.NETWORK_ERROR)
            }
            is UnknownHostException -> {
                throw RepositoryException(RepositoryException.Error.CONNECTION_ERROR)
            }
            is IOException -> {
                throw RepositoryException(RepositoryException.Error.PARSING_ERROR)
            }
            is IllegalArgumentException -> {
                throw RepositoryException(RepositoryException.Error.EMPTY_DATA)
            }
            is JsonIOException -> {
                throw RepositoryException(RepositoryException.Error.EMPTY_DATA)
            }
            else -> throwable.message?.let {
                throw RepositoryException(RepositoryException.Error.UNKNOWN_ERROR)
            }
        }
    }

}