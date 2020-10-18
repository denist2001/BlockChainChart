package com.codingchallenge.blockchainchart.domain

import io.reactivex.Observable

interface BitCoinRepository {
    fun loadData(): Observable<DomainData>
}