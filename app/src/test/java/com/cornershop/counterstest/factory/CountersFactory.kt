package com.cornershop.counterstest.factory

import com.cornershop.counterstest.data.counters.entity.AddCounter
import com.cornershop.counterstest.data.counters.entity.ModifyCounter
import com.cornershop.counterstest.domain.counters.entity.Counter
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType
import com.cornershop.counterstest.domain.counters.entity.ModifyCounterUseCaseArgs
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterUI
import java.util.*
import kotlin.random.Random

object CountersFactory {
    
    private fun randomString() = UUID.randomUUID().toString()
    private fun randomInt() = Random.nextInt(100)

    fun newCounter() = Counter(
        id = randomString(),
        title = randomString(),
        count = randomInt()
    )

    fun newCountersList() = listOf(newCounter(), newCounter())

    fun newAddCounter(title: String = randomString()) = AddCounter(title)

    fun newModifyCounter(id: String = randomString()) = ModifyCounter(id)

    fun newModifyCounterUseCaseArgs(type: ModifyCounterType) =
        ModifyCounterUseCaseArgs(randomString(), type)

    fun newCounterUI(id: String, title: String, count: Int) = CounterUI(id, title, count)
}
