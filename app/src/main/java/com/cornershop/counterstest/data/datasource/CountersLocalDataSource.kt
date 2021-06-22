package com.cornershop.counterstest.data.datasource

import com.cornershop.counterstest.data.dao.CountersDao
import com.cornershop.counterstest.domain.entity.Counter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountersLocalDataSource @Inject constructor(private val dao: CountersDao) {

    suspend fun saveCountersList(countersList: List<Counter>) = dao.saveCountersList(countersList)

    fun getCountersList(): Flow<List<Counter>> = dao.getCountersList()

    suspend fun deleteCountersNotIn(idsList: List<String>) = dao.deleteCountersNotIn(idsList)
}