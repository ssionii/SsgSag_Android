package com.icoo.ssgsag_android.ui.main.allPosters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.poster.posterDetail.PosterDetail
import com.icoo.ssgsag_android.databinding.ItemAllPostersCardBinding
import com.icoo.ssgsag_android.ui.main.subscribe.subscribeDialog.SubscribeInternDialogFragment
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class CardViewPagerAdapter(
    private val context : Context,
    private val posterList: ArrayList<PosterDetail>?
) : PagerAdapter() {

    private var mOnItemClickListener: OnItemClickListener? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val viewDataBinding = DataBindingUtil.inflate<ItemAllPostersCardBinding>(
            LayoutInflater.from(context),
            R.layout.item_all_posters_card, container, false
        )

        if(posterList!= null){
            viewDataBinding.poster = posterList!![position]

            viewDataBinding.itemAllPostersCardCvContainer.setSafeOnClickListener {
                mOnItemClickListener?.onItemClick(posterList[position].posterIdx)
            }
        }

        viewDataBinding.root.setTag(position)
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
        fun onItemClick(posterIdx: Int)
    }

}