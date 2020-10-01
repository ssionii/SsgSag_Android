package com.icoo.ssgsag_android.ui.main.review.club.write

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.data.model.review.ReviewWriteRelam
import com.icoo.ssgsag_android.databinding.ActivityClubReviewWriteBinding
import com.icoo.ssgsag_android.ui.main.community.review.ReviewType
import io.realm.Realm
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewWriteActivity : BaseActivity<ActivityClubReviewWriteBinding, ReviewWriteViewModel>(){

    override val layoutResID: Int
        get() = R.layout.activity_club_review_write
    override val viewModel: ReviewWriteViewModel by viewModel()

    lateinit var ReviewWritePagerAdapter: ReviewWritePagerAdapter

    val realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        realm.beginTransaction()
        val realmObject = realm.createObject(ReviewWriteRelam::class.java)
        realmObject.apply {
            this.id = 1
        }
        realm.commitTransaction()

        viewModel.reviewType = intent.getIntExtra("reviewType", ReviewType.ACT)
        viewModel.setClubType( intent.getIntExtra("reviewType", ReviewType.ACT))

        viewDataBinding.vm = viewModel

        if(intent.getStringExtra("from") == "reviewDetail"){
            setReviewWriteStringRealm("clubName", intent.getStringExtra("clubName"))
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

    fun setReviewWriteStringRealm(type: String, input : String){
        realm.beginTransaction()
        val reviewWriteRealm = realm.where(ReviewWriteRelam::class.java).equalTo("id", 1 as Int).findFirst()
        if(reviewWriteRealm != null){
            when(type){
                "clubName"-> reviewWriteRealm.clubName = input
                "univOrLocation"-> reviewWriteRealm.univOrLocation = input
                "fieldName"-> reviewWriteRealm.fieldName = input
                "startYear"-> reviewWriteRealm.startYear = input
                "startMonth"-> reviewWriteRealm.startMonth = input
                "endYear"-> reviewWriteRealm.endYear = input
                "endMonth"-> reviewWriteRealm.endMonth = input
                "oneLine"-> reviewWriteRealm.oneLine = input
                "advantage"-> reviewWriteRealm.advantage = input
                "disadvantage"-> reviewWriteRealm.disadvantage = input
                "honeyTip"-> reviewWriteRealm.honeyTip = input
                "categoryList" -> reviewWriteRealm.categoryList = input
                else -> Log.e("setReviewWSRealm", "parameter 오류")
            }
        }
        realm.commitTransaction()
    }

    fun setReviewWriteIntRealm(type: Int, input : Int){
        realm.beginTransaction()
        val reviewWriteRealm = realm.where(ReviewWriteRelam::class.java).equalTo("id", 1 as Int).findFirst()
        if(reviewWriteRealm != null){
            when(type){
                0-> reviewWriteRealm.score0 = input
                1-> reviewWriteRealm.score1 = input
                2-> reviewWriteRealm.score2 = input
                3-> reviewWriteRealm.score3 = input
                4-> reviewWriteRealm.score4 = input
                else -> Log.e("setReviewWIRealm", "parameter 오류")

            }
        }else{

            Log.e("Realm review score","realm 객체 없음")
        }
        realm.commitTransaction()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.iniPostStatus()

        realm.beginTransaction()
        val reviewWriteRealm = realm.where(ReviewWriteRelam::class.java).findAll()
        reviewWriteRealm.deleteAllFromRealm()
        realm.commitTransaction()


    }
}