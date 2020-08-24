package com.icoo.ssgsag_android.util.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.ads.AdItem
import com.icoo.ssgsag_android.databinding.ItemBannerImageBinding
import com.icoo.ssgsag_android.ui.main.allPosters.AllPostersFragment
import com.icoo.ssgsag_android.ui.main.feed.FeedWebActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class AutoScrollViewPagerAdapter(
    private val context: Context,
    private val adList: ArrayList<AdItem>
) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val viewDataBinding = DataBindingUtil.inflate<ItemBannerImageBinding>(
            LayoutInflater.from(context),
            R.layout.item_banner_image, container, false
        )

        viewDataBinding.root.setSafeOnClickListener {
            val intent = Intent(context, FeedWebActivity::class.java)
            intent.putExtra("url", adList[position].adUrl)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            val bundle = Bundle().apply {
                putString("url", adList[position].adUrl)
            }

            startActivity(context, intent, bundle)
        }

        viewDataBinding.root.tag = position
        container.addView(viewDataBinding.root)
        container.clipToPadding= false

        return viewDataBinding.root

    }

    override fun getCount(): Int {
        return adList.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return (view == (o as View))
    }
}
