package com.cornershop.counterstest.presentation.counters.counterslist.viewmodel

import androidx.lifecycle.Observer
import com.cornershop.counterstest.domain.counters.entity.Counter
import com.cornershop.counterstest.domain.counters.usecase.GetCountersListUseCase
import com.cornershop.counterstest.domain.counters.usecase.RefreshCountersListUseCase
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.factory.CountersFactory.newCounterUI
import com.cornershop.counterstest.factory.CountersFactory.newCountersList
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterView
import com.cornershop.counterstest.testutil.TestDispatcher.newTestDispatcher
import com.cornershop.counterstest.testutil.ViewModelTest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class CountersViewModelDelegateImplTest : ViewModelTest() {

    @MockK
    private lateinit var getCountersListUseCase: GetCountersListUseCase
    @MockK
    private lateinit var refreshCountersListUseCase: RefreshCountersListUseCase
    @RelaxedMockK
    private lateinit var viewToShowObserver: Observer<CounterView>
    private val slot = slot<CounterView>()
    private val viewToShowObserverList = mutableListOf<CounterView>()
    private val countersList = newCountersList()

    @Test
    fun onInit_loadCountersList() {
        // Act
        val viewModel = newViewModel()

        // Assert
        assertEquals(countersList.mapToUI(), viewModel.filteredCountersList.value)
    }

    private fun newViewModel(
        emptyList: Boolean = false,
        isRefreshSuccess: Boolean = true
    ): CountersViewModelDelegateImpl {
        every { getCountersListUseCase(null) } returns flowOf(Result.Success(
            if (emptyList) {
                emptyList()
            } else {
                countersList
            }
        ))
        coEvery { refreshCountersListUseCase(null) } returns if (isRefreshSuccess) {
            Result.Success(Unit)
        } else {
            Result.Error(Exception())
        }

        every { viewToShowObserver.onChanged(capture(slot)) } answers {
            viewToShowObserverList.add(slot.captured)
        }

        return CountersViewModelDelegateImpl(
            getCountersListUseCase,
            refreshCountersListUseCase,
            newTestDispatcher()
        )
    }

    private fun List<Counter>.mapToUI() = map { newCounterUI(it.id, it.title, it.count) }

    @Test
    fun onInit_callRefreshUseCase() {
        // Act
        newViewModel()

        // Assert
        coVerify { refreshCountersListUseCase(null) }
    }

    @Test
    fun onRefresh_callRefreshUseCase() {
        // Arrange
        val viewModel = newViewModel()
        val refreshingObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.isRefreshing.observeForever(refreshingObserver)

        // Act
        viewModel.onRefresh()

        // Assert
        coVerifyOrder {
            refreshingObserver.onChanged(true)
            refreshCountersListUseCase(null)
            refreshingObserver.onChanged(false)
        }
    }

    @Test
    fun onSetSelected_updateCountersList() {
        // Arrange
        val viewModel = newViewModel()
        val countersUiList = countersList.mapToUI()

        // Act
        viewModel.setSelected(countersUiList.first())
        countersUiList.first().isSelected = true

        // Assert
        assertEquals(countersUiList, viewModel.filteredCountersList.value)
    }

    @Test
    fun onRetry_callRefreshUseCase() {
        // Arrange
        val viewModel = newViewModel()

        // Act
        viewModel.onRetryLoadCountersClicked().onRetryClicked()

        // Assert
        coVerify(exactly = 2) { refreshCountersListUseCase(null) }
    }

    @Test
    fun onSearchQueryChanged_filterCountersList() {
        // Arrange
        val viewModel = newViewModel()
        val firstCounter = countersList.mapToUI().first()

        // Act
        viewModel.onSearchQueryChanged()(firstCounter.title)

        // Assert
        assertEquals(listOf(firstCounter), viewModel.filteredCountersList.value)
    }

    @Test
    fun onSearchQueryNotFound_showNoSearchResults() {
        // Arrange
        val viewModel = newViewModel()
        viewModel.viewToShow.observeForever(viewToShowObserver)

        // Act
        viewModel.onSearchQueryChanged()("Hello")

        // Assert
        assertEquals(CounterView.NO_SEARCH_RESULTS, viewToShowObserverList.last())
    }

    @Test
    fun onEmptyList_showEmptyCountersList() {
        // Act
        val viewModel = newViewModel(emptyList = true)
        viewModel.viewToShow.observeForever(viewToShowObserver)

        // Assert
        assertEquals(CounterView.EMPTY_COUNTERS_LIST, viewToShowObserverList.last())
    }

    @Test
    fun onNotEmptyList_showCountersList() {
        // Act
        val viewModel = newViewModel()
        viewModel.viewToShow.observeForever(viewToShowObserver)

        // Assert
        assertEquals(CounterView.COUNTERS_LIST, viewToShowObserverList.last())
    }

    @Test
    fun onEmptyListWithRefreshError_showError() {
        // Act
        val viewModel = newViewModel(emptyList = true, isRefreshSuccess = false)
        viewModel.viewToShow.observeForever(viewToShowObserver)

        // Assert
        assertEquals(CounterView.ERROR, viewToShowObserverList.last())
    }
}
