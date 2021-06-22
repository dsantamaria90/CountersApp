package com.cornershop.counterstest.data.counters.datasource

import com.cornershop.counterstest.data.counters.dao.CountersDao
import com.cornershop.counterstest.domain.counters.entity.Counter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountersLocalDataSource @Inject constructor(private val dao: CountersDao) {

    suspend fun saveCountersList(countersList: List<Counter>) = dao.saveCountersList(countersList)

    fun getCountersList(): Flow<List<Counter>> = dao.getCountersList()

    suspend fun deleteCountersNotIn(idsList: List<String>) = dao.deleteCountersNotIn(idsList)
}