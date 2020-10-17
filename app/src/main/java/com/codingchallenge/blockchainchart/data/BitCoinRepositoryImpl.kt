package com.codingchallenge.blockchainchart.data

import com.codingchallenge.blockchainchart.data.model.ApiResponse
import com.codingchallenge.blockchainchart.domain.BitCoinRepository
import io.reactivex.Observable
import javax.inject.Inject

class BitCoinRepositoryImpl @Inject constructor(
    private val service: RepositoryService
): BitCoinRepository {

    override fun loadData(): Observable<ApiResponse> {
        return service.loadData()
    }

}