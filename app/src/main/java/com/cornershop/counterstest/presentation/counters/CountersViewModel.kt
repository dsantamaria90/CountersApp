package com.cornershop.counterstest.presentation.counters

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.map
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.entity.ModifyCounterType
import com.cornershop.counterstest.domain.entity.ModifyCounterUseCaseArgs
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.usecase.GetCountersListUseCase
import com.cornershop.counterstest.domain.usecase.ModifyCounterUseCase
import com.cornershop.counterstest.domain.usecase.RefreshCountersListUseCase
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

    private val _isError = MutableLiveData(false)
    private val _isCountersListLoading = MutableLiveData(false)
    private val _countersList = MutableLiveData<List<Counter>>(emptyList())
    private val _goToAddCounter = MutableLiveData<Event<Unit>>()
    private val _modifyCounterError = MutableLiveData<Event<ModifyCounterError>>()

    val isCountersListLoading: LiveData<Boolean> get() = _isCountersListLoading
    val countersList: LiveData<List<Counter>> get() = _countersList
    val goToAddCounter: LiveData<Event<Unit>> get() = _goToAddCounter
    val modifyCounterError: LiveData<Event<ModifyCounterError>> get() = _modifyCounterError
    val summedCounters: LiveData<Int> = map(countersList) { list -> list.sumBy { it.count } }
    val showCountersList: LiveData<Boolean> = map(countersList) { it.isNotEmpty() }
    val isLoading: LiveData<Boolean> = getAllSources(isLoading())
    val showEmptyCounters: LiveData<Boolean> = getAllSources(showEmptyCounters())
    val showError: LiveData<Boolean> = getAllSources(showError())

    private fun getAllSources(call: () -> Boolean) = MediatorLiveData<Boolean>().apply {
        addSource(countersList) { postValue(call()) }
        addSource(_isCountersListLoading) { postValue(call()) }
        addSource(_isError) { postValue(call()) }
    }

    private fun isLoading(): () -> Boolean = {
        countersList.value.isNullOrEmpty() &&
            _isCountersListLoading.value == true &&
            _isError.value == false
    }

    private fun showEmptyCounters(): () -> Boolean = {
        countersList.value.isNullOrEmpty() &&
            _isCountersListLoading.value == false &&
            _isError.value == false
    }

    private fun showError(): () -> Boolean = {
        countersList.value.isNullOrEmpty() &&
            _isCountersListLoading.value == false &&
            _isError.value == true
    }

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
            _isCountersListLoading.postValue(true)
            delay(2000) // TODO delete
            refreshCountersListUseCase(null).also {
                _isCountersListLoading.postValue(false)
                _isError.postValue(it is Result.Error)
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
            _isCountersListLoading.postValue(true)
            delay(2000) // TODO delete
            modifyCounterUseCase(ModifyCounterUseCaseArgs(counter.id, type)).also { result ->
                _isCountersListLoading.postValue(false)
                when (result) {
                    is Result.Error -> setModifyCounterError(counter, type)
                }
            }
        }
    }

    private fun setModifyCounterError(counter: Counter, type: ModifyCounterType) {
        _modifyCounterError.postValue(Event(
            ModifyCounterError(counter, type, counter.count + when (type) {
                ModifyCounterType.INCREMENT -> NEXT_COUNT
                ModifyCounterType.DECREMENT -> PREVIOUS_COUNT
            })
        ))
    }

    fun onRetryModifyCounterClicked(modifyCounter: ModifyCounterError) {
        with(modifyCounter) {
            modifyCounter(counter, type)
        }
    }

    fun onRetryLoadCountersClicked() = object : CounterRetryClickListener  {
        override fun onRetryClicked() {
            _isError.postValue(false)
            onRefresh()
        }
    }

    fun onAddCounterClicked() {
        _goToAddCounter.postValue(Event(Unit))
    }

    companion object {
        private const val NEXT_COUNT = 1
        private const val PREVIOUS_COUNT = -1
    }
}