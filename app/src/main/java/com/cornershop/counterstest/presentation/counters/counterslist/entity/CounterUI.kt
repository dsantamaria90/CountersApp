package com.cornershop.counterstest.presentation.counters.counterslist.entity

data class CounterUI(
    val id: String,
    val title: String,
    val count: Int,
    var isSelected: Boolean = false
) {

    fun isDecrementable(): Boolean = count > EMPTY_COUNT

    companion object {
        private const val EMPTY_COUNT = 0
    }
}
