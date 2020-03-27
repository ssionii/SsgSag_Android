package com.icoo.ssgsag_android.ui.main.subscribe.subscribeDialog

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeFilter
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColor

@BindingAdapter("filterBg")
fun setFilterBackground(view: View, interest: SubscribeFilter) {
    if(interest.isInterested)
        view.isSelected = true
    else
        view.isSelected = false
}

@BindingAdapter("selectAllImage")
fun setSelectAllBackGround(view: ImageView, selected: Boolean){
    if(selected)
        view.imageResource = R.drawable.select_all
    else
        view.imageResource = R.drawable.select_all_passive
}

@BindingAdapter("selectAllText")
fun setSelectAllColor(view: TextView, selected: Boolean){
    if(selected){
        view.textColor = view.context.getColor(R.color.selectedTabColor)
    }
    else
        view.textColor = view.context.getColor(R.color.click)
}