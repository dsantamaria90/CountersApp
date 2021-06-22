package com.cornershop.counterstest.data.counters.repository

import com.cornershop.counterstest.data.counters.datasource.CountersLocalDataSource
import com.cornershop.counterstest.data.counters.datasource.CountersRemoteDataSource
import com.cornershop.counterstest.data.counters.entity.AddCounter
import com.cornershop.counterstest.data.counters.entity.ModifyCounter
import com.cornershop.counterstest.domain.counters.entity.Counter
import com.cornershop.counterstest.domain.counters.repository.CountersRepository
import com.cornershop.counterstest.domain.entity.Result
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
            // Delete counters not present in case the same user is logged on another device deleting a counter
            local.deleteCountersNotIn(data.map { it.id })
            local.saveCountersList(data)
            Result.Success(Unit)
        }
        is Result.Error -> this
    }

    override suspend fun addCounter(title: String): Result<Unit> =
        remote.addCounter(AddCounter(title)).saveList()

    override suspend fun deleteCounter(id: String): Result<Unit> =
        remote.deleteCounter(newModifyCounter(id)).saveList()

    private fun newModifyCounter(id: String) = ModifyCounter(id)

    override suspend fun incrementCounter(id: String): Result<Unit> =
        remote.incrementCounter(newModifyCounter(id)).saveList()

    override suspend fun decrementCounter(id: String): Result<Unit> =
        remote.decrementCounter(newModifyCounter(id)).saveList()
}