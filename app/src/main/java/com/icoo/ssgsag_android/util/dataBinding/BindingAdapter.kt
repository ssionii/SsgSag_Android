package com.icoo.ssgsag_android.util.dataBinding
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.util.extensionFunction.dayOfWeekExtension
import com.icoo.ssgsag_android.util.extensionFunction.getDateInfo
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import java.text.SimpleDateFormat
import java.util.*

//View BindingAdapter
@Suppress("UNCHECKED_CAST")
@BindingAdapter("replaceAll")
fun RecyclerView.replaceAll(list: List<Any>?) {
    (this.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
        replaceAll(list)
        notifyItemRangeChanged(0, this.itemCount)
    }
}

@BindingAdapter("addAll")
fun RecyclerView.addAll(list: List<Any>?) {
    (this.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
        addItem(list)
        notifyItemRangeChanged(0, this.itemCount)
    }
}

@BindingAdapter("nullDataInvisible")
fun setNullDataInvisible(view: LinearLayout, data: Any?) {
    data?.run {
        view.visibility = View.VISIBLE
    } ?: run {
        view.visibility = View.GONE
    }
}

@BindingAdapter("noDataDirective")
fun setNoDataDirective(view: TextView, data: List<Any>?) {
    data?.run {
        if (size == 0) view.visibility = View.VISIBLE
        else view.visibility = View.GONE
    }
}


//Category BindingAdapter
@BindingAdapter("textColorByCate")
fun setTextColorByCate(view: TextView, categoryIdx: Int?) {
    when (categoryIdx) {
        0 -> view.textColor = view.context.getColor(R.color.contest)
        1 -> view.textColor = view.context.getColor(R.color.act)
        2 -> view.textColor = view.context.getColor(R.color.club)
        3 -> view.textColor = view.context.getColor(R.color.notice)
        4 -> view.textColor = view.context.getColor(R.color.recruit)
        5 -> view.textColor = view.context.getColor(R.color.etcText)
        7 -> view.textColor = view.context.getColor(R.color.education)
        8 -> view.textColor = view.context.getColor(R.color.scholarship)
    }
}

@BindingAdapter("textByCate")
fun setTextByCate(view: TextView, categoryIdx: Int?) {
    when (categoryIdx) {
        0 -> view.text = "공모전"
        1 -> view.text = "대외활동"
        2 -> view.text = "동아리"
        3 -> view.text = "교내공지"
        4 -> view.text = "인턴"
        5 -> view.text = "기타"
        6 -> view.text = "동아리"
        7 -> view.text = "교육/강연"
        8-> view.text ="장학금/지원"
    }
}

@BindingAdapter("cateCardBg")
fun setCateBg(view: CardView, categoryIdx: Int?) {
    when (categoryIdx) {
        0 -> view.setCardBackgroundColor(view.context.getColor(R.color.contestBg))
        1 -> view.setCardBackgroundColor(view.context.getColor(R.color.actBg))
        2 -> view.setCardBackgroundColor(view.context.getColor(R.color.clubBg))
        3 -> view.setCardBackgroundColor(view.context.getColor(R.color.noticeBg))
        4 -> view.setCardBackgroundColor(view.context.getColor(R.color.recruitBg))
        5 -> view.setCardBackgroundColor(view.context.getColor(R.color.etcBg))
        7 -> view.setCardBackgroundColor(view.context.getColor(R.color.educationBg))
        8 -> view.setCardBackgroundColor(view.context.getColor(R.color.scholarshipBg))

    }
}

@BindingAdapter("cateBg")
fun setCateBg(view: View, categoryIdx: Int?) {
    when (categoryIdx) {
        0 -> view.backgroundColor = view.context.getColor(R.color.contest)
        1 -> view.backgroundColor = view.context.getColor(R.color.act)
        2 -> view.backgroundColor = view.context.getColor(R.color.club)
        3 -> view.backgroundColor = view.context.getColor(R.color.notice)
        4 -> view.backgroundColor = view.context.getColor(R.color.recruit)
        5 -> view.backgroundColor = view.context.getColor(R.color.etc)
        7 -> view.backgroundColor = view.context.getColor(R.color.education)
        8 -> view.backgroundColor = view.context.getColor(R.color.scholarship)
    }
}

