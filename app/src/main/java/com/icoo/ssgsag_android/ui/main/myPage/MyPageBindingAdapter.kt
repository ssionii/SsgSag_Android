package com.icoo.ssgsag_android.ui.main.myPage

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.icoo.ssgsag_android.R

@BindingAdapter("profileImg")
fun setUserInfoImg(view: ImageView, userProfileUrl: String?) {
    Glide.with(view.context)
        .load(userProfileUrl)
        .error(R.drawable.img_default_profile)
        .apply(RequestOptions().circleCrop())
        .into(view)
}

@BindingAdapter("myReviewRegDate")
fun setMyReviewRegDate(view: TextView, regDate: String){
    view.text = "작성일 : " + regDate.substring(0,4) + "." + regDate.substring(5,7) + "."+regDate.substring(8,10)
}

@BindingAdapter("myReviewAcceptStatus")
fun setMyReviewRegDate(view: TextView, adminAccept: Int){
    when(adminAccept){
        0 -> view.text = "승인 대기중"
        1 -> view.text = ""
        2 -> view.text = "거절"
    }
}

@BindingAdapter("myPostIcon")
fun setMyPageIcon(view : ImageView, category : String){
    when(category){
        "FREE" ->{

        }
    }
}

@BindingAdapter("myCommentType")
fun setMyCommentType(view : TextView, category : String){
    when(category){
        "FREE" ->{
            view.text = "자유 수다톡"
        }
        "UNIV" -> {
            view.text = "고민 상담톡 | 대학생활"
        }
        "CAREER" -> {
            view.text = "고민 상담톡 | 취업/진로"
        }
        "THE_OTHERS" -> {
            view.text = "고민 상담톡 | 기타"
        }
    }
}
