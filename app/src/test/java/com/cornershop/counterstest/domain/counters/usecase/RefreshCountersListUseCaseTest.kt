package com.cornershop.counterstest.domain.counters.usecase

import com.cornershop.counterstest.domain.counters.repository.CountersRepository
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.testutil.MockKTest
import com.cornershop.counterstest.testutil.TestDispatcher.newTestDispatcher
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RefreshCountersListUseCaseTest : MockKTest() {

    @MockK
    private lateinit var repository: CountersRepository

    private lateinit var useCase: RefreshCountersListUseCase

    @Before
    fun setUp() {
        useCase = RefreshCountersListUseCase(repository, newTestDispatcher())
    }

    @Test
    fun onExecuteSuccess_returnSuccess() = runBlockingTest {
        // Arrange
        val expected = Result.Success(Unit)
        coEvery { repository.refreshCountersList() } returns expected

        // Act
        val result = useCase(null)

        // Assert
        assertEquals(expected, result)
    }
}
