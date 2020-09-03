package com.icoo.ssgsag_android.ui.main.community.review

import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentReviewListPageBinding
import com.icoo.ssgsag_android.ui.main.review.club.ReviewDetailActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewListPageFragment : BaseFragment<FragmentReviewListPageBinding, ReviewViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_review_list_page
    override val viewModel: ReviewViewModel by viewModel()

    private var reviewListRecyclerViewAdapter: ReviewListRecyclerViewAdapter? = null

    var reviewType = MutableLiveData<Int>()
    private var curPage = 0

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        arguments?.getInt("reviewType")?.apply{
            reviewType.value = this
        }

        if(reviewType.value == 0){
            setTab()
        }

        viewModel.getClubReviews(curPage, reviewType.value!!)
        viewDataBinding.fragment = this
        viewDataBinding.vm = viewModel

        setRv()
    }

    private fun setTab(){
        viewDataBinding.fragReviewListPageClClubTab.visibility = VISIBLE

        viewDataBinding.fragReviewListPageCvUnion.setSafeOnClickListener {
            reviewType.value = ReviewType.UNION_CLUB

            curPage = 0
            reviewListRecyclerViewAdapter?.clearAll()
            viewModel.getClubReviews(curPage, reviewType.value!!)
        }

        viewDataBinding.fragReviewListPageCvUniv.setSafeOnClickListener {
            reviewType.value = ReviewType.UNIV_CLUB

            curPage = 0
            reviewListRecyclerViewAdapter?.clearAll()
            viewModel.getClubReviews(curPage, reviewType.value!!)
        }
    }


    private fun setRv() {

        viewModel.reviewList.observe(requireActivity(), Observer { value ->
            if (reviewListRecyclerViewAdapter != null) {
                reviewListRecyclerViewAdapter!!.apply {
                    addItem(value)
                    notifyDataSetChanged()

                }
            } else {
                reviewListRecyclerViewAdapter =
                    ReviewListRecyclerViewAdapter(value)
                reviewListRecyclerViewAdapter!!.run {
                    setOnReviewClickListener(onReviewClickListener)
                    setHasStableIds(true)
                }

                viewDataBinding.fragReviewPageRv.apply {
                    adapter = reviewListRecyclerViewAdapter

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

                                viewModel.getClubReviews(curPage, reviewType.value!!)
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
                            viewModel.getClubReviews(curPage, reviewType.value!!)

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