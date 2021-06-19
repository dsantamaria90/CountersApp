package com.cornershop.counterstest.data.datasource

import com.cornershop.counterstest.data.datasource.base.BaseRemoteDataSource
import com.cornershop.counterstest.data.entity.AddCounter
import com.cornershop.counterstest.data.entity.DeleteCounter
import com.cornershop.counterstest.data.service.CountersService
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.entity.Result
import javax.inject.Inject

class CountersRemoteDataSource @Inject constructor(
    private val service: CountersService
) : BaseRemoteDataSource() {

    suspend fun getCountersList(): Result<List<Counter>> =
        getResult { service.getCountersList() }

    suspend fun addCounter(addCounter: AddCounter) =
        getResult { service.addCounter(addCounter) }

    suspend fun deleteCounter(deleteCounter: DeleteCounter) =
        getResult { service.deleteCounter(deleteCounter) }

    suspend fun incrementCounter(id: String): Result<List<Counter>> =
        getResult { service.incrementCounter(id) }

    suspend fun decrementCounter(id: String): Result<List<Counter>> =
        getResult { service.decrementCounter(id) }
}