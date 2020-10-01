package com.icoo.ssgsag_android.ui.main.community.feed

import SsgSagNewsViewPagerAdapter
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.model.feed.Feed
import com.icoo.ssgsag_android.databinding.ActivityCommunityFeedBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommunityFeedActivity : BaseActivity<ActivityCommunityFeedBinding, FeedViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_community_feed

    override val viewModel: FeedViewModel by viewModel()

    val bookmarkFeedRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult : ActivityResult ->
        val resultCode : Int = activityResult.resultCode
        val data : Intent? = activityResult.data

        if(resultCode == Activity.RESULT_OK) {
            data?.let{
                val isSave = it.getIntExtra("isSave", 0)
                val position =  it.getIntExtra("position", 0)

                feedRecyclerViewAdapter.run{
                    refreshItemIsSave(isSave, position)
                    notifyItemChanged(position)
                }
            }
        }
    }

    private lateinit var feedRecyclerViewAdapter: FeedRecyclerViewAdapter

    var curPage = 0
    var pageSize = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel

        viewDataBinding.actCommunityFeedToolbar.toolbarBackTvTitle.text = "슥삭 추천정보"

        viewModel.getTodayFeeds(curPage, pageSize)

        setSsgsagNewsVp()
        setRv()
        setButton()
        setObserver()
        setPullToRefresh()
    }

    private fun setSsgsagNewsVp(){

        val d = resources.displayMetrics.density

        // 화면 전체 사이즈
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width = (size.x / d).toInt()

        val leftMargin = (20 * d).toInt()
        val middleMargin = (10 * d).toInt()
        val rightMargin = (30 * d).toInt()
        val contentWidth = ((width - 20 - 10 - 30) * d).toInt()



        viewModel.bestFeedList.observe(this, androidx.lifecycle.Observer {
            val ssgSagNewsViewPagerAdapter = SsgSagNewsViewPagerAdapter(this,  it, "feed")

            ssgSagNewsViewPagerAdapter.replaceAll(it)
            ssgSagNewsViewPagerAdapter.apply {
                newsWidth = contentWidth
                setOnItemClickListener(bestFeedItemClickListener)
            }
            viewDataBinding.actCommunityFeedAvp.apply{
                clipToPadding = false
                setPadding(leftMargin, 0, rightMargin, 0)
                pageMargin = middleMargin
                adapter = ssgSagNewsViewPagerAdapter
            }
        })

    }

    val bestFeedItemClickListener = object : SsgSagNewsViewPagerAdapter.OnItemClickListener {
        override fun onItemClick(idx: Int, url: String, name: String, position: Int) {
            val intent = Intent(this@CommunityFeedActivity, FeedWebActivity::class.java)
            intent.apply{
                putExtra("from","feed")
                putExtra("url",url)
                putExtra("idx",idx)
                putExtra("title",name)
                putExtra("position", position)
            }

            startActivity(intent)
        }

        override fun bookmark(feed: Feed, position: Int) {
            viewModel.bookmark(feed, position, true)
        }
    }

    private fun setRv(){

        feedRecyclerViewAdapter = FeedRecyclerViewAdapter()
        feedRecyclerViewAdapter.apply {
            setOnFeedItemClickListener(feedItemClickListener)
            setHasStableIds(true)
        }

        viewDataBinding.actCommunityFeedRv.run{
            adapter = feedRecyclerViewAdapter
            layoutManager = WrapContentLinearLayoutManager()


            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position =
                            (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {
                            if (this.itemCount > 0 && (10 * (curPage + 1) - 2 < position)) {
                                curPage = (position + 1) / 10
                                viewModel.getTodayFeeds(curPage, pageSize)
                            }
                        }

                    }
                }

            })
        }

        viewModel.feedList.observe(this, androidx.lifecycle.Observer {
            feedRecyclerViewAdapter.run{
                addItem(it)
                notifyDataSetChanged()
            }
        })

    }

    val feedItemClickListener = object : FeedRecyclerViewAdapter.OnFeedItemClickListener {
        override fun onBookmarkClicked(feedItem: Feed, position: Int) {
            viewModel.bookmark(feedItem, position, false)
        }
        override fun onItemClicked(feedIdx: Int, feedUrl: String, feedName: String, position: Int) {
            val intent = Intent(this@CommunityFeedActivity, FeedWebActivity::class.java)
            intent.apply{
                putExtra("from","feed")
                putExtra("url",feedUrl)
                putExtra("idx",feedIdx)
                putExtra("title",feedName)
                putExtra("position", position)
            }

            bookmarkFeedRequest.launch(intent)
        }
    }

    private fun setObserver(){
        viewModel.feedBookmarkStatus.observe(this, Observer {
            if(it == 200){
                feedRecyclerViewAdapter.refreshItem(viewModel.refreshedFeed, viewModel.refreshedFeedPosition)
            }
        })

        viewModel.bestFeedBookmarkStatus.observe(this, Observer {
            if(it == 200){
                (viewDataBinding.actCommunityFeedAvp.adapter as SsgSagNewsViewPagerAdapter)
                    .replaceItem(viewModel.refreshedFeed, viewModel.refreshedFeedPosition)
            }
        })
    }

    private fun setPullToRefresh(){

        viewDataBinding.actCommunityFeedSrl.run{
            setOnRefreshListener {
                feedRecyclerViewAdapter.clearAll()
                curPage = 0
                viewModel.getTodayFeeds(curPage, pageSize)
                viewDataBinding.actCommunityFeedSrl.isRefreshing = false
            }
        }

    }

    private fun setButton(){

        viewDataBinding.actCommunityFeedToolbar.toolbarBackClBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actCommunityFeedIvScrollToTop.setOnClickListener {
            viewDataBinding.actCommunityNsv.smoothScrollTo(0, 0)
        }

    }


}