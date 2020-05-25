package com.icoo.ssgsag_android.ui.main.review.club

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.icoo.ssgsag_android.BR
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.base.BasePagerAdapter
import com.icoo.ssgsag_android.base.BaseRecyclerViewAdapter
import com.icoo.ssgsag_android.data.model.review.ReviewGrade
import com.icoo.ssgsag_android.databinding.ActivityClubReviewDetailBinding
import com.icoo.ssgsag_android.databinding.ItemClubReviewDetailGradeBinding
import com.icoo.ssgsag_android.databinding.ItemReviewCategoryBinding
import com.icoo.ssgsag_android.ui.main.review.HowWriteReviewActivity
import com.icoo.ssgsag_android.ui.main.review.reviewDetail.ReviewDetailViewModel
import com.icoo.ssgsag_android.ui.main.review.club.info.ClubInfoFragment
import com.icoo.ssgsag_android.ui.main.review.club.reviews.ReviewsFragment
import com.icoo.ssgsag_android.util.DialogPlusAdapter
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import com.icoo.ssgsag_android.util.view.WrapContentLinearLayoutManager
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

        viewModel.mClubIdx = intent.getIntExtra("clubIdx", 0)

        viewDataBinding.vm = viewModel


        viewModel.run {
            getClubDetail()
            getAlreadyWrite()
        }

        setCategoryRv()
        setGradeRv()
        setButton()

        val reviewsFragment = ReviewsFragment()
        reviewsFragment.setClubIdx(viewModel.mClubIdx)
        reviewsFragment.setReviewType(viewModel.reviewType)

        supportFragmentManager!!.beginTransaction().add(R.id.act_review_detail_fl_bottom_container,reviewsFragment).commit()
        supportFragmentManager!!.beginTransaction().show(reviewsFragment).commit()

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

    private fun setGradeRv(){

        viewDataBinding.actClubReviewDetailRvGrade.run{
            adapter = object : BaseRecyclerViewAdapter<ReviewGrade,ItemClubReviewDetailGradeBinding>(){
                override val layoutResID: Int
                    get() = R.layout.item_club_review_detail_grade
                override val bindingVariableId: Int
                    get() = BR.reviewGrade
                override val listener: OnItemClickListener?
                    get() = onItemClickListener
            }

            layoutManager = WrapContentLinearLayoutManager(RecyclerView.HORIZONTAL)
        }

        viewModel.reviewGradeList.observe(this, Observer {
            (viewDataBinding.actClubReviewDetailRvGrade.adapter as BaseRecyclerViewAdapter <ReviewGrade,ItemClubReviewDetailGradeBinding>).run{
                replaceAll(it)
                notifyDataSetChanged()

                Log.e("reviewGradeList size", this.items[2].score.toString())
            }
        })
    }

    val onItemClickListener = object : BaseRecyclerViewAdapter.OnItemClickListener{
        override fun onItemClicked(item: Any?, position: Int?) {
            showDialog()
        }
    }

    private fun setButton(){
        viewDataBinding.actReviewDetailIvBack.setSafeOnClickListener {
            finish()
        }
        viewDataBinding.actReviewDetailIvWrite.setSafeOnClickListener {

            if(viewModel.isAlreadyWrite){
                toast("이미 이 활동에 대해 후기를 하나 쓰셨네요! 새로운 후기를 남기시려면 기존 후기를 삭제해주세요 \uD83D\uDE02")
            }else {
                val intent = Intent(this, HowWriteReviewActivity::class.java)
                intent.apply {
                    putExtra("from", "reviewDetail")
                    putExtra("reviewType", viewModel.reviewType)
                    putExtra("clubName", viewModel.reviewDetail.value!!.clubName)
                    putExtra("clubIdx", viewModel.reviewDetail.value!!.clubIdx)
                }

                startActivity(intent)
            }
        }

        viewDataBinding.actClubReviewDetailClGrade.setSafeOnClickListener {
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