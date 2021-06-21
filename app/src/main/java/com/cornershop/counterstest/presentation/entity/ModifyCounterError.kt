package com.cornershop.counterstest.presentation.entity

import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.domain.entity.ModifyCounterType

data class ModifyCounterError(
    val counter: Counter,
    val type: ModifyCounterType,
    val modifiedCount: Int
)
