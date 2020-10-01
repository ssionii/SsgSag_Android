package com.icoo.ssgsag_android.ui.main.calendar.calendarDetail

import android.graphics.Color
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.util.extensionFunction.getItemBase
import org.jetbrains.anko.textColor
import org.w3c.dom.Text
import java.lang.StringBuilder

//View BindingAdapter
@Suppress("UNCHECKED_CAST")
@BindingAdapter("calendarDetailReplaceAll")
fun RecyclerView.replaceAll(poster: PosterDetail?) {
    (this.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
        replaceAllWithoutEnter(getItemBase(poster))
        notifyDataSetChanged()
    }
}

@BindingAdapter("commentUserImg")
fun setCommentUserImg(view: ImageView, imgUrl: String?) {
    Glide.with(view.context)
        .load(imgUrl)
        .error(R.drawable.user_anonymous)
        .apply(RequestOptions().circleCrop())
        .into(view)
}

@BindingAdapter("bookmarkBtnImg")
fun setBookmarkBtnImg(view: ImageView, isFavorite: Int?) {
    isFavorite?.run {
        if (isFavorite == 1)
            view.setImageResource(R.drawable.favorite_white_box)
        else
            view.setImageResource(R.drawable.favorite_white_box_passive)
    }
}

@BindingAdapter("likeBtnSrc")
fun setLikeBtnSrc(view: ImageView, isLike: Int?) {
    isLike?.run {
        if (isLike == 1)
            view.setImageResource(R.drawable.like)
        else
            view.setImageResource(R.drawable.like_passive)
    }
}

@BindingAdapter("likeCntText")
fun setLikeCntText(view: TextView, likeNum: Int?) {
    view.text = "${likeNum}"
}

@BindingAdapter("detailPhoto")
fun setDetailPhoto(view: CardView, photoUrl: String?){
    if(photoUrl.equals("empty") || photoUrl.equals(null))
        view.visibility = GONE
    else
        view.visibility = VISIBLE
}

@BindingAdapter("savePoster")
fun setSavePoster(view: ImageView, isSave: Int){
    if(isSave == 0)
        view.setImageResource(R.drawable.calendar_save)
    else
        view.setImageResource(R.drawable.calendar_delete)
}

@BindingAdapter("savePoster")
fun setSavePoster(view: TextView, isSave: Int){
    if(isSave == 0)
        view.text = "캘린더에 저장"
    else
        view.text = "캘린더에서 삭제"
}

@BindingAdapter("posterDetailDday")
fun setPosterDday(view: TextView, dday: String?) {
    dday?.let {
        if (it.toInt() != 0)
            view.text = "D-${dday}"
        else
            view.text = "D-day"
    }
}

@BindingAdapter("posterDetailDday")
fun setPosterDday(view: TextView, dday: Int?) {
    dday?.let {
        if (it != 0)
            view.text = "D-${dday}"
        else
            view.text = "D-day"
    }
}

@BindingAdapter("posterDetailDday")
fun setPosterDday(view: CardView, dday: String?) {
    dday?.let {
        if (it.toInt() > 0)
            view.visibility = VISIBLE
        else
            view.visibility = GONE
    }
}

@BindingAdapter("posterDetailText")
fun setPosterDetailText(view:TextView, str: String){

    var newStr :StringBuilder? = null
    var bulletList = arrayListOf("∙","•","・","◦","●","○","◎","◉","⦿","⁃ ","- ","■","□","☐","▪","▫︎","◼︎","◇","◆","▶","▷","‣","▸","▹","►","▻","➢","★","☆","✯","✩","→",">","-")
    for(i in 0..bulletList.size -1 ){
        if(str.contains(bulletList[i])) {
            newStr = StringBuilder(str)
            newStr.replace(str.indexOf(bulletList[i]), str.indexOf(bulletList[i]) + 1, "")
        }

    }
    view.text = newStr.toString()
}