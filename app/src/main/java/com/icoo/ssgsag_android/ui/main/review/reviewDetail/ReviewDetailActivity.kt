package com.icoo.ssgsag_android.ui.main.review.club

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.databinding.ActivityClubReviewDetailBinding
import com.icoo.ssgsag_android.databinding.ItemReviewCategoryBinding
import com.icoo.ssgsag_android.ui.main.review.HowWriteReviewActivity
import com.icoo.ssgsag_android.ui.main.review.reviewDetail.ReviewDetailViewModel
import com.icoo.ssgsag_android.ui.main.review.club.info.ClubInfoFragment
import com.icoo.ssgsag_android.ui.main.review.club.reviews.ReviewsFragment
import com.icoo.ssgsag_android.util.DialogPlusAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewDetailActivity : BaseActivity<ActivityClubReviewDetailBinding, ReviewDetailViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_club_review_detail
    override val viewModel: ReviewDetailViewModel by viewModel()

    lateinit var mAdapter: DialogPlusAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding.vm = viewModel

        viewModel.mClubIdx = intent.getIntExtra("clubIdx", 0)
        viewModel.getClubDetail()
        viewModel.getAlreadyWrite()

        setCategoryRv()
        setButton()
        setVp()
        setTab()
    }


    private fun setCategoryRv(){

        viewDataBinding.actReviewDetailRvCategory.run{
            adapter = object : BaseRecyclerViewAdapter<String,ItemReviewCategoryBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_review_category
                override val bindingVariableId: Int
                    get() = BR.reviewCategory
                override val listener: OnItemClickListener?
                    get() = null
                }
        }

        viewModel.clubCategory.observe(this, Observer { value ->
            (viewDataBinding.actReviewDetailRvCategory.adapter as BaseRecyclerViewAdapter<String,ItemReviewCategoryBinding>).run{
                replaceAll(value)
                notifyDataSetChanged()
            }
        })
    }

    private fun setVp(){
        viewDataBinding.actReviewDetailVp.run{
            adapter = BasePagerAdapter(supportFragmentManager).apply {
                addFragment(ClubInfoFragment())
                addFragment(ReviewsFragment())
                isSaveEnabled = false
            }
            currentItem = 0
            offscreenPageLimit = 1
        }
    }

    private fun setTab(){
        viewDataBinding.actReviewDetailTl.run{
            setupWithViewPager(viewDataBinding.actReviewDetailVp)
            getTabAt(0)!!.text = "정보"
            getTabAt(1)!!.text = "후기"
            setTabRippleColor(null)
        }
    }

    private fun setButton(){
        viewDataBinding.actReviewDetailIvBack.setSafeOnClickListener {
            finish()
        }
        viewDataBinding.actReviewDetailIvWrite.setSafeOnClickListener {

            if(viewModel.isAlreadyWrite){
                toast("이미 후기를 작성하셨습니다.")
            }else {
                val intent = Intent(this, HowWriteReviewActivity::class.java)
                intent.apply {
                    putExtra("from", "reviewDetail")
                    putExtra("clubName", viewModel.reviewDetail.value!!.clubName)
                    putExtra("clubIdx", viewModel.reviewDetail.value!!.clubIdx)
                }

                startActivity(intent)
            }



        }

        viewDataBinding.actClubReviewDetailLlGrade.setSafeOnClickListener {
            showDialog()
        }
    }

    private fun showDialog(){

        mAdapter = DialogPlusAdapter(this, false, 0, 0, -1)


        val builder = DialogPlus.newDialog(this).apply {

            setContentHolder(ViewHolder(R.layout.fragment_review_grade_inform))
            setCancelable(true)
            setGravity(Gravity.BOTTOM)

            setOnClickListener { dialog, view ->
                dialog.onBackPressed(dialog)

            }
            setAdapter(mAdapter)
            setOverlayBackgroundResource(R.color.dialog_background)
            setContentBackgroundResource(R.drawable.header_dialog_plus_radius)

            val horizontalDpValue = 32
            val topDpValue = 44
            val bottomDpValue = 48
            val d = resources.displayMetrics.density
            val horizontalMargin = (horizontalDpValue * d).toInt()
            val topMargin = (topDpValue * d).toInt()
            val bottomMargin = (bottomDpValue * d).toInt()
            setPadding(horizontalMargin, topMargin, horizontalMargin, bottomMargin)

        }
        builder.create().show()

    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel
    }
}