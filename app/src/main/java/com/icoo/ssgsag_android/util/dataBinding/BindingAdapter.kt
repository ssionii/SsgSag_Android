package com.icoo.ssgsag_android.util.dataBinding
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage.CalendarDialogPageRecyclerViewAdapter
import com.icoo.ssgsag_android.util.extensionFunction.dayOfWeekExtension
import com.icoo.ssgsag_android.util.extensionFunction.getDateInfo
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropTransformation
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.textColor
import org.w3c.dom.Text
import java.io.File
import java.lang.ref.Reference

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

@BindingAdapter("glideImg")
fun setGlideImg(view: ImageView, imgUrl: String?) {

    val requestOptions = RequestOptions
        .skipMemoryCacheOf(false)//memory cache 사용
        .diskCacheStrategy(DiskCacheStrategy.NONE)//disk cache 사용하지 않음

    Glide.with(view.context)
        .load(imgUrl)
        .placeholder(R.drawable.img_default)
      //  .listener(createLoggerListener("glideImg"))
        .thumbnail(0.1f)
        .error(R.drawable.img_default) //에러시 나올 이미지 적용
        .apply(requestOptions)
        .into(view)

}

@BindingAdapter("glideSsgSagImg")
fun setGlideSsgSagImg(view: ImageView, imgUrl: String?) {

    val requestOptions = RequestOptions
        .skipMemoryCacheOf(false)//memory cache 사용
        .diskCacheStrategy(DiskCacheStrategy.NONE)//disk cache 사용하지 않음

    Glide.with(view.context)
        .load(imgUrl)
        .placeholder(R.drawable.img_default)
      //  .listener(createLoggerListener("glideSsgSagImg"))
        .thumbnail(0.1f)
        .override(501, 704)
        .error(R.drawable.img_default) //에러시 나올 이미지 적용
        .apply(requestOptions)
        .into(view)

}

@BindingAdapter("allPosterCardGlideImg")
fun setAllPosterCardGlideImg(view: ImageView, imgUrl: String?) {

    val requestOptions = RequestOptions
        .skipMemoryCacheOf(false)//memory cache 사용
        .diskCacheStrategy(DiskCacheStrategy.NONE)//disk cache 사용하지 않음

    Glide.with(view.context)
        .load(imgUrl)
//        .listener(createLoggerListener("allPosterCardGlideImg"))
        .placeholder(R.drawable.img_default)
        .thumbnail(0.1f)
        .apply(RequestOptions.bitmapTransform(CropTransformation(view.width, view.height, CropTransformation.CropType.TOP)))
        .into(view)
}

@BindingAdapter("imgResId")
fun setImgResId(view: ImageView, resId: Reference<Any>) {

    Log.e("imgResId", resId.toString())
    Glide.with(view.context)
        .load(resId)
        .into(view)
}

@BindingAdapter("glideImgFromSquare")
fun setGlideImgFromSquare(view: ImageView, string: String?) {
    if(string != "") {

        if (string!![0] == '/') {
            Glide.with(view.context)
                .load(File(string))
                .placeholder(R.drawable.img_default)
                .thumbnail(0.1f)
                .error(R.drawable.img_default) //에러시 나올 이미지 적용
                .into(view)
        } else {
            Glide.with(view.context)
                .load(string)
                .placeholder(R.drawable.img_default)
                .thumbnail(0.1f)
                .error(R.drawable.img_default) //에러시 나올 이미지 적용
                .into(view)
        }
    }

}

@BindingAdapter("glideTopCrop")
fun setGlideTopCropImg(view: ImageView, imgUrl: String?) {
    Glide.with(view.context)
        .load(imgUrl)
        .listener(createLoggerListener("glideTopCrop"))
        .apply(RequestOptions.bitmapTransform(CropTransformation(view.width, 1520, CropTransformation.CropType.TOP)))
        .into(view)
}



