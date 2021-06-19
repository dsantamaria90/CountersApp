package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.entity.ModifyCounterType
import com.cornershop.counterstest.domain.entity.ModifyCounterUseCaseArgs
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.repository.CountersRepository
import com.cornershop.counterstest.domain.usecase.base.SuspendUseCase
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