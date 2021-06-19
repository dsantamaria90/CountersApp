package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.repository.CountersRepository
import com.cornershop.counterstest.domain.usecase.base.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddCounterUseCase @Inject constructor(
    private val repository: CountersRepository,
    dispatcher: CoroutineDispatcher,
) : SuspendUseCase<String, Unit>(dispatcher) {

    override suspend fun execute(parameters: String): Result<Unit> =
        repository.addCounter(parameters)
}