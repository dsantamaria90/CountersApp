package com.cornershop.counterstest.presentation.counters.counterslist.delegate

import androidx.lifecycle.LiveData
import com.cornershop.counterstest.presentation.counters.counterslist.CounterRetryClickListener
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterUI
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterView

interface CountersViewModelDelegate {

    val filteredCountersList: LiveData<List<CounterUI>>
    val isRefreshing: LiveData<Boolean>
    val viewToShow: LiveData<CounterView>
    val summedCounters: LiveData<Int>

    fun onRefresh()
    fun setSelected(counter: CounterUI?)
    fun onRetryLoadCountersClicked(): CounterRetryClickListener
    fun onSearchQueryChanged(): (String) -> Unit
}
