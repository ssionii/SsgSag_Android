package com.icoo.ssgsag_android.ui.main.subscribe

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.subscribe.Subscribe
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColor


@BindingAdapter("subscribeBtn")
fun setSubscribeBtn(view: ImageView, userIdx: Int) {
    if(userIdx != 0) {
        view.imageResource = R.drawable.unfollow
    } else {
        view.imageResource = R.drawable.follow
    }
}