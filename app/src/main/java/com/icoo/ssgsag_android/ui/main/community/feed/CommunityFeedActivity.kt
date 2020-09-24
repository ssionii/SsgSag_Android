package com.icoo.ssgsag_android.ui.main.community.feed

import SsgSagNewsViewPagerAdapter
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityCommunityFeedBinding
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class CommunityFeedActivity : BaseActivity<ActivityCommunityFeedBinding, FeedViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_community_feed

    override val viewModel: FeedViewModel by viewModel()

    private lateinit var feedRecyclerViewAdapter: FeedRecyclerViewAdapter
    var curPage = 0
    var pageSize = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.actCommunityFeedToolbar.toolbarBackTvTitle.text = "슥삭 추천정보"

        viewModel.getTodayFeeds(curPage, pageSize)

        setSsgsagNewsVp()
        setRv()
        setButton()
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
            val cardViewPagerAdapter = SsgSagNewsViewPagerAdapter(this, it, "feed")
            cardViewPagerAdapter.apply {
                newsWidth = contentWidth
                setOnItemClickListener(bestFeedItemClickListener)
            }
            viewDataBinding.actCommunityFeedAvp.apply{
                clipToPadding = false
                setPadding(leftMargin, 0, rightMargin, 0)
                pageMargin = middleMargin
                adapter = cardViewPagerAdapter
            }
        })

    }

    val bestFeedItemClickListener = object : SsgSagNewsViewPagerAdapter.OnItemClickListener {
        override fun onItemClick(url: String, name: String, isSave: Int) {
            getWebActivity(url, name, isSave)
        }

        override fun bookmark(idx: Int) {
            TODO("Not yet implemented")
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
        override fun onBookmarkClicked(feedIdx: Int, isSaved: Int, position: Int) {
            TODO("Not yet implemented")
        }

        override fun onItemClicked(feedIdx: Int, feedUrl: String, feedName: String, isSave: Int, position: Int) {
            getWebActivity(feedUrl, feedName, isSave)
        }
    }

    private fun setButton(){

        viewDataBinding.actCommunityFeedToolbar.toolbarBackClBack.setSafeOnClickListener {
            finish()
        }

    }

    private fun getWebActivity(url : String, name : String, isSave : Int){
        val intent = Intent(this@CommunityFeedActivity, FeedWebActivity::class.java)
        intent.apply{
            putExtra("from","feed")
            putExtra("url",url)
            putExtra("title",name)
            putExtra("isSave",isSave)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }

        startActivity(intent)
    }


}