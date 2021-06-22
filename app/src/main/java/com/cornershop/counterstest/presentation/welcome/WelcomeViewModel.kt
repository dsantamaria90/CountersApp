package com.cornershop.counterstest.presentation.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cornershop.counterstest.presentation.util.Event

class WelcomeViewModel : ViewModel() {

    val goToCountersList: LiveData<Event<Unit>> get() = _goToCountersList

    private val _goToCountersList = MutableLiveData<Event<Unit>>()

    fun onGetStartedClicked() = object : GetStartedClickListener {
        override fun onClicked() {
            _goToCountersList.value = Event(Unit)
        }
    }
}
