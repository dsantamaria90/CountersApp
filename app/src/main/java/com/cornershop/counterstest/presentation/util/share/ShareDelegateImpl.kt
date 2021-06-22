package com.cornershop.counterstest.presentation.util.share

import android.content.Context
import android.content.Intent
import com.cornershop.counterstest.R
import javax.inject.Inject

class ShareDelegateImpl @Inject constructor() : ShareDelegate {

    override fun share(context: Context, text: String) {
        with(Intent(Intent.ACTION_SEND)) {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
            context.startActivity(Intent.createChooser(this, context.getString(R.string.share)))
        }
    }
}
