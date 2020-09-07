package com.icoo.ssgsag_android.util.dataBinding

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.util.dataBinding.createLoggerListener
import jp.wasabeef.glide.transformations.CropTransformation
import java.io.File
import java.lang.ref.Reference

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

@BindingAdapter("topCropGlideImg")
fun setAllPosterCardGlideImg(view: ImageView, imgUrl: String?) {

    val requestOptions = RequestOptions
        .skipMemoryCacheOf(false)//memory cache 사용
        .diskCacheStrategy(DiskCacheStrategy.NONE)//disk cache 사용하지 않음

    Glide.with(view.context)
        .load(imgUrl)
//        .listener(createLoggerListener("allPosterCardGlideImg"))
        .placeholder(R.drawable.img_default)
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