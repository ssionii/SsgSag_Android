package com.icoo.ssgsag_android.ui.main.calendar.calendarPage

import android.view.View
import androidx.databinding.BindingAdapter
import android.graphics.Point
import android.content.Context.WINDOW_SERVICE
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.icu.util.ULocale
import android.util.Log
import android.view.View.*
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.category.Category
import com.icoo.ssgsag_android.data.model.date.Date
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@BindingAdapter("calendar_height")
fun setHeight(view: View, lines: Int) {
    view.parent
    val layoutParams = view.layoutParams
    val wm = view.context.getSystemService(WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    val height = size.y// - 480

    layoutParams.height = ((height / lines) / 1.35).toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("calendar_date")
fun setDate(view: TextView, date: Date) {
    view.text = date.date
    if (date.isToDay) {
        view.textColor = view.context.getColor(R.color.white)
        val oval = ShapeDrawable(OvalShape())
        oval.setColorFilter(view.context.getColor(R.color.today), PorterDuff.Mode.SRC_OVER)
        view.background = oval
    } else {
        if (!date.isCurrentMonth)
            view.textColor = view.context.getColor(R.color.passiveMonth)
        else {
            if (date.dayOfWeek == 1)
                view.textColor = view.context.getColor(R.color.sunday)
            else if (date.dayOfWeek == 7)
                view.textColor = view.context.getColor(R.color.saturday)
            else
                view.textColor = view.context.getColor(R.color.activeMonth)
        }
    }
}

@BindingAdapter("date", "position")
fun setSchedule(view: CardView, date: Date, position: Int) {

       date.schedule?.let {
           if (it.size > position) {
               view.visibility = VISIBLE

               /* 지난 포스터 회색 처리
               if (date.schedule!![position].isEnded == 1)
                   view.setCardBackgroundColor(view.context.getColor(R.color.passive))
               else {*/
               when (date.schedule!![position].categoryIdx) {
                   0 -> view.setCardBackgroundColor(view.context.getColor(R.color.contest))
                   1 -> view.setCardBackgroundColor(view.context.getColor(R.color.act))
                   2, 6 -> view.setCardBackgroundColor(view.context.getColor(R.color.club))
                   3 -> view.setCardBackgroundColor(view.context.getColor(R.color.notice))
                   4 -> view.setCardBackgroundColor(view.context.getColor(R.color.recruit))
                   5 -> view.setCardBackgroundColor(view.context.getColor(R.color.etcText))
                   7 -> view.setCardBackgroundColor(view.context.getColor(R.color.education))
                   8 -> view.setCardBackgroundColor(view.context.getColor(R.color.scholarship))
               }
               //}
           } else
               view.visibility = INVISIBLE

           if(it.size > 4){
               if(position == 3)
                   view.visibility = INVISIBLE
               else if(position == 4) {
                   view.visibility = VISIBLE
                   view.setCardBackgroundColor(view.context.getColor(R.color.white))
               }
           }

       }

}

@BindingAdapter("date", "position")
fun setBookmarkBtnImgInCal(view: ImageView, date: Date, position: Int) {
    date.schedule?.let {
        if (it.size > position) {
            if (it[position].isFavorite == 1)
                view.visibility = View.VISIBLE
            else
                view.visibility = View.GONE
        } else
            view.visibility = View.GONE
    }
}

@BindingAdapter("date", "position")
fun setText(view: TextView, date: Date, position: Int) {
    date.schedule?.let {
        if (it.size > position) {
            view.text = it[position].posterName
        }

        if(it.size > 4 && position == 4){
            view.text = "+" + (it.size - 3).toString()
        }
    }
}

@BindingAdapter("list_date")
fun setListDate(view: TextView, date: String){

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val nDate = dateFormat.parse(date)

    val cal = Calendar.getInstance()
    cal.time = nDate

    val dayNum = cal.get(Calendar.DAY_OF_WEEK)

    lateinit var dayOfWeek : String

    when(dayNum){
        1 -> dayOfWeek = "일"
        2 -> dayOfWeek = "월"
        3 -> dayOfWeek = "화"
        4 -> dayOfWeek = "수"
        5 -> dayOfWeek = "목"
        6 -> dayOfWeek = "금"
        7 -> dayOfWeek = "토"
    }

    view.text = date.substring(5,7) + "." + date.substring(8,10) + "(" + dayOfWeek + ")"
}

@BindingAdapter("dateLine")
fun setDateLine(relativeLayout: RelativeLayout, isAlone:Boolean){
    if(isAlone)
        relativeLayout.visibility = VISIBLE
    else
        relativeLayout.visibility = GONE
}


@BindingAdapter("bottomLine")
fun setBottomLine(view: View, isLast:Boolean){
    if(isLast)
        view.visibility = VISIBLE
    else
        view.visibility = GONE
}

@BindingAdapter("favoriteVisibility")
fun setFavoriteVisibility(view: ImageView, selectType: Int) {
    when(selectType){
        0 -> {
            view.visibility = VISIBLE
        }
        1 -> view.visibility = GONE
    }

}


@BindingAdapter("selectorVisibility")
fun setSelectorVisibility(view: ImageView, selectType: Int) {
    when(selectType){
        0-> {
            view.visibility = GONE
            view.isSelected = false
        }
        1 -> view.visibility = VISIBLE

    }
}

@BindingAdapter("selectedBgSsgsag")
fun setSelectedBgSsgsag(view: CardView, bool: Boolean) {
    if(bool){
        view.setCardBackgroundColor(view.context.getColor(R.color.ssgsag8))
    }else{
        view.setCardBackgroundColor(Color.parseColor("#f2f2f2"))
    }
}

@BindingAdapter("selectedTextSsgsag")
fun setSelectedTextSsgsag(view: TextView, bool:Boolean){
    if(bool){
        view.setTextColor(view.context.getColor(R.color.ssgsag))
    }else{
        view.setTextColor(view.context.getColor(R.color.grey_1))
    }
}

@BindingAdapter("selectedFilterImg")
fun setSelectedFilterImg(view: ImageView, bool:Boolean){
    if(bool){
        view.setImageDrawable(view.context.getDrawable(R.drawable.ic_circle_checked_active))
    }else{
        view.setImageDrawable(view.context.getDrawable(R.drawable.ic_circle_checked))
    }
}