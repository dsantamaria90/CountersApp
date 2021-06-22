package com.cornershop.counterstest.presentation.entity

import com.cornershop.counterstest.domain.entity.ModifyCounterType

data class ModifyCounterError(
    val counter: CounterUI,
    val type: ModifyCounterType,
    val modifiedCount: Int
)
