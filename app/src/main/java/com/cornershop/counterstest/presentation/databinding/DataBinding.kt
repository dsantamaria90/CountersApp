package com.cornershop.counterstest.presentation.databinding

import android.view.View
import android.widget.SearchView
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun View.setVisibility(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("visibilityNoGone")
fun View.setVisibilityNoGone(isVisible: Boolean) {
    visibility = if (isVisible) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

@BindingAdapter("onLongClick")
fun View.setLongClickListener(call: () -> Unit) {
    setOnLongClickListener {
        call()
        true
    }
}

@BindingAdapter("textChanged")
fun SearchView.setTextChangedListener(call: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(text: String?): Boolean {
            call(text ?: "")
            return true
        }
        override fun onQueryTextSubmit(text: String?): Boolean = false
    })
}