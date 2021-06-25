package com.cornershop.counterstest.presentation.counters.counterslist.viewmodel

import androidx.lifecycle.LiveData
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterUI
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterView
import com.cornershop.counterstest.presentation.counters.counterslist.view.CounterRetryClickListener

interface CountersViewModelDelegate {

    val filteredCountersList: LiveData<List<CounterUI>>
    val isRefreshing: LiveData<Boolean>
    val viewToShow: LiveData<CounterView>

    fun onRefresh()
    fun setSelected(counter: CounterUI?)
    fun onRetryLoadCountersClicked(): CounterRetryClickListener
    fun onSearchQueryChanged(): (String) -> Unit
}
