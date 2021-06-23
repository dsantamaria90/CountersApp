package com.cornershop.counterstest.presentation.counters.selectcounter.databinding

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.presentation.counters.selectcounter.SelectCounterAdapter
import com.cornershop.counterstest.presentation.counters.selectcounter.SelectCounterClickListener

@BindingAdapter(value = ["listener", "countersList"], requireAll = true)
fun RecyclerView.setAdapter(
    listener: SelectCounterClickListener,
    countersList: Array<String>,
) {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    adapter = SelectCounterAdapter(listener, countersList)
}
