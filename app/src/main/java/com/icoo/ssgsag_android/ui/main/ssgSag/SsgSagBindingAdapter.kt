package com.icoo.ssgsag_android.ui.main.ssgSag

import android.graphics.Color
import android.media.Image
import android.provider.ContactsContract
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.item.ItemBase
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.databinding.ItemSsgsagDetailBinding
import com.icoo.ssgsag_android.util.extensionFunction.getItemBase

@Suppress("UNCHECKED_CAST")
@BindingAdapter("poster_detail")
fun setPosterDetail(view: RecyclerView, poster: PosterDetail?) {
    view.run {
        adapter =
            object : BaseRecyclerViewAdapter<ItemBase, ItemSsgsagDetailBinding>() {
                override val layoutResID: Int
                    get() = R.layout.item_ssgsag_detail
                override val bindingVariableId: Int
                    get() = BR.ssgSagDetail
                override val listener: OnItemClickListener?
                    get() = null
            }
        (this.adapter as? BaseRecyclerViewAdapter<Any, *>)?.run {
            replaceAll(getItemBase(poster))
            notifyDataSetChanged()
        }
    }
}

@BindingAdapter("ssgSagHeaderBg")
fun setSsgSagHeaderBg(view: CardView, categoryIdx: Int?) {
    when (categoryIdx) {
        0 -> view.setCardBackgroundColor(view.context.getColor(R.color.contest))
        1 -> view.setCardBackgroundColor(view.context.getColor(R.color.act))
        2 -> view.setCardBackgroundColor(view.context.getColor(R.color.club))
        4 -> view.setCardBackgroundColor(view.context.getColor(R.color.recruit))
        5 -> view.setCardBackgroundColor(view.context.getColor(R.color.etc))
        7 -> view.setCardBackgroundColor(view.context.getColor(R.color.education))
        8 -> view.setCardBackgroundColor(view.context.getColor(R.color.scholarship))
    }
}

@BindingAdapter("posterDdayBg")
fun setPosterDdayBg(view:CardView, dday: String?){
    dday?.let {
        if (it.toInt() >= 0) {
            view.setCardBackgroundColor(Color.parseColor("#14656ef0"))
        } else if(it.toInt() < 0) {
            view.setCardBackgroundColor(Color.parseColor("#14787878"))
        }
    }
}

@BindingAdapter("posterDday")
fun setPosterDday(view: TextView, dday: String?) {
    dday?.let {
        if (it.toInt() > 0) {
            view.text = "D - ${dday}"
            view.setTextColor(Color.parseColor("#656ef0"))
        } else if(it.toInt() == 0) {
            view.text = "D - day"
            view.setTextColor(Color.parseColor("#656ef0"))
        } else if(it.toInt() < 0) {
            view.text = "마감"
            view.setTextColor(Color.parseColor("#787878"))
        }
    }
}

@BindingAdapter("posterDdaySsgSag")
fun setPosterDdaySsgSag(view: TextView, dday: String?) {
    dday?.let {
        if (it.toInt() > 0) {
            view.text = "D - ${dday}"
        } else if(it.toInt() == 0) {
            view.text = "D - day"
        }

        view.setTextColor(Color.parseColor("#ffffff"))
    }
}


@BindingAdapter("favoriteNum")
fun setFavoriteNum(view: TextView, favoriteNum: Int) {
    view.text = favoriteNum.toString()
}

@BindingAdapter("endMentJob")
fun setEndMentJob(view: TextView, arrayList: ArrayList<Int>?){

    view.text = ""

    if(arrayList!= null &&  arrayList?.size != 0) {
        for (i in 0..arrayList!!.size - 1) {
            when (arrayList[i]) {
                301 -> view.text = view.text.toString() + "#개발자 "
                302 -> view.text = view.text.toString() + "#디자이너 "
                303 -> view.text = view.text.toString() + "#마케터 "
                304 -> view.text = view.text.toString() + "#기획자 "
                305 -> view.text = view.text.toString() + "#대학생 "
            }
        }
    }

    view.setTextColor(Color.parseColor("#656ef0"))
}

@BindingAdapter("endMentJobVisibility")
fun setEndMentJobVisibility(view: TextView, arrayList: ArrayList<Int>?){
    if(arrayList!= null && arrayList?.size != 0){
        view.visibility = GONE
        for(i in 0.. arrayList!!.size-1){
            if(arrayList[i] != 305) {
                view.visibility = VISIBLE
                break
            }
        }
    }else {
        view.visibility = GONE
    }
}

@BindingAdapter("endMent")
fun setEndMentJob(view: TextView, nickname: String?){

    if(nickname != ""){
        view.text = nickname + "님을 위한 정보를 매일 추천해드려요 :)"
    }else{
        view.text = "당신을 위한 정보를 매일 추천해드려요 :]"
    }

}

@BindingAdapter("endMentField")
fun setEndMentField(view: TextView, field: String?){
    if(field == ""){
        view.text = "*맞춤 정보를 설정해주세요"
    }else{
        view.text = field
    }

}

@BindingAdapter("endImage")
fun setEndImage(view: ImageView, num: Int?){
    if(num!= null){
        when(num){
            301 -> view.setImageResource(R.drawable.developer)
            302 -> view.setImageResource(R.drawable.designer)
            303 -> view.setImageResource(R.drawable.marketer)
            304 -> view.setImageResource(R.drawable.planner)
            305 -> view.setImageResource(R.drawable.traveler)
        }
    }else {
        view.setImageResource(R.drawable.traveler)
    }
}

@BindingAdapter("imgNo")
fun setImgNo(view: ImageView, isClicked: Boolean){
    if(isClicked)
        view.setImageResource(R.drawable.ic_no)
    else
        view.setImageResource(R.drawable.ic_no_active)
}


@BindingAdapter("imgYes")
fun setImgYes(view: ImageView, isClicked: Boolean){
    if(isClicked)
        view.setImageResource(R.drawable.ic_yes_active)
    else
        view.setImageResource(R.drawable.ic_yes)
}

@BindingAdapter("todaySsgSagBtnBg")
fun setTodaySsgSagBtnBg(view: CardView, size: Int){
    if(size == 0) {
        view.setCardBackgroundColor(Color.parseColor("#aaaaaa"))
        view.isClickable = false
    } else {
        view.setCardBackgroundColor(Color.parseColor("#656ef0"))
        view.isClickable = true
    }
}