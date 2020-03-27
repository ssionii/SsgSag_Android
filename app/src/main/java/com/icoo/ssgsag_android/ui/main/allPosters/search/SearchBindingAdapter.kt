package com.icoo.ssgsag_android.ui.main.allPosters.search

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail

@BindingAdapter("keyword", "resultSize")
fun setKeyword(view: TextView, keyword: String?, resultSize: Int?) {
    if(resultSize != null) {
        if (resultSize > 0) {
            view.visibility = VISIBLE
            view.text = "'" + keyword + "'에 대한 검색 결과입니다."
        } else {
            view.visibility = VISIBLE
            view.text = "'" + keyword + "'에 대한 검색 결과가 없습니다."
        }
    }
    if(keyword == null)
        view.visibility = GONE
}

@BindingAdapter("clubRgstr")
fun setClubRgstr(view: TextView, keyword: String?){
    view.text = "\'" + keyword + "\' 동아리 등록하러가기"
}