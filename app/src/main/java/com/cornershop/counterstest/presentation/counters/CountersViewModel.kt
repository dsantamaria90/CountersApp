package com.cornershop.counterstest.presentation.counters

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.usecase.GetCountersListUseCase
import com.cornershop.counterstest.domain.usecase.RefreshCountersListUseCase
import com.cornershop.counterstest.presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val getCountersListUseCase: GetCountersListUseCase,
    private val refreshCountersListUseCase: RefreshCountersListUseCase
) : ViewModel() {

    private val _isError = MutableLiveData(false)
    private val _isLoading = MutableLiveData(false)
    private val _countersList = MutableLiveData<List<Counter>>(emptyList())
    private val _goToAddCounter = MutableLiveData<Event<Unit>>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    val countersList: LiveData<List<Counter>> get() = _countersList
    val goToAddCounter: LiveData<Event<Unit>> get() = _goToAddCounter
    val summedCounters: LiveData<Int> = map(countersList) { list -> list.sumBy { it.count } }
    val showCountersList: LiveData<Boolean> = map(countersList) { it.isNotEmpty() }
    val showEmptyCounters: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(countersList) { postValue(showEmptyCounters()) }
        addSource(isLoading) { postValue(showEmptyCounters()) }
        addSource(_isError) { postValue(showEmptyCounters()) }
    }
    val showError: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(countersList) { postValue(showError()) }
        addSource(isLoading) { postValue(showError()) }
        addSource(_isError) { postValue(showError()) }
    }

    private fun showEmptyCounters() = countersList.value.isNullOrEmpty() &&
        isLoading.value == false &&
        _isError.value == false

    private fun showError() = !countersList.value.isNullOrEmpty() &&
        isLoading.value == false &&
        _isError.value == true

    init {
        loadCountersList()
        onRefresh()
    }

    private fun loadCountersList() {
        viewModelScope.launch {
            getCountersListUseCase(null).collect { result ->
                when (result) {
                    is Result.Success -> _countersList.postValue(result.data)
                }
            }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            refreshCountersListUseCase(null).also {
                delay(2000) // TODO delete
                _isLoading.postValue(false)
                _isError.postValue(it is Result.Error)
            }
        }
    }

    fun onCounterItemClicked() = object : CounterItemClickListener {
        override fun onIncrementClicked(counter: Counter) {
            println("onIncrementClicked")
        }

        override fun onDecrementClicked(counter: Counter) {
            println("onDecrementClicked")
        }
    }

    fun onAddCounterClicked() {
        _goToAddCounter.postValue(Event(Unit))
    }
}