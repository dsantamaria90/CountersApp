package com.cornershop.counterstest.presentation.util.actionmode

import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity

interface ActionModeDelegate {

    var onMenuItemClicked: ((Int) -> Unit)?
    var onDestroyClicked: (() -> Unit)?

    fun set(title: String, @MenuRes menuResId: Int? = null)
    fun start(activity: AppCompatActivity)
    fun finish()
}
