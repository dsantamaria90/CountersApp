package com.cornershop.counterstest.presentation.counters.counterslist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterUseCaseArgs
import com.cornershop.counterstest.domain.counters.usecase.DeleteCounterUseCase
import com.cornershop.counterstest.domain.counters.usecase.ModifyCounterUseCase
import com.cornershop.counterstest.domain.di.MainDispatcher
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.entity.isError
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterUI
import com.cornershop.counterstest.presentation.counters.counterslist.entity.ModifyCounterError
import com.cornershop.counterstest.presentation.util.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class UpdateCountersViewModelDelegateImpl @Inject constructor(
    private val modifyCounterUseCase: ModifyCounterUseCase,
    private val deleteCounterUseCase: DeleteCounterUseCase,
    @MainDispatcher dispatcher: CoroutineDispatcher
) : UpdateCountersViewModelDelegate,
    CoroutineScope by CoroutineScope(dispatcher) {

    override val isUpdating: LiveData<Boolean> get() = _isUpdating
    override val modifyCounterError: LiveData<Event<ModifyCounterError>> get() = _modifyCounterError
    override val deleteError: LiveData<Event<Unit>> get() = _deleteError

    private val _isUpdating = MutableLiveData<Boolean>()
    private val _modifyCounterError = MutableLiveData<Event<ModifyCounterError>>()
    private val _deleteError = MutableLiveData<Event<Unit>>()

    override fun onModifyCounter(counter: CounterUI, type: ModifyCounterType) {
        launch {
            _isUpdating.value = true
            modifyCounterUseCase(ModifyCounterUseCaseArgs(counter.id, type)).also { result ->
                _isUpdating.value = false
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

    override fun onDeleteConfirmed(counter: CounterUI, onSuccess: () -> Unit) {
        launch {
            _isUpdating.value = true
            deleteCounterUseCase(counter.id).also { result ->
                _isUpdating.value = false
                when (result) {
                    is Result.Success -> onSuccess()
                    is Result.Error -> _deleteError.value = Event(Unit)
                }
            }
        }
    }

    companion object {
        private const val NEXT_COUNT = 1
        private const val PREVIOUS_COUNT = -1
    }
}