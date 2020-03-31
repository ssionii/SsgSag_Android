package com.icoo.ssgsag_android.ui.main.review

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentReviewPageBinding
import com.icoo.ssgsag_android.ui.main.review.club.ReviewDetailActivity
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubManagerCheckActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewListFragment : BaseFragment<FragmentReviewPageBinding, ReviewViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_review_page
    override val viewModel: ReviewViewModel by viewModel()

    private var ReviewListRecyclerViewAdapter: ReviewListRecyclerViewAdapter? = null

    private var curPage = 0
    var reviewType = -1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.getInt("reviewType")?.apply{
            reviewType = this
        }

        viewDataBinding.vm = viewModel
        viewDataBinding.reviewListFragment = this

        viewModel.getClubReviews(curPage, reviewType)

        setButton()
        setRv()

        // navigator()
    }


    private fun setButton() {
        viewDataBinding.fragReviewClRegiClub.setSafeOnClickListener {
            val intent = Intent(activity!!, ClubManagerCheckActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setRv() {

        viewModel.reviewList.observe(this, Observer { value ->
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
                        var position =
                            (layoutManager as WrapContentLinearLayoutManager).findLastVisibleItemPosition()

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

            var reviewTypeString = ""
            when(reviewType){
                0,1 -> reviewTypeString = "club"
                2 -> reviewTypeString = "act"
                3 -> reviewTypeString = "intern"
            }

            val intent = Intent(activity!!, ReviewDetailActivity::class.java)
            intent.putExtra("clubIdx", clubIdx)
            intent.putExtra("reviewType", reviewTypeString)
            startActivity(intent)
        }
    }

    companion object {

        fun newInstance(reviewType: Int): ReviewListFragment {
            val fragment = ReviewListFragment()
            val bundle = Bundle()
            bundle.putInt("reviewType", reviewType)
            fragment.arguments = bundle
            return fragment
        }
    }
}