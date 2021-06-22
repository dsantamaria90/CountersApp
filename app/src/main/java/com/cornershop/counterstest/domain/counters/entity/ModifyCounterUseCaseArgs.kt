package com.cornershop.counterstest.domain.counters.entity

data class ModifyCounterUseCaseArgs(
    val id: String,
    val type: ModifyCounterType
)
