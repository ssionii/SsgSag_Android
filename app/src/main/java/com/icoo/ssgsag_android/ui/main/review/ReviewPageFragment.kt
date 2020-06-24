package com.icoo.ssgsag_android.ui.main.review

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.databinding.FragmentReviewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.widget.LinearLayout
import android.widget.Toast
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.allPosters.search.SearchActivity
import com.icoo.ssgsag_android.ui.main.myPage.MyPageActivity
import com.icoo.ssgsag_android.ui.main.review.main.ReviewMainFragment
import com.icoo.ssgsag_android.util.DialogPlusAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.toast


class ReviewPageFragment : BaseFragment<FragmentReviewBinding, ReviewViewModel>(), MainActivity.onKeyBackPressedListener {

    override val layoutResID: Int
        get() = R.layout.fragment_review
    override val viewModel: ReviewViewModel by viewModel()

    var reviewType = -1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel
        viewDataBinding.reviewPageFragment = this

        if(reviewType == 1){
            setViewPagerForClub()
            setTabLayout()
            tabLayoutClickListener()
        }else{
            setViewPager(reviewType)
            viewDataBinding.fragReviewTlCategory.visibility = GONE
        }

        setButton()

    }


    private fun setViewPagerForClub() {

        val campusClubReviewFrag = ReviewListFragment.newInstance(1)
        val unionClubReviewFrag = ReviewListFragment.newInstance(0)

        viewDataBinding.fragReviewVp.run {
            adapter = BasePagerAdapter(childFragmentManager).apply {
                addFragment(unionClubReviewFrag)
                addFragment(campusClubReviewFrag)
            }
            currentItem = 0
            offscreenPageLimit = 1
        }
    }

    private fun setViewPager(reviewType : Int){
        val reviewFrag = ReviewListFragment.newInstance(reviewType)

        viewDataBinding.fragReviewVp.run{
            adapter = BasePagerAdapter(childFragmentManager).apply {
                addFragment(reviewFrag)
            }
            currentItem = 0
        }

    }

    private fun setTabLayout(){
        viewDataBinding.fragReviewTlCategory.run {
            setupWithViewPager(viewDataBinding.fragReviewVp)
            getTabAt(0)!!.text = "연합 동아리"
            getTabAt(1)!!.text = "교내 동아리"
            setTabRippleColor(null)
        }

    }

    private fun tabLayoutClickListener() {
        val tabStrip = viewDataBinding.fragReviewTlCategory.getChildAt(0) as LinearLayout
        for (i in 0..1) {
            tabStrip.getChildAt(i).setOnTouchListener(View.OnTouchListener { v, event ->
                if (event?.action == MotionEvent.ACTION_UP) {
                    if(i == 1) reviewType = 1
                    else reviewType = 0
                }
                false
            })
        }
    }

    private fun setButton(){

        viewDataBinding.fragReviewClBack.setSafeOnClickListener {

            fragmentManager!!.beginTransaction().remove(this).commit()
        }

        viewDataBinding.fragReviewIvSearch.setSafeOnClickListener {
            val intent = Intent(activity!!, SearchActivity::class.java)
            intent.putExtra("from", "club")
            intent.putExtra("clubType", reviewType)
            startActivity(intent)
        }

        viewDataBinding.fragReviewCvWriteReview.setSafeOnClickListener {
            goToReviewWrite()
        }
    }

    private fun goToReviewWrite(){
        val intent = Intent(activity!!, HowWriteReviewActivity::class.java)
        intent.putExtra("from", "main")
        when(reviewType){
            0,1 -> intent.putExtra("reviewType", "club")
            2 -> intent.putExtra("reviewType", "act")
            3 -> intent.putExtra("reviewType", "intern")
        }
        startActivity(intent)
    }

    override fun onBack() {

        val activity = activity as MainActivity
        activity.setOnKeyBackPressedListener(this)
        fragmentManager!!.popBackStack()

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context as MainActivity).setOnKeyBackPressedListener(this)
    }

}