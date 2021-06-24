package com.cornershop.counterstest.presentation.counters.addcounter

import androidx.lifecycle.Observer
import com.cornershop.counterstest.domain.counters.usecase.AddCounterUseCase
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.testutil.ViewModelTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AddCounterViewModelTest : ViewModelTest() {

    @MockK
    private lateinit var useCase: AddCounterUseCase

    private lateinit var viewModel: AddCounterViewModel

    @Before
    fun setUp() {
        viewModel = AddCounterViewModel(useCase)
    }

    @Test
    fun onAddCounter_setLoading() {
        // Arrange
        viewModel.title.value = "title"
        coEvery { useCase(any()) } returns Result.Success(Unit)

        val loadingObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.isLoading.observeForever(loadingObserver)

        // Act
        viewModel.onSaveClicked()

        // Assert
        coVerifyOrder {
            loadingObserver.onChanged(true)
            useCase(any())
            loadingObserver.onChanged(false)
        }
    }

    @Test
    fun onAddCounterSuccess_setGoToCounters() {
        // Arrange
        val title = "title"
        viewModel.title.value = title
        coEvery { useCase(title) } returns Result.Success(Unit)

        val loadingObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.isLoading.observeForever(loadingObserver)

        // Act
        viewModel.onSaveClicked()

        // Assert
        assertEquals(Unit, viewModel.goToCounters.value?.peekContent())
    }

    @Test
    fun onAddCounterError_setError() {
        // Arrange
        val title = "title"
        viewModel.title.value = title
        coEvery { useCase(title) } returns Result.Error(Exception())

        // Act
        viewModel.onSaveClicked()

        // Assert
        assertEquals(Unit, viewModel.error.value?.peekContent())
    }

    @Test
    fun onEmptyTitle_doNothing() {
        // Arrange
        viewModel.title.value = ""

        // Act
        viewModel.onSaveClicked()

        // Assert
        coVerify(exactly = 0) { useCase(any()) }
    }

    @Test
    fun onSeeExamples_setGoToExamples() {
        // Act
        viewModel.onSeeExamplesClicked()

        // Assert
        assertEquals(Unit, viewModel.goToExamples.value?.peekContent())
    }

    @Test
    fun onCounterClicked_setTitle() {
        // Arrange
        val title = "title"

        // Act
        viewModel.onCounterClicked().onClicked(title)

        // Assert
        assertEquals(title, viewModel.title.value)
    }

    @Test
    fun onCounterClicked_setGoToAddCounter() {
        // Act
        viewModel.onCounterClicked().onClicked("title")

        // Assert
        assertEquals(Unit, viewModel.goToAddCounter.value?.peekContent())
    }
}
