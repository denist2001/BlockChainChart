package com.codingchallenge.blockchainchart.domain

import com.codingchallenge.blockchainchart.data.model.ApiResponse
import io.reactivex.Observable

interface BitCoinRepository {
    fun loadData(): Observable<ApiResponse>
}