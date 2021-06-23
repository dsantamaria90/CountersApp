package com.cornershop.counterstest.presentation.extension

import android.app.AlertDialog
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cornershop.counterstest.R

fun Fragment.showAlertDialog(
    title: String,
    message: String? = null,
    @StringRes positiveButtonText: Int,
    @StringRes negativeButtonText: Int? = null,
    action: ((isPositive: Boolean) -> Unit)? = null
) = context?.let { ctx ->
    AlertDialog.Builder(ctx, R.style.AlertDialog).apply {
        setTitle(title)
        message?.let { setMessage(it) }
        setPositiveButton(positiveButtonText) { _, _ -> action?.invoke(true) }
        negativeButtonText?.let { setNegativeButton(it) { _, _ -> action?.invoke(false) } }
        create()
    }.show()
}

val Fragment.appCompatActivity get() = activity as? AppCompatActivity
