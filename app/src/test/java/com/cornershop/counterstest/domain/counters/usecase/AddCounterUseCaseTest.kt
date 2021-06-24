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
class AddCounterUseCaseTest : MockKTest() {

    @MockK
    private lateinit var repository: CountersRepository

    private lateinit var useCase: AddCounterUseCase

    @Before
    fun setUp() {
        useCase = AddCounterUseCase(repository, newTestDispatcher())
    }

    @Test
    fun onExecuteSuccess_returnSuccess() = runBlockingTest {
        // Arrange
        val title = "title"
        val expected = Result.Success(Unit)
        coEvery { repository.addCounter(title) } returns expected

        // Act
        val result = useCase(title)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun onExecuteError_returnError() = runBlockingTest {
        // Arrange
        val title = "title"
        val expected = Result.Error(Exception())
        coEvery { repository.addCounter(title) } returns expected

        // Act
        val result = useCase(title)

        // Assert
        assertEquals(expected, result)
    }
}
