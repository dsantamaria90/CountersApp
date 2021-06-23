package com.cornershop.counterstest.presentation.counters.counterslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.domain.counters.entity.Counter
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterUseCaseArgs
import com.cornershop.counterstest.domain.counters.usecase.DeleteCounterUseCase
import com.cornershop.counterstest.domain.counters.usecase.GetCountersListUseCase
import com.cornershop.counterstest.domain.counters.usecase.ModifyCounterUseCase
import com.cornershop.counterstest.domain.counters.usecase.RefreshCountersListUseCase
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.entity.getData
import com.cornershop.counterstest.domain.entity.isError
import com.cornershop.counterstest.domain.entity.isSuccess
import com.cornershop.counterstest.presentation.counters.entity.CounterUI
import com.cornershop.counterstest.presentation.counters.entity.CounterView
import com.cornershop.counterstest.presentation.counters.entity.ModifyCounterError
import com.cornershop.counterstest.presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO ReadCountersViewModelDelegate UpdateCountersViewModelDelegate
@HiltViewModel
class CountersViewModel @Inject constructor(
    private val getCountersListUseCase: GetCountersListUseCase,
    private val refreshCountersListUseCase: RefreshCountersListUseCase,
    private val modifyCounterUseCase: ModifyCounterUseCase,
    private val deleteCounterUseCase: DeleteCounterUseCase
) : ViewModel() {

    val isLoading: LiveData<Boolean> get() = _isLoading
    val filteredCountersList: LiveData<List<CounterUI>> get() = _filteredCountersList
    val goToAddCounter: LiveData<Event<Unit>> get() = _goToAddCounter
    val modifyCounterError: LiveData<Event<ModifyCounterError>> get() = _modifyCounterError
    val viewToShow: LiveData<CounterView> get() = _viewToShow
    val isEditing: LiveData<Boolean> get() = _isEditing
    val confirmDelete: LiveData<Event<CounterUI>> get() = _confirmDelete
    val deleteError: LiveData<Event<Unit>> get() = _deleteError
    val share: LiveData<Event<CounterUI>> get() = _share

    private val _isLoading = MutableLiveData<Boolean>()
    private val _filteredCountersList = MutableLiveData<List<CounterUI>>(emptyList())
    private val _goToAddCounter = MutableLiveData<Event<Unit>>()
    private val _modifyCounterError = MutableLiveData<Event<ModifyCounterError>>()
    private val _viewToShow = MutableLiveData(CounterView.NONE)
    private val _isEditing = MutableLiveData(false)
    private val _confirmDelete = MutableLiveData<Event<CounterUI>>()
    private val _deleteError = MutableLiveData<Event<Unit>>()
    private val _share = MutableLiveData<Event<CounterUI>>()
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
        override fun onIncrementClicked(counter: CounterUI) {
            modifyCounter(counter, ModifyCounterType.INCREMENT)
        }

        override fun onDecrementClicked(counter: CounterUI) {
            modifyCounter(counter, ModifyCounterType.DECREMENT)
        }

        override fun onLongClicked(counter: CounterUI) {
            if (_isEditing.value == false && _isLoading.value == false) {
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

    private fun setSelected(counter: CounterUI?) {
        _filteredCountersList.value?.let { list ->
            list.forEach {
                it.isSelected = it == counter
            }
            _filteredCountersList.value = list
        }
    }

    private fun modifyCounter(counter: CounterUI, type: ModifyCounterType) {
        viewModelScope.launch {
            _isLoading.value = true
            modifyCounterUseCase(ModifyCounterUseCaseArgs(counter.id, type)).also { result ->
                _isLoading.value = false
                if (result.isError()) {
                    setModifyCounterError(counter, type)
                }
            }
        }
    }

    private fun setModifyCounterError(counter: CounterUI, type: ModifyCounterType) {
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

    fun onRetryLoadCountersClicked() = object : CounterRetryClickListener {
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

    fun onEditingEnded() {
        _isEditing.value = false
        setSelected(null)
    }

    fun onDeleteClicked() {
        _filteredCountersList.value?.let { list ->
            list.find { it.isSelected }.also { counter ->
                counter?.let {
                    _confirmDelete.value = Event(it)
                }
            }
        }
    }

    fun onDeleteConfirmed(counter: CounterUI) {
        viewModelScope.launch {
            _isLoading.value = true
            deleteCounterUseCase(counter.id).also { result ->
                _isLoading.value = false
                when (result) {
                    is Result.Success -> onEditingEnded()
                    is Result.Error -> _deleteError.value = Event(Unit)
                }
            }
        }
    }

    fun onShareClicked() {
        _filteredCountersList.value?.let { list ->
            list.find { it.isSelected }.also { counter ->
                counter?.let {
                    _share.value = Event(it)
                }
            }
        }
    }

    companion object {
        private const val NEXT_COUNT = 1
        private const val PREVIOUS_COUNT = -1
    }
}