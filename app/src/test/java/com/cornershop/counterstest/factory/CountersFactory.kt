package com.cornershop.counterstest.factory

import com.cornershop.counterstest.data.counters.entity.AddCounter
import com.cornershop.counterstest.data.counters.entity.ModifyCounter
import com.cornershop.counterstest.domain.counters.entity.Counter
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterUseCaseArgs
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterUI
import java.util.*

object CountersFactory {
    
    private fun randomString() = UUID.randomUUID().toString()
    private fun randomInt() = (0..100).random()

    private fun newCounter() = Counter(randomString(), randomString(), randomInt())

    fun newCountersList() = listOf(newCounter(), newCounter(), newCounter())

    fun newAddCounter(title: String = randomString()) = AddCounter(title)

    fun newModifyCounter(id: String = randomString()) = ModifyCounter(id)

    fun newModifyCounterUseCaseArgs(id: String = randomString(), type: ModifyCounterType) =
        ModifyCounterUseCaseArgs(id, type)

    fun newCounterUI(
        id: String = randomString(),
        title: String = randomString(),
        count: Int  = randomInt()
    ) = CounterUI(id, title, count)

    fun newCountersUiList() = listOf(newCounterUI(), newCounterUI(), newCounterUI())
}
