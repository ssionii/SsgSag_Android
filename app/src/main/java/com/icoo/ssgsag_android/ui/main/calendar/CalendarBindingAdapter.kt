package com.icoo.ssgsag_android.ui.main.calendar

import android.graphics.Color
import android.graphics.Typeface
import android.text.Layout
import android.view.View
import android.view.View.*
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.marginEnd
import androidx.databinding.BindingAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.SsgSagApplication
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

//Category BindingAdapter
@BindingAdapter("checkedSortTextColor", "sortIsChecked")
fun setCheckedSortTextColor(view: TextView, categoryIdx: Int?, sortIsChecked: Boolean) {
    if (sortIsChecked) {
        when (categoryIdx) {
            -2,-1 -> {
                view.textColor = Color.parseColor("#656ef0")
                view.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_bold)
            }
            0 -> view.textColor = view.context.getColor(R.color.contest)
            1 -> view.textColor = view.context.getColor(R.color.act)
            2 -> view.textColor = view.context.getColor(R.color.club)
            4 -> view.textColor = view.context.getColor(R.color.recruit)
            5 -> view.textColor = view.context.getColor(R.color.etcText)
            7 -> view.textColor = view.context.getColor(R.color.education)
            8 -> view.textColor = view.context.getColor(R.color.scholarship)
        }
    } else {
        when(categoryIdx){
            -2, -1 -> view.textColor = Color.parseColor("#777777")
            0, 1, 2, 3, 4, 5, 6, 7, 8 -> view.textColor = Color.parseColor("#d7d8d8")
        }

    }
}

@BindingAdapter("checkedSortText")
fun setCheckedSortText(view: TextView, categoryIdx: Int?) {
    when (categoryIdx) {
        -2 ->{
            view.text = "전체"
            view.textSize = 14f
        }
        -1 ->{
            view.text = "즐겨찾기"
            view.textSize = 14f
        }
        0 -> {
            view.text = "공모전"
            view.textSize = 13f
        }
        1 -> {
            view.text = "대외활동"
            view.textSize = 13f
        }
        2 -> {
            view.text = "동아리"
            view.textSize = 13f
        }
        4 -> {
            view.text = "인턴"
            view.textSize = 13f
        }
        5 -> {
            view.text = "기타"
            view.textSize = 13f
        }
        7 -> {
            view.text = "교육/강연"
            view.textSize = 13f
        }
        8 -> {
            view.text = "장학금/지원"
            view.textSize = 13f
        }
    }

}

@BindingAdapter("checkedSortCardBg", "sortIsChecked")
fun setCheckedSortCardBg(view: CardView, categoryIdx: Int?, sortIsChecked: Boolean) {
    if (sortIsChecked) {
        when (categoryIdx) {
            0 -> view.setCardBackgroundColor(view.context.getColor(R.color.contestBg))
            1 -> view.setCardBackgroundColor(view.context.getColor(R.color.actBg))
            2 -> view.setCardBackgroundColor(view.context.getColor(R.color.clubBg))
            4 -> view.setCardBackgroundColor(view.context.getColor(R.color.recruitBg))
            5 -> view.setCardBackgroundColor(view.context.getColor(R.color.etcBg))
            7 -> view.setCardBackgroundColor(view.context.getColor(R.color.educationBg))
            8 -> view.setCardBackgroundColor(view.context.getColor(R.color.scholarshipBg))
            else -> view.setCardBackgroundColor(null)
        }
    } else
        view.setCardBackgroundColor(view.context.getColor(R.color.white))
}

@BindingAdapter("sortVisibility")
fun setSortVisibility(view: CardView, categoryIdx: Int?){
    when (categoryIdx) {
        3,6 -> view.visibility = GONE
        else -> view.visibility = VISIBLE
    }
}

@BindingAdapter("calendarFavoriteTabText")
fun setCalendarFavoriteTab(view: TextView, bool: Boolean){
    if(bool) {
        view.textColor = view.context.getColor(R.color.ssgsag)
    }else{
        view.textColor = view.context.getColor(R.color.grey_2)
    }
}

@BindingAdapter("calendarFavoriteTabView")
fun setCalendarFavoriteTab(view: View, bool: Boolean){
    if(bool) {
        view.backgroundColor = view.context.getColor(R.color.ssgsag)
    }else{
        view.backgroundColor = view.context.getColor(R.color.white)
    }
}
