package com.icoo.ssgsag_android.ui.main.review.reviewDetail

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.icoo.ssgsag_android.R
import org.jetbrains.anko.textColor

@BindingAdapter("clubType")
fun setClubType(view: TextView, categoryIdx: Int){
    when(categoryIdx){
        0 -> view.text = "연합"
        1 -> view.text = "교내"
    }
}

@BindingAdapter("detailReviewCount")
fun setDetailReviewCount(view: TextView, reviewCount: Int){
    view.text = "평점(" + reviewCount.toString() + "개)"
}

@BindingAdapter("detailReviewScore")
fun setDetailReviewScore(view: TextView, reviewScore: Double){
    view.text = "(" + (Math.round(reviewScore*10)/10.0).toString() + "/5.0)"
}

@BindingAdapter("detailReviewGrade")
fun setDetailReviewGrade(view: TextView, score: Double){
    if(score >= 4.3) view.text = "A"
    else if(score >= 3.5) view.text = "B"
    else if(score >= 2.7) view.text = "C"
    else if(score >= 1.9) view.text = "D"
    else if(score > 0.0) view.text = "F"
    else view.text="-"
}

@BindingAdapter( "detailReviewGradeScore", "detailReviewGradeCategory")
fun setDetailReviewGradeScore(view: TextView, score: Double?, category: Int){

    var text = ""

    score?.let{
        if(it >= 4.3 && it <= 5.0) {
            when(category){
                0 -> text = "매우 높음"
                1 -> text = "개꿀잼"
                2 -> text = "널널"
                3 -> text = "매우 좋음"
            }
        } else if(it >= 3.5) {
            when(category){
                0 -> text = "높음"
                1 -> text = "꿀잼"
                2 -> text = "안빡셈"
                3 -> text = "좋음"
            }
        } else if(it >= 2.7) {
            when(category){
                0 -> text = "보통"
                1 -> text = "보통"
                2 -> text = "보통"
                3 -> text = "보통"
            }
        } else if(it >= 1.9) {
            when(category){
                0 -> text = "낮음"
                1 -> text = "노잼"
                2 -> text = "빡셈"
                3 -> text = "안좋음"
            }
        } else if(it > 0.0) {
            when(category){
                0 -> text = "매우 낮음"
                1 -> text = "개노잼"
                2 -> text = "개빡셈"
                3 -> text = "최악"
            }
        } else{
           text = "-"
        }
    }


    view.text = text
}


@BindingAdapter("reviewHelp")
fun setReviewHelp(view: TextView, helpCount: Int){
    view.text = "도움돼요 " + helpCount.toString() + "개"
}

@BindingAdapter("reviewMoreVisibility")
fun setReviewMoreVisibility(layout: ConstraintLayout, reviewCount: Int){
    if(reviewCount == 0)
        layout.visibility = View.GONE
    else
        layout.visibility = View.VISIBLE
}

@BindingAdapter("reviewsVisibility")
fun setReviewsVisibility(view: RecyclerView, reviewCount: Int){
    if(reviewCount == 0)
        view.visibility = View.GONE
    else
        view.visibility = View.VISIBLE
}

@BindingAdapter("reviewsEmptyVisibility")
fun setReviewsEmptyVisibility(layout: LinearLayout, reviewCount: Int){
    if(reviewCount != 0)
        layout.visibility = View.GONE
    else
        layout.visibility = View.VISIBLE
}

@BindingAdapter("blogReviewImg")
fun setBlogReviewImg(view: ImageView, imgUrl: String?){
    Glide.with(view.context)
        .load(imgUrl)
        .centerCrop()
        .into(view)
}

@BindingAdapter("ssgsagReviewHelpBg")
fun setSsgsagReviewHelp(view: CardView, isHelp: Int){
    if(isHelp == 1){
        view.setBackgroundResource(R.drawable.bg_ff2d55_5)
    }else{
        view.setBackgroundResource(R.drawable.border_dddddd_5)
    }
}
@BindingAdapter("ssgsagReviewHelpText")
fun setSsgsagReviewHelp(view: TextView, isHelp: Int){
    if(isHelp == 1){
        view.setTextColor(view.context.getColor(R.color.white))
    }else{
        view.textColor = Color.parseColor("#777777")
    }
}

@BindingAdapter("ssgsagReviewHelpImg")
fun setSsgsagReviewHelp(view: ImageView, isHelp: Int){
    if(isHelp == 1){
        view.setImageResource(R.drawable.ic_helpful_active)
    }else{
        view.setImageResource(R.drawable.ic_helpful)
    }
}