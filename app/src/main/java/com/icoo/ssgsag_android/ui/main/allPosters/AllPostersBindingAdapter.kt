package com.icoo.ssgsag_android.ui.main.allPosters

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import org.w3c.dom.Text

@BindingAdapter("isSave")
fun setIsSave(view: ImageView, isSave: Int?) {
    isSave?.run {
        if (isSave == 1)
            view.visibility = VISIBLE
        else
            view.visibility = INVISIBLE
    }
}

@BindingAdapter("isSave")
fun setIsSave(view: TextView, isSave: Int?) {
    isSave?.run {
        if (isSave == 1)
            view.visibility = VISIBLE
        else
            view.visibility = INVISIBLE
    }
}