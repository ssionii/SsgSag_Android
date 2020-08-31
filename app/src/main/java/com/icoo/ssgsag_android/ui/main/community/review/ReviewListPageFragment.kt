package com.icoo.ssgsag_android.ui.main.community.review

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentReviewListPageBinding
import com.icoo.ssgsag_android.ui.main.review.club.ReviewDetailActivity
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewListPageFragment : BaseFragment<FragmentReviewListPageBinding, ReviewViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_review_list_page
    override val viewModel: ReviewViewModel by viewModel()

    private var ReviewListRecyclerViewAdapter: ReviewListRecyclerViewAdapter? = null

    private var curPage = 0
    var reviewType = -1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.getInt("reviewType")?.apply{
            reviewType = this
        }

        if(reviewType == 0){
            setTab()
        }

        viewModel.getClubReviews(curPage, reviewType)
        viewDataBinding.vm = viewModel

        setRv()
    }

    private fun setTab(){
        viewDataBinding.fragReviewListPageClClubTab.visibility = VISIBLE
    }

    private fun setRv() {

        viewModel.reviewList.observe(requireActivity(), Observer { value ->
            if (ReviewListRecyclerViewAdapter != null) {
                ReviewListRecyclerViewAdapter!!.apply {
                    addItem(value)
                    notifyDataSetChanged()
                }
            } else {
                ReviewListRecyclerViewAdapter =
                    ReviewListRecyclerViewAdapter(value)
                ReviewListRecyclerViewAdapter!!.run {
                    setOnReviewClickListener(onReviewClickListener)
                    setHasStableIds(true)
                }

                viewDataBinding.fragReviewPageRv.apply {
                    adapter = ReviewListRecyclerViewAdapter

                    (itemAnimator as SimpleItemAnimator).run {
                        changeDuration = 0
                        supportsChangeAnimations = false
                    }

                    layoutManager = WrapContentLinearLayoutManager()
                }
            }

        })

        viewDataBinding.fragReviewPageRv.apply {
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_IDLE) {
                        var position = (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

                        adapter!!.apply {

                            if (itemCount > 0 && (10 * (curPage + 1) - 2 < position)) {
                                curPage = (position + 1) / 10

                                viewModel.getClubReviews(curPage, reviewType)
                            }
                        }

                    }
                }

            })
        }

        viewDataBinding.fragReviewPageSrl.apply {
            setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
                override fun onRefresh() {
                    // 새로고침 코드
                    viewDataBinding.fragReviewPageRv.apply {
                        (this.adapter as ReviewListRecyclerViewAdapter).apply {
                            clearAll()
                            curPage = 0
                            viewModel.getClubReviews(curPage, reviewType)

                        }
                        viewDataBinding.fragReviewPageSrl.isRefreshing = false

                    }
                }
            })
        }
    }

    private val onReviewClickListener
            = object : ReviewListRecyclerViewAdapter.OnReviewClickListener {
        override fun onItemClickListener(clubIdx: Int) {

            val intent = Intent(activity!!, ReviewDetailActivity::class.java)
            intent.putExtra("clubIdx", clubIdx)
            startActivity(intent)
        }
    }

    companion object {

        fun newInstance(reviewType: Int): ReviewListPageFragment {
            val fragment = ReviewListPageFragment()
            val bundle = Bundle()
            bundle.putInt("reviewType", reviewType)
            fragment.arguments = bundle
            return fragment
        }
    }
}