package com.icoo.ssgsag_android.ui.main.review

import android.graphics.Color
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.ui.main.feed.context
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor


@BindingAdapter("reviewClubType")
fun setReviewClubType(view: TextView, type: Int){
    when(type){
        0 -> view.text = "연합"
        1 -> view.text = "교내"
    }
}

@BindingAdapter("reviewGrade")
fun setReviewGrade(view: TextView, grade: Float){
    view.text = "평점 " + (Math.round(grade * 10)/10.0).toString()

}

@BindingAdapter("reviewCount")
fun setReviewCount(view: TextView, reviewCount: Int){
    view.text = "후기 " + reviewCount.toString() +"개"
}

@BindingAdapter("clubRgstrButton")
fun setClubRgstrButton(layout: ConstraintLayout, isClickable: Boolean){
    if(isClickable){
        layout.backgroundColor = layout.resources.getColor(R.color.ssgsag)
        layout.isClickable = true
    }else{
        layout.backgroundColor = Color.parseColor("#aaaaaa")
        layout.isClickable = false
    }
}

@BindingAdapter("clubRgstrCategoryBg")
fun setClubRgstrCategoryBg(view: CardView, isSelected: Boolean){
    if(isSelected)
        view.setCardBackgroundColor(Color.parseColor("#26656ef0"))
    else
        view.setCardBackgroundColor(context.resources.getColor(R.color.grey_4))

}

@BindingAdapter("clubRgstrCategoryText")
fun setClubRgstrCategoryText(view: TextView, isSelected: Boolean){
    if(isSelected)
        view.textColor = context.resources.getColor(R.color.ssgsag)
    else
        view.textColor = context.resources.getColor(R.color.grey_2)
}

@BindingAdapter("clubActDate")
fun setClubActDate(view: TextView, endDate: String?){
    view.text = endDate?.substring(0,4) +"년 활동"
}


@BindingAdapter("noClubIntroduce")
fun setNoClubIntroduce(view: TextView, intro: String?){
    if(intro!! == "")
        view.text = "동아리 한줄 소개가 없습니다."
    else
        view.text = intro
}

@BindingAdapter("clubBannerVisibility")
fun ConstraintLayout.setClubBannerVisibility(reviewType: Int){
    when(reviewType){
        0, 1 -> this.visibility = VISIBLE
        else -> this.visibility = GONE
    }
}

@BindingAdapter("reviewListTitle")
fun TextView.setReviewListTitle(reviewType: Int){
    when(reviewType){
        0, 1 -> this.text = "동아리 후기"
        2 -> this.text = "대외활동 후기"
        3 -> this.text = "인턴 후기"
    }
}