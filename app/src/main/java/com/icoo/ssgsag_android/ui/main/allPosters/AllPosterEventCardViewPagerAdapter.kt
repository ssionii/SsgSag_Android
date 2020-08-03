package com.icoo.ssgsag_android.ui.main.allPosters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.databinding.ItemAllPosterEventBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class AllPosterEventCardViewPagerAdapter(
    private val context : Context,
    private val eventList: ArrayList<AdItem>?
) : PagerAdapter() {

    private var mOnItemClickListener: OnItemClickListener? = null
    var eventWidth = 0
    var rowIdx = 0

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val viewDataBinding = DataBindingUtil.inflate<ItemAllPosterEventBinding>(
            LayoutInflater.from(context),
            R.layout.item_all_poster_event, container, false
        )

        if(eventList!= null){
            val eventItem = eventList[position]

            viewDataBinding.event = eventItem
            viewDataBinding.itemAllPosterEventIv.layoutParams.height = (eventWidth * 0.5).toInt()

            viewDataBinding.root.setSafeOnClickListener {
                mOnItemClickListener?.onEventClick(eventItem.adUrl, eventItem.contentTitle)
            }

        }

        viewDataBinding.root.tag = position
        container.addView(viewDataBinding.root)
        container.clipToPadding= false

        return viewDataBinding.root

    }

    override fun getCount(): Int {
        if(eventList!= null)
            return eventList.size
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
        fun onEventClick(adUrl : String?, title: String)
    }

}