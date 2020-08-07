package com.icoo.ssgsag_android.ui.main.feed.category

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.data.model.feed.FeedCategory
import com.icoo.ssgsag_android.databinding.RvFeedItemBinding
import com.icoo.ssgsag_android.ui.main.feed.adapter.FeedRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.feed.FeedViewModel
import com.icoo.ssgsag_android.ui.main.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.SimpleItemAnimator
import kotlinx.coroutines.runBlocking


class FeedCategoryRecyclerViewAdapter(
    var itemList: ArrayList<FeedCategory>,
    val viewModel: FeedViewModel
) : RecyclerView.Adapter<FeedCategoryRecyclerViewAdapter.ViewHolder>(),
LifecycleOwner, LifecycleObserver{

    private var lifecycleRegistry = LifecycleRegistry(this)

    private var listener: OnFeedCategoryClickListener? = null
    private var feedRecyclerViewAdapter1 : FeedRecyclerViewAdapter? = null
    private var feedRecyclerViewAdapter2 : FeedRecyclerViewAdapter? = null
    fun setOnFeedCategoryClickListener(listener: OnFeedCategoryClickListener) {
        this.listener = listener
    }

    fun replaceAll(array: ArrayList<FeedCategory>?) {
        feedRecyclerViewAdapter1?.replaceAll(array!![0].feedList)
        feedRecyclerViewAdapter2?.replaceAll(array!![1].feedList)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewDataBinding = DataBindingUtil.inflate<RvFeedItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.rv_feed_item, parent, false
        )

        return ViewHolder(viewDataBinding)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.feedCategory = itemList[position]
        holder.dataBinding.rvFeedItemLlMore.setSafeOnClickListener {
            listener?.onMoreBtnClicked(itemList[position].categoryIdx)
        }

        when(position){
            0 -> {
                if(feedRecyclerViewAdapter1 == null){
                    feedRecyclerViewAdapter1 = FeedRecyclerViewAdapter(itemList[position].feedList)
                    feedRecyclerViewAdapter1!!.apply{
                        setOnFeedItemClickListener(onFeedItemClickListener)
                        setHasStableIds(true)
                    }
                    holder.dataBinding.rvFeedItemRvFeeds.apply {
                        adapter = feedRecyclerViewAdapter1
                        layoutManager = WrapContentLinearLayoutManager()
                        (this.itemAnimator as SimpleItemAnimator).run {
                            changeDuration = 0
                            supportsChangeAnimations = false
                        }
                    }
                }
            }
            1 -> {
                if(feedRecyclerViewAdapter2 == null){
                    feedRecyclerViewAdapter2 = FeedRecyclerViewAdapter(itemList[position].feedList)
                    feedRecyclerViewAdapter2!!.apply{
                        setOnFeedItemClickListener(onFeedItemClickListener)
                        setHasStableIds(true)
                    }
                    holder.dataBinding.rvFeedItemRvFeeds.apply {
                        adapter = feedRecyclerViewAdapter2
                        layoutManager = WrapContentLinearLayoutManager()
                        (this.itemAnimator as SimpleItemAnimator).run {
                            changeDuration = 0
                            supportsChangeAnimations = false
                        }
                    }
                }
            }
        }



    }

    override fun getItemId(position: Int): Long {
        return itemList[position].categoryIdx.toLong()
    }

    inner class ViewHolder(val dataBinding: RvFeedItemBinding) :
        RecyclerView.ViewHolder(dataBinding.root)

    interface OnFeedCategoryClickListener {
        fun onMoreBtnClicked(categoryIdx: Int)
    }

    private val onFeedItemClickListener =
        object :
            FeedRecyclerViewAdapter.OnFeedItemClickListener {
            override fun onItemClicked(
                feedIdx: Int, feedUrl: String, feedName: String, isSave: Int, position: Int
            ) {

                val intent = Intent(context, FeedWebActivity::class.java)
                intent.putExtra("url", feedUrl)
                intent.putExtra("from", "feed")
                intent.putExtra("idx", feedIdx)
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

                val bundle = Bundle().apply {
                    putString("url", feedUrl)
                    putString("from", "feed")
                    putInt("idx", feedIdx)
                }

                startActivity(context, intent, bundle)
            }

            override fun onShareClicked(feedUrl: String) {

                val bundle = Bundle().apply {
                    putString("clubWebsite", feedUrl)
                }
                
                val intent = Intent()
                intent.setAction(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_TEXT, feedUrl)
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)

                startActivity(context, Intent.createChooser(intent, "링크 공유").addFlags(
                    FLAG_ACTIVITY_NEW_TASK), bundle)
            }

            override fun onBookmarkClicked(feedIdx: Int, isSaved: Int, position: Int) {
                viewModel.bookmark(feedIdx, isSaved, position)

                feedRecyclerViewAdapter1?.run {
                    refreshBookmark(position, isSaved)
                    notifyItemChanged(position)
                }


                feedRecyclerViewAdapter2?.run {
                    refreshBookmark(position, isSaved)
                    notifyItemChanged(position)
                }
            }
        }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }

}