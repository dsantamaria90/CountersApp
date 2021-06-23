package com.cornershop.counterstest.domain.counters.usecase

import com.cornershop.counterstest.domain.counters.entity.Counter
import com.cornershop.counterstest.domain.counters.repository.CountersRepository
import com.cornershop.counterstest.domain.di.IoDispatcher
import com.cornershop.counterstest.domain.entity.Result
import com.cornershop.counterstest.domain.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCountersListUseCase @Inject constructor(
    private val repository: CountersRepository,
    @IoDispatcher dispatcher: CoroutineDispatcher,
) : FlowUseCase<Unit?, List<Counter>>(dispatcher) {

    override fun execute(parameters: Unit?): Flow<Result<List<Counter>>> =
        repository.getCountersList()
}
