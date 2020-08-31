package com.icoo.ssgsag_android.ui.main.community.review

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BasePagerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityCommunityReviewBinding
import com.icoo.ssgsag_android.ui.main.allPosters.search.SearchActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener


class CommunityReviewActivity : BaseActivity<ActivityCommunityReviewBinding, ReviewViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_community_review
    override val viewModel: ReviewViewModel by viewModel()

    var reviewType = ReviewType.UNION_CLUB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.actCommunityReviewToolbar.toolbarBackSearchTvTitle.text = "활동 후기"

        setViewPager()
        setTabLayout()
        setButton()
    }

    private fun setViewPager() {
        val actReviewListFragment = ReviewListPageFragment.newInstance(ReviewType.ACT)
        val clubReviewListfragment = ReviewListPageFragment.newInstance(ReviewType.UNION_CLUB)
        val internReviewListfragment = ReviewListPageFragment.newInstance(ReviewType.INTERN)

        viewDataBinding.fragReviewVp.run {
            adapter = BasePagerAdapter(supportFragmentManager).apply {
                addFragment(actReviewListFragment)
                addFragment(clubReviewListfragment)
                addFragment(internReviewListfragment)
            }
            currentItem = 0
            offscreenPageLimit = 2
        }
    }


    private fun setTabLayout(){
        viewDataBinding.actCommunityReviewTlCategory.run {
            setupWithViewPager(viewDataBinding.fragReviewVp)
            getTabAt(0)!!.text = "대외활동"
            getTabAt(1)!!.text = "동아리"
            getTabAt(2)!!.text = "인턴"
            setTabRippleColor(null)
        }

        val tabStrip = viewDataBinding.actCommunityReviewTlCategory.getChildAt(0) as LinearLayout
        for (i in 0..2) {
            tabStrip.getChildAt(i).setOnTouchListener(View.OnTouchListener { v, event ->
                if (event?.action == MotionEvent.ACTION_UP) {
                    when(i){
                        0 -> reviewType = ReviewType.ACT
                        1 -> reviewType = ReviewType.UNION_CLUB
                        2 -> reviewType =  ReviewType.INTERN
                    }
                }
                false
            })
        }
    }

    private fun setButton(){

        viewDataBinding.actCommunityReviewToolbar.toolbarBackSearchClBack.setSafeOnClickListener {
            finish()
        }

        viewDataBinding.actCommunityReviewToolbar.toolbarBackSearchClSearch.setSafeOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            intent.putExtra("from", "review")
            startActivity(intent)
        }

        viewDataBinding.actCommunityReviewCvWriteReview.setSafeOnClickListener {
            goToReviewWrite()
        }
    }

    private fun goToReviewWrite(){
        val intent = Intent(this, HowWriteReviewActivity::class.java)
        intent.putExtra("from", "main")
        intent.putExtra("reviewType", reviewType)
        startActivity(intent)
    }

}

object ReviewType{
    const val UNION_CLUB = 0
    const val UNIV_CLUB = 1
    const val ACT = 2
    const val INTERN = 3
}