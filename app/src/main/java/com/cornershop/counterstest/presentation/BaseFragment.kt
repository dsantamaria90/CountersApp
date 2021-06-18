package com.cornershop.counterstest.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.presentation.util.Event
import com.cornershop.counterstest.presentation.util.EventObserver

abstract class BaseFragment<T : ViewModel> : Fragment() {

    protected abstract val viewModel: T
    protected var shouldShowToolbar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.let {
            if (shouldShowToolbar) {
                it.show()
            } else {
                it.hide()
            }
        }
    }

    protected fun <T> LiveData<Event<T>>.observeEvent(cb: (T) -> Unit) {
        observe(viewLifecycleOwner, EventObserver(cb))
    }

    protected fun <T> LiveData<T>.observe(cb: (T?) -> Unit) {
        observe(viewLifecycleOwner, cb)
    }

    protected fun <DB : ViewDataBinding> DB.bindLayout(bind: DB.() -> Unit) = bind(this).let {
        lifecycleOwner = viewLifecycleOwner
        root
    }
}
