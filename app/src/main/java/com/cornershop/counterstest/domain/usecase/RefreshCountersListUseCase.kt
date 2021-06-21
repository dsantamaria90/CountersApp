package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.repository.CountersRepository
import com.cornershop.counterstest.domain.usecase.base.SuspendUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RefreshCountersListUseCase @Inject constructor(
    private val repository: CountersRepository,
    dispatcher: CoroutineDispatcher,
) : SuspendUseCase<Unit?, Unit>(dispatcher) {

    override suspend fun execute(parameters: Unit?): Result<Unit> = repository.refreshCountersList()
}
