package com.cornershop.counterstest.data.counters.repository

import com.cornershop.counterstest.data.counters.datasource.CountersLocalDataSource
import com.cornershop.counterstest.data.counters.datasource.CountersRemoteDataSource
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.factory.CountersFactory.newAddCounter
import com.cornershop.counterstest.factory.CountersFactory.newCountersList
import com.cornershop.counterstest.factory.CountersFactory.newModifyCounter
import com.cornershop.counterstest.testutil.MockKTest
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CountersRepositoryImplTest : MockKTest() {

    @RelaxedMockK
    private lateinit var local: CountersLocalDataSource
    @MockK
    private lateinit var remote: CountersRemoteDataSource

    private lateinit var repository: CountersRepositoryImpl
    private val countersList = newCountersList()
    private val countersIdsList = countersList.map { it.id }

    @Before
    fun setUp() {
        repository = CountersRepositoryImpl(remote, local)
    }

    @Test
    fun onGetCountersList_returnSuccessList() = runBlockingTest {
        // Arrange
        val countersListFlow = flowOf(countersList)
        coEvery { local.getCountersList() } returns countersListFlow

        // Act
        val result = repository.getCountersList()

        // Assert
        assertEquals(
            countersListFlow.map { Result.Success(it) }.toList(),
            result.toList()
        )
    }

    @Test
    fun onRefreshCountersListSuccess_saveToLocal() = runBlockingTest {
        // Arrange
        coEvery { remote.getCountersList() } returns Result.Success(countersList)

        // Act
        val result = repository.refreshCountersList()

        // Assert
        coVerifyOrder {
            local.deleteCountersNotIn(countersIdsList)
            local.saveCountersList(countersList)
        }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun onRefreshCountersListError_returnError() = runBlockingTest {
        // Arrange
        val expected = Result.Error(Exception())
        coEvery { remote.getCountersList() } returns expected

        // Act
        val result = repository.refreshCountersList()

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun onAddCounterSuccess_saveToLocal() = runBlockingTest {
        // Arrange
        val title = "title"
        coEvery { remote.addCounter(newAddCounter(title)) } returns Result.Success(countersList)

        // Act
        val result = repository.addCounter(title)

        // Assert
        coVerifyOrder {
            local.deleteCountersNotIn(countersIdsList)
            local.saveCountersList(countersList)
        }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun onDeleteCounterSuccess_saveToLocal() = runBlockingTest {
        // Arrange
        val id = "id"
        coEvery { remote.deleteCounter(newModifyCounter(id)) } returns Result.Success(countersList)

        // Act
        val result = repository.deleteCounter(id)

        // Assert
        coVerifyOrder {
            local.deleteCountersNotIn(countersIdsList)
            local.saveCountersList(countersList)
        }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun onIncrementCounterSuccess_saveToLocal() = runBlockingTest {
        // Arrange
        val id = "id"
        coEvery { remote.incrementCounter(newModifyCounter(id)) } returns
            Result.Success(countersList)

        // Act
        val result = repository.incrementCounter(id)

        // Assert
        coVerifyOrder {
            local.deleteCountersNotIn(countersIdsList)
            local.saveCountersList(countersList)
        }
        assertEquals(Result.Success(Unit), result)
    }

    @Test
    fun onDecrementCounterSuccess_saveToLocal() = runBlockingTest {
        // Arrange
        val id = "id"
        coEvery { remote.decrementCounter(newModifyCounter(id)) } returns
            Result.Success(countersList)

        // Act
        val result = repository.decrementCounter(id)

        // Assert
        coVerifyOrder {
            local.deleteCountersNotIn(countersIdsList)
            local.saveCountersList(countersList)
        }
        assertEquals(Result.Success(Unit), result)
    }
}