package com.cornershop.counterstest.domain.entity

data class ModifyCounterUseCaseArgs(
    val id: String,
    val type: ModifyCounterType
)
