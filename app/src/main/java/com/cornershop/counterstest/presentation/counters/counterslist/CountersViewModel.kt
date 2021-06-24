package com.cornershop.counterstest.presentation.counters.counterslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType
import com.cornershop.counterstest.presentation.counters.counterslist.delegate.CountersViewModelDelegate
import com.cornershop.counterstest.presentation.counters.counterslist.delegate.UpdateCountersViewModelDelegate
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterUI
import com.cornershop.counterstest.presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    countersListViewModelDelegate: CountersViewModelDelegate,
    updateCountersViewModelDelegate: UpdateCountersViewModelDelegate
) : ViewModel(),
    CountersViewModelDelegate by countersListViewModelDelegate,
    UpdateCountersViewModelDelegate by updateCountersViewModelDelegate {

    val goToAddCounter: LiveData<Event<Unit>> get() = _goToAddCounter
    val isEditing: LiveData<Boolean> get() = _isEditing
    val confirmDelete: LiveData<Event<CounterUI>> get() = _confirmDelete
    val share: LiveData<Event<CounterUI>> get() = _share
    val summedCounters: LiveData<Int> = map(filteredCountersList) { list -> list.sumBy { it.count } }
    val isLoading: LiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(isRefreshing) { value = it }
        addSource(isUpdating) { value = it }
    }

    private val _goToAddCounter = MutableLiveData<Event<Unit>>()
    private val _isEditing = MutableLiveData(false)
    private val _confirmDelete = MutableLiveData<Event<CounterUI>>()
    private val _share = MutableLiveData<Event<CounterUI>>()

    fun onCounterItemClicked() = object : CounterItemClickListener {
        override fun onIncrementClicked(counter: CounterUI) {
            onModifyCounter(counter, ModifyCounterType.INCREMENT)
        }

        override fun onDecrementClicked(counter: CounterUI) {
            onModifyCounter(counter, ModifyCounterType.DECREMENT)
        }

        override fun onLongClicked(counter: CounterUI) {
            if (_isEditing.value == false && isLoading.value == false) {
                _isEditing.value = true
                setSelected(counter)
            }
        }

        override fun onClicked(counter: CounterUI) {
            if (_isEditing.value == true) {
                setSelected(counter)
            }
        }
    }

    fun onAddCounterClicked() {
        _goToAddCounter.value = Event(Unit)
    }

    fun onEditingEnded() {
        _isEditing.value = false
        setSelected(null)
    }

    fun onDeleteClicked() {
        findSelectedCounter()?.let {
            _confirmDelete.value = Event(it)
        }
    }

    private fun findSelectedCounter(): CounterUI? = filteredCountersList.value?.let { list ->
        list.find { it.isSelected }
    }

    fun onDeleteConfirmed(counter: CounterUI) {
        onDeleteConfirmed(counter) {
            onEditingEnded()
        }
    }

    fun onShareClicked() {
        findSelectedCounter()?.let {
            _share.value = Event(it)
        }
    }
}
