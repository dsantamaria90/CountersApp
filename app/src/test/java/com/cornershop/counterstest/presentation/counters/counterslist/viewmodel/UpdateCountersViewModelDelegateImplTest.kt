package com.cornershop.counterstest.presentation.counters.counterslist.viewmodel

import androidx.lifecycle.Observer
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType
import com.cornershop.counterstest.domain.counters.usecase.DeleteCounterUseCase
import com.cornershop.counterstest.domain.counters.usecase.ModifyCounterUseCase
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.factory.CountersFactory.newCounterUI
import com.cornershop.counterstest.factory.CountersFactory.newModifyCounterUseCaseArgs
import com.cornershop.counterstest.presentation.counters.counterslist.entity.ModifyCounterError
import com.cornershop.counterstest.testutil.TestDispatcher.newTestDispatcher
import com.cornershop.counterstest.testutil.ViewModelTest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateCountersViewModelDelegateImplTest : ViewModelTest() {

    @MockK
    private lateinit var modifyCounterUseCase: ModifyCounterUseCase

    @MockK
    private lateinit var deleteCounterUseCase: DeleteCounterUseCase

    private lateinit var viewModel: UpdateCountersViewModelDelegateImpl
    private val counter = newCounterUI()

    @Before
    fun setUp() {
        viewModel = UpdateCountersViewModelDelegateImpl(
            modifyCounterUseCase,
            deleteCounterUseCase,
            newTestDispatcher()
        )
    }

    @Test
    fun onModifyCounter_setUpdating() {
        // Arrange
        coEvery { modifyCounterUseCase(any()) } returns Result.Success(Unit)
        val updatingObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.isUpdating.observeForever(updatingObserver)

        // Act
        viewModel.onModifyCounter(counter, ModifyCounterType.INCREMENT)

        // Assert
        coVerifyOrder {
            updatingObserver.onChanged(true)
            modifyCounterUseCase(any())
            updatingObserver.onChanged(false)
        }
    }

    @Test
    fun onModifyCounterSuccess_doesNotSetError() {
        // Arrange
        val args = newModifyCounterUseCaseArgs(counter.id, ModifyCounterType.INCREMENT)
        coEvery { modifyCounterUseCase(args) } returns Result.Success(Unit)

        // Act
        viewModel.onModifyCounter(counter, args.type)

        // Assert
        assertEquals(null, viewModel.modifyCounterError.value?.peekContent())
    }

    @Test
    fun onIncrementCounterError_setError() {
        // Arrange
        val args = newModifyCounterUseCaseArgs(counter.id, ModifyCounterType.INCREMENT)
        coEvery { modifyCounterUseCase(args) } returns Result.Error(Exception())

        // Act
        viewModel.onModifyCounter(counter, args.type)

        // Assert
        assertEquals(
            ModifyCounterError(counter, args.type, counter.count + 1),
            viewModel.modifyCounterError.value?.peekContent()
        )
    }

    @Test
    fun onDecrementCounterError_setError() {
        // Arrange
        val args = newModifyCounterUseCaseArgs(counter.id, ModifyCounterType.DECREMENT)
        coEvery { modifyCounterUseCase(args) } returns Result.Error(Exception())

        // Act
        viewModel.onModifyCounter(counter, args.type)

        // Assert
        assertEquals(
            ModifyCounterError(counter, args.type, counter.count - 1),
            viewModel.modifyCounterError.value?.peekContent()
        )
    }

    @Test
    fun onDeleteConfirmed_setUpdating() {
        // Arrange
        coEvery { deleteCounterUseCase(any()) } returns Result.Success(Unit)
        val updatingObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.isUpdating.observeForever(updatingObserver)

        // Act
        viewModel.onDeleteConfirmed(counter) { }

        // Assert
        coVerifyOrder {
            updatingObserver.onChanged(true)
            deleteCounterUseCase(any())
            updatingObserver.onChanged(false)
        }
    }

    @Test
    fun onDeleteSuccess_callOnSuccess() {
        // Arrange
        coEvery { deleteCounterUseCase(counter.id) } returns Result.Success(Unit)
        val onSuccess: () -> Unit = mockk(relaxed = true)

        // Act
        viewModel.onDeleteConfirmed(counter, onSuccess)

        // Assert
        verify { onSuccess() }
    }

    @Test
    fun onDeleteError_setError() {
        // Arrange
        coEvery { deleteCounterUseCase(counter.id) } returns Result.Error(Exception())
        val onSuccess: () -> Unit = mockk(relaxed = true)

        // Act
        viewModel.onDeleteConfirmed(counter, onSuccess)

        // Assert
        assertEquals(Unit, viewModel.deleteError.value?.peekContent())
        verify { onSuccess wasNot Called }
    }
}
