package com.cornershop.counterstest.data.counters.datasource

import com.cornershop.counterstest.data.counters.dao.CountersDao
import com.cornershop.counterstest.factory.CountersFactory.newCountersList
import com.cornershop.counterstest.testutil.MockKTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CountersLocalDataSourceTest : MockKTest() {

    @RelaxedMockK
    private lateinit var dao: CountersDao

    private lateinit var local: CountersLocalDataSource
    private val countersList = newCountersList()

    @Before
    fun setUp() {
        local = CountersLocalDataSource(dao)
    }

    @Test
    fun onSaveCountersList_callDao() = runBlockingTest {
        // Act
        local.saveCountersList(countersList)

        // Assert
        coVerify { dao.saveCountersList(countersList) }
    }

    @Test
    fun onGetCountersList_fromDao() = runBlockingTest {
        // Arrange
        val expected = flowOf(countersList)
        coEvery { dao.getCountersList() } returns expected

        // Act
        val result = local.getCountersList()

        // Assert
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun onDeleteCounters_callDao() = runBlockingTest {
        // Arrange
        val idsList = countersList.map { it.id }

        // Act
        local.deleteCountersNotIn(idsList)

        // Assert
        coVerify { dao.deleteCountersNotIn(idsList) }
    }
}