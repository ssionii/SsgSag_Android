package com.icoo.ssgsag_android.ui.main.feed.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.databinding.ItemFeedBestBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class FeedBestCardViewPagerAdapter(
    private val context : Context,
    private val feedList: ArrayList<Feed>?
) : PagerAdapter() {

    private var mOnItemClickListener: OnItemClickListener? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val viewDataBinding = DataBindingUtil.inflate<ItemFeedBestBinding>(
            LayoutInflater.from(context),
            R.layout.item_feed_best, container, false
        )

        if(feedList!= null){
            viewDataBinding.feed = feedList!![position]

            viewDataBinding.itemFeedBestLlContainer.setSafeOnClickListener {
                mOnItemClickListener!!.onItemClick(feedList[position].feedUrl, feedList[position].feedIdx, position)
            }
        }

        viewDataBinding.root.setTag(position)
        container.addView(viewDataBinding.root)
        container.clipToPadding= false

        return viewDataBinding.root

    }

    override fun getCount(): Int {
        if(feedList!= null)
            return feedList.size
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
        fun onItemClick(feedUrl: String, feedIdx: Int, position: Int)
    }

}