package com.cornershop.counterstest.domain.counters.usecase

import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType
import com.cornershop.counterstest.domain.counters.repository.CountersRepository
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.factory.CountersFactory.newModifyCounterUseCaseArgs
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
class ModifyCounterUseCaseTest : MockKTest() {

    @MockK
    private lateinit var repository: CountersRepository

    private lateinit var useCase: ModifyCounterUseCase

    @Before
    fun setUp() {
        useCase = ModifyCounterUseCase(repository, newTestDispatcher())
    }

    @Test
    fun onIncrementSuccess_returnSuccess() = runBlockingTest {
        // Arrange
        val args = newModifyCounterUseCaseArgs(ModifyCounterType.INCREMENT)
        val expected = Result.Success(Unit)
        coEvery { repository.incrementCounter(args.id) } returns expected

        // Act
        val result = useCase(args)

        // Assert
        assertEquals(expected, result)
    }

    @Test
    fun onDecrementSuccess_returnSuccess() = runBlockingTest {
        // Arrange
        val args = newModifyCounterUseCaseArgs(ModifyCounterType.DECREMENT)
        val expected = Result.Success(Unit)
        coEvery { repository.decrementCounter(args.id) } returns expected

        // Act
        val result = useCase(args)

        // Assert
        assertEquals(expected, result)
    }
}
