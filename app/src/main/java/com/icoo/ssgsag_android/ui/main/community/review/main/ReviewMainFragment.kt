package com.icoo.ssgsag_android.ui.main.community.review.main

import android.content.Intent
import android.graphics.Point
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.FragmentReviewMainBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.community.review.ReviewListPageFragment
import com.icoo.ssgsag_android.ui.main.myPage.MyPageActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.AutoScrollAdapter
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.verticalMargin
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewMainFragment : BaseFragment<FragmentReviewMainBinding, ReviewMainViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_review_main

    override val viewModel: ReviewMainViewModel by viewModel()

    lateinit var clubReviewPageFragment: ReviewListPageFragment
    lateinit var actReviewPageFragment: ReviewListPageFragment
    lateinit var internReviewPageFragment: ReviewListPageFragment


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setButton()
        setRollingViewPager()
        if(!SharedPreferenceController.getReviewCoachMark(requireContext()))
            setCoachMark()
    }

    private fun setButton(){

        viewDataBinding.fragReviewMainRlClub.setSafeOnClickListener {
            clubReviewPageFragment = ReviewListPageFragment()
            clubReviewPageFragment.reviewType = 1

            childFragmentManager.beginTransaction().add(R.id.frag_review_main_fl_container, clubReviewPageFragment, "club").addToBackStack(null).commit()
            childFragmentManager.beginTransaction().show(clubReviewPageFragment).commit()
        }

        viewDataBinding.fragReviewMainRlAct.setSafeOnClickListener {
            actReviewPageFragment = ReviewListPageFragment()
            actReviewPageFragment.reviewType = 2

            childFragmentManager.beginTransaction().add(R.id.frag_review_main_fl_container, actReviewPageFragment, "act").addToBackStack(null).commit()
            childFragmentManager.beginTransaction().show(actReviewPageFragment).commit()
        }

        viewDataBinding.fragReviewMainRlIntern.setSafeOnClickListener {
            internReviewPageFragment = ReviewListPageFragment()
            internReviewPageFragment.reviewType = 3

            childFragmentManager.beginTransaction().add(R.id.frag_review_main_fl_container, internReviewPageFragment, "intern").addToBackStack(null).commit()
            childFragmentManager.beginTransaction().show(internReviewPageFragment).commit()
        }

        viewDataBinding.fragReviewMainIvMyPage.setSafeOnClickListener {
            startActivity<MyPageActivity>()
            requireActivity().overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_not_move
            )
        }
    }

    private fun setRollingViewPager(){

        // UI
        val display = requireActivity().windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width =size.x

        viewDataBinding.fragReviewMainClBanner.apply{
            layoutParams.height = width/2;
            requestLayout()

        }

        viewDataBinding.fragReviewMainAsvpBanner.apply{
            val autoScrollAdapter =
                AutoScrollAdapter(
                    requireActivity()
                )
            autoScrollAdapter.setOnItemClickListener(onBannerItemClickListener)
            adapter = autoScrollAdapter
            setInterval(3000)
        }



        //data
        viewModel.adList.observe(viewLifecycleOwner, Observer {
            (viewDataBinding.fragReviewMainAsvpBanner.adapter as AutoScrollAdapter).apply{
                replaceAll(it)
                notifyDataSetChanged()
            }

            viewDataBinding.fragReviewMainAsvpBanner.startAutoScroll()
        })

    }

    val onBannerItemClickListener
            = object : AutoScrollAdapter.OnItemClickListener{
        override fun onItemClick(url: String?) {

            if(url != "") {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }

        }
    }

    private fun setCoachMark(){

        viewDataBinding.fragReviewMainClCoachmarkContainer.visibility = View.VISIBLE
        SharedPreferenceController.setReviewCoachMark(requireActivity(), true)

        val d = resources.displayMetrics.density

        val widthPx = MainActivity.GetWidth.windowWidth / 8

        val rightDpValue = (widthPx / d) - 30
        val bottomDpValue = 13

        val rightMargin = (rightDpValue * d).toInt()
        val bottomMargin = (bottomDpValue * d).toInt()

        (viewDataBinding.fragReviewMainClCoachmark.layoutParams as ConstraintLayout.LayoutParams).apply{
            marginEnd = rightMargin
            verticalMargin = bottomMargin
        }

        viewDataBinding.fragReviewMainClCoachmarkContainer.setOnTouchListener( object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true
            }
        })

        viewDataBinding.fragReviewMainClCoachmark.setOnClickListener {
            viewDataBinding.fragReviewMainClCoachmarkContainer.visibility = View.GONE

        }

    }
}
