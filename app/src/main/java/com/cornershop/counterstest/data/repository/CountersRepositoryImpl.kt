package com.cornershop.counterstest.data.repository

import com.cornershop.counterstest.data.datasource.CountersLocalDataSource
import com.cornershop.counterstest.data.datasource.CountersRemoteDataSource
import com.cornershop.counterstest.data.entity.AddCounter
import com.cornershop.counterstest.data.entity.DeleteCounter
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.repository.CountersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CountersRepositoryImpl @Inject constructor(
    private val remote: CountersRemoteDataSource,
    private val local: CountersLocalDataSource
) : CountersRepository {

    override fun getCountersList(): Flow<Result<List<Counter>>> =
        local.getCountersList().map { Result.Success(it) }

    override suspend fun refreshCountersList(): Result<Unit> = remote.getCountersList().saveList()

    private suspend fun Result<List<Counter>>.saveList(): Result<Unit> = when (this) {
        is Result.Success -> {
            // Delete all counters in case the same user is logged on another device deleting a counter
            local.deleteCountersList()
            local.saveCountersList(data)
            Result.Success(Unit)
        }
        is Result.Error -> this
    }

    override suspend fun addCounter(title: String): Result<Unit> =
        remote.addCounter(AddCounter(title)).saveList()

    override suspend fun deleteCounter(id: String): Result<Unit> =
        remote.deleteCounter(DeleteCounter(id)).saveList()

    override suspend fun incrementCounter(id: String): Result<Unit> =
        remote.incrementCounter(id).saveList()

    override suspend fun decrementCounter(id: String): Result<Unit> =
        remote.decrementCounter(id).saveList()
}