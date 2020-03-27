package com.icoo.ssgsag_android.ui.main.review.club

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.databinding.FragmentReviewPageBinding
import com.icoo.ssgsag_android.ui.main.feed.category.ClubListRecyclerViewAdapter
import com.icoo.ssgsag_android.ui.main.review.ReviewViewModel
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubManagerCheckActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel


class ReviewListFragment : BaseFragment<FragmentReviewPageBinding, ReviewViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_review_page
    override val viewModel: ReviewViewModel by viewModel()

    private var ClubListRecyclerViewAdapter: ClubListRecyclerViewAdapter? = null

    private var curPage = 0
    var clubType = 1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        viewModel.getClubReviews(curPage, clubType)

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
            if (ClubListRecyclerViewAdapter != null) {
                ClubListRecyclerViewAdapter!!.apply {
                    addItem(value)
                    notifyDataSetChanged()

                }
            } else {
                ClubListRecyclerViewAdapter =
                    ClubListRecyclerViewAdapter(value)
                ClubListRecyclerViewAdapter!!.run {
                    setOnReviewClickListener(onReviewClickListener)
                    setHasStableIds(true)
                }

                viewDataBinding.fragReviewPageRv.apply {
                    adapter = ClubListRecyclerViewAdapter

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

                                viewModel.getClubReviews(curPage, clubType)
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
                        (this.adapter as ClubListRecyclerViewAdapter).apply {
                            clearAll()
                            curPage = 0
                            viewModel.getClubReviews(curPage, clubType)

                        }
                        viewDataBinding.fragReviewPageSrl.isRefreshing = false

                    }
                }
            })
        }
    }

    private val onReviewClickListener
            = object : ClubListRecyclerViewAdapter.OnReviewClickListener {
        override fun onItemClickListener(clubIdx: Int) {

            val intent = Intent(activity!!, ClubReviewDetailActivity::class.java)
            intent.putExtra("clubIdx", clubIdx)
            startActivity(intent)
        }
    }

    companion object {

        fun newInstance(category: Int): ReviewListFragment {
            val fragment = ReviewListFragment()
            val bundle = Bundle()
            bundle.putInt("categoryList", category)
            fragment.arguments = bundle
            return fragment
        }
    }
}