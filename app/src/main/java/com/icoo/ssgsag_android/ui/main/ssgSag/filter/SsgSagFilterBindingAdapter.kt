package com.icoo.ssgsag_android.ui.main.ssgSag.filter

import android.graphics.Color
import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.SsgSagApplication
import org.jetbrains.anko.textColor

@BindingAdapter("selectedGrade")
fun setSelectedGradeText(view: TextView, selectedGrade: Int) {

    if(view.text == selectedGrade.toString()){
        view.textColor = Color.parseColor("#656ef0")
        view.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_medium)
    }else{
        view.textColor = Color.parseColor("#999999")
        view.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_regular)
    }

    if(selectedGrade.toString() == "5" && view.text == "5+"){
        view.textColor = Color.parseColor("#656ef0")
        view.setTypeface(view.typeface, Typeface.BOLD)
    }

}