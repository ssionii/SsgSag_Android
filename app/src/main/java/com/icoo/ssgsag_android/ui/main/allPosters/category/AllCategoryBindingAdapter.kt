package com.icoo.ssgsag_android.ui.main.allPosters.category

import android.graphics.Color
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import com.icoo.ssgsag_android.R
import org.jetbrains.anko.textColor

@BindingAdapter("categoryTitle")
fun setCategoryTitle(view: TextView, num: Int) {
    when(num){
        0 -> view.text ="공모전"
        1 -> view.text ="대외활동"
        2,6 -> view.text = "동아리"
        4 -> view.text ="인턴"
        5 -> view.text = "기타"
        7 -> view.text = "교육/강연"
    }
}

@BindingAdapter("categorySortType")
fun setCategorySortType(view: TextView, num: Int) {
    when(num){
        0 -> view.text = "인기순"
        1 -> view.text = "마감순"
        2 -> view.text = "최신순"
    }
}

@BindingAdapter("categoryField", "isUnivClub")
fun setCategoryField(view: TextView, input: String, isUnivClub: Boolean?) {
    var num = 0
    if(input == "10000, 251"){
        num = 10000251
    }else if(input == "50000, 251"){
        num = 50000251
    } else{
        num = input.toInt()
    }

    view.setTextColor(Color.parseColor("#656ef0"))
    view.text = "분야: "

    if(isUnivClub != null && (num > 400 || num == 0)){
        if(isUnivClub) view.text= view.text.toString() + "교내 - "
        else view.text= view.text.toString() + "연합 - "
    }

    num.run{
        when(this){
            0, 300 -> {
                view.text = view.text.toString() + "전체"
                view.setTextColor(Color.parseColor("#777777"))
            }

            101 -> view.text= view.text.toString() + "경영/비즈니스"
            110 -> view.text= view.text.toString() + "광고/마케팅"
            104 -> view.text= view.text.toString() + "개발"
            107 -> view.text= view.text.toString() + "미디어"
            109 -> view.text= view.text.toString() + "엔지니어링/설계"
            112 -> view.text= view.text.toString() + "디자인"
            102 -> view.text= view.text.toString() + "영업"
            106 -> view.text= view.text.toString() + "인사/교육"
            111 -> view.text= view.text.toString() + "고객서비스/리테일"
            103 -> view.text= view.text.toString() + "제조/생산"

            201 -> view.text= view.text.toString() + "기획/아이디어"
            202 -> view.text= view.text.toString() + "광고/마케팅"
            204 -> view.text= view.text.toString() + "문학/시나리오"
            205 -> view.text= view.text.toString() + "디자인"
            206 -> view.text= view.text.toString() + "영상/콘텐츠"
            207 -> view.text= view.text.toString() + "IT/공학"
            208 -> view.text= view.text.toString() + "창업/스타트업"
            215 -> view.text= view.text.toString() + "금융/경제"

            10000251 -> view.text= view.text.toString() + "대기업 서포터즈"
            50000251 -> view.text= view.text.toString() + "공사/공기업 서포터즈"
            252 -> view.text= view.text.toString() + "봉사활동"
            255 -> view.text= view.text.toString() + "리뷰/체험단"
            254 -> view.text= view.text.toString() + "해외봉사/탐방"

            301 -> view.text= view.text.toString() + "개발자"
            302 -> view.text= view.text.toString() + "디자이너"
            303 -> view.text= view.text.toString() + "마케터"
            304 -> view.text= view.text.toString() + "기획자"
            305 -> view.text= view.text.toString() + "기타"

            401 -> view.text = view.text.toString() + "문화생활"
            402 -> view.text = view.text.toString() + "스포츠"
            403 -> view.text = view.text.toString() + "여행"
            404 -> view.text = view.text.toString() + "음악/예술"
            405 -> view.text = view.text.toString() + "봉사"
            406 -> view.text = view.text.toString() + "스터디/학회"
            407 -> view.text = view.text.toString() + "어학"
            408 -> view.text = view.text.toString() + "창업"
            409 -> view.text = view.text.toString() + "친목"
            410 -> view.text = view.text.toString() + "IT/공학"

            10000 -> view.text = view.text.toString() + "대기업"
            20000 -> view.text = view.text.toString() + "중견기업"
            30000 -> view.text = view.text.toString() + "중소기업"
            40000 -> view.text = view.text.toString() + "스타트업"
            50000 -> view.text = view.text.toString() + "공사/공기업"
            60000 -> view.text = view.text.toString() + "외국계기업"

            199, 299, 411, 95000 -> view.text= view.text.toString() + "기타"

            else -> view.text = ""
        }
    }
}

@BindingAdapter("categoryFieldBackground")
fun setCategoryFieldBackground(view:CardView, num: String){
    if(num == "0")
        view.setBackgroundResource(R.drawable.border_gray_box)
    else
        view.setBackgroundResource(R.drawable.border_selected_box)
}

@BindingAdapter("categoryVisibility")
fun setCategoryVisibility(view: CardView, num: Int) {
    when(num){
        0 -> view.visibility = VISIBLE
        1 -> view.visibility = VISIBLE
        4 -> view.visibility = VISIBLE
        5 -> view.visibility = INVISIBLE
        7 -> view.visibility = INVISIBLE
    }
}
