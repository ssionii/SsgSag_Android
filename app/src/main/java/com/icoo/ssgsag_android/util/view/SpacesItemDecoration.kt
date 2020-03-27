package com.icoo.ssgsag_android.util.view

import android.R.attr.top
import android.R.attr.bottom
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import android.R.attr.top
import android.R.attr.bottom




class SpacesItemDecoration(private val spanCount : Int, private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // item position
        val column = position % spanCount // item column

        outRect.left = column * space / spanCount // column * ((1f / spanCount) * spacing)
        outRect.right =
            space - (column + 1) * space / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
        /*
        if (position >= spanCount) {
            outRect.top = space // item top

        }*/
    }
}