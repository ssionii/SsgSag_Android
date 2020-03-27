package com.icoo.ssgsag_android.ui.main.feed.bookmark

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.databinding.ActivityBookmarkedFeedBinding
import com.icoo.ssgsag_android.ui.main.feed.FeedViewModel
import com.icoo.ssgsag_android.ui.main.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.feed.adapter.FeedRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.feed.context
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookmarkedFeedActivity : BaseActivity<ActivityBookmarkedFeedBinding, FeedViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_bookmarked_feed
    override val viewModel: FeedViewModel by viewModel()

    private var feedRecyclerViewAdapter: FeedRecyclerViewAdapter? = null
    private var curPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        setRv()
        setRefreshRv()
        setButton()
    }

    override fun onResume() {
        super.onResume()

        curPage = 0
        if(feedRecyclerViewAdapter != null){
            feedRecyclerViewAdapter!!.clearAll()
        }
        viewModel.getBookmarkedFeeds(curPage)
    }

    private fun setRv(){

        viewModel.bookmarkedFeeds.observe(this, Observer { value ->
            if(feedRecyclerViewAdapter != null){
                feedRecyclerViewAdapter!!.run{
                    addItem(value)
                    notifyDataSetChanged()
                }
            }else{
                feedRecyclerViewAdapter = FeedRecyclerViewAdapter(value)
                feedRecyclerViewAdapter!!.run{
                    setOnFeedItemClickListener(onFeedItemClickListener)
                    setHasStableIds(true)
                }

                viewDataBinding.actBookmarkedFeedRv.apply{
                    adapter = feedRecyclerViewAdapter
                    layoutManager = WrapContentLinearLayoutManager()
                }
            }
        })

        viewDataBinding.actBookmarkedFeedRv.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position =
                            (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {

                            if (itemCount > 0 && (10 * (curPage+1) - 2 < position)) {
                                curPage = (position + 1) / 10

                                viewModel.getBookmarkedFeeds(curPage)
                            }
                        }

                    }
                }

            })
            (this.itemAnimator as SimpleItemAnimator).run {
                changeDuration = 0
                supportsChangeAnimations = false
            }
        }

    }

    private fun setRefreshRv(){
        viewModel.refreshedFeed.observe(this, Observer { value ->
            (viewDataBinding.actBookmarkedFeedRv.adapter as? FeedRecyclerViewAdapter)?.run{
                removedItem(viewModel.refreshedFeedPosition)
                notifyItemRangeChanged(0, itemCount + 1)
            }
        })
    }

    private fun setButton(){
        viewDataBinding.actBookmarkedFeedIvBack.setSafeOnClickListener {
            finish()
        }
    }

    private val onFeedItemClickListener =
        object :
            FeedRecyclerViewAdapter.OnFeedItemClickListener {
            override fun onItemClicked(
                feedIdx: Int, feedUrl: String, feedName: String, isSave: Int, position: Int
            ) {

                val intent = Intent(context, FeedWebActivity::class.java)
                intent.putExtra("clubWebsite", feedUrl)
                intent.putExtra("idx", feedIdx)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

                val bundle = Bundle().apply {
                    putString("clubWebsite", feedUrl)
                    putInt("idx", feedIdx)
                }

                ContextCompat.startActivity(context, intent, bundle)
            }

            override fun onShareClicked(feedUrl: String) {

                val bundle = Bundle().apply {
                    putString("clubWebsite", feedUrl)
                }

                val intent = Intent()
                intent.setAction(Intent.ACTION_SEND)
                intent.setType("text/plain")
                intent.putExtra(Intent.EXTRA_TEXT, feedUrl)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                ContextCompat.startActivity(
                    context, Intent.createChooser(intent, "링크 공유").addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK
                    ), bundle
                )
            }

            override fun onBookmarkClicked(feedIdx: Int, isSaved: Int, position: Int) {
                viewModel.bookmark(feedIdx, isSaved, position, "bookmarked")
                //viewModel.unBookmarkedFeed(position)
            }
        }

}