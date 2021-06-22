package com.cornershop.counterstest.domain.counters.usecase

import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterUseCaseArgs
import com.cornershop.counterstest.domain.counters.repository.CountersRepository
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ModifyCounterUseCase @Inject constructor(
    private val repository: CountersRepository,
    dispatcher: CoroutineDispatcher,
) : SuspendUseCase<ModifyCounterUseCaseArgs, Unit>(dispatcher) {

    override suspend fun execute(parameters: ModifyCounterUseCaseArgs): Result<Unit> =
        with(parameters) {
            when (type) {
                ModifyCounterType.INCREMENT -> repository.incrementCounter(id)
                ModifyCounterType.DECREMENT -> repository.decrementCounter(id)
            }
        }
}