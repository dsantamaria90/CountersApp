package com.cornershop.counterstest.presentation.counters.addcounter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.usecase.AddCounterUseCase
import com.cornershop.counterstest.presentation.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCounterViewModel @Inject constructor(
    private val addCounterUseCase: AddCounterUseCase
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    private val _error = MutableLiveData<Event<Unit>>()
    private val _goToCounters = MutableLiveData<Event<Unit>>()

    val isLoading: LiveData<Boolean> get() = _isLoading
    val error: LiveData<Event<Unit>> get() = _error
    val goToCounters: LiveData<Event<Unit>> get() = _goToCounters

    fun onSaveClicked(title: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            delay(2000) // TODO delete
            addCounterUseCase(title.trim()).also { result ->
                _isLoading.postValue(false)
                when (result) {
                    is Result.Success -> _goToCounters.postValue(Event(Unit))
                    is Result.Error -> _error.postValue(Event(Unit))
                }
            }
        }
    }
}