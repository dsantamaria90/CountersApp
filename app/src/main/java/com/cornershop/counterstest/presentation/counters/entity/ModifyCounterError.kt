package com.cornershop.counterstest.presentation.counters.entity

import com.cornershop.counterstest.domain.counters.entity.ModifyCounterType

data class ModifyCounterError(
    val counter: CounterUI,
    val type: ModifyCounterType,
    val modifiedCount: Int
)
