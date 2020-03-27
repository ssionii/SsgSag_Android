package com.icoo.ssgsag_android.util.view

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NonScrollLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}