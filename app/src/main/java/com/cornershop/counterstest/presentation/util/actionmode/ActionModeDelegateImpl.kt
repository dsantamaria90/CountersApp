package com.cornershop.counterstest.presentation.util.actionmode

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import javax.inject.Inject

class ActionModeDelegateImpl @Inject constructor() : ActionModeDelegate {

    override var onMenuItemClicked: ((Int) -> Unit)? = null
    override var onDestroyClicked: (() -> Unit)? = null

    private var title = ""
    private var menuResId: Int? = null
    private var actionMode: ActionMode? = null

    override fun set(title: String, menuResId: Int?) {
        this.title = title
        this.menuResId = menuResId
    }

    override fun start(activity: AppCompatActivity) {
        actionMode = activity.startSupportActionMode(getCallback(activity))?.apply {
            title = this@ActionModeDelegateImpl.title
        }
    }

    private fun getCallback(activity: AppCompatActivity) = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            menuResId?.let {
                activity.menuInflater.inflate(it, menu)
            }
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean =
            item?.itemId?.let { itemId ->
                onMenuItemClicked?.let {
                    it.invoke(itemId)
                    true
                }
            } ?: false

        override fun onDestroyActionMode(mode: ActionMode?) {
            onDestroyClicked?.invoke()
        }
    }

    override fun finish() {
        actionMode?.finish()
    }
}
