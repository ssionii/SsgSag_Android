package com.icoo.ssgsag_android.ui.main.review.main

import android.app.Activity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.databinding.FragmentReviewMainBinding
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.myPage.MyPageActivity
import com.icoo.ssgsag_android.ui.main.review.ReviewPageFragment
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.verticalMargin
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewMainFragment : BaseFragment<FragmentReviewMainBinding, ReviewMainViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_review_main

    override val viewModel: ReviewMainViewModel by viewModel()

    lateinit var clubReviewPageFragment: ReviewPageFragment
    lateinit var actReviewPageFragment: ReviewPageFragment
    lateinit var internReviewPageFragment: ReviewPageFragment


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setButton()
        if(!SharedPreferenceController.getReviewCoachMark(activity!!))
            setCoachMark()
    }

    private fun setButton(){

        viewDataBinding.fragReviewMainRlClub.setSafeOnClickListener {
            clubReviewPageFragment = ReviewPageFragment()
            clubReviewPageFragment.reviewType = 1

            fragmentManager!!.beginTransaction().add(R.id.frag_review_main_fl_container, clubReviewPageFragment, "club").addToBackStack(null).commit()


            fragmentManager!!.beginTransaction().show(clubReviewPageFragment).commit()
        }

        viewDataBinding.fragReviewMainRlAct.setSafeOnClickListener {
            actReviewPageFragment = ReviewPageFragment()
            actReviewPageFragment.reviewType = 2

            fragmentManager!!.beginTransaction().add(R.id.frag_review_main_fl_container, actReviewPageFragment, "act").addToBackStack(null).commit()
            fragmentManager!!.beginTransaction().show(actReviewPageFragment).commit()
        }

        viewDataBinding.fragReviewMainRlIntern.setSafeOnClickListener {
            internReviewPageFragment = ReviewPageFragment()
            internReviewPageFragment.reviewType = 3

            fragmentManager!!.beginTransaction().add(R.id.frag_review_main_fl_container, internReviewPageFragment, "intern").addToBackStack(null).commit()
            fragmentManager!!.beginTransaction().show(internReviewPageFragment).commit()
        }

        viewDataBinding.fragReviewMainIvBanner.setSafeOnClickListener {
            startActivity<ReviewMainEventActivity>()
        }

        viewDataBinding.fragReviewMainIvMyPage.setSafeOnClickListener {
            view!!.context.startActivity<MyPageActivity>()
            (view!!.context as Activity).overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_not_move
            )

        }
    }

    fun removeFragment(tag: String){

        fragmentManager!!.findFragmentByTag(tag)?.also {
            fragmentManager!!.beginTransaction().remove(it)
        }

    }

    private fun setCoachMark(){

        viewDataBinding.fragReviewMainClCoachmarkContainer.visibility = View.VISIBLE
        SharedPreferenceController.setReviewCoachMark(activity!!, true)

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
