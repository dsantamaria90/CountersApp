package com.cornershop.counterstest.presentation.counters.databinding

import android.view.View
import android.view.ViewStub
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.domain.entity.Counter
import com.cornershop.counterstest.presentation.counters.CounterItemClickListener
import com.cornershop.counterstest.presentation.counters.CountersAdapter

@BindingAdapter(value = ["listener", "countersList"], requireAll = true)
fun RecyclerView.setCountersAdapter(
    listener: CounterItemClickListener,
    countersList: List<Counter>
) {
    if (adapter == null) {
        layoutManager = LinearLayoutManager(context)
        adapter = CountersAdapter(listener)
    }
    (adapter as? CountersAdapter)?.countersList = countersList
}

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