//date BindingAdapter
@BindingAdapter("startDateFormat1", "endDateFormat1")
fun setDateTextFormat1(view: TextView, posterStartDate: String?, posterEndDate: String?) {
    var startDate = ""
    var endDate = ""
    posterStartDate?.let {
        if(!posterStartDate[0].equals('0')) {
            val startDateArr =
                arrayListOf(getDateInfo(it, 0), getDateInfo(it, 1), getDateInfo(it, 2))
            startDate = "${startDateArr[1]}.${startDateArr[2]}"
        }
    }
    posterEndDate?.let {
        val endDateArr = arrayListOf(getDateInfo(it, 0), getDateInfo(it, 1), getDateInfo(it, 2))
        endDate = " ~ ${endDateArr[1]}.${endDateArr[2]}"
    }
    view.text = startDate + endDate
}

@BindingAdapter("startDateFormat2", "endDateFormat2")
fun setDateTextFormat2(view: TextView, posterStartDate: String?, posterEndDate: String?) {

    var startDate = ""
    var endDate = ""
    posterStartDate?.let {
        if(!posterStartDate[0].equals('0')){
            val startDateArr = arrayListOf(getDateInfo(it, 0), getDateInfo(it, 1), getDateInfo(it, 2))
            startDate = "${startDateArr[1]}.${startDateArr[2]}(${dayOfWeekExtension(startDateArr)})"

        }
    }
    posterEndDate?.let {
        val endDateArr = arrayListOf(getDateInfo(it, 0), getDateInfo(it, 1), getDateInfo(it, 2))
        endDate = " ~ ${endDateArr[1]}.${endDateArr[2]}(${dayOfWeekExtension(endDateArr)})"
    }

    view.text = startDate + endDate
}

@BindingAdapter("singleDateFormat1")
fun setSingleDateTextForm1(view: TextView, date: String?) {
    date?.run {
        view.text = substring(5, 16).replace(" ", "일 ").replace("-", "월 ")
    }
}

@BindingAdapter("regDate")
fun setRegDate(view: TextView, date: String?){

    if(date != null){
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        val current = Date()
        val dateString = formatter.format(current)

        val date2 = formatter.parse(date)
        val formatter2 = SimpleDateFormat("yy/M/d", Locale.KOREA)
        val dateString2 = formatter2.format(date2)


        var text = ""

        val monthGap = dateString.substring(5, 7).toInt() - date.substring(5, 7).toInt()
        val dayGap = dateString.substring(8, 10).toInt() - date.substring(8, 10).toInt()
        val hourGap = dateString.substring(11, 13).toInt() - date.substring(11, 13).toInt()
        val minuteGap = dateString.substring(14, 16).toInt() - date.substring(14, 16).toInt()


        if(monthGap == 0 && dayGap < 7){
            if(dayGap == 0){
                if(hourGap == 0){
                    if(minuteGap == 0){ text = "지금" }
                    else{ text = minuteGap.toString() + "분 전" }
                }else{ text = hourGap.toString() + "시간 전" }
            }else{ text = dayGap.toString() + "일 전" }
        }else{ text = dateString2}

        view.text = text
    }

}


@BindingAdapter("regDateByDot")
fun setRegDateByDot(view: TextView, date: String?){
    if(date != null){
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA)
        val date2 = formatter.parse(date)
        val formatter2 = SimpleDateFormat("yyyy.MM.dd", Locale.KOREA)
        val dateString = formatter2.format(date2)
        view.text = dateString
    }

}


@BindingAdapter("intToString")
fun setIntToString(view: TextView, input: Int){
    view.text = input.toString()
}

