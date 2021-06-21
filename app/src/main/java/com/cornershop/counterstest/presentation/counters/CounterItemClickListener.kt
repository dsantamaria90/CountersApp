package com.cornershop.counterstest.presentation.counters

import com.cornershop.counterstest.domain.entity.Counter

interface CounterItemClickListener {
    fun onIncrementClicked(counter: Counter)
    fun onDecrementClicked(counter: Counter)
}