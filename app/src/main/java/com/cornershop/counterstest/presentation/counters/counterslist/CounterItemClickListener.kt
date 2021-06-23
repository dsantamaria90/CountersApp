package com.cornershop.counterstest.presentation.counters.counterslist

import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterUI

interface CounterItemClickListener {

    fun onIncrementClicked(counter: CounterUI)
    fun onDecrementClicked(counter: CounterUI)
    fun onLongClicked(counter: CounterUI)
    fun onClicked(counter: CounterUI)
}