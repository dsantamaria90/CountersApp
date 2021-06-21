package com.cornershop.counterstest.presentation.counters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.entity.ModifyCounterType
import com.cornershop.counterstest.domain.entity.ModifyCounterUseCaseArgs
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.usecase.GetCountersListUseCase
import com.cornershop.counterstest.domain.usecase.ModifyCounterUseCase
import com.cornershop.counterstest.domain.usecase.RefreshCountersListUseCase
import com.cornershop.counterstest.presentation.entity.CounterView
import com.cornershop.counterstest.presentation.entity.ModifyCounterError
import com.cornershop.counterstest.presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val getCountersListUseCase: GetCountersListUseCase,
    private val refreshCountersListUseCase: RefreshCountersListUseCase,
    private val modifyCounterUseCase: ModifyCounterUseCase
) : ViewModel() {

    val isLoading: LiveData<Boolean> get() = _isLoading
    val filteredCountersList: LiveData<List<Counter>> get() = _filteredCountersList
    val goToAddCounter: LiveData<Event<Unit>> get() = _goToAddCounter
    val modifyCounterError: LiveData<Event<ModifyCounterError>> get() = _modifyCounterError
    val viewToShow: LiveData<CounterView> get() = _viewToShow

    private val _isLoading = MutableLiveData<Boolean>()
    private val _filteredCountersList = MutableLiveData<List<Counter>>(emptyList())
    private val _goToAddCounter = MutableLiveData<Event<Unit>>()
    private val _modifyCounterError = MutableLiveData<Event<ModifyCounterError>>()
    private val _viewToShow = MutableLiveData(CounterView.NONE)
    private var countersList = emptyList<Counter>()
    private var lastSearchedQuery = ""

    val summedCounters: LiveData<Int> = map(filteredCountersList) { list -> list.sumBy { it.count } }

    init {
        loadCountersList()
        onRefresh()
    }

    private fun loadCountersList() {
        viewModelScope.launch {
            getCountersListUseCase(null).collect { result ->
                when (result) {
                    is Result.Success -> {
                        countersList = result.data
                        filterCountersList()
                    }
                }
            }
        }
    }

    private fun filterCountersList() {
        _filteredCountersList.value = countersList.filter { it.title.contains(lastSearchedQuery) }
        setViewToShow()
    }

    private fun setViewToShow() {
        val isLoading = _isLoading.value == true
        val isEmpty = filteredCountersList.value?.isNullOrEmpty() == true
        _viewToShow.value = if (isEmpty && lastSearchedQuery.isNotEmpty() && !isLoading) {
            CounterView.NO_SEARCH_RESULTS
        } else if (isEmpty && !isLoading) {
            CounterView.EMPTY_COUNTERS_LIST
        } else if (!isEmpty) {
            CounterView.COUNTERS_LIST
        } else {
            CounterView.NONE
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000) // TODO delete
            refreshCountersListUseCase(null).also { result ->
                _isLoading.value = false
                when (result) {
                    is Result.Success -> setViewToShow()
                    is Result.Error -> if (filteredCountersList.value?.isNullOrEmpty() == true) {
                        _viewToShow.value = CounterView.ERROR
                    }
                }
            }
        }
    }

    fun onCounterItemClicked() = object : CounterItemClickListener {
        override fun onIncrementClicked(counter: Counter) {
            modifyCounter(counter, ModifyCounterType.INCREMENT)
        }

        override fun onDecrementClicked(counter: Counter) {
            modifyCounter(counter, ModifyCounterType.DECREMENT)
        }
    }

    private fun modifyCounter(counter: Counter, type: ModifyCounterType) {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000) // TODO delete
            modifyCounterUseCase(ModifyCounterUseCaseArgs(counter.id, type)).also { result ->
                _isLoading.value = false
                when (result) {
                    is Result.Error -> setModifyCounterError(counter, type)
                }
            }
        }
    }

    private fun setModifyCounterError(counter: Counter, type: ModifyCounterType) {
        _modifyCounterError.value = Event(
            ModifyCounterError(counter, type, counter.count + when (type) {
                ModifyCounterType.INCREMENT -> NEXT_COUNT
                ModifyCounterType.DECREMENT -> PREVIOUS_COUNT
            })
        )
    }

    fun onRetryModifyCounterClicked(modifyCounter: ModifyCounterError) {
        with(modifyCounter) {
            modifyCounter(counter, type)
        }
    }

    fun onRetryLoadCountersClicked() = object : CounterRetryClickListener  {
        override fun onRetryClicked() {
            _viewToShow.value = CounterView.NONE
            onRefresh()
        }
    }

    fun onAddCounterClicked() {
        _goToAddCounter.value = Event(Unit)
    }

    fun onSearchQueryChanged(): (String) -> Unit = {
        lastSearchedQuery = it.trim()
        filterCountersList()
    }

    companion object {
        private const val NEXT_COUNT = 1
        private const val PREVIOUS_COUNT = -1
    }
}