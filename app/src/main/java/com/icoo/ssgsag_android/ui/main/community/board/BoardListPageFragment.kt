package com.icoo.ssgsag_android.ui.main.community.board

import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.community.board.BoardPostDetail
import com.icoo.ssgsag_android.data.model.community.board.PostInfo
import com.icoo.ssgsag_android.databinding.FragmentBoardListPageBinding
import com.icoo.ssgsag_android.databinding.ItemBoardBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.community.board.postDetail.BoardPostDetailActivity
import com.icoo.ssgsag_android.ui.main.community.feed.FeedWebActivity
import com.icoo.ssgsag_android.ui.main.community.review.ReviewListRecyclerViewAdapter
import com.icoo.ssgsag_android.util.view.AutoScrollAdapter
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel

class BoardListPageFragment : BaseFragment<FragmentBoardListPageBinding, CommunityBoardViewModel>(), BaseRecyclerViewAdapter.OnItemClickListener{

    override val layoutResID: Int
        get() = R.layout.fragment_board_list_page

    override val viewModel: CommunityBoardViewModel by viewModel()

    val postRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult : ActivityResult ->
        val resultCode : Int = activityResult.resultCode
        val data : Intent? = activityResult.data

        if(resultCode == Activity.RESULT_OK) {
            if(data!!.getStringExtra("type") == "delete" || data.getStringExtra("type") == "edit") {
                (requireActivity() as CommunityBoardActivity).setCounselVp()
            }
        }
    }


    var category = "ALL"
    var communityBoardType = CommunityBoardType.COUNSEL

    var curPage = 0
    var pageSize = 10

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewDataBinding.vm = viewModel

        category = requireArguments().getString("category")!!
        communityBoardType = requireArguments().getInt("communityBoardType")

        if(communityBoardType == CommunityBoardType.TALK){
            viewModel.getTalkList(curPage, pageSize)
        }else{
            viewModel.getCounselList(category, curPage, pageSize)
        }

        setTopBanner()
        setRv()
        pullToRefresh()

    }


    private fun setTopBanner(){

        val d = resources.displayMetrics.density
        val width = MainActivity.GetWidth.windowWidth

        val vpAdapter =
            AutoScrollAdapter(requireActivity())
        vpAdapter.setOnItemClickListener(onBannerItemClickListener)

        viewDataBinding.fragBoardListPageAsvpBanner.run{
            adapter = vpAdapter
            setInterval(3000)
            setCycle(true)

            layoutParams.height = (width * 0.32).toInt()

            addOnPageChangeListener(onBannerPageChangeListener)
        }

        viewModel.adList.observe(viewLifecycleOwner, Observer {
            vpAdapter.apply{
                replaceAll(it)
                notifyDataSetChanged()
            }

            viewDataBinding.fragBoardListPagerCai.run{
                deleteDoPanel()
                createDotPanel(it.size, R.drawable.dot_2, R.drawable.dot_1)
            }

            viewDataBinding.fragBoardListPageAsvpBanner.startAutoScroll()
        })
    }

    val onBannerItemClickListener
            = object : AutoScrollAdapter.OnItemClickListener{
        override fun onItemClick(url: String?) {

            val intent = Intent(context, FeedWebActivity::class.java)
            intent.putExtra("url",url)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            val bundle = Bundle().apply {
                putString("url", url)
            }

            ContextCompat.startActivity(activity!!, intent, bundle)
        }
    }

    val onBannerPageChangeListener
            = object : ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) { }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            viewDataBinding.fragBoardListPagerCai.selectDot(position)
        }
    }

    private fun pullToRefresh(){
        viewDataBinding.fragBoardListPageSrl.apply {
            setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
                override fun onRefresh() {
                    // 새로고침 코드
                    viewDataBinding.actCommunityBoardRv.apply {
                        (this.adapter as BaseRecyclerViewAdapter<BoardPostDetail, *>).apply {
                            clearAll()
                            curPage = 0
                            if(communityBoardType == CommunityBoardType.COUNSEL)
                                viewModel.refreshCounselList(category, curPage, pageSize)
                            else
                                viewModel.refreshTalkList(curPage, pageSize)

                        }
                        viewDataBinding.fragBoardListPageSrl.isRefreshing = false

                    }
                }
            })
        }
    }

    private fun setRv(){

        viewDataBinding.actCommunityBoardRv.run{
            adapter = object : BaseRecyclerViewAdapter<PostInfo, ItemBoardBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_board
                override val bindingVariableId: Int
                    get() = BR.postInfo
                override val listener: OnItemClickListener?
                    get() = this@BoardListPageFragment
            }

            (itemAnimator as SimpleItemAnimator).run {
                changeDuration = 0
                supportsChangeAnimations = false
            }

            layoutManager = LinearLayoutManager(requireContext())

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {
                            if (itemCount > 0 && (pageSize * (curPage + 1) - 2 < position)) {
                                curPage = (position + 1) / pageSize
                                viewModel.getCounselList(category, curPage, pageSize)
                            }
                        }

                    }
                }

            })
        }

        val emptyView = viewDataBinding.actCommunityBoardLlEmpty
        val recyclerView = viewDataBinding.actCommunityBoardRv

        viewModel.postList.observe(viewLifecycleOwner, Observer {
            (viewDataBinding.actCommunityBoardRv.adapter as BaseRecyclerViewAdapter<PostInfo, *>).run{
                if(curPage > 0)
                    addItem(it)
                else
                    replaceAll(it)
                notifyItemRangeChanged(curPage * 10, (curPage * 10) + pageSize + 1)
                if(this.itemCount > 0) {
                    emptyView.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }else{
                    emptyView.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }

            }
        })

    }

    override fun onItemClicked(item: Any?, position: Int?) {
        val intent = Intent(requireContext(), BoardPostDetailActivity::class.java)
        intent.putExtra("type", communityBoardType)
        intent.putExtra("postIdx", (item as PostInfo).communityIdx)

        postRequest.launch(intent)
    }


    companion object {

        fun newInstance(category: String, communityBoardType : Int): BoardListPageFragment {
            val frg = BoardListPageFragment()
            val bundle = Bundle()
            bundle.putString("category", category)
            bundle.putInt("communityBoardType", communityBoardType)
            frg.arguments = bundle
            return frg
        }
    }

}