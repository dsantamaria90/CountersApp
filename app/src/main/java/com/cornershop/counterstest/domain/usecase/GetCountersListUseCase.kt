package com.cornershop.counterstest.domain.usecase

import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.repository.CountersRepository
import com.cornershop.counterstest.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCountersListUseCase @Inject constructor(
    private val repository: CountersRepository,
    dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit, List<Counter>>(dispatcher) {

    override fun execute(parameters: Unit): Flow<Result<List<Counter>>> =
        repository.getCountersList()
}