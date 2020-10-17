package com.codingchallenge.blockchainchart.data

import com.codingchallenge.blockchainchart.data.model.ApiResponse
import io.reactivex.Observable
import retrofit2.http.*

interface RepositoryService {

    @GET("market-price?timespan=5weeks&rollingAverage=8hours&format=json")
    fun loadData(): Observable<ApiResponse>
}