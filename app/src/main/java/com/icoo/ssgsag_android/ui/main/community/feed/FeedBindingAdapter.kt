package com.icoo.ssgsag_android.ui.main.community.feed

import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.ui.login.LoginActivity
import com.icoo.ssgsag_android.ui.main.MainActivity
import jp.wasabeef.glide.transformations.CropTransformation
import org.jetbrains.anko.textColor
import java.text.DecimalFormat

@BindingAdapter("previewImg")
fun setPreviewImg(view: ImageView, imgUrl: String?) {

    Glide.with(view.context).load(imgUrl)
        .apply(RequestOptions.bitmapTransform(CropTransformation(MainActivity.GetWidth.windowWidth, 450, CropTransformation.CropType.TOP)))
        //.error(R.drawable.img_poster) //에러시 나올 이미지 적용
        .into(view)
}

@BindingAdapter("feedDate")
fun setFeedDate(view: TextView, date: String) {

    var tmp = ""

    if(date.substring(5,6) == "0")
        tmp = "∙" + date.substring(6, 7) + "월 "
    else
        tmp = "∙" + date.substring(5,7) +"월 "

    if(date.substring(8,9) == "0")
        view.text = tmp + date.substring(9,10) + "일"
    else
        view.text = tmp+date.substring(8,10) + "일"

}

@BindingAdapter("showNum")
fun setShowNum(view: TextView, showNum: Int) {
    val formatter = DecimalFormat("###,###")

    view.text = "조회수 " + formatter.format(showNum).toString()
}

@BindingAdapter("isBookmarked")
fun setIsBookmarked(view: ImageView, isSave: Int){
    if(isSave == 0){
        view.setImageResource(R.drawable.ic_bookmark)
    }else if(isSave == 1){
        view.setImageResource(R.drawable.ic_bookmark_filled_active)
    }
}

@BindingAdapter("anchorText")
fun setAnchorText(view: TextView, categoryIdx: Int?) {
    when(categoryIdx){
        100 -> view.text = "BEST"
        101 -> view.text = "학교생활"
        102 -> view.text = "취업뉴스"
        103 -> view.text = "커리어"

    }
}

@BindingAdapter("selectedAnchorText")
fun setSelectedAnchorText(view: TextView, isSelected: Boolean) {
    when(isSelected){
        true -> {
            view.textColor = Color.parseColor("#656ef0")
            view.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_bold)
        }
        false -> {
            view.textColor = Color.parseColor("#bbbbbb")
            view.typeface = ResourcesCompat.getFont(SsgSagApplication.getGlobalApplicationContext(), R.font.noto_sans_kr_regular)
        }
    }
}

@BindingAdapter("selectedAnchorSelector")
fun setSelectedAnchorSelector(view: ImageView, isSelected: Boolean) {
    when(isSelected){
        true -> {
            view.visibility = VISIBLE
        }
        false -> {
            view.visibility = INVISIBLE
        }
    }
}

@BindingAdapter("feedCategoryTitle")
fun setFeedCategoryTitle(view: TextView, categoryIdx: Int?) {
    when(categoryIdx){
        101 -> view.text = "대학생활"
        102 -> view.text = "취업뉴스"
        else -> view.visibility = GONE
    }
}

@BindingAdapter("feedsTitle")
fun setFeedsTitle(view: TextView, categoryIdx: Int?) {
    when(categoryIdx){
        101 -> view.text = "대학생활"
        102 -> view.text = "취업뉴스"
        103 -> view.text = "IT"
        104 -> view.text = "마케팅"
        105 -> view.text = "디자인"
    }
}

@BindingAdapter("glideTopCropBestImg")
fun setGlideTopCropImg(view: ImageView, imgUrl: String?) {
    Glide.with(view.context)
        .load(imgUrl)
        .into(view)
}

@BindingAdapter("glideTopCropFeedImg")
fun setGlideTopFeedImg(view: ImageView, imgUrl: String?) {

    Glide.with(view.context)
        .load(imgUrl)
        .placeholder(R.drawable.img_default)
       // .listener(createLoggerListener("glideTopCropFeedImg"))
        .thumbnail(0.1f)
        .error(R.drawable.img_default) //에러시 나올 이미지 적용
        .into(view)
}

@BindingAdapter("feedShowNum")
fun setFeedShowNum(view: TextView, showNum: Int?) {

    view.text = showNum.toString()
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