@BindingAdapter("glideCenterCrop")
fun setGlideCenterCropImg(view: ImageView, imgUrl: String?) {
    Glide.with(view.context)
        .load(imgUrl)
        .centerCrop()
        .error(R.drawable.img_poster) //에러시 나올 이미지 적용
        .into(view)
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

@BindingAdapter("intToString")
fun setIntToString(view: TextView, input: Int){
    view.text = input.toString()
}

@BindingAdapter("llVisibilityByString")
fun setLinearLayoutVisibility(layout: LinearLayout, string: String?){
    if(string == "")
        layout.visibility = GONE
    else
        layout.visibility = VISIBLE
}

// R: 리버스 라는 뜻
@BindingAdapter("clVisibilityByStringR")
fun setConstraintLayoutVisibilityR(layout: ConstraintLayout, string: String?){
    if(string != "")
        layout.visibility = GONE
    else
        layout.visibility = VISIBLE
}

@BindingAdapter("tvVisibilityByInt")
fun setTextViewVisibility(view: TextView, num: Int){
    if(num == 1)
        view.visibility = GONE
    else
        view.visibility = VISIBLE
}

@BindingAdapter("tvVisibilityByString")
fun setTextviewVisivility(view: TextView, str: String?){
    if(str == null || str == "")
        view.visibility = GONE
    else
        view.visibility = VISIBLE
}

@BindingAdapter("tvVisibilityFromEmptyView")
fun setTextViewVisibilityFromEmptyView(view: TextView, num: Int){

    if(num == 0)
        view.visibility = VISIBLE
    else
        view.visibility = GONE
}


@BindingAdapter("ivVisibilityByInt")
fun setImageViewVisibility(view: ImageView, num: Int){
    if(num == 0)
        view.visibility = VISIBLE
    else
        view.visibility = GONE
}

@BindingAdapter("clVisibilityByBool")
fun setConstraintLayoutVisibility(layout: ConstraintLayout, bool: Boolean){
    if(bool)
        layout.visibility = VISIBLE
    else
        layout.visibility = GONE
}

@BindingAdapter("llVisibilityByInt")
fun LinearLayout.setVisibilityByInt(num: Int){
    when(num){
        1 -> this.visibility = VISIBLE
        else -> this.visibility = GONE
    }
}

@BindingAdapter("llVisibilityByIntR")
fun LinearLayout.setVisibilityByIntR(num: Int){
    when(num){
        0 -> this.visibility = VISIBLE
        else -> this.visibility = GONE
    }
}

@BindingAdapter("cvVisibilityByIntR")
fun CardView.setVisibilityByIntR(num: Int){
    when(num){
        0 -> this.visibility = VISIBLE
        else -> this.visibility = GONE
    }
}

@BindingAdapter("cvVisibilityByInt")
fun CardView.setVisibilityByInt(num: Int){
    when(num){
        1 -> this.visibility = VISIBLE
        else -> this.visibility = GONE
    }
}

@BindingAdapter("rlVisibilityByInt")
fun RelativeLayout.setVisibilityByInt(num: Int){
    when(num){
        0 -> this.visibility = GONE
        1 -> this.visibility = VISIBLE

    }
}


@BindingAdapter("rlVisibilityByIntR")
fun RelativeLayout.setVisibilityByIntR(num: Int){
    when(num){
        0 -> this.visibility = VISIBLE
        1 -> this.visibility = GONE
    }
}


private fun createLoggerListener(name: String): RequestListener<Drawable> {
    return object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?,
                                  model: Any?,
                                  target: com.bumptech.glide.request.target.Target<Drawable>?,
                                  isFirstResource: Boolean): Boolean {
            return false
        }

        override fun onResourceReady(resource: Drawable?,
                                     model: Any?,
                                     target: com.bumptech.glide.request.target.Target<Drawable>?,
                                     dataSource: DataSource?,
                                     isFirstResource: Boolean): Boolean {
            if (resource is BitmapDrawable) {
                val bitmap = resource.bitmap
                Log.d("GlideApp",
                    String.format("Ready %s bitmap %,d bytes, size: %d x %d",
                        name,
                        bitmap.byteCount,
                        bitmap.width,
                        bitmap.height))
            }
            return false
        }
    }
}
