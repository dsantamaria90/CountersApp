package com.cornershop.counterstest.presentation.counters.counterslist.delegate

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import com.cornershop.counterstest.domain.counters.entity.Counter
import com.cornershop.counterstest.domain.counters.usecase.GetCountersListUseCase
import com.cornershop.counterstest.domain.counters.usecase.RefreshCountersListUseCase
import com.cornershop.counterstest.domain.di.MainDispatcher
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.entity.getData
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
    override val viewToShow: LiveData<CounterView> get() = _viewToShow

    private val _isRefreshing = MutableLiveData<Boolean>()
    private val _filteredCountersList = MutableLiveData<List<CounterUI>>(emptyList())
    private val _viewToShow = MutableLiveData(CounterView.NONE)
    private var countersList = emptyList<Counter>()
    private var lastSearchedQuery = ""

    override val summedCounters: LiveData<Int> =
        map(filteredCountersList) { list -> list.sumBy { it.count } }

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
        setViewToShow()
    }

    private fun setViewToShow() {
        val isLoading = _isRefreshing.value == true
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

    override fun onRefresh() {
        launch {
            _isRefreshing.value = true
            refreshCountersListUseCase(null).also { result ->
                _isRefreshing.value = false
                when (result) {
                    is Result.Success -> setViewToShow()
                    is Result.Error -> if (filteredCountersList.value?.isNullOrEmpty() == true) {
                        _viewToShow.value = CounterView.ERROR
                    }
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
            _viewToShow.value = CounterView.NONE
            onRefresh()
        }
    }

    override fun onSearchQueryChanged(): (String) -> Unit = {
        lastSearchedQuery = it.trim()
        filterCountersList()
    }
}
