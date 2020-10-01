package com.icoo.ssgsag_android.ui.main.userNotice

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.SsgSagApplication
import org.jetbrains.anko.textColor

@BindingAdapter("userNoticeIcon")
fun setUserNoticeIcon(view: ImageView, category: String){
    val categoryMap = mapOf(
        "POSTER_END" to view.resources.getDrawable(R.drawable.ic_calendar_active),
        "COMMUNITY" to view.resources.getDrawable(R.drawable.ic_community_active)
    )

    view.setImageDrawable(categoryMap[category])
}
