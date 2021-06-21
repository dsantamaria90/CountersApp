package com.cornershop.counterstest.presentation.counters.databinding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.presentation.counters.CounterItemClickListener
import com.cornershop.counterstest.presentation.counters.CountersAdapter

@BindingAdapter(value = ["listener", "countersList", "loading"], requireAll = true)
fun RecyclerView.setCountersAdapter(
    listener: CounterItemClickListener,
    countersList: List<Counter>,
    isLoading: Boolean
) {
    if (adapter == null) {
        layoutManager = LinearLayoutManager(context)
        adapter = CountersAdapter(listener)
    }
    (adapter as? CountersAdapter)?.set(countersList, isLoading)
}

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("loading", "decrementable")
fun View.setEnabled(isLoading: Boolean, isDecrementable: Boolean) {
    isEnabled = !isLoading && isDecrementable
}
