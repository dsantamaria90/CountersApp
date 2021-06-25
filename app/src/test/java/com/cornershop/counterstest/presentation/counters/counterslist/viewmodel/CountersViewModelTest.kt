package com.cornershop.counterstest.presentation.counters.counterslist.viewmodel

import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType
import com.cornershop.counterstest.factory.CountersFactory.newCounterUI
import com.cornershop.counterstest.factory.CountersFactory.newCountersUiList
import com.cornershop.counterstest.testutil.ViewModelTest
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CountersViewModelTest : ViewModelTest() {

    @RelaxedMockK
    private lateinit var countersDelegate: CountersViewModelDelegate

    @RelaxedMockK
    private lateinit var updateDelegate: UpdateCountersViewModelDelegate

    private lateinit var viewModel: CountersViewModel
    private val counter = newCounterUI()

    @Before
    fun setUp() {
        viewModel = CountersViewModel(
            countersDelegate,
            updateDelegate
        )
    }

    @Test
    fun onIncrementClicked_modifyCounter() {
        // Act
        viewModel.onCounterItemClicked().onIncrementClicked(counter)

        // Assert
        verify { updateDelegate.onModifyCounter(counter, ModifyCounterType.INCREMENT) }
    }

    @Test
    fun onDecrementClicked_modifyCounter() {
        // Act
        viewModel.onCounterItemClicked().onDecrementClicked(counter)

        // Assert
        verify { updateDelegate.onModifyCounter(counter, ModifyCounterType.DECREMENT) }
    }

    @Test
    fun onNotEditingLongClicked_setCounterSelection() {
        // Act
        viewModel.onCounterItemClicked().onLongClicked(counter)

        // Assert
        assertEquals(true, viewModel.isEditing.value)
        verify { countersDelegate.setSelected(counter) }
    }

    @Test
    fun onEditingLongClicked_doNothing() {
        // Arrange
        viewModel.onCounterItemClicked().onLongClicked(counter)

        // Act
        viewModel.onCounterItemClicked().onLongClicked(counter)

        // Assert
        verify(exactly = 1) { countersDelegate.setSelected(any()) }
    }

    @Test
    fun onNotEditingClicked_doNothing() {
        // Act
        viewModel.onCounterItemClicked().onClicked(counter)

        // Assert
        verify(exactly = 0) { countersDelegate.setSelected(any()) }
    }

    @Test
    fun onEditingClicked_setCounterSelection() {
        // Arrange
        viewModel.onCounterItemClicked().onLongClicked(counter)

        // Act
        viewModel.onCounterItemClicked().onClicked(counter)

        // Assert
        verify(exactly = 2) { countersDelegate.setSelected(counter) }
    }

    @Test
    fun onAddCounterClicked_setGoToAddCounter() {
        // Act
        viewModel.onAddCounterClicked()

        // Assert
        assertEquals(Unit, viewModel.goToAddCounter.value?.peekContent())
    }

    @Test
    fun onEditingEnded_setEditingToFalse() {
        // Act
        viewModel.onEditingEnded()

        // Assert
        assertEquals(false, viewModel.isEditing.value)
        verify { countersDelegate.setSelected(null) }
    }

    @Test
    fun onDeleteClicked_setConfirmDelete() {
        // Arrange
        val countersUiList = newCountersUiList()
        val position = (countersUiList.indices).random()
        countersUiList[position].isSelected = true
        every { countersDelegate.filteredCountersList.value } returns countersUiList

        // Act
        viewModel.onDeleteClicked()

        // Assert
        assertEquals(countersUiList[position], viewModel.confirmDelete.value?.peekContent())
    }

    @Test
    fun onDeleteConfirmed_endEditing() {
        // Arrange
        val onSuccess = slot<() -> Unit>()

        // Act
        viewModel.onDeleteConfirmed(counter)
        verify { updateDelegate.onDeleteConfirmed(counter, capture(onSuccess)) }
        onSuccess.captured()

        // Assert
        assertEquals(false, viewModel.isEditing.value)
        verify { countersDelegate.setSelected(null) }
    }

    @Test
    fun onShareClicked_setShare() {
        // Arrange
        val countersUiList = newCountersUiList()
        val position = (countersUiList.indices).random()
        countersUiList[position].isSelected = true
        every { countersDelegate.filteredCountersList.value } returns countersUiList

        // Act
        viewModel.onShareClicked()

        // Assert
        assertEquals(countersUiList[position], viewModel.share.value?.peekContent())
    }
}
