package com.icoo.ssgsag_android.ui.main.review

import android.graphics.Color
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
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
fun setClubRgstrCategoryBg(layout: ConstraintLayout, isSelected: Boolean){
    if(isSelected)
        layout.setBackgroundResource(R.drawable.bg_656ef0_4)
    else
        layout.setBackgroundColor(context.resources.getColor(R.color.grey_4))

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

@BindingAdapter("IvVisibilityByReviewType")
fun ImageView.setVisibilityByReviewType(reviewType: String){
    when(reviewType){
        "club" -> this.visibility = VISIBLE
        else -> this.visibility = GONE
    }
}

@BindingAdapter("IvVisibilityByReviewTypeR")
fun ImageView.setVisibilityByReviewTypeR(reviewType: String){
    when(reviewType){
        "club" -> this.visibility = GONE
        else -> this.visibility = VISIBLE
    }
}

@BindingAdapter("reviewWriteStartMent")
fun TextView.setReviewWriteStartMent(reviewType: String){

    val strFront = "내가 했던 "
    val strMiddle = " \uD83E\uDD84이야~\n슨배님~ 이 "
    val strBack = "어땠어요?"

    var clubFront =""
    var clubBack =""
    when(reviewType){
        "club" -> {
            clubFront = "동아리는"
            clubBack = "동아리 "
        }
        "act" ->{
            clubFront = "대외활동은"
            clubBack = "대외활동 "
        }
        "intern" -> {
            clubFront = "인턴은"
            clubBack = "인턴 "
        }
    }

    this.text = strFront + clubFront + strMiddle + clubBack + strBack
}

@BindingAdapter("reviewTypeString")
fun TextView.setReviewTypeString(reviewType: String){
    when(reviewType){
        "club" -> {
           this.text = "동아리"
        }
        "act" ->{
          this.text = "대외활동"
        }
        "intern" -> {
            this.text = "인턴십"
        }
    }
}

@BindingAdapter("reviewWriteSimpleEditTextHint")
fun EditText.setReviewWriteSimpleEditTextHint(reviewType: String){

    when(reviewType){
        "club" -> {
            this.hint = "이 동아리를 한줄로 표현하자면?"
        }
        "act" ->{
            this.hint ="이 대외활동을 한줄로 표현하자면?"
        }
        "intern" -> {
            this.hint = "이 인턴을 한줄로 표현하자면?"
        }
    }
}


@BindingAdapter("vVisibilityByClubType")
fun View.setVVisivility(clubType: Int){
    when(clubType){
        0,1 -> this.visibility = GONE
        2,3 -> this.visibility = VISIBLE
    }
}

@BindingAdapter("llVisibilityByReviewType")
fun LinearLayout.setVisibilityByReviewType(reviewType: String){
    when(reviewType){
        "intern" -> this.visibility = VISIBLE
        else -> this.visibility = GONE
    }
}
