package com.icoo.ssgsag_android.ui.main.allPosters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.databinding.ItemAllPostersCardBinding
import com.icoo.ssgsag_android.ui.main.subscribe.subscribeDialog.SubscribeInternDialogFragment
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class CardViewPagerAdapter(
    private val context : Context,
    private val posterList: ArrayList<AdItem>?
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