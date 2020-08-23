package com.icoo.ssgsag_android.ui.main.community

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter

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
    view.text = "댓글 " + num + "개"
}

@BindingAdapter("postDetailViewNum")
fun setPostDetailViewNum(view: TextView, num: Int) {
    view.text = "조회수 " + num + "회"
}

@BindingAdapter("postDetailLikeNum")
fun setPostDetailLikeNum(view: TextView, num: Int) {
    view.text = "공감 " + num
}