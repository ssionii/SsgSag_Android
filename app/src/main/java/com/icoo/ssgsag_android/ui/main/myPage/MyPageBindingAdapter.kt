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


@BindingAdapter("myCommentCategory")
fun seyMyCommentCategory(view : TextView, category : String){
    when(category){
        "FREE" ->{
            view.text = "자유 수다톡"
        }
        "UNIV" -> {
            view.text = "고민 상담톡 | 학교생활"
        }
        "CAREER" -> {
            view.text = "고민 상담톡 | 취업/진로"
        }
        "THE_OTHERS" -> {
            view.text = "고민 상담톡 | 기타"
        }
    }
}


@BindingAdapter("myCommentCategory")
fun seyMyCommentCategory(view : ImageView, category : String){
    when(category){
        "FREE" -> view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_board_free_grey))
        else -> view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_board_counsel_grey))

    }
}



@BindingAdapter("myPostCategory1", "myPostCategory2")
fun setMyPostCategory(view : TextView, category1 : String,  category2 : String){
    when(category1){
        "COMMUNITY" -> {
            when(category2){
                "FREE" ->{
                    view.text = "자유 수다톡"
                }
                "UNIV" -> {
                    view.text = "고민 상담톡 | 학교생활"
                }
                "CAREER" -> {
                    view.text = "고민 상담톡 | 취업/진로"
                }
                "THE_OTHERS" -> {
                    view.text = "고민 상담톡 | 기타"
                }
            }
        }

        "CLUB_POST"-> {
            when(category2){
                "0" ->{
                    view.text = "활동후기 | 연합동아리"
                }
                "1" -> {
                    view.text = "활동후기 | 교내동아리"
                }
                "2" -> {
                    view.text = "활동후기 | 대외활동"
                }
                "3" -> {
                    view.text = "활동후기 | 인턴"
                }
            }
        }
    }
}


@BindingAdapter("myPostCategory1", "myPostCategory2")
fun setMyPostIcon(view : ImageView, category1 : String,  category2 : String){
    when(category1){
        "COMMUNITY" -> {
            when(category2){
                "FREE" ->{
                    view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_board_free_grey))
                }
                else -> {
                    view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_board_counsel_grey))
                }
            }
        }

        "CLUB_POST"-> {
            view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_board_review_grey))
        }
    }
}

