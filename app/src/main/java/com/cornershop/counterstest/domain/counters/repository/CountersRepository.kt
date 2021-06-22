package com.cornershop.counterstest.domain.counters.repository

import com.cornershop.counterstest.domain.counters.entity.Counter
import com.cornershop.counterstest.domain.entity.Result
import kotlinx.coroutines.flow.Flow

interface CountersRepository {

    fun getCountersList(): Flow<Result<List<Counter>>>
    suspend fun refreshCountersList(): Result<Unit>
    suspend fun addCounter(title: String): Result<Unit>
    suspend fun deleteCounter(id: String): Result<Unit>
    suspend fun incrementCounter(id: String): Result<Unit>
    suspend fun decrementCounter(id: String): Result<Unit>
}