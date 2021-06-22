package com.cornershop.counterstest.domain.counters.usecase

import com.cornershop.counterstest.domain.counters.repository.CountersRepository
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.usecase.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RefreshCountersListUseCase @Inject constructor(
    private val repository: CountersRepository,
    dispatcher: CoroutineDispatcher,
) : SuspendUseCase<Unit?, Unit>(dispatcher) {

    override suspend fun execute(parameters: Unit?): Result<Unit> = repository.refreshCountersList()
}
