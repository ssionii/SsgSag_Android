package com.icoo.ssgsag_android.ui.main.feed.adapter

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
import com.icoo.ssgsag_android.data.model.feed.FeedCategory
import com.icoo.ssgsag_android.databinding.RvFeedItemBinding
import com.icoo.ssgsag_android.ui.main.feed.FeedViewModel
import com.icoo.ssgsag_android.ui.main.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.feed.category.FeedCategoryRecyclerViewAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.NonScrollLinearLayoutManager


class FeedCareerViewPagerAdapter(
    private val context : Context,
    private val feedCategoryList: ArrayList<FeedCategory>?,
    val viewModel: FeedViewModel
) : PagerAdapter() {

    private var mOnItemClickListener: OnItemClickListener? = null
    private var feedRecyclerViewAdapter1 : FeedRecyclerViewAdapter? = null
    private var feedRecyclerViewAdapter2 : FeedRecyclerViewAdapter? = null
    private var feedRecyclerViewAdapter3 : FeedRecyclerViewAdapter? = null

    private var listener: FeedCategoryRecyclerViewAdapter.OnFeedCategoryClickListener? = null

    fun setOnFeedCategoryClickListener(listener: FeedCategoryRecyclerViewAdapter.OnFeedCategoryClickListener) {
        this.listener = listener
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val viewDataBinding = DataBindingUtil.inflate<RvFeedItemBinding>(
            LayoutInflater.from(context),
            R.layout.rv_feed_item, container, false
        )

        if(feedCategoryList!= null){
            viewDataBinding.feedCategory = feedCategoryList!![position]
        }

        when(position){
            0 -> {
                feedRecyclerViewAdapter1 =
                    FeedRecyclerViewAdapter(
                        feedCategoryList!![position].feedList)
                feedRecyclerViewAdapter1!!.run {
                    setOnFeedItemClickListener(onFeedItemClickListener)
                    setHasStableIds(true)
                }

                viewDataBinding.rvFeedItemRvFeeds.apply {
                    adapter = feedRecyclerViewAdapter1
                    layoutManager = NonScrollLinearLayoutManager(context)
                }

            }
            1 -> {
                feedRecyclerViewAdapter2 =
                    FeedRecyclerViewAdapter(
                        feedCategoryList!![position].feedList)
                feedRecyclerViewAdapter2!!.run {
                    setOnFeedItemClickListener(onFeedItemClickListener)
                    setHasStableIds(true)
                }

                viewDataBinding.rvFeedItemRvFeeds.apply {
                    adapter = feedRecyclerViewAdapter2
                    layoutManager = NonScrollLinearLayoutManager(context)
                }
            }
            2 ->{
                feedRecyclerViewAdapter3 =
                    FeedRecyclerViewAdapter(
                        feedCategoryList!![position].feedList)
                feedRecyclerViewAdapter3!!.run {
                    setOnFeedItemClickListener(onFeedItemClickListener)
                    setHasStableIds(true)
                }

                viewDataBinding.rvFeedItemRvFeeds.apply {
                    adapter = feedRecyclerViewAdapter3
                    layoutManager = NonScrollLinearLayoutManager(context)
                }
            }
        }

        viewDataBinding.rvFeedItemLlMore.setSafeOnClickListener {
            listener?.onMoreBtnClicked(feedCategoryList!![position].categoryIdx)
        }

        viewDataBinding.root.setTag(position)
        container.addView(viewDataBinding.root)
        container.clipToPadding= false

        return viewDataBinding.root

    }

    fun replaceAll(array: ArrayList<FeedCategory>?) {
        feedRecyclerViewAdapter1?.replaceAll(array!![0].feedList)
        feedRecyclerViewAdapter2?.replaceAll(array!![1].feedList)
        feedRecyclerViewAdapter3?.replaceAll(array!![2].feedList)
    }

    override fun getCount(): Int {
       return 3
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return (view == (o as View))
    }

    interface OnFeedCategoryClickListener {
        fun onMoreBtnClicked(categoryIdx: Int)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(feedUrl: String, feedIdx: Int, position: Int)
    }

    private val onFeedItemClickListener =
        object : FeedRecyclerViewAdapter.OnFeedItemClickListener {
            override fun onItemClicked(
                feedIdx: Int, feedUrl: String, feedName: String, isSave: Int, position: Int
            ) {
                val bundle = Bundle().apply {
                    putString("clubWebsite", feedUrl)
                    putInt("idx", feedIdx)
                }

                val intent = Intent(com.icoo.ssgsag_android.ui.main.feed.context, FeedWebActivity::class.java)
                intent.putExtra("clubWebsite", feedUrl)
                intent.putExtra("idx", feedIdx)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

                startActivity(context, intent, bundle)
            }

            override fun onShareClicked(feedUrl: String) {

                val bundle = Bundle().apply {
                    putString("clubWebsite", feedUrl)
                }

                val intent = Intent(android.content.Intent.ACTION_SEND)
                intent.setType("text/plain")

                intent.putExtra(Intent.EXTRA_TEXT, feedUrl)

                val chooser = Intent.createChooser(intent, "친구에게 링크 공유하기")
                startActivity(context, chooser, bundle)
            }

            override fun onBookmarkClicked(feedIdx: Int, isSaved: Int, position: Int) {
                viewModel.bookmark(feedIdx, isSaved, position, "today")
                replaceAll(viewModel.careerFeeds.value!!)
            }
        }

}