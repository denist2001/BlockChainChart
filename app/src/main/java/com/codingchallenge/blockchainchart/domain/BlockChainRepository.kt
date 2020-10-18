package com.codingchallenge.blockchainchart.domain

import io.reactivex.Observable

interface BlockChainRepository {
    fun loadData(): Observable<DomainData>
}