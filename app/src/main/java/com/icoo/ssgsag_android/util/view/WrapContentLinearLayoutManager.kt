package com.icoo.ssgsag_android.util.view

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.icoo.ssgsag_android.SsgSagApplication


class WrapContentLinearLayoutManager(orientation: Int = RecyclerView.VERTICAL) : LinearLayoutManager(
    SsgSagApplication.getGlobalApplicationContext(), orientation?: RecyclerView.VERTICAL, false) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            Log.e("TAG", "meet a IOOBE in RecyclerView")
        }

    }
}