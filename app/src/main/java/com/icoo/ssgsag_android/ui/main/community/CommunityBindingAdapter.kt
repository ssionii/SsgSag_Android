package com.icoo.ssgsag_android.ui.main.community

import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.ui.main.community.review.ReviewType
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import java.text.DecimalFormat

@BindingAdapter("reviewMainText")
fun setReviewMain(view: ImageView, isSave: Int?) {
    isSave?.run {
        if (isSave == 1)
            view.visibility = View.VISIBLE
        else
            view.visibility = View.INVISIBLE
    }
}

@BindingAdapter("communityMainReviewTypeBg")
fun setCommunityMainReviewTypeBg(view: CardView, type: Int){
    when(type){
        ReviewType.UNION_CLUB, ReviewType.UNIV_CLUB  -> view.setCardBackgroundColor(view.resources.getColor(R.color.categoryClubBg))
        ReviewType.ACT -> view.setCardBackgroundColor(view.resources.getColor(R.color.categoryActBg))
        ReviewType.INTERN -> view.setCardBackgroundColor(view.resources.getColor(R.color.categoryInternBg))
    }
}


@BindingAdapter("communityMainReviewTypeText")
fun setCommunityMainReviewTypeText(view: TextView, type: Int){
    when(type){
        ReviewType.UNION_CLUB -> {
            view.text = "연합동아리"
            view.textColor = view.resources.getColor(R.color.categoryClubText)
        }
        ReviewType.UNIV_CLUB -> {
            view.text = "교내동아리"
            view.textColor = view.resources.getColor(R.color.categoryClubText)
        }
        ReviewType.ACT -> {
            view.text = "대외활동"
            view.textColor = view.resources.getColor(R.color.categoryActText)
        }
        ReviewType.INTERN -> {
            view.text = "인턴"
            view.textColor = view.resources.getColor(R.color.categoryInternText)
        }
    }
}

@BindingAdapter("postDetailCommentNum")
fun setPostDetailCommentNum(view: TextView, num: Int) {
    val formatter = DecimalFormat("###,###")

    view.text = "댓글 " + formatter.format(num)
}

@BindingAdapter("postDetailViewNum")
fun setPostDetailViewNum(view: TextView, num: Int) {
    val formatter = DecimalFormat("###,###")

    view.text = "조회수 " +  formatter.format(num)
}

@BindingAdapter("postDetailLikeNum")
fun setPostDetailLikeNum(view: TextView, num: Int) {
    val formatter = DecimalFormat("###,###")

    view.text = "공감 " + formatter.format(num)
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

@BindingAdapter("commentLike")
fun setCommentLike(view: ImageView, bool : Boolean) {
    if(bool){
        view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_like_filled))
    }else{
        view.setImageDrawable(view.resources.getDrawable(R.drawable.ic_like_outlined))
    }
}

@BindingAdapter("commentLikeTextColor")
fun setCommentLikeTextColor(view: TextView, like : Boolean) {
    if(like){
        view.textColor = Color.parseColor("#fe6d6d")
    }else{
        view.textColor = view.resources.getColor(R.color.grey_1)
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
    val BoardCategoryMap = mapOf("ALL" to "전체", "CAREER" to "취업/진로", "UNIV" to "학교생활", "THE_OTHERS" to "기타", "FREE" to "자유수다")
    view.text = BoardCategoryMap[category]
}

@BindingAdapter("replyUserName")
fun setReplyUserName(view: TextView, userName: String){
    view.text = "@" + userName
}

@BindingAdapter("commentEditTextHint")
fun setCommentEditTextHint(view: EditText, isReply : Boolean){
    if(isReply) view.hint = "답글을 입력해주세요"
    else view.hint = "댓글을 입력해주세요"
}

@BindingAdapter("isReply", "replyUserName")
fun setReplyUserName(view: TextView, isReply : Boolean, userName : String){
    if(isReply){
        view.text = userName + "님에게 답글 작성 중…"
    }
}

@BindingAdapter("replyCommentBg")
fun setReplyCommentBg(layout: ConstraintLayout, isSelected : Boolean){
    if(isSelected) layout.backgroundColor = Color.parseColor("#14656ef0")
    else layout.backgroundColor = layout.resources.getColor(R.color.white)
}