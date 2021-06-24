package com.cornershop.counterstest.presentation.counters.counterslist.delegate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.cornershop.counterstest.domain.counters.entity.Counter
import com.cornershop.counterstest.domain.counters.usecase.GetCountersListUseCase
import com.cornershop.counterstest.domain.counters.usecase.RefreshCountersListUseCase
import com.cornershop.counterstest.domain.di.MainDispatcher
import com.cornershop.counterstest.domain.entity.getData
import com.cornershop.counterstest.domain.entity.isError
import com.cornershop.counterstest.domain.entity.isSuccess
import com.cornershop.counterstest.presentation.counters.counterslist.CounterRetryClickListener
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterUI
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class CountersViewModelDelegateImpl @Inject constructor(
    private val getCountersListUseCase: GetCountersListUseCase,
    private val refreshCountersListUseCase: RefreshCountersListUseCase,
    @MainDispatcher dispatcher: CoroutineDispatcher
) : CountersViewModelDelegate,
    CoroutineScope by CoroutineScope(dispatcher) {

    override val isRefreshing: LiveData<Boolean> get() = _isRefreshing
    override val filteredCountersList: LiveData<List<CounterUI>> get() = _filteredCountersList

    private val _isRefreshing = MutableLiveData<Boolean>()
    private val _filteredCountersList = MutableLiveData<List<CounterUI>>(emptyList())
    private val _refreshError = MutableLiveData<Boolean>()
    private var countersList = emptyList<Counter>()
    private var lastSearchedQuery = ""

    override val viewToShow: LiveData<CounterView> = MediatorLiveData<CounterView>().apply {
        addSource(_filteredCountersList) { value = getViewToShow() }
        addSource(_isRefreshing) { value = getViewToShow() }
        addSource(_refreshError) { value = getViewToShow() }
    }

    private fun getViewToShow(): CounterView {
        val isLoading = _isRefreshing.value == true
        val isEmpty = _filteredCountersList.value?.isNullOrEmpty() == true
        val isError = _refreshError.value == true
        val isQueryEmpty = lastSearchedQuery.isEmpty()

        return if (isEmpty && !isQueryEmpty && !isLoading) {
            CounterView.NO_SEARCH_RESULTS
        } else if (isEmpty && !isLoading && !isError) {
            CounterView.EMPTY_COUNTERS_LIST
        } else if (!isEmpty) {
            CounterView.COUNTERS_LIST
        } else if (isLoading) {
            CounterView.LOADING
        } else if (isEmpty && isError) {
            CounterView.ERROR
        } else {
            CounterView.NONE
        }
    }

    init {
        loadCountersList()
        onRefresh()
    }

    private fun loadCountersList() {
        launch {
            getCountersListUseCase(null).collect { result ->
                if (result.isSuccess()) {
                    countersList = result.getData()
                    filterCountersList()
                }
            }
        }
    }

    private fun filterCountersList() {
        _filteredCountersList.value = countersList.filter {
            it.title.contains(lastSearchedQuery)
        }.map {
            with(it) {
                CounterUI(id, title, count)
            }
        }
    }

    override fun onRefresh() {
        launch {
            _isRefreshing.value = true
            _refreshError.value = false
            refreshCountersListUseCase(null).also { result ->
                _isRefreshing.value = false
                if (result.isError()) {
                    _refreshError.value = true
                }
            }
        }
    }

    override fun setSelected(counter: CounterUI?) {
        _filteredCountersList.value?.let { list ->
            list.forEach {
                it.isSelected = it == counter
            }
            _filteredCountersList.value = list
        }
    }

    override fun onRetryLoadCountersClicked() = object : CounterRetryClickListener {
        override fun onRetryClicked() {
            onRefresh()
        }
    }

    override fun onSearchQueryChanged(): (String) -> Unit = {
        lastSearchedQuery = it.trim()
        filterCountersList()
    }
}
