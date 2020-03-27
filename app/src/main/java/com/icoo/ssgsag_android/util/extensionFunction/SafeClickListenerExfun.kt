package com.icoo.ssgsag_android.util.extensionFunction

import android.view.View
import com.icoo.ssgsag_android.util.listener.SafeClickListener

fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}