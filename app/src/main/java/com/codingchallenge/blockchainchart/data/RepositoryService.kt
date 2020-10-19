package com.codingchallenge.blockchainchart.data

import com.codingchallenge.blockchainchart.data.model.ApiResponse
import io.reactivex.Observable
import retrofit2.http.*

interface RepositoryService {

    @GET("market-price")
    fun loadData(
        @Query("timespan") timespan: String,
        @Query("rollingAverage") rollingAverage: String,
        @Query("format") format: String
    ): Observable<ApiResponse>
}