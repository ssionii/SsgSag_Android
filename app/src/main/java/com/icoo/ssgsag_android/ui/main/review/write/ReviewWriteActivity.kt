package com.icoo.ssgsag_android.ui.main.review.club.write

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityClubReviewWriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewWriteActivity : BaseActivity<ActivityClubReviewWriteBinding, ReviewWriteViewModel>(){

    object ClubReviewWriteData {
        // 동아리 정보
        lateinit var clubName: String
        var univOrLocation = ""
        var categoryList = mutableListOf<String>()

        var startYear = ""
        var startMonth = ""
        var endYear =""
        var endMonth =""

        // 점수 평가
        var score0 = 0
        var score1 = 0
        var score2 = 0
        var score3 = 0
        var score4 = 0

        // 간단 평가
        lateinit var oneLine: String
        lateinit var advantage: String
        lateinit var disadvantage: String
        var honeyTip =""
    }

    override val layoutResID: Int
        get() = R.layout.activity_club_review_write
    override val viewModel: ReviewWriteViewModel by viewModel()

    lateinit var ReviewWritePagerAdapter: ReviewWritePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.reviewType = intent.getStringExtra("reviewType")

        viewDataBinding.vm = viewModel

        if(intent.getStringExtra("from") == "reviewDetail"){
            ClubReviewWriteData.clubName = intent.getStringExtra("clubName")
            viewModel.clubIdx = intent.getIntExtra("clubIdx", -1)
        }

        setVp(intent.getStringExtra("from"))

    }



    private fun setVp(from: String){
        ReviewWritePagerAdapter = ReviewWritePagerAdapter(supportFragmentManager, 4, from, viewModel.reviewType)
        viewDataBinding.actClubReviewWriteNvp.run{
            adapter = ReviewWritePagerAdapter
            currentItem = 0
            offscreenPageLimit = 4

        }
    }

    fun toNextPage(curPage :Int){
        if(curPage < ReviewWritePagerAdapter.fragmentCount - 1) {
            viewDataBinding.actClubReviewWriteNvp.setCurrentItem(curPage + 1)
        }
    }

    fun toPrevPage(curPage: Int){
        if(curPage > 0) {
            viewDataBinding.actClubReviewWriteNvp.setCurrentItem(curPage - 1)
        }
    }

    fun hideKeyboard(et: EditText){
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.hideSoftInputFromWindow(et.getWindowToken(), 0)

    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.iniPostStatus()
        // 초기화
        ClubReviewWriteData.apply{
            clubName = ""
            univOrLocation =""
            startYear = ""
            startMonth = ""
            endYear =""
            endMonth =""

            score0 = 0
            score1 = 0
            score2 = 0
            score3 = 0
            score4 = 0

            oneLine = ""
            advantage =""
            disadvantage = ""
            honeyTip =""
        }

    }
}