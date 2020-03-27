package com.icoo.ssgsag_android.ui.main.review

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseFragment
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.databinding.FragmentReviewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.widget.LinearLayout
import android.widget.Toast
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.ui.main.allPosters.search.SearchActivity
import com.icoo.ssgsag_android.ui.main.myPage.MyPageActivity
import com.icoo.ssgsag_android.ui.main.review.club.ReviewListFragment
import com.icoo.ssgsag_android.util.DialogPlusAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import org.jetbrains.anko.startActivity


class ReviewFragment : BaseFragment<FragmentReviewBinding, ReviewViewModel>() {

    override val layoutResID: Int
        get() = R.layout.fragment_review
    override val viewModel: ReviewViewModel by viewModel()

    var reviewType = ""

    lateinit var mAdapter: DialogPlusAdapter
    var isReviewOpend = false

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.vm = viewModel

        setViewPager()
        setTabLayout()
        setButton()

    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser) {
            popupFirst()
        }
    }


    private fun setViewPager() {

        val campusClubReviewFrag = ReviewListFragment.newInstance(0)
        campusClubReviewFrag.clubType = 1
        val unionClubReviewFrag = ReviewListFragment.newInstance(0)
        unionClubReviewFrag.clubType = 0

        viewDataBinding.fragReviewVp.run {
            adapter = BasePagerAdapter(childFragmentManager).apply {
                addFragment(campusClubReviewFrag)
                addFragment(unionClubReviewFrag)
            }
            currentItem = 0
            offscreenPageLimit = 1
        }
    }

    private fun setTabLayout(){
        viewDataBinding.fragReviewTlCategory.run {
            setupWithViewPager(viewDataBinding.fragReviewVp)
            getTabAt(0)!!.text = "교내 동아리"
            getTabAt(1)!!.text = "연합 동아리"
            setTabRippleColor(null)
        }

    }

    private fun setTabLayoutEnable() {
        val tabStrip = viewDataBinding.fragReviewTlCategory.getChildAt(0) as LinearLayout
        for (i in 1..2) {
            tabStrip.getChildAt(i).setOnTouchListener(View.OnTouchListener { v, event ->
                if (event?.action == MotionEvent.ACTION_UP) {

                    val toast = Toast.makeText(context, "2월 말 출시 예정이니 조금만 \n" +
                            "기다려주세요 :)", Toast.LENGTH_SHORT)
                    val group = toast.view as ViewGroup
                    group.getChildAt(0).textAlignment = View.TEXT_ALIGNMENT_CENTER
                    toast.show()
                }
                true
            })
        }
    }

    private fun setButton(){
        viewDataBinding.fragReviewIvMyPage.setSafeOnClickListener {
            view!!.context.startActivity<MyPageActivity>()
            (view!!.context as Activity).overridePendingTransition(
                R.anim.anim_slide_in_left,
                R.anim.anim_not_move
            )
        }

        viewDataBinding.fragReviewIvSearch.setSafeOnClickListener {
            val intent = Intent(activity!!, SearchActivity::class.java)
            intent.putExtra("from", "club")
            startActivity(intent)
        }

        viewDataBinding.fragReviewCvWriteReview.setSafeOnClickListener {
            val intent = Intent(activity!!, HowWriteReviewActivity::class.java)
            intent.putExtra("from", "main")
            startActivity(intent)
        }
    }

    private fun showDialog(){

        mAdapter = DialogPlusAdapter(activity!!, false, 0, 0, -1)


        val builder = DialogPlus.newDialog(activity!!).apply {

            setContentHolder(ViewHolder(R.layout.fragment_review_main_popup))
            setCancelable(true)
            setGravity(Gravity.BOTTOM)

            setOnClickListener { dialog, view ->
                if(view.id == R.id.frag_review_main_popup_cv){
                    dialog.dismiss()
                    val intent = Intent(activity!!, HowWriteReviewActivity::class.java)
                    intent.putExtra("from", "main")
                    startActivity(intent)
                }
                else {
                    dialog.dismiss()
                }


            }
            setAdapter(mAdapter)
            //setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
            setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
            setOverlayBackgroundResource(R.color.dialog_background)
            setContentBackgroundResource(R.drawable.header_dialog_plus_radius)

//            val horizontalDpValue = 32
//            val topDpValue = 69
            val horizontalDpValue = 0
            val topDpValue = 39
            val bottomDpValue = 48
            val d = resources.displayMetrics.density
            val horizontalMargin = (horizontalDpValue * d).toInt()
            val topMargin = (topDpValue * d).toInt()
            val bottomMargin = (bottomDpValue * d).toInt()

            setPadding(horizontalMargin, topMargin, horizontalMargin, bottomMargin)

        }
        builder.create().show()

    }

    private fun popupFirst(){
        if(SharedPreferenceController.getIsFirstReview(activity!!) < 5 && !isReviewOpend){
            showDialog()
            SharedPreferenceController.setIsFirstReview(activity!!, SharedPreferenceController.getIsFirstReview(activity!!)+1)
            isReviewOpend = true
        }
    }

}