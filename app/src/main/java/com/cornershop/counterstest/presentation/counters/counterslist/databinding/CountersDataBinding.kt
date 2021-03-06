package com.cornershop.counterstest.presentation.counters.counterslist.databinding

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.presentation.counters.counterslist.entity.CounterUI
import com.cornershop.counterstest.presentation.counters.counterslist.view.CounterItemClickListener
import com.cornershop.counterstest.presentation.counters.counterslist.view.CountersAdapter

@BindingAdapter(value = ["listener", "countersList", "loading", "editing"], requireAll = true)
fun RecyclerView.setAdapter(
    listener: CounterItemClickListener,
    countersList: List<CounterUI>,
    isLoading: Boolean,
    isEditing: Boolean
) {
    if (adapter == null) {
        layoutManager = LinearLayoutManager(context)
        adapter = CountersAdapter(listener)
    }
    (adapter as? CountersAdapter)?.set(countersList, isLoading, isEditing)
}

@BindingAdapter(value = ["loading", "decrementable"], requireAll = true)
fun View.setEnabled(isLoading: Boolean, isDecrementable: Boolean) {
    isEnabled = !isLoading && isDecrementable
}

@BindingAdapter("background")
fun View.setBackground(isSelected: Boolean) {
    background = if (isSelected) {
        ContextCompat.getDrawable(context, R.drawable.counter_item_editing_background)
    } else {
        null
    }
}
