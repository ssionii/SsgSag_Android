package com.icoo.ssgsag_android.ui.main.community

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.icoo.ssgsag_android.R

@BindingAdapter("reviewMainText")
fun setReviewMain(view: ImageView, isSave: Int?) {
    isSave?.run {
        if (isSave == 1)
            view.visibility = View.VISIBLE
        else
            view.visibility = View.INVISIBLE
    }
}

@BindingAdapter("postDetailCommentNum")
fun setPostDetailCommentNum(view: TextView, num: Int) {
    view.text = "댓글 " + num
}

@BindingAdapter("postDetailViewNum")
fun setPostDetailViewNum(view: TextView, num: Int) {
    view.text = "조회수 " + num
}

@BindingAdapter("postDetailLikeNum")
fun setPostDetailLikeNum(view: TextView, num: Int) {
    view.text = "공감 " + num
}

@BindingAdapter("postBookmark")
fun setPostBookmark(view: ImageView, bool : Boolean) {
    if(bool){
        view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_bookmark_big_active))
    }else{
        view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_bookmark_big))
    }
}

@BindingAdapter("postLike")
fun setPostLike(view: ImageView, bool : Boolean) {
    if(bool){
        view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_like_big_filled_active))
    }else{
        view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_like_big_outlined))
    }
}

@BindingAdapter("categoryBg")
fun setCategoryBg(view : CardView, isChecked : Boolean){
    if(isChecked){
        view.setCardBackgroundColor(Color.parseColor("#26656ef0"))
    }else{
        view.setCardBackgroundColor(Color.parseColor("#f2f2f2"))
    }
}

@BindingAdapter("categoryText")
fun setCategoryText(view : TextView, isChecked : Boolean){
    if(isChecked){
        view.setTextColor(view.resources.getColor(R.color.ssgsag))
    }else{
        view.setTextColor(view.resources.getColor(R.color.grey_2))
    }
}

@BindingAdapter("unionClubBackgroundColor", "isUnion")
fun setClubTabBackgroundColor(view : CardView, reviewType : Int, isUnion : Boolean){
    if((isUnion && reviewType == 0) || (!isUnion && reviewType == 1)){
        view.setCardBackgroundColor(Color.parseColor("#14656ef0"))
    } else{
        view.setCardBackgroundColor(Color.parseColor("#f7f7f7"))
    }
}

@BindingAdapter("unionClubTextColor", "isUnion")
fun setClubTabTextColor(view : TextView, reviewType : Int, isUnion : Boolean){
    if((isUnion && reviewType == 0) || (!isUnion && reviewType == 1)){
        view.setTextColor(view.resources.getColor(R.color.ssgsag))
    }else{
        view.setTextColor(view.resources.getColor(R.color.grey_2))
    }
}

@BindingAdapter("boardCategoryText")
fun setBoardCategoryText(view: TextView, category : String?){
    val BoardCategoryMap = mapOf("ALL" to "전체", "CAREER" to "취업/진로", "UNIV" to "대학생활", "THE_OTHERS" to "기타", "TALK" to "자유수다")
    view.text = BoardCategoryMap[category]
}
