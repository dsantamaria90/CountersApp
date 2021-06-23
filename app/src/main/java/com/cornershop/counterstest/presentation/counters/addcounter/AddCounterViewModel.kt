package com.cornershop.counterstest.presentation.counters.addcounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.domain.counters.usecase.AddCounterUseCase
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.presentation.counters.selectcounter.SelectCounterClickListener
import com.cornershop.counterstest.presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCounterViewModel @Inject constructor(
    private val addCounterUseCase: AddCounterUseCase
) : ViewModel() {

    val isLoading: LiveData<Boolean> get() = _isLoading
    val error: LiveData<Event<Unit>> get() = _error
    val goToCounters: LiveData<Event<Unit>> get() = _goToCounters
    val goToExamples: LiveData<Event<Unit>> get() = _goToExamples
    val goToAddCounter: LiveData<Event<Unit>> get() = _goToAddCounter
    val title = MutableLiveData<String>()

    private val _isLoading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<Event<Unit>>()
    private val _goToCounters = MutableLiveData<Event<Unit>>()
    private val _goToExamples = MutableLiveData<Event<Unit>>()
    private val _goToAddCounter = MutableLiveData<Event<Unit>>()

    fun onSaveClicked() {
        val title = title.value?.trim()
        if (!title.isNullOrEmpty()) {
            viewModelScope.launch {
                _isLoading.value = true
                addCounterUseCase(title).also { result ->
                    _isLoading.value = false
                    when (result) {
                        is Result.Success -> _goToCounters.value = Event(Unit)
                        is Result.Error -> _error.value = Event(Unit)
                    }
                }
            }
        }
    }

    fun onSeeExamplesClicked() {
        _goToExamples.value = Event(Unit)
    }

    fun onCounterClicked() = object : SelectCounterClickListener {
        override fun onClicked(title: String) {
            this@AddCounterViewModel.title.value = title
            _goToAddCounter.value = Event(Unit)
        }
    }
}