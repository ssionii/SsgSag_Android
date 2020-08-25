package com.icoo.ssgsag_android.ui.main.allPosters.collection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.databinding.ItemAllPostersCardBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import jp.wasabeef.glide.transformations.CropTransformation

class AllPosterCardViewPagerAdapter(
    private val context : Context,
    private val posterList: ArrayList<AdItem>?,
    private val isIntern : Boolean
) : PagerAdapter() {

    private var mOnItemClickListener: OnItemClickListener? = null
    var posterWidth = 0
    var rowIdx = 0

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val viewDataBinding = DataBindingUtil.inflate<ItemAllPostersCardBinding>(
            LayoutInflater.from(context),
            R.layout.item_all_posters_card, container, false
        )

        if(posterList!= null){
            val posterItem = posterList[position]

            viewDataBinding.poster = posterItem
            viewDataBinding.itemAllPostersCardCvPoster.layoutParams.height = (posterWidth * 1.42).toInt()

            if(isIntern){
                val view = viewDataBinding.itemAllPostersCardIvPoster
                Glide.with(context)
                    .load(posterItem.photoUrl)
                    .placeholder(R.drawable.img_default)
                    .thumbnail(0.1f)
                    .fitCenter()
                    .into(view)
            }else{
                val view = viewDataBinding.itemAllPostersCardIvPoster
                view.adjustViewBounds = true
                Glide.with(context)
                    .load(posterItem.photoUrl)
                    .placeholder(R.drawable.img_default)
                    .apply(RequestOptions.bitmapTransform(CropTransformation(view.width, view.height, CropTransformation.CropType.TOP)))
                    .into(view)
            }


            viewDataBinding.itemAllPostersCardLlContainer.setSafeOnClickListener {
                mOnItemClickListener?.onPosterClick(posterItem.contentIdx, rowIdx, position)
            }
            viewDataBinding.itemAllPosterCardRlCancel.setSafeOnClickListener {
                mOnItemClickListener?.onPosterStoreCancel(posterItem.contentIdx, posterItem.isSave)
                posterItem.isSave = 0

                viewDataBinding.itemAllPosterCardRlStore.visibility = VISIBLE
                viewDataBinding.itemAllPosterCardRlCancel.visibility = GONE

            }
            viewDataBinding.itemAllPosterCardRlStore.setSafeOnClickListener {
                mOnItemClickListener?.onPosterStore(posterItem.contentIdx, posterItem.isSave)
                posterItem.isSave = 1

                viewDataBinding.itemAllPosterCardRlStore.visibility = GONE
                viewDataBinding.itemAllPosterCardRlCancel.visibility = VISIBLE
            }
        }

        viewDataBinding.root.tag = position
        container.addView(viewDataBinding.root)
        container.clipToPadding= false

        return viewDataBinding.root

    }

    override fun getCount(): Int {
        if(posterList!= null)
            return posterList.size
        else
            return 0
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return (view == (o as View))
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onPosterClick(posterIdx: Int, rowIdx : Int, position: Int)
        fun onPosterStoreCancel(posterIdx: Int, isSave : Int)
        fun onPosterStore(posterIdx: Int, isSave: Int)
    }

}