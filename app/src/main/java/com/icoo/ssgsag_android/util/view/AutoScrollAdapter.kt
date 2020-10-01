package com.icoo.ssgsag_android.util.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.databinding.ItemBannerImageBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener

class AutoScrollAdapter(
    private val context : Context
) : PagerAdapter(){

    private var mOnItemClickListener: OnItemClickListener? = null
    private val itemList = ArrayList<AdItem>()

    fun replaceAll(items: ArrayList<AdItem>){
        itemList.clear()
        itemList.addAll(items)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val viewDataBinding = DataBindingUtil.inflate<ItemBannerImageBinding>(
            LayoutInflater.from(context),
            R.layout.item_banner_image, container, false
        )

        viewDataBinding.banner = itemList[position]

        viewDataBinding.itemBannerImageIv.setSafeOnClickListener {
            mOnItemClickListener?.onItemClick(itemList[position].adUrl)
        }


        viewDataBinding.root.setTag(position)
        container.addView(viewDataBinding.root)
        container.clipToPadding= false

        return viewDataBinding.root

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
        fun onItemClick(url: String?)
    }

    override fun getCount(): Int {
         return itemList.size
    }

}