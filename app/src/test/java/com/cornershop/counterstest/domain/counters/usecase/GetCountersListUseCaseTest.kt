package com.cornershop.counterstest.domain.counters.usecase

import com.cornershop.counterstest.domain.counters.repository.CountersRepository
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.factory.CountersFactory.newCountersList
import com.cornershop.counterstest.testutil.MockKTest
import com.cornershop.counterstest.testutil.TestDispatcher.newTestDispatcher
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCountersListUseCaseTest : MockKTest() {

    @MockK
    private lateinit var repository: CountersRepository

    private lateinit var useCase: GetCountersListUseCase

    @Before
    fun setUp() {
        useCase = GetCountersListUseCase(repository, newTestDispatcher())
    }

    @Test
    fun onExecuteSuccess_returnSuccess() = runBlockingTest {
        // Arrange
        val expected = flowOf(Result.Success(newCountersList()))
        every { repository.getCountersList() } returns expected

        // Act
        val result = useCase(null)

        // Assert
        assertEquals(expected.toList(), result.toList())
    }

    @Test
    fun onExecuteError_returnError() = runBlockingTest {
        // Arrange
        val expected = flowOf(Result.Error(Exception()))
        every { repository.getCountersList() } returns expected

        // Act
        val result = useCase(null)

        // Assert
        assertEquals(expected.toList(), result.toList())
    }
}
