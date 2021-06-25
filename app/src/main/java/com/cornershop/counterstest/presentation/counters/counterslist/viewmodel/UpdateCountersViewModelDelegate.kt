package com.cornershop.counterstest.presentation.counters.counterslist.viewmodel

import androidx.lifecycle.LiveData
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterUI
import com.cornershop.counterstest.presentation.counters.counterslist.entity.ModifyCounterError
import com.cornershop.counterstest.presentation.util.Event

interface UpdateCountersViewModelDelegate {

    val isUpdating: LiveData<Boolean>
    val modifyCounterError: LiveData<Event<ModifyCounterError>>
    val deleteError: LiveData<Event<Unit>>

    fun onModifyCounter(counter: CounterUI, type: ModifyCounterType)
    fun onDeleteConfirmed(counter: CounterUI, onSuccess: () -> Unit)
}
