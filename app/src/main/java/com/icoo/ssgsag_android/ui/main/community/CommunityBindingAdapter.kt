package com.icoo.ssgsag_android.ui.main.community

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("reviewMainText")
fun setReviewMain(view: ImageView, isSave: Int?) {
    isSave?.run {
        if (isSave == 1)
            view.visibility = View.VISIBLE
        else
            view.visibility = View.INVISIBLE
    }
}