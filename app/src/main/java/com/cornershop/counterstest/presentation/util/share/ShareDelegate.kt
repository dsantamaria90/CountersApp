package com.cornershop.counterstest.presentation.util.share

import android.content.Context

interface ShareDelegate {

    fun share(context: Context, text: String)
}
