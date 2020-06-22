package com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.util.dataBinding.replaceAll

@BindingAdapter("bookmarkBtnImgInDialog")
fun setBookmarkBtnImgInDialog(view: ImageView, isFavorite: Int?) {
    isFavorite?.run {
        if (isFavorite == 1)
            view.setImageResource(R.drawable.ic_favorite_active)
        else
            view.setImageResource(R.drawable.ic_favorite)
    }
}


@BindingAdapter("scheduleDday")
fun setScheduleDday(view: TextView, dday: Int?) {
    dday?.let {
        if (it != 0)
            view.text = "D - ${dday}"
        else
            view.text = "D - day"
    }
}

@BindingAdapter("scheduleLikeNum")
fun setScheduleLikeNum(view: TextView, likeNum: Int) {
        view.text = likeNum.toString()
}


@BindingAdapter("favoriteVisibility")
fun setFavoriteVisibility(view: ImageView, selectType: Int) {
    when(selectType){
        0 -> {
            view.visibility = VISIBLE
        }
        1 -> view.visibility = GONE
    }

    Log.e("favoritevisibility: ", view.visibility.toString())
}


@BindingAdapter("selectorVisibility")
fun setSelectorVisibility(view: ImageView, selectType: Int) {
    when(selectType){
        0->{
            view.visibility = GONE
            view.setImageResource(R.drawable.select_all_passive)
        }
        1 -> view.visibility = VISIBLE
    }
}
