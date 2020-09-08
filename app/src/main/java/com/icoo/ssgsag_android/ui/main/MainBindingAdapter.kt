package com.icoo.ssgsag_android.ui.main

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.SsgSagApplication
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

@BindingAdapter("tabType")
fun setTabType(view: TextView, boolean: Boolean){
    if(boolean) {
        view.textColor = view.context.getColor(R.color.selectedTabColor)
        view.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_bold)

    } else {
        view.textColor = view.context.getColor(R.color.unselected_text_color)
        view.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_regular)

    }
}


@BindingAdapter("fontFamilyMediumRegular")
fun setFontFamilyMediumRegular(view: TextView, boolean: Boolean){
    if(boolean) {
        view.textColor = view.context.getColor(R.color.selectedTabColor)
        view.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_medium)

    } else {
        view.textColor = view.context.getColor(R.color.unselected_text_color)
        view.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_regular)

    }
}
