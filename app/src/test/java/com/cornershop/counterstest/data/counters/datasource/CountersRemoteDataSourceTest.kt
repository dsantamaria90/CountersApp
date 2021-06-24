package com.cornershop.counterstest.data.counters.datasource

import com.cornershop.counterstest.data.counters.service.CountersService
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.factory.CountersFactory.newAddCounter
import com.cornershop.counterstest.factory.CountersFactory.newCountersList
import com.cornershop.counterstest.factory.CountersFactory.newModifyCounter
import com.cornershop.counterstest.testutil.MockKTest
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CountersRemoteDataSourceTest : MockKTest() {

    @MockK
    private lateinit var service: CountersService

    private lateinit var remote: CountersRemoteDataSource
    private val countersList = newCountersList()

    @Before
    fun setUp() {
        remote = CountersRemoteDataSource(service)
    }

    @Test
    fun onGetCountersListSuccess_returnCountersList() = runBlockingTest {
        // Arrange
        coEvery { service.getCountersList() } returns countersList

        // Act
        val result = remote.getCountersList()

        // Assert
        assertEquals(Result.Success(countersList), result)
    }

    @Test
    fun onGetCountersListError_returnError() = runBlockingTest {
        // Arrange
        val error = Exception()
        coEvery { service.getCountersList() } throws error

        // Act
        val result = remote.getCountersList()

        // Assert
        assertEquals(Result.Error(error), result)
    }

    @Test
    fun onAddCounterSuccess_returnCountersList() = runBlockingTest {
        // Arrange
        val addCounter = newAddCounter()
        coEvery { service.addCounter(addCounter) } returns countersList

        // Act
        val result = remote.addCounter(addCounter)

        // Assert
        assertEquals(Result.Success(countersList), result)
    }

    @Test
    fun onDeleteCounterSuccess_returnCountersList() = runBlockingTest {
        // Arrange
        val modifyCounter = newModifyCounter()
        coEvery { service.deleteCounter(modifyCounter) } returns countersList

        // Act
        val result = remote.deleteCounter(modifyCounter)

        // Assert
        assertEquals(Result.Success(countersList), result)
    }

    @Test
    fun onIncrementCounterSuccess_returnCountersList() = runBlockingTest {
        // Arrange
        val modifyCounter = newModifyCounter()
        coEvery { service.incrementCounter(modifyCounter) } returns countersList

        // Act
        val result = remote.incrementCounter(modifyCounter)

        // Assert
        assertEquals(Result.Success(countersList), result)
    }

    @Test
    fun onDecrementCounterSuccess_returnCountersList() = runBlockingTest {
        // Arrange
        val modifyCounter = newModifyCounter()
        coEvery { service.decrementCounter(modifyCounter) } returns countersList

        // Act
        val result = remote.decrementCounter(modifyCounter)

        // Assert
        assertEquals(Result.Success(countersList), result)
    }
}