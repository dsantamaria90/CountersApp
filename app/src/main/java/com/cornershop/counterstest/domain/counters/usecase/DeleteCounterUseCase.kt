package com.cornershop.counterstest.domain.counters.usecase

import com.cornershop.counterstest.domain.counters.repository.CountersRepository
import com.cornershop.counterstest.domain.di.IoDispatcher
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteCounterUseCase @Inject constructor(
    private val repository: CountersRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : SuspendUseCase<String, Unit>(dispatcher) {

    override suspend fun execute(parameters: String): Result<Unit> =
        repository.deleteCounter(parameters)
}