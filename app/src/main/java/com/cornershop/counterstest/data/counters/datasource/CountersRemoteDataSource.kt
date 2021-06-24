package com.cornershop.counterstest.data.counters.datasource

import com.cornershop.counterstest.data.counters.entity.AddCounter
import com.cornershop.counterstest.data.counters.entity.ModifyCounter
import com.cornershop.counterstest.data.counters.service.CountersService
import com.cornershop.counterstest.data.datasource.BaseRemoteDataSource
import com.cornershop.counterstest.domain.counters.entity.Counter
import com.cornershop.counterstest.domain.entity.Result
import javax.inject.Inject

class CountersRemoteDataSource @Inject constructor(
    private val service: CountersService
) : BaseRemoteDataSource() {

    suspend fun getCountersList(): Result<List<Counter>> =
        getResult { service.getCountersList() }

    suspend fun addCounter(addCounter: AddCounter): Result<List<Counter>> =
        getResult { service.addCounter(addCounter) }

    suspend fun deleteCounter(modifyCounter: ModifyCounter): Result<List<Counter>> =
        getResult { service.deleteCounter(modifyCounter) }

    suspend fun incrementCounter(modifyCounter: ModifyCounter): Result<List<Counter>> =
        getResult { service.incrementCounter(modifyCounter) }

    suspend fun decrementCounter(modifyCounter: ModifyCounter): Result<List<Counter>> =
        getResult { service.decrementCounter(modifyCounter) }
}